package com.contribute.xtrct.batch.listener;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.business.util.ExtractType;
import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import com.contribute.xtrct.dao.model.ExtractWarning;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.contribute.xtrct.batch.component.ExtractConstants.CONSUMER_RESPONSE_DIR;
import static com.contribute.xtrct.batch.component.ExtractConstants.COUNTRY;
import static com.contribute.xtrct.batch.component.ExtractConstants.DEALER_RESPONSE_DIR;
import static com.contribute.xtrct.batch.component.ExtractConstants.DELIVERY_TYPE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.DELIVERY_TYPE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.EXIT_STATUS_FAIL;
import static com.contribute.xtrct.batch.component.ExtractConstants.EXIT_STATUS_SUCCESS;
import static com.contribute.xtrct.batch.component.ExtractConstants.EXIT_STATUS_WARN;
import static com.contribute.xtrct.batch.component.ExtractConstants.GEOGRAPHY_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.GEOGRAPHY_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.GEOGRAPHY_DETAIL_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.GEOGRAPHY_DETAIL_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.INCENTIVES_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.INCENTIVES_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.INSTITUTION_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.INSTITUTION_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.LANGUAGE;
import static com.contribute.xtrct.batch.component.ExtractConstants.LOAN_TO_VALUE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.LOAN_TO_VALUE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.MILEAGE_RESTRICTION_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.MILEAGE_RESTRICTION_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.PROGRAM_RULE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.PROGRAM_RULE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.PROGRAM_VALUE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.PROGRAM_VALUE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.contribute.xtrct.batch.component.ExtractConstants.STACKABILITY_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.STACKABILITY_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.TERM_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.TERM_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VALUE_VARIATION_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VALUE_VARIATION_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_INCENTIVE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_INCENTIVE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_STATUS_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_STATUS_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_STYLE_CONSUMER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.VEHICLE_STYLE_DEALER_COUNT;
import static com.contribute.xtrct.batch.component.ExtractConstants.WARNINGS;

/**
 * Extract job listener is the place to implement business logic before & after job processing
 */
@Component("incentiveJobListener")
public class ExtractIncentiveJobListener implements JobExecutionListener {

    private static final Logger LOG = LogManager.getLogger(ExtractIncentiveJobListener.class);

    @Autowired
    private CommonComponent commonComponent;

    @Autowired
    private ExtractReadListener<AugmentedIncentiveData> extractReadListener;

    private Instant executionStartTime;

