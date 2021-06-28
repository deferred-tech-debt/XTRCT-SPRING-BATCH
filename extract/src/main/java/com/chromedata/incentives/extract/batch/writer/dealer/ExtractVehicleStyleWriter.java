package com.chromedata.incentives.extract.batch.writer.dealer;

import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.dao.model.BatchVehicleStyle;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.VehicleStyle;
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
import java.util.stream.Collectors;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.DEALER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.STYLES_DEALER;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.VEHICLE_STYLE_DEALER_COUNT;

@Component("vehicleStyleWriter")
public class ExtractVehicleStyleWriter extends ExtractAbstractWriter<CompleteIncentive> {

    private static final Logger LOG = LogManager.getLogger(ExtractVehicleStyleWriter.class);

    private final CSVDataSetWriter<VehicleStyle> csvWriter;

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    public ExtractVehicleStyleWriter() {
        this.csvWriter = new CSVDataSetWriter<>(VehicleStyle.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        final List<BatchVehicleStyle> vehicleStyles = completeIncentives
                                                       .stream()
                                                       .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getVehicles())
                                                       .flatMap(List::stream)
                                                       .flatMap(vehicle1 ->
                                                                    vehicle1.getVehicleStyles().stream().map(style -> new BatchVehicleStyle(vehicle1.getAcode(), style))
                                                       )
                                                       .distinct()
                                                       .collect(Collectors.toList());

        synchronized (ExtractVehicleStyleWriter.class) {
            final Set<BatchVehicleStyle> usedStyles = Objects.nonNull(jobExecutionContext.get(STYLES_DEALER)) ? (Set<BatchVehicleStyle>) jobExecutionContext.get(
                STYLES_DEALER) : new HashSet<>();
            if(! CollectionUtils.isEmpty(usedStyles)){
                vehicleStyles.removeIf(usedStyles::contains);
            }

            if(CollectionUtils.isEmpty(vehicleStyles)){
                return;
            }
            usedStyles.addAll(vehicleStyles);
            jobExecutionContext.put(STYLES_DEALER, usedStyles);
        }
        writeVehicleStyle(vehicleStyles);
    }

    private void writeVehicleStyle(final List<BatchVehicleStyle> vehicleStyles) {
        LOG.debug("Started Writing vehicle styles in dealer {}", vehicleStyles.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setVehicleStyleList(vehicleStyles);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(REQUESTED_LOCALE));


        dataSet.getVehicleStyleList().forEach(presentationMarshaller::addVehicleStyle);

        CSVDataSet data = presentationMarshaller.getData();

        csvWriter.writeCollection(data.getVehicleStyleList());

        VEHICLE_STYLE_DEALER_COUNT.getAndAdd(data.getVehicleStyleList().size());

        presentationMarshaller.resetData();

        LOG.debug("Wrote vehicle incentives in dealer {}", vehicleStyles.size());
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File responseFileXtl = (File) jobExecutionContext.get(DEALER_RESPONSE_DIR);
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
