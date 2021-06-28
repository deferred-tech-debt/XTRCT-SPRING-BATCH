package com.chromedata.incentives.extract.batch.writer.consumer;

import com.chromedata.incentives.extract.batch.writer.ConsumerMarkerInterface;
import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.dao.model.Vehicle;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
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

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.ACODES_CONSUMER;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.CONSUMER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.VEHICLE_DEALER_COUNT;

@Component("vehicleWriterToConsumer")
public class ExtractVehicleWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleWriter.class);

    private CSVDataSetWriter<com.chromedata.incentives.extract.presentation.model.Vehicle> csvWriter;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    public ExtractVehicleWriter() {
        this.csvWriter = new CSVDataSetWriter<>(com.chromedata.incentives.extract.presentation.model.Vehicle.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        completeIncentives.removeIf(consumerPredicate);
        final List<Vehicle> vehicleList = completeIncentives.stream()
                                                            .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getVehicles())
                                                            .flatMap(List::stream)
                                                            .distinct()
                                                            .collect(Collectors.toList());

        synchronized (ExtractVehicleWriter.class) {
            final Set<String> usedAcodes = Objects.nonNull(jobExecutionContext.get(ACODES_CONSUMER)) ? (Set<String>) jobExecutionContext.get(
                ACODES_CONSUMER) : new HashSet<>();
            if (!CollectionUtils.isEmpty(usedAcodes)) {
                vehicleList.removeIf(vehicle -> usedAcodes.contains(vehicle.getAcode()));
            }

            if (CollectionUtils.isEmpty(vehicleList)) {
                return;
            }

            usedAcodes.addAll(vehicleList.stream().map(Vehicle::getAcode).collect(Collectors.toSet()));
            jobExecutionContext.put(ACODES_CONSUMER, usedAcodes);
        }
        writeVehicle(vehicleList);
    }

    private void writeVehicle(final List<Vehicle> vehicleList) {

        LOG.debug("Started Writing vehicle {}", vehicleList.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setVehicleList(vehicleList);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale) jobExecutionContext.get(REQUESTED_LOCALE));

        dataSet.getVehicleList().forEach(presentationMarshaller::addVehicle);

        CSVDataSet data = presentationMarshaller.getData();

        csvWriter.writeCollection(data.getVehicleList());

        VEHICLE_DEALER_COUNT.getAndAdd(data.getVehicleList().size());

        presentationMarshaller.resetData();

        LOG.debug("Wrote vehicle {}", vehicleList.size());
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File responseFileXtl = (File) jobExecutionContext.get(CONSUMER_RESPONSE_DIR);
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