    @Override
    public void beforeJob(final JobExecution jobExecution) {

        extractReadListener.setJobExecution(jobExecution);

        if (jobExecution.getJobParameters().getString(LANGUAGE) == null) {
            jobExecution.stop();
            return;
        }

        executionStartTime = Instant.now();

        String                 language         = jobExecution.getJobParameters().getString(LANGUAGE);
        String                 country          = jobExecution.getJobParameters().getString(COUNTRY);
        final ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
        final Locale           locale           = new Locale(language, country);

        File dealerResponseDir   = ExtractType.DEALER.getFile(commonComponent.getCommandLineArgs().getResponseDirectory(),
                                                              commonComponent.getApplicationVersion(),
                                                              locale);
        File consumerResponseDir = ExtractType.CONSUMER.getFile(commonComponent.getCommandLineArgs().getResponseDirectory(),
                                                                commonComponent.getApplicationVersion(),
                                                                locale);
        deleteFileIfExists(ExtractType.DEALER, dealerResponseDir);
        deleteFileIfExists(ExtractType.CONSUMER, consumerResponseDir);
        if (!dealerResponseDir.mkdir()) {
            LOG.error("Unable to create directory {}", dealerResponseDir.getAbsolutePath());
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
            jobExecution.stop();
            return;
        }

        if (!consumerResponseDir.mkdir()) {
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
            LOG.error("Unable to create directory {}", consumerResponseDir.getAbsolutePath());
            jobExecution.stop();
            return;
        }

        jobExecutionContext.put(WARNINGS, new ArrayList<ExtractWarning>());
        jobExecutionContext.put(REQUESTED_LOCALE, locale);
        jobExecutionContext.put(DEALER_RESPONSE_DIR, dealerResponseDir);
        jobExecutionContext.put(CONSUMER_RESPONSE_DIR, consumerResponseDir);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterJob(final JobExecution jobExecution) {
        if (jobExecution.getJobParameters().getString(LANGUAGE) == null) {
            return;
        }
        final ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();

        final File   dealerResponseDir = (File) jobExecutionContext.get(DEALER_RESPONSE_DIR);
        final String dealerExtractZip  = dealerResponseDir.getAbsolutePath() + ".zip";

        final File   consumerResponseDir = (File) jobExecutionContext.get(CONSUMER_RESPONSE_DIR);
        final String consumerExtractZip  = consumerResponseDir.getAbsolutePath() + ".zip";

        try {
            ExtractIncentiveJobListener.zipIt(dealerResponseDir, dealerExtractZip);
            ExtractIncentiveJobListener.zipIt(consumerResponseDir, consumerExtractZip);
        } catch (IOException e) {
            LOG.error("Unable to create zip file", e);
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
            jobExecution.stop();
        }

        try {
            deleteDirectory(dealerResponseDir);
            deleteDirectory(consumerResponseDir);
        } catch (IOException e) {
            LOG.error("Unable to delete the directory ", e);
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
            jobExecution.stop();
        }

        List<ExtractWarning> warnings = (List<ExtractWarning>) jobExecutionContext.get(WARNINGS);
        if (!CollectionUtils.isEmpty(warnings)) {
            warnings.forEach(extractWarning -> LOG.warn(extractWarning.getMessage(), extractWarning.getException()));
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_WARN);
        }

        final Instant  executionEndTime = Instant.now();
        final Duration executionTime    = Duration.between(executionStartTime, executionEndTime);

        LOG.info("Job completed {} with Execution Time: {}",
                 jobExecution.getJobInstance().getJobName(),
                 DurationFormatUtils.formatDuration(executionTime.toMillis(), "H:mm:ss", true));
        ExtractIncentiveJobListener.logExtractedRecordCounters();
        CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_SUCCESS);
    }

    private static void logExtractedRecordCounters() {
        LOG.info("Number of Incentives written for Dealer >>  {}", INCENTIVES_DEALER_COUNT.get());
        LOG.info("Number of Regions written for Dealer >>  {}", GEOGRAPHY_DEALER_COUNT.get());
        LOG.info("Number of RegionDetails written for Dealer >>  {}", GEOGRAPHY_DETAIL_DEALER_COUNT.get());
        LOG.info("Number of VehicleStatuses written for Dealer >>  {}", VEHICLE_STATUS_DEALER_COUNT.get());
        LOG.info("Number of ProgramRules written for Dealer >>  {}", PROGRAM_RULE_DEALER_COUNT.get());
        LOG.info("Number of Institutions written for Dealer >>  {}", INSTITUTION_DEALER_COUNT.get());
        LOG.info("Number of DeliveryTypes written for Dealer >>  {}", DELIVERY_TYPE_DEALER_COUNT.get());
        LOG.info("Number of ValueVariations written for Dealer >>  {}", VALUE_VARIATION_DEALER_COUNT.get());
        LOG.info("Number of ProgramValues written for Dealer >>  {}", PROGRAM_VALUE_DEALER_COUNT.get());
        LOG.info("Number of Terms written for Dealer >>  {}", TERM_DEALER_COUNT.get());
        LOG.info("Number of MileageRestrictions written for Dealer >>  {}", MILEAGE_RESTRICTION_DEALER_COUNT.get());
        LOG.info("Number of LoanToValue written for Consumer >>  {}", LOAN_TO_VALUE_DEALER_COUNT.get());

        LOG.info("Number of VehicleIncentives written for Dealer >>  {}", VEHICLE_INCENTIVE_DEALER_COUNT.get());
        LOG.info("Number of VehicleStyles written for Dealer >>  {}", VEHICLE_STYLE_DEALER_COUNT.get());
        LOG.info("Number of Vehicles written for Dealer >>  {}", VEHICLE_DEALER_COUNT.get());
        LOG.info("Number of Stackabilities written for Dealer >>  {}", STACKABILITY_DEALER_COUNT.get());

        LOG.info("Number of Incentives written for Consumer >>  {}", INCENTIVES_CONSUMER_COUNT.get());
        LOG.info("Number of Regions written for Consumer >>  {}", GEOGRAPHY_CONSUMER_COUNT.get());
        LOG.info("Number of RegionDetails written for Consumer >>  {}", GEOGRAPHY_DETAIL_CONSUMER_COUNT.get());
        LOG.info("Number of VehicleStatuses written for Consumer >>  {}", VEHICLE_STATUS_CONSUMER_COUNT.get());
        LOG.info("Number of ProgramRules written for Consumer >>  {}", PROGRAM_RULE_CONSUMER_COUNT.get());
        LOG.info("Number of Institutions written for Consumer >>  {}", INSTITUTION_CONSUMER_COUNT.get());
        LOG.info("Number of DeliveryTypes written for Consumer >>  {}", DELIVERY_TYPE_CONSUMER_COUNT.get());
        LOG.info("Number of ValueVariations written for Consumer >>  {}", VALUE_VARIATION_CONSUMER_COUNT.get());
        LOG.info("Number of ProgramValues written for Consumer >>  {}", PROGRAM_VALUE_CONSUMER_COUNT.get());
        LOG.info("Number of Terms written for Consumer >>  {}", TERM_CONSUMER_COUNT.get());
        LOG.info("Number of MileageRestrictions written for Consumer >>  {}", MILEAGE_RESTRICTION_CONSUMER_COUNT.get());
        LOG.info("Number of LoanToValue written for Consumer >>  {}", LOAN_TO_VALUE_CONSUMER_COUNT.get());
        LOG.info("Number of VehicleIncentives written for Consumer >>  {}", VEHICLE_INCENTIVE_CONSUMER_COUNT.get());
        LOG.info("Number of VehicleStyles written for Consumer >>  {}", VEHICLE_STYLE_CONSUMER_COUNT.get());
        LOG.info("Number of Vehicles written for Consumer >>  {}", VEHICLE_CONSUMER_COUNT.get());
        LOG.info("Number of Stackabilities written for Consumer >>  {}", STACKABILITY_CONSUMER_COUNT.get());
    }

    /**
     * Since it is timestamped, this should never happen, but just to be safe...
     *
     * @param extractType extractType
     * @param file        file
     */
    private static void deleteFileIfExists(ExtractType extractType, File file) {
        File existingZipFile = new File(file.getAbsolutePath() + ".zip");
        if (existingZipFile.exists()) {
            LOG.info("{} response directory already exists, deleting...", extractType);
            existingZipFile.delete();
        }

        if (file.exists()) {
            LOG.info("{} response file already exists, deleting...", extractType);
            try {
                deleteDirectory(file);
            } catch (IOException e) {
                LOG.fatal(String.format("Unable to delete the existing file %s", file.getAbsolutePath()), e);
            }
        }
    }

    /**
     * Delete the directory and it's content recursively
     *
     * @param file instance of file or directory
     * @throws IOException IO Exception
     */
    private static void deleteDirectory(final File file) throws IOException {
        if (file.isDirectory()) {
            try (Stream<Path> paths = Files.walk(file.toPath())) {
                Iterator<Path> pathIterator = paths.iterator();
                while (pathIterator.hasNext()) {
                    File fileToDelete = pathIterator.next().toFile();
                    fileToDelete.delete();
                }
            }
        }
        file.delete();
    }

    private static void zipIt(File sourceDirPath, String zipFilePath) throws IOException {
        Path zipPath    = Paths.get(zipFilePath);
        Path sourcePath = sourceDirPath.toPath();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath));
             Stream<Path> paths = Files.walk(sourcePath)) {

            paths.filter(path -> !Files.isDirectory(path))
                 .forEach(path -> {
                     ZipEntry zipEntry = new ZipEntry(sourcePath.relativize(path).toString());
                     try {
                         zipOutputStream.putNextEntry(zipEntry);
                         Files.copy(path, zipOutputStream);
                         zipOutputStream.closeEntry();
                     } catch (IOException e) {
                         LOG.error("Unable to zip {}", zipEntry.getName());
                     }
                 });
        }
    }
}
