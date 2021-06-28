package com.chromedata.incentives.extract.batch.writer.consumer;

import com.chromedata.incentives.extract.batch.component.CommonComponent;
import com.chromedata.incentives.extract.batch.writer.ConsumerMarkerInterface;
import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.business.model.Incentive;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.DeliveryType;
import com.chromedata.incentives.extract.presentation.model.Institution;
import com.chromedata.incentives.extract.presentation.model.LoanToValue;
import com.chromedata.incentives.extract.presentation.model.MileageRestriction;
import com.chromedata.incentives.extract.presentation.model.ProgramRule;
import com.chromedata.incentives.extract.presentation.model.ProgramValue;
import com.chromedata.incentives.extract.presentation.model.Term;
import com.chromedata.incentives.extract.presentation.model.ValueVariation;
import com.chromedata.incentives.extract.presentation.model.VehicleStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.CONSUMER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.DELIVERY_TYPE_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.INCENTIVES_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.INSTITUTION_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.LOAN_TO_VALUE_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.MILEAGE_RESTRICTION_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.PROGRAM_RULE_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.PROGRAM_VALUE_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.TERM_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.VALUE_VARIATION_CONSUMER_COUNT;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.VEHICLE_STATUS_CONSUMER_COUNT;

@Component("incentiveWriterToConsumer")
public class ExtractIncentiveWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    private static final Logger LOG = LogManager.getLogger(ExtractIncentiveWriter.class);

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    private final CSVDataSetWriter<com.chromedata.incentives.extract.presentation.model.Incentive>  incentiveCSVWriter;
    private final CSVDataSetWriter<VehicleStatus> vehicleStatusCSVWriter;
    private final CSVDataSetWriter<ProgramRule> programRuleCSVWriter;
    private final CSVDataSetWriter<Institution> institutionCSVWriter;
    private final CSVDataSetWriter<DeliveryType> deliveryTypeCSVWriter;
    private final CSVDataSetWriter<ValueVariation> valueVariationCSVWriter;
    private final CSVDataSetWriter<ProgramValue> programValueCSVWriter;
    private final CSVDataSetWriter<Term> termCSVWriter;
    private final CSVDataSetWriter<MileageRestriction> mileageRestrictionCSVWriter;
    private final CSVDataSetWriter<LoanToValue> loanToValueCSVWriter;


    @Autowired
    private CommonComponent commonComponent;

    public ExtractIncentiveWriter(){
        incentiveCSVWriter= new CSVDataSetWriter<>(com.chromedata.incentives.extract.presentation.model.Incentive.class);
        vehicleStatusCSVWriter = new CSVDataSetWriter<>(VehicleStatus.class);
        programRuleCSVWriter = new CSVDataSetWriter<>(ProgramRule.class);
        institutionCSVWriter = new CSVDataSetWriter<>(Institution.class);
        deliveryTypeCSVWriter = new CSVDataSetWriter<>(DeliveryType.class);
        valueVariationCSVWriter = new CSVDataSetWriter<>(ValueVariation.class);
        programValueCSVWriter = new CSVDataSetWriter<>(ProgramValue.class);
        termCSVWriter = new CSVDataSetWriter<>(Term.class);
        mileageRestrictionCSVWriter = new CSVDataSetWriter<>(MileageRestriction.class);
        loanToValueCSVWriter = new CSVDataSetWriter<>(LoanToValue.class);


    }

    @Override
    public void write(final List<? extends CompleteIncentive> completeIncentives) {

        completeIncentives.removeIf(consumerPredicate);
        final List<Incentive> incentiveList = completeIncentives.stream().map(CompleteIncentive::getIncentive).collect(Collectors.toList());

        LOG.debug("Started Writing incentives in consumer {}", incentiveList.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setIncentiveList(incentiveList);

        PresentationMarshaller presentationMarshaller= marshallerFactory.create((Locale)jobExecutionContext.get(REQUESTED_LOCALE));
        dataSet.getIncentiveList().forEach(presentationMarshaller::addIncentive);

        CSVDataSet data = presentationMarshaller.getData();
        incentiveCSVWriter.writeCollection(data.getIncentiveList());
        vehicleStatusCSVWriter.writeCollection(data.getVehicleStatusList());
        programRuleCSVWriter.writeCollection(data.getProgramRuleList());
        institutionCSVWriter.writeCollection(data.getInstitutionList());
        deliveryTypeCSVWriter.writeCollection(data.getDeliveryTypeList());
        valueVariationCSVWriter.writeCollection(data.getValueVariationList());
        programValueCSVWriter.writeCollection(data.getProgramValueList());
        termCSVWriter.writeCollection(data.getTermList());
        mileageRestrictionCSVWriter.writeCollection(data.getMileageRestrictionList());
        loanToValueCSVWriter.writeCollection(data.getLoanToValues());

        getCount(data);

        cacheIncentivesAttributes(incentiveList);

        presentationMarshaller.resetData();

        LOG.debug("Wrote incentives in consumer {}", incentiveList.size());
    }

    private void getCount(final CSVDataSet data) {
        INCENTIVES_CONSUMER_COUNT.getAndAdd(data.getIncentiveList().size());
        VEHICLE_STATUS_CONSUMER_COUNT.getAndAdd(data.getVehicleStatusList().size());
        PROGRAM_RULE_CONSUMER_COUNT.getAndAdd(data.getProgramRuleList().size());
        INSTITUTION_CONSUMER_COUNT.getAndAdd(data.getInstitutionList().size());
        DELIVERY_TYPE_CONSUMER_COUNT.getAndAdd(data.getDeliveryTypeList().size());
        VALUE_VARIATION_CONSUMER_COUNT.getAndAdd(data.getValueVariationList().size());
        PROGRAM_VALUE_CONSUMER_COUNT.getAndAdd(data.getProgramValueList().size());
        TERM_CONSUMER_COUNT.getAndAdd(data.getTermList().size());
        MILEAGE_RESTRICTION_CONSUMER_COUNT.getAndAdd(data.getMileageRestrictionList().size());
        LOAN_TO_VALUE_CONSUMER_COUNT.getAndAdd(data.getLoanToValues().size());
    }

    @SuppressWarnings("unchecked")
    private void cacheIncentivesAttributes(final List<? extends Incentive> incentiveList) {
        commonComponent.addCachedSignatureIdsToConsumer(incentiveList.parallelStream().map(Incentive:: getSignatureId).collect(Collectors.toSet()));
        commonComponent.addCachedSignatureHistoryIdsToConsumer(incentiveList.parallelStream().map(Incentive:: getSignatureHistoryId).collect(Collectors.toSet()));
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File responseFileXtl = (File) jobExecutionContext.get(CONSUMER_RESPONSE_DIR);
        try {
            incentiveCSVWriter.openFile(responseFileXtl);
            vehicleStatusCSVWriter.openFile(responseFileXtl);
            programRuleCSVWriter.openFile(responseFileXtl);
            institutionCSVWriter.openFile(responseFileXtl);
            deliveryTypeCSVWriter.openFile(responseFileXtl);
            valueVariationCSVWriter.openFile(responseFileXtl);
            programValueCSVWriter.openFile(responseFileXtl);
            termCSVWriter.openFile(responseFileXtl);
            mileageRestrictionCSVWriter.openFile(responseFileXtl);
            loanToValueCSVWriter.openFile(responseFileXtl);

        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }

    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {
        // Nothing to update
    }

    @Override
    public void close() throws ItemStreamException {
        try {
            incentiveCSVWriter.closeFile();
            vehicleStatusCSVWriter.closeFile();
            programRuleCSVWriter.closeFile();
            institutionCSVWriter.closeFile();
            deliveryTypeCSVWriter.closeFile();
            valueVariationCSVWriter.closeFile();
            programValueCSVWriter.closeFile();
            termCSVWriter.closeFile();
            mileageRestrictionCSVWriter.closeFile();
            loanToValueCSVWriter.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}
