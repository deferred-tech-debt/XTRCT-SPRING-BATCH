package com.contribute.xtrct.batch.writer.consumer;

import com.contribute.xtrct.batch.writer.ConsumerMarkerInterface;
import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.dao.model.BatchVehicleStyle;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import com.contribute.xtrct.presentation.model.VehicleStyle;
import com.contribute.xtrct.batch.component.ExtractConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("vehicleStyleWriterToConsumer")
public class ExtractVehicleStyleWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleStyleWriter.class);

    private final CSVDataSetWriter<VehicleStyle> csvWriter;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    public ExtractVehicleStyleWriter() {
        this.csvWriter = new CSVDataSetWriter<>(VehicleStyle.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        completeIncentives.removeIf(consumerPredicate);
        final List<BatchVehicleStyle> vehicleStyles = completeIncentives.stream()
                                                                        .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getVehicles())
                                                                        .flatMap(List::stream)
                                                                        .flatMap( vehicle  -> vehicle.getVehicleStyles().stream().map(style -> new BatchVehicleStyle(vehicle.getAcode(), style))
                                                                        )
                                                                        .distinct()
                                                                        .collect(Collectors.toList());

        synchronized (ExtractVehicleStyleWriter.class) {
            final Set<BatchVehicleStyle> usedStyles = Objects.nonNull(jobExecutionContext.get(ExtractConstants.STYLES_CONSUMER)) ? (Set<BatchVehicleStyle>) jobExecutionContext.get(
                ExtractConstants.STYLES_CONSUMER) : new HashSet<>();
            if(! CollectionUtils.isEmpty(usedStyles)){
                vehicleStyles.removeIf(usedStyles::contains);
            }

            if(CollectionUtils.isEmpty(vehicleStyles)){
                return;
            }

            usedStyles.addAll(vehicleStyles);
            jobExecutionContext.put(ExtractConstants.STYLES_CONSUMER, usedStyles);
        }
        writeVehicleStyle(vehicleStyles);
    }

    private void writeVehicleStyle(final List<BatchVehicleStyle> vehicleStyles) {
        LOG.debug("Started Writing vehicle styles in consumer {}", vehicleStyles.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setVehicleStyleList(vehicleStyles);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));


        dataSet.getVehicleStyleList().forEach(presentationMarshaller::addVehicleStyle);

        CSVDataSet data = presentationMarshaller.getData();

        csvWriter.writeCollection(data.getVehicleStyleList());

        ExtractConstants.VEHICLE_STYLE_CONSUMER_COUNT.getAndAdd(data.getVehicleStyleList().size());

        presentationMarshaller.resetData();

        LOG.debug("Wrote vehicle styles incentives in consumer {}", vehicleStyles.size());
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File responseFileXtl = (File) jobExecutionContext.get(ExtractConstants.CONSUMER_RESPONSE_DIR);
        try {
            csvWriter.openFile(responseFileXtl);
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
            csvWriter.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}
