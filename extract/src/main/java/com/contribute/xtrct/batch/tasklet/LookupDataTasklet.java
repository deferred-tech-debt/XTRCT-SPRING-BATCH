package com.contribute.xtrct.batch.tasklet;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.business.util.ApplicationVersion;
import com.contribute.xtrct.dao.lookup.model.Category;
import com.contribute.xtrct.dao.lookup.model.CategoryResponse;
import com.contribute.xtrct.dao.lookup.model.DeliveryType;
import com.contribute.xtrct.dao.lookup.model.DeliveryTypeResponse;
import com.contribute.xtrct.dao.lookup.model.EndRecipient;
import com.contribute.xtrct.dao.lookup.model.EndRecipientResponse;
import com.contribute.xtrct.dao.lookup.model.GroupAffiliation;
import com.contribute.xtrct.dao.lookup.model.GroupAffiliationResponse;
import com.contribute.xtrct.dao.lookup.model.Institution;
import com.contribute.xtrct.dao.lookup.model.InstitutionResponse;
import com.contribute.xtrct.dao.lookup.model.Market;
import com.contribute.xtrct.dao.lookup.model.MarketResponse;
import com.contribute.xtrct.dao.lookup.model.PreviousOwnership;
import com.contribute.xtrct.dao.lookup.model.PreviousOwnershipResponse;
import com.contribute.xtrct.dao.lookup.model.TaxRuleResponse;
import com.contribute.xtrct.dao.lookup.model.VehicleStatus;
import com.contribute.xtrct.dao.lookup.model.VehicleStatusResponse;
import com.contribute.xtrct.dao.lookup.rest.LookupDao;
import com.contribute.xtrct.dao.model.BatchDataSet;
import com.contribute.xtrct.presentation.CSVDataSetWriter;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import com.contribute.xtrct.presentation.model.LUCategory;
import com.contribute.xtrct.presentation.model.LUDeliveryType;
import com.contribute.xtrct.presentation.model.LUEndRecipient;
import com.contribute.xtrct.presentation.model.LUGroupAffiliation;
import com.contribute.xtrct.presentation.model.LUInstitution;
import com.contribute.xtrct.presentation.model.LUMarket;
import com.contribute.xtrct.presentation.model.LUPreviousOwnership;
import com.contribute.xtrct.presentation.model.LUVehicleStatus;
import com.contribute.xtrct.presentation.model.Version;
import com.contribute.xtrct.batch.component.ExtractConstants;
import com.contribute.xtrct.dao.lookup.enums.LookupType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.Instant;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component("lookupDataTasklet")
public class LookupDataTasklet implements Tasklet {

    private static final Logger LOG = LogManager.getLogger(LookupDataTasklet.class);

    private static final String INCENTIVE_DEALER_EXTRACT = "Incentives Dealer Extract";
    private static final String INCENTIVE_CONSUMER_EXTRACT = "Incentives Retail Extract";

    @Autowired
    private LookupDao lookupDao;

    @Autowired
    private MessageSource extractMessageSource;

    @Autowired
    private ApplicationVersion appVersion;


