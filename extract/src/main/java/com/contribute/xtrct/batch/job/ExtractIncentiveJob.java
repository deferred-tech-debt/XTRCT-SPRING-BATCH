package com.contribute.xtrct.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Extract Job configuration to create incentive job
 */
@Configuration
public class ExtractIncentiveJob {

    @Bean
    public Job incentivesJob(JobBuilderFactory jobBuilderFactory,
                             @Qualifier("incentiveJobListener") JobExecutionListener xtlIncentiveListener,
                             Flow incentiveParallelFlow,
                             Flow stackabilityFlow) {

        return jobBuilderFactory.get("incentivesJob")
                                .incrementer(new RunIdIncrementer())
                                .listener(xtlIncentiveListener)
                                .start(incentiveParallelFlow)
                                .next(stackabilityFlow)
                                .build().build();
    }
}
