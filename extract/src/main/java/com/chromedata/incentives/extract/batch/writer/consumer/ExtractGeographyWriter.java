package com.chromedata.incentives.extract.batch.writer.consumer;

import com.chromedata.incentives.extract.batch.component.ExtractConstants;
import com.chromedata.incentives.extract.batch.writer.ConsumerMarkerInterface;
import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.dao.model.BatchGeographiesDetail;
import com.chromedata.incentives.extract.dao.model.BatchGeography;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.LURegion;
import com.chromedata.incentives.extract.presentation.model.RegionDetail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.GEOGRAPHY_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.GEOGRAPHY_DETAIL_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.GEO_IDS_CONSUMER;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;

@Component("geographyWriterToConsumer")
public class ExtractGeographyWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractGeographyWriter.class);

    private final CSVDataSetWriter<LURegion> geographyCSVWriter;
    private final CSVDataSetWriter<RegionDetail> geographyDetailCSVWriter;

    public ExtractGeographyWriter() {
        geographyCSVWriter = new CSVDataSetWriter<>(LURegion.class);
        geographyDetailCSVWriter = new CSVDataSetWriter<>(RegionDetail.class);
    }

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        completeIncentives.removeIf(consumerPredicate);
        final List<BatchGeography> geographies = completeIncentives.stream()
                                                                   .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getGeographies())
                                                                   .flatMap(List::stream)
                                                                   .distinct()
                                                                   .collect(Collectors.toList());
        synchronized (ExtractGeographyWriter.class) {
            final Set<Integer> usedGeoIds = Objects.nonNull(jobExecutionContext.get(GEO_IDS_CONSUMER)) ? (Set<Integer>) jobExecutionContext.get(
                GEO_IDS_CONSUMER) : new HashSet<>();
            if (!CollectionUtils.isEmpty(usedGeoIds)) {
                geographies.removeIf(geography -> usedGeoIds.contains(geography.getGeographyId()));
            }

            if (CollectionUtils.isEmpty(geographies)) {
                return;
            }
            usedGeoIds.addAll(geographies.stream().map(BatchGeography::getGeographyId).collect(Collectors.toSet()));
            jobExecutionContext.put(GEO_IDS_CONSUMER, usedGeoIds);
        }
        writeGeographies(geographies);
    }

    private void writeGeographies(final List<BatchGeography> geographies) throws IOException {
        LOG.debug("Started Writing geographies in consumer {}", geographies.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setGeographyList(geographies);

        PresentationMarshaller presentationMarshaller= marshallerFactory.create((Locale)jobExecutionContext.get(REQUESTED_LOCALE));

        dataSet.getGeographyList().forEach(presentationMarshaller::addGeography);

        CSVDataSet data = presentationMarshaller.getData();

        geographyCSVWriter.writeCollection(data.getLuRegionList());
        GEOGRAPHY_CONSUMER_COUNT.getAndAdd(data.getLuRegionList().size());
        presentationMarshaller.resetData();

        LOG.debug("Wrote geographies in consumer {}", geographies.size());

        writeGeographyDetails(geographies);
    }

    private void writeGeographyDetails(final List<BatchGeography> geographies) throws IOException {
        final List<BatchGeographiesDetail> geographiesDetails = new ArrayList<>();

        geographies.forEach(geography ->  {
            final Integer geographyId = geography.getGeographyId();
            final List<BatchGeographiesDetail> batchGeographiesDetails = geography.getZipPostalPatterns()
                                                                                  .stream()
                                                                                  .map(zipPostalPattern -> new BatchGeographiesDetail(geographyId, zipPostalPattern))
                                                                                  .collect(Collectors.toList());

            geographiesDetails.addAll(batchGeographiesDetails);
        });

        LOG.debug("Started Writing geographiesDetails in consumer {}", geographiesDetails.size());


        PresentationMarshaller presentationMarshaller= marshallerFactory.create((Locale)jobExecutionContext.get(REQUESTED_LOCALE));

        geographiesDetails.forEach(presentationMarshaller::addGeographyDetail);

        CSVDataSet data = presentationMarshaller.getData();

        geographyDetailCSVWriter.writeCollection(data.getGeoDetailList());

        GEOGRAPHY_DETAIL_CONSUMER_COUNT.getAndAdd(data.getGeoDetailList().size());
        presentationMarshaller.resetData();

        LOG.debug("Wrote geographiesDetails in consumer {}", geographiesDetails.size());
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File consumerResponseDir = (File) jobExecutionContext.get(ExtractConstants.CONSUMER_RESPONSE_DIR);
        try {
            geographyCSVWriter.openFile(consumerResponseDir);
            geographyDetailCSVWriter.openFile(consumerResponseDir);
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
        try {
            geographyCSVWriter.closeFile();
            geographyDetailCSVWriter.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}