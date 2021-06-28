package com.chromedata.incentives.extract.batch.tasklet;

import com.chromedata.incentives.extract.batch.component.CommonComponent;
import com.chromedata.incentives.extract.business.util.ApplicationVersion;
import com.chromedata.incentives.extract.dao.lookup.model.Category;
import com.chromedata.incentives.extract.dao.lookup.model.CategoryResponse;
import com.chromedata.incentives.extract.dao.lookup.model.DeliveryType;
import com.chromedata.incentives.extract.dao.lookup.model.DeliveryTypeResponse;
import com.chromedata.incentives.extract.dao.lookup.model.EndRecipient;
import com.chromedata.incentives.extract.dao.lookup.model.EndRecipientResponse;
import com.chromedata.incentives.extract.dao.lookup.model.GroupAffiliation;
import com.chromedata.incentives.extract.dao.lookup.model.GroupAffiliationResponse;
import com.chromedata.incentives.extract.dao.lookup.model.Institution;
import com.chromedata.incentives.extract.dao.lookup.model.InstitutionResponse;
import com.chromedata.incentives.extract.dao.lookup.model.Market;
import com.chromedata.incentives.extract.dao.lookup.model.MarketResponse;
import com.chromedata.incentives.extract.dao.lookup.model.PreviousOwnership;
import com.chromedata.incentives.extract.dao.lookup.model.PreviousOwnershipResponse;
import com.chromedata.incentives.extract.dao.lookup.model.TaxRuleResponse;
import com.chromedata.incentives.extract.dao.lookup.model.VehicleStatus;
import com.chromedata.incentives.extract.dao.lookup.model.VehicleStatusResponse;
import com.chromedata.incentives.extract.dao.lookup.rest.LookupDao;
import com.chromedata.incentives.extract.dao.model.BatchDataSet;
import com.chromedata.incentives.extract.presentation.CSVDataSetWriter;
import com.chromedata.incentives.extract.presentation.PresentationMarshaller;
import com.chromedata.incentives.extract.presentation.PresentationMarshallerFactory;
import com.chromedata.incentives.extract.presentation.model.CSVDataSet;
import com.chromedata.incentives.extract.presentation.model.LUCategory;
import com.chromedata.incentives.extract.presentation.model.LUDeliveryType;
import com.chromedata.incentives.extract.presentation.model.LUEndRecipient;
import com.chromedata.incentives.extract.presentation.model.LUGroupAffiliation;
import com.chromedata.incentives.extract.presentation.model.LUInstitution;
import com.chromedata.incentives.extract.presentation.model.LUMarket;
import com.chromedata.incentives.extract.presentation.model.LUPreviousOwnership;
import com.chromedata.incentives.extract.presentation.model.LUVehicleStatus;
import com.chromedata.incentives.extract.presentation.model.TaxRule;
import com.chromedata.incentives.extract.presentation.model.Version;
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

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.CONSUMER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.COUNTRY;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.DEALER_RESPONSE_DIR;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.EXIT_STATUS_FAIL;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.LANGUAGE;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.CATEGORIES;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.DELIVERY_TYPES;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.END_RECIPIENTS;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.GROUP_AFFILIATIONS;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.INSTITUTIONS;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.MARKETS;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.PREVIOUS_OWNERSHIPS;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.TAX_RULES;
import static com.chromedata.incentives.extract.dao.lookup.enums.LookupType.VEHICLE_STATUSES;

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
            final Locale  locale        = new Locale(jobParameters.getString(LANGUAGE), jobParameters.getString(COUNTRY));

            List<com.chromedata.incentives.extract.dao.lookup.model.TaxRule> taxRules = lookupDao.<TaxRuleResponse>getLookupData(TAX_RULES.getPath(),
                                                                                                                                 locale,
                                                                                                                                 TAX_RULES.getType()).getTaxRules();
            List<Category> categories = lookupDao.<CategoryResponse>getLookupData(CATEGORIES.getPath(), locale,
                                                                                  CATEGORIES.getType()).getCategories();
            List<DeliveryType> deliveryTypes = lookupDao.<DeliveryTypeResponse>getLookupData(DELIVERY_TYPES.getPath(), locale,
                                                                                             DELIVERY_TYPES.getType()).getDeliveryTypes();
            List<EndRecipient> endRecipients = lookupDao.<EndRecipientResponse>getLookupData(END_RECIPIENTS.getPath(), locale,
                                                                                             END_RECIPIENTS.getType()).getEndRecipients();
            List<GroupAffiliation> groupAffiliations = lookupDao.<GroupAffiliationResponse>getLookupData(GROUP_AFFILIATIONS.getPath(), locale,
                                                                                                         GROUP_AFFILIATIONS.getType()).getGroupAffiliations();
            List<Institution> institutions = lookupDao.<InstitutionResponse>getLookupData(INSTITUTIONS.getPath(), locale,
                                                                                          INSTITUTIONS.getType()).getInstitutions();
            List<PreviousOwnership> previousOwnerships = lookupDao.<PreviousOwnershipResponse>getLookupData(PREVIOUS_OWNERSHIPS.getPath(), locale,
                                                                                                            PREVIOUS_OWNERSHIPS.getType()).getPreviousOwnerships();
            List<VehicleStatus> vehicleStatuses = lookupDao.<VehicleStatusResponse>getLookupData(VEHICLE_STATUSES.getPath(), locale,
                                                                                                 VEHICLE_STATUSES.getType()).getVehicleStatus();
            List<Market> markets = lookupDao.<MarketResponse>getLookupData(MARKETS.getPath(), locale,
                                                                           MARKETS.getType()).getMarkets();

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
            File       dealerResponseDirectory   = (File) chunkContext.getStepContext().getJobExecutionContext().get(DEALER_RESPONSE_DIR);
            File       consumerResponseDirectory = (File) chunkContext.getStepContext().getJobExecutionContext().get(CONSUMER_RESPONSE_DIR);

            writeLookupDataByDir(data, dealerResponseDirectory, locale, INCENTIVE_DEALER_EXTRACT);
            writeLookupDataByDir(data, consumerResponseDirectory, locale, INCENTIVE_CONSUMER_EXTRACT);
            LOG.info("Lookup Data writing completed.");
            return RepeatStatus.FINISHED;
        } catch (Exception e) {
            LOG.error("Error...", e);
            CommonComponent.updateJobExecutionCompleteStatus(chunkContext.getStepContext().getStepExecution().getJobExecution(), EXIT_STATUS_FAIL);
            throw e;
        }
    }

    private void writeLookupDataByDir(final CSVDataSet data, final File responseFileXtl, Locale locale, String product) throws java.io.IOException {

        int currentYear= Calendar.getInstance(locale).get(Calendar.YEAR);
        String copyright=extractMessageSource.getMessage("copyright.message", new String[]{String.valueOf(currentYear)}, locale);

        Version version = new Version(product, appVersion.toString(), locale.getCountry(), locale.getLanguage(), Instant.now().toString(), copyright);
        CSVDataSetWriter<LUGroupAffiliation>  groupAffiliationCSVDataSetWriter  = new CSVDataSetWriter<>(LUGroupAffiliation.class);
        CSVDataSetWriter<LUPreviousOwnership> previousOwnershipCSVDataSetWriter = new CSVDataSetWriter<>(LUPreviousOwnership.class);
        CSVDataSetWriter<LUDeliveryType>      deliveryTypeCSVDataSetWriter      = new CSVDataSetWriter<>(LUDeliveryType.class);
        CSVDataSetWriter<LUCategory>          categoryCSVDataSetWriter          = new CSVDataSetWriter<>(LUCategory.class);
        CSVDataSetWriter<LUInstitution>       institutionCSVDataSetWriter       = new CSVDataSetWriter<>(LUInstitution.class);
        CSVDataSetWriter<LUMarket>            marketCSVDataSetWriter            = new CSVDataSetWriter<>(LUMarket.class);
        CSVDataSetWriter<LUEndRecipient>      endRecipientCSVDataSetWriter      = new CSVDataSetWriter<>(LUEndRecipient.class);
        CSVDataSetWriter<LUVehicleStatus>     vehicleStatusCSVDataSetWriter     = new CSVDataSetWriter<>(LUVehicleStatus.class);
        CSVDataSetWriter<TaxRule>             taxRuleCSVDataSetWriter           = new CSVDataSetWriter<>(TaxRule.class);
        CSVDataSetWriter<Version>             versionCSVDataSetWriter           = new CSVDataSetWriter<>(Version.class);

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
