package com.contribute.xtrct.batch.writer.dealer;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.business.model.Incentive;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import com.contribute.xtrct.presentation.model.DeliveryType;
import com.contribute.xtrct.presentation.model.Institution;
import com.contribute.xtrct.presentation.model.LoanToValue;
import com.contribute.xtrct.presentation.model.MileageRestriction;
import com.contribute.xtrct.presentation.model.ProgramRule;
import com.contribute.xtrct.presentation.model.ProgramValue;
import com.contribute.xtrct.presentation.model.Term;
import com.contribute.xtrct.presentation.model.ValueVariation;
import com.contribute.xtrct.presentation.model.VehicleStatus;
import com.contribute.xtrct.batch.component.ExtractConstants;
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
import java.util.stream.Collectors;

@Component("incentiveWriter")
public class ExtractIncentiveWriter extends ExtractAbstractWriter<CompleteIncentive> {

    private static final Logger LOG = LogManager.getLogger(ExtractIncentiveWriter.class);

    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    private final CSVDataSetWriter<Incentive> incentiveCSVWriter;
    private final CSVDataSetWriter<VehicleStatus> vehicleStatusCSVWriter;
    private final CSVDataSetWriter<ProgramRule> programRuleCSVWriter;
    private final CSVDataSetWriter<Institution> institutionCSVWriter;
    private final CSVDataSetWriter<DeliveryType> deliveryTypeCSVWriter;
    private final CSVDataSetWriter<ValueVariation> valueVariationCSVWriter;
    private final CSVDataSetWriter<ProgramValue> programValueCSVWriter;
    private final CSVDataSetWriter<Term> termCSVWriter;
    private final CSVDataSetWriter<MileageRestriction> mileageRestrictionCSVWriter;
    private final CSVDataSetWriter<LoanToValue> loanToValueCSVDataSetWriter;

    @Autowired
    private CommonComponent commonComponent;

    public ExtractIncentiveWriter(){
        incentiveCSVWriter= new CSVDataSetWriter<>(com.contribute.xtrct.presentation.model.Incentive.class);
        vehicleStatusCSVWriter = new CSVDataSetWriter<>(VehicleStatus.class);
        programRuleCSVWriter = new CSVDataSetWriter<>(ProgramRule.class);
        institutionCSVWriter = new CSVDataSetWriter<>(Institution.class);
        deliveryTypeCSVWriter = new CSVDataSetWriter<>(DeliveryType.class);
        valueVariationCSVWriter = new CSVDataSetWriter<>(ValueVariation.class);
        programValueCSVWriter = new CSVDataSetWriter<>(ProgramValue.class);
        termCSVWriter = new CSVDataSetWriter<>(Term.class);
        mileageRestrictionCSVWriter = new CSVDataSetWriter<>(MileageRestriction.class);
        loanToValueCSVDataSetWriter = new CSVDataSetWriter<>(LoanToValue.class);

    }

    @Override
    public void write(final List<? extends CompleteIncentive> completeIncentives) throws Exception {

        final List<com.contribute.xtrct.business.model.Incentive> incentiveList = completeIncentives.stream().map(CompleteIncentive::getIncentive).collect(Collectors.toList());

        LOG.debug("Started Writing incentives in dealer {}", incentiveList.size());

        // Store results in business response object
        BatchDataSet dataSet = new BatchDataSet();
        dataSet.setIncentiveList(incentiveList);

        PresentationMarshaller presentationMarshaller = marshallerFactory.create((Locale)jobExecutionContext.get(ExtractConstants.REQUESTED_LOCALE));
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
        loanToValueCSVDataSetWriter.writeCollection(data.getLoanToValues());

        getCount(data);

        cacheIncentivesAttributes(incentiveList);

        presentationMarshaller.resetData();

        LOG.debug("Wrote incentives in dealer {}", incentiveList.size());
    }

    private void getCount(final CSVDataSet data) {
        ExtractConstants.INCENTIVES_DEALER_COUNT.getAndAdd(data.getIncentiveList().size());
        ExtractConstants.VEHICLE_STATUS_DEALER_COUNT.getAndAdd(data.getVehicleStatusList().size());
        ExtractConstants.PROGRAM_RULE_DEALER_COUNT.getAndAdd(data.getProgramRuleList().size());
        ExtractConstants.INSTITUTION_DEALER_COUNT.getAndAdd(data.getInstitutionList().size());
        ExtractConstants.DELIVERY_TYPE_DEALER_COUNT.getAndAdd(data.getDeliveryTypeList().size());
        ExtractConstants.VALUE_VARIATION_DEALER_COUNT.getAndAdd(data.getValueVariationList().size());
        ExtractConstants.PROGRAM_VALUE_DEALER_COUNT.getAndAdd(data.getProgramValueList().size());
        ExtractConstants.TERM_DEALER_COUNT.getAndAdd(data.getTermList().size());
        ExtractConstants.MILEAGE_RESTRICTION_DEALER_COUNT.getAndAdd(data.getMileageRestrictionList().size());
        ExtractConstants.LOAN_TO_VALUE_DEALER_COUNT.getAndAdd(data.getLoanToValues().size());
    }

    @SuppressWarnings("unchecked")
    private void cacheIncentivesAttributes(final List<? extends com.contribute.xtrct.business.model.Incentive> incentiveList) {
        commonComponent.addCachedSignatureIdsToDealer(incentiveList.parallelStream().map(com.contribute.xtrct.business.model.Incentive:: getSignatureId).collect(Collectors.toSet()));
        commonComponent.addCachedSignatureHistoryIdsToDealer(incentiveList.parallelStream().map(com.contribute.xtrct.business.model.Incentive:: getSignatureHistoryId).collect(Collectors.toSet()));
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
        File responseFileXtl = (File) jobExecutionContext.get(ExtractConstants.DEALER_RESPONSE_DIR);
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
            loanToValueCSVDataSetWriter.openFile(responseFileXtl);

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
            loanToValueCSVDataSetWriter.closeFile();
        } catch (IOException e) {
            LOG.error(e);
            throw new ItemStreamException(e);
        }
    }
}
