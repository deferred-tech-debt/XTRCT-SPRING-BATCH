package com.contribute.xtrct;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.batch.component.ExtractConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

/**
 * Main entry point to the Extract application
 */
@EnableBatchProcessing
@SpringBootApplication
@EnableCaching
public class Main implements CommandLineRunner {

    private static final Logger LOG = LogManager.getLogger(Main.class);

    @Autowired
    private SimpleJobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Autowired
    private CommonComponent commonComponent;

    @Bean
    @Qualifier("extractDataSource4")
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository(@Qualifier("extractDataSource4") ResourcelessTransactionManager transactionManager) throws Exception {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        return mapJobRepositoryFactoryBean.getObject();
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }

    private static CommandLineArgs parseCommandLine(String... args) {
        CommandLineArgs commandLine = new CommandLineArgs();
        JCommander commandLineParser = new JCommander(commandLine);
        commandLineParser.setProgramName("incentives-extract");
        try {
            commandLineParser.parse(args);
        } catch (ParameterException e) {
            LOG.fatal("Failed to parse command line arguments", e);
            commandLineParser.usage();
            System.exit(ExtractConstants.EXIT_STATUS_FAIL);
        }

        if (commandLine.isHelp()) {
            commandLineParser.usage();
            System.exit(ExtractConstants.EXIT_STATUS_SUCCESS);
        }

        return commandLine;
    }

    @Override
    public void run(final String... args) {
        try {
            CommandLineArgs commandLine = parseCommandLine(args);
            commonComponent.setCommandLineArgs(commandLine);

            new ExtractTimeout(commandLine.getTimeout()).start();

            Locale               locale               = commandLine.getLocale();
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            JobParameters jobParameter = jobParametersBuilder
                .addString(ExtractConstants.LANGUAGE, locale.getLanguage())
                .addString(ExtractConstants.COUNTRY, locale.getCountry())
                .toJobParameters();

            final JobExecution jobExecution = jobLauncher.run(job, jobParameter);
            if(! jobExecution.getExitStatus().equals(ExitStatus.COMPLETED) || ! jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
                System.exit(ExtractConstants.EXIT_STATUS_FAIL);
            }
            System.exit((Integer) jobExecution.getExecutionContext().get(ExtractConstants.COMPLETE_STATUS));

        } catch (Exception e) {
            LOG.error("Something went wrong", e);
            System.exit(ExtractConstants.EXIT_STATUS_FAIL);
        }
    }

    public static void main(String[] args) {
        try {
            SpringApplication.run(Main.class, args).close();
        } catch (Exception e) {
            LOG.error("Error while starting the extract....", e);
            System.exit(ExtractConstants.EXIT_STATUS_FAIL);
        }
    }
}
