package com.contribute.xtrct.batch.writer.consumer;

import com.contribute.xtrct.batch.component.ExtractConstants;
import com.contribute.xtrct.batch.writer.ConsumerMarkerInterface;
import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.dao.model.BatchGeographiesDetail;
import com.contribute.xtrct.dao.model.BatchGeography;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import com.contribute.xtrct.presentation.model.LURegion;
import com.contribute.xtrct.presentation.model.RegionDetail;
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
            final Set<Integer> usedGeoIds = Objects.nonNull(jobExecutionContext.get(ExtractConstants.GEO_IDS_CONSUMER)) ? (Set<Integer>) jobExecutionContext.get(
                ExtractConstants.GEO_IDS_CONSUMER) : new HashSet<>();
            if (!CollectionUtils.isEmpty(usedGeoIds)) {
                geographies.removeIf(geography -> usedGeoIds.contains(geography.getGeographyId()));
            }

            if (CollectionUtils.isEmpty(geographies)) {
                return;
            }
            usedGeoIds.addAll(geographies.stream().map(BatchGeography::getGeographyId).collect(Collectors.toSet()));
            jobExecutionContext.put(ExtractConstants.GEO_IDS_CONSUMER, usedGeoIds);
        }
        writeGeographies(geographies);
    }

    private void writeGeographies(final List<BatchGeography> geographies) throws IOException {
        LOG.debug("Started Writing geographies in consumer {}", geographies.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setGeographyList(geographies);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));

        dataSet.getGeographyList().forEach(presentationMarshaller::addGeography);

        CSVDataSet data = presentationMarshaller.getData();

        geographyCSVWriter.writeCollection(data.getLuRegionList());
        ExtractConstants.GEOGRAPHY_CONSUMER_COUNT.getAndAdd(data.getLuRegionList().size());
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


        PresentationMarshaller presentationMarshaller= marshallerFactory.create((Locale)jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));

        geographiesDetails.forEach(presentationMarshaller::addGeographyDetail);

        CSVDataSet data = presentationMarshaller.getData();

        geographyDetailCSVWriter.writeCollection(data.getGeoDetailList());

        ExtractConstants.GEOGRAPHY_DETAIL_CONSUMER_COUNT.getAndAdd(data.getGeoDetailList().size());
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
