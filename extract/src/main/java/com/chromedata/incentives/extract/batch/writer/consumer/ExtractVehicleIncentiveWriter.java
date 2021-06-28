package com.chromedata.incentives.extract.batch.writer.consumer;

import com.chromedata.incentives.extract.batch.writer.ConsumerMarkerInterface;
import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.dao.model.BatchVehicleIncentive;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.VehicleIncentive;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.CONSUMER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.VEHICLE_INCENTIVE_CONSUMER_COUNT;

@Component("vehicleIncentiveWriterToConsumer")
public class ExtractVehicleIncentiveWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleIncentiveWriter.class);

    private final CSVDataSetWriter<VehicleIncentive> csvWriterToConsumer;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    public ExtractVehicleIncentiveWriter() {
        this.csvWriterToConsumer = new CSVDataSetWriter<>(VehicleIncentive.class);
    }

    @Override
    public void write(final List<? extends CompleteIncentive> completeIncentives) {

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(REQUESTED_LOCALE));
        completeIncentives.removeIf(consumerPredicate);
        writeInConsumerDir(completeIncentives, presentationMarshaller);

    }

    private void writeInConsumerDir(final List<? extends CompleteIncentive> completeIncentives, final PresentationMarshaller presentationMarshaller) {
        final List<BatchVehicleIncentive> vehicleIncentives = new ArrayList<>();
        completeIncentives.forEach(completeIncentive -> {
            final Integer incentiveID = Integer.valueOf(completeIncentive.getIncentive().getId());
            final List<BatchVehicleIncentive> batchVehicleIncentives = completeIncentive.getAugmentedIncentiveData().
                getVehicles().stream().
                                                                                            map(vehicle -> new BatchVehicleIncentive(incentiveID, vehicle.getAcode())).
                                                                                            collect(Collectors.toList());

            vehicleIncentives.addAll(batchVehicleIncentives);
        });

        LOG.debug("Started Writing vehicle incentives in consumer directory {}", vehicleIncentives.size());

        // Store results in business response object
        CSVDataSet data = mapToCSVDataSet(presentationMarshaller, vehicleIncentives);

        csvWriterToConsumer.writeCollection(data.getVehicleIncentiveList());

        VEHICLE_INCENTIVE_CONSUMER_COUNT.getAndAdd(data.getVehicleIncentiveList().size());

        LOG.debug("Wrote vehicle incentives in consumer directory {}", vehicleIncentives.size());
    }

    private static CSVDataSet mapToCSVDataSet(final PresentationMarshaller presentationMarshaller,
                                              final List<BatchVehicleIncentive> vehicleIncentives) {
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setVehicleIncentiveList(vehicleIncentives);

        dataSet.getVehicleIncentiveList().forEach(presentationMarshaller::addVehicleIncentive);

        return presentationMarshaller.getData();
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        final File consumerResponseDir = (File) jobExecutionContext.get(CONSUMER_RESPONSE_DIR);
        try {
            csvWriterToConsumer.openFile(consumerResponseDir);
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
            csvWriterToConsumer.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}