    @Autowired
    private PresentationMarshallerFactory marshallerFactory;

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        try {
            JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters();
            final Locale  locale        = new Locale(jobParameters.getString(ExtractConstants.LANGUAGE), jobParameters.getString(ExtractConstants.COUNTRY));

            List<com.contribute.xtrct.dao.lookup.model.TaxRule> taxRules = lookupDao.<TaxRuleResponse>getLookupData(LookupType.TAX_RULES.getPath(),
                                                                                                                    locale,
                                                                                                                    LookupType.TAX_RULES.getType()).getTaxRules();
            List<Category> categories = lookupDao.<CategoryResponse>getLookupData(LookupType.CATEGORIES.getPath(), locale,
                                                                                  LookupType.CATEGORIES.getType()).getCategories();
            List<DeliveryType> deliveryTypes = lookupDao.<DeliveryTypeResponse>getLookupData(LookupType.DELIVERY_TYPES.getPath(), locale,
                                                                                             LookupType.DELIVERY_TYPES.getType()).getDeliveryTypes();
            List<EndRecipient> endRecipients = lookupDao.<EndRecipientResponse>getLookupData(LookupType.END_RECIPIENTS.getPath(), locale,
                                                                                             LookupType.END_RECIPIENTS.getType()).getEndRecipients();
            List<GroupAffiliation> groupAffiliations = lookupDao.<GroupAffiliationResponse>getLookupData(LookupType.GROUP_AFFILIATIONS.getPath(), locale,
                                                                                                         LookupType.GROUP_AFFILIATIONS.getType()).getGroupAffiliations();
            List<Institution> institutions = lookupDao.<InstitutionResponse>getLookupData(LookupType.INSTITUTIONS.getPath(), locale,
                                                                                          LookupType.INSTITUTIONS.getType()).getInstitutions();
            List<PreviousOwnership> previousOwnerships = lookupDao.<PreviousOwnershipResponse>getLookupData(LookupType.PREVIOUS_OWNERSHIPS.getPath(), locale,
                                                                                                            LookupType.PREVIOUS_OWNERSHIPS.getType()).getPreviousOwnerships();
            List<VehicleStatus> vehicleStatuses = lookupDao.<VehicleStatusResponse>getLookupData(LookupType.VEHICLE_STATUSES.getPath(), locale,
                                                                                                 LookupType.VEHICLE_STATUSES.getType()).getVehicleStatus();
            List<Market> markets = lookupDao.<MarketResponse>getLookupData(LookupType.MARKETS.getPath(), locale,
                                                                           LookupType.MARKETS.getType()).getMarkets();

            // Store results in business response object
            BatchDataSet dataSet = new BatchDataSet();
            dataSet.setTaxRuleList(taxRules);
            dataSet.setCategoryList(categories);
            dataSet.setDeliveryTypeList(deliveryTypes);
            dataSet.setEndReceiptList(endRecipients);
            dataSet.setGroupAffiliationList(groupAffiliations);
            dataSet.setInstitutionList(institutions);
            dataSet.setPreviousOwnershipList(previousOwnerships);
            dataSet.setVehicleStatusList(vehicleStatuses);
            dataSet.setMarketList(markets);

            PresentationMarshaller presentationMarshaller = marshallerFactory.create(locale);

            dataSet.getTaxRuleList().forEach(presentationMarshaller::addTaxRule);
            dataSet.getCategoryList().forEach(presentationMarshaller::addCategory);
            dataSet.getDeliveryTypeList().forEach(presentationMarshaller::addDeliveryType);
            dataSet.getEndReceiptList().forEach(presentationMarshaller::addEndReceipt);
            dataSet.getGroupAffiliationList().forEach(presentationMarshaller::addGroupAffiliation);
            dataSet.getInstitutionList().forEach(presentationMarshaller::addInstitution);
            dataSet.getPreviousOwnershipList().forEach(presentationMarshaller::addPreviousOwnership);
            dataSet.getVehicleStatusList().forEach(presentationMarshaller::addVehicleStatus);
            dataSet.getMarketList().forEach(presentationMarshaller::addMarket);

            CSVDataSet data                      = presentationMarshaller.getData();
            File       dealerResponseDirectory   = (File) chunkContext.getStepContext().getJobExecutionContext().get(ExtractConstants.DEALER_RESPONSE_DIR);
            File       consumerResponseDirectory = (File) chunkContext.getStepContext().getJobExecutionContext().get(ExtractConstants.CONSUMER_RESPONSE_DIR);

            writeLookupDataByDir(data, dealerResponseDirectory, locale, INCENTIVE_DEALER_EXTRACT);
            writeLookupDataByDir(data, consumerResponseDirectory, locale, INCENTIVE_CONSUMER_EXTRACT);
            LOG.info("Lookup Data writing completed.");
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            LOG.error("Error...", e);
            CommonComponent.updateJobExecutionCompleteStatus(chunkContext.getStepContext().getStepExecution().getJobExecution(), ExtractConstants.EXIT_STATUS_FAIL);
            throw e;
        }
    }

    private void writeLookupDataByDir(final CSVDataSet data, final File responseFileXtl, Locale locale, String product) throws java.io.IOException {

        int currentYear= Calendar.getInstance(locale).get(Calendar.YEAR);
        String copyright=extractMessageSource.getMessage("copyright.message", new String[]{String.valueOf(currentYear)}, locale);

        Version                                                           version                           = new Version(product, appVersion.toString(), locale.getCountry(), locale.getLanguage(), Instant.now().toString(), copyright);
        CSVDataSetWriter<LUGroupAffiliation>                              groupAffiliationCSVDataSetWriter  = new CSVDataSetWriter<>(
            LUGroupAffiliation.class);
        CSVDataSetWriter<LUPreviousOwnership>                             previousOwnershipCSVDataSetWriter = new CSVDataSetWriter<>(LUPreviousOwnership.class);
        CSVDataSetWriter<LUDeliveryType>                                  deliveryTypeCSVDataSetWriter      = new CSVDataSetWriter<>(LUDeliveryType.class);
        CSVDataSetWriter<LUCategory>                                      categoryCSVDataSetWriter          = new CSVDataSetWriter<>(LUCategory.class);
        CSVDataSetWriter<LUInstitution>                                   institutionCSVDataSetWriter       = new CSVDataSetWriter<>(LUInstitution.class);
        CSVDataSetWriter<LUMarket>                                        marketCSVDataSetWriter            = new CSVDataSetWriter<>(LUMarket.class);
        CSVDataSetWriter<LUEndRecipient>                                  endRecipientCSVDataSetWriter      = new CSVDataSetWriter<>(LUEndRecipient.class);
        CSVDataSetWriter<LUVehicleStatus>                                 vehicleStatusCSVDataSetWriter     = new CSVDataSetWriter<>(LUVehicleStatus.class);
        CSVDataSetWriter<com.contribute.xtrct.presentation.model.TaxRule> taxRuleCSVDataSetWriter           = new CSVDataSetWriter<>(com.contribute.xtrct.presentation.model.TaxRule.class);
        CSVDataSetWriter<Version>                                         versionCSVDataSetWriter           = new CSVDataSetWriter<>(Version.class);

        try {
            groupAffiliationCSVDataSetWriter.openFile(responseFileXtl);
            previousOwnershipCSVDataSetWriter.openFile(responseFileXtl);
            deliveryTypeCSVDataSetWriter.openFile(responseFileXtl);
            categoryCSVDataSetWriter.openFile(responseFileXtl);
            institutionCSVDataSetWriter.openFile(responseFileXtl);
            marketCSVDataSetWriter.openFile(responseFileXtl);
            endRecipientCSVDataSetWriter.openFile(responseFileXtl);
            vehicleStatusCSVDataSetWriter.openFile(responseFileXtl);
            taxRuleCSVDataSetWriter.openFile(responseFileXtl);
            versionCSVDataSetWriter.openFile(responseFileXtl);

            groupAffiliationCSVDataSetWriter.writeCollection(data.getLuGroupAffiliationList());
            previousOwnershipCSVDataSetWriter.writeCollection(data.getLuPreviousOwnershipList());
            deliveryTypeCSVDataSetWriter.writeCollection(data.getLuDeliveryTypeList());
            categoryCSVDataSetWriter.writeCollection(data.getLuCategoryList());
            institutionCSVDataSetWriter.writeCollection(data.getLuInstitutionList());
            marketCSVDataSetWriter.writeCollection(data.getLuMarketList());
            endRecipientCSVDataSetWriter.writeCollection(data.getLuEndRecipientList());
            vehicleStatusCSVDataSetWriter.writeCollection(data.getLuVehicleStatusList());
            taxRuleCSVDataSetWriter.writeCollection(data.getTaxRuleList());
            versionCSVDataSetWriter.writeCollection(Collections.singletonList(version));
        } finally {

            groupAffiliationCSVDataSetWriter.closeFile();
            previousOwnershipCSVDataSetWriter.closeFile();
            deliveryTypeCSVDataSetWriter.closeFile();
            categoryCSVDataSetWriter.closeFile();
            institutionCSVDataSetWriter.closeFile();
            marketCSVDataSetWriter.closeFile();
            endRecipientCSVDataSetWriter.closeFile();
            vehicleStatusCSVDataSetWriter.closeFile();
            taxRuleCSVDataSetWriter.closeFile();
            versionCSVDataSetWriter.closeFile();
        }
    }
}
