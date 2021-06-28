package com.contribute.xtrct.batch.writer.consumer;

import com.contribute.xtrct.batch.writer.ConsumerMarkerInterface;
import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.dao.model.Vehicle;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
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

@Component("vehicleWriterToConsumer")
public class ExtractVehicleWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleWriter.class);

    private CSVDataSetWriter<Vehicle> csvWriter;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    public ExtractVehicleWriter() {
        this.csvWriter = new CSVDataSetWriter<>(com.contribute.xtrct.presentation.model.Vehicle.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        completeIncentives.removeIf(consumerPredicate);
        final List<com.contribute.xtrct.dao.model.Vehicle> vehicleList = completeIncentives.stream()
                                                                                           .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getVehicles())
                                                                                           .flatMap(List::stream)
                                                                                           .distinct()
                                                                                           .collect(Collectors.toList());

        synchronized (ExtractVehicleWriter.class) {
            final Set<String> usedAcodes = Objects.nonNull(jobExecutionContext.get(ExtractConstants.ACODES_CONSUMER)) ? (Set<String>) jobExecutionContext.get(
                ExtractConstants.ACODES_CONSUMER) : new HashSet<>();
            if (!CollectionUtils.isEmpty(usedAcodes)) {
                vehicleList.removeIf(vehicle -> usedAcodes.contains(vehicle.getAcode()));
            }

            if (CollectionUtils.isEmpty(vehicleList)) {
                return;
            }

            usedAcodes.addAll(vehicleList.stream().map(com.contribute.xtrct.dao.model.Vehicle::getAcode).collect(Collectors.toSet()));
            jobExecutionContext.put(ExtractConstants.ACODES_CONSUMER, usedAcodes);
        }
        writeVehicle(vehicleList);
    }

    private void writeVehicle(final List<com.contribute.xtrct.dao.model.Vehicle> vehicleList) {

        LOG.debug("Started Writing vehicle {}", vehicleList.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setVehicleList(vehicleList);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale) jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));

        dataSet.getVehicleList().forEach(presentationMarshaller::addVehicle);

        CSVDataSet data = presentationMarshaller.getData();

        csvWriter.writeCollection(data.getVehicleList());

        ExtractConstants.VEHICLE_DEALER_COUNT.getAndAdd(data.getVehicleList().size());

        presentationMarshaller.resetData();

        LOG.debug("Wrote vehicle {}", vehicleList.size());
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
