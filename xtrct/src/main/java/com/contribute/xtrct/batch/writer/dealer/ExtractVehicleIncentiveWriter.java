package com.contribute.xtrct.batch.writer.dealer;

import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.dao.model.BatchVehicleIncentive;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import com.contribute.xtrct.presentation.model.VehicleIncentive;
import com.contribute.xtrct.batch.component.ExtractConstants;
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
import java.util.stream.Collectors;

@Component("vehicleIncentiveWriter")
public class ExtractVehicleIncentiveWriter extends ExtractAbstractWriter<CompleteIncentive> {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleIncentiveWriter.class);

    private final CSVDataSetWriter<VehicleIncentive> csvWriterToDealer;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    public ExtractVehicleIncentiveWriter() {
        this.csvWriterToDealer = new CSVDataSetWriter<>(VehicleIncentive.class);
    }

    @Override
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));
        writeInDealerDir(completeIncentives, presentationMarshaller);
    }

    private void writeInDealerDir(final List<? extends CompleteIncentive> completeIncentives, final PresentationMarshaller presentationMarshaller) {
        final List<BatchVehicleIncentive> vehicleIncentives = new ArrayList<>();
        completeIncentives.forEach(completeIncentive -> {
            final Integer incentiveID = Integer.valueOf(completeIncentive.getIncentive().getId());
            final List<BatchVehicleIncentive> batchVehicleIncentives = completeIncentive.getAugmentedIncentiveData().
                                                                        getVehicles().stream().
                                                                        map(vehicle -> new BatchVehicleIncentive(incentiveID, vehicle.getAcode())).
                                                                        collect(Collectors.toList());

            vehicleIncentives.addAll(batchVehicleIncentives);
        });

        LOG.debug("Started Writing vehicle incentives in dealer {}", vehicleIncentives.size());

        // Store results in business response object
        CSVDataSet data = mapToCSVDataSet(presentationMarshaller, vehicleIncentives);

        csvWriterToDealer.writeCollection(data.getVehicleIncentiveList());

        ExtractConstants.VEHICLE_INCENTIVE_DEALER_COUNT.getAndAdd(data.getVehicleIncentiveList().size());

        LOG.debug("Wrote vehicle incentives in dealer {}", vehicleIncentives.size());
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
        final File dealerResponseDir = (File) jobExecutionContext.get(ExtractConstants.DEALER_RESPONSE_DIR);
        try {
            csvWriterToDealer.openFile(dealerResponseDir);
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
            csvWriterToDealer.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}
