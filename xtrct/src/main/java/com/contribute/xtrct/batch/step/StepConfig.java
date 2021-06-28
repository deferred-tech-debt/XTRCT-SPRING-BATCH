package com.contribute.xtrct.batch.step;

import com.contribute.xtrct.batch.writer.ParallelCompositeItemWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.AugmentedIncentiveData;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

/**
 * Configuration to create steps and flow which have to be configured in incentive job
 */
@Configuration
@ComponentScan(basePackages = "com.chromedata.incentives.extract.batch")
public class StepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    // This defines the number of records eligible for write.
    // As soon as these number of records are processed by processor, spring batch will handover them to configured writer.
    @Value("${records.per.writer:1000}")
    private int chunk_size;

    // Number of threads to execute the writers configured in ParallelCompositeItemWriter
    @Value("${thread.pool.size:50}")
    private int threadsPerWriter;


    /**
     * Defines the spring batch flow executing the step configured by provided reader, processor & writer.
     * @param incentivesReader extract pagination reader
     * @param incentiveProcessor extract processor
     * @param extractCompositeWriter extract composite writer
     * @param extractReadListener extract read listener to handle errors while reading incentives
     * @return incentive's master {@link Flow}
     */
    @Bean
    public Flow incentiveMasterFlow(ItemReader<AugmentedIncentiveData> incentivesReader,
                              ItemProcessor<AugmentedIncentiveData, CompleteIncentive>  incentiveProcessor,
                              ItemWriter<CompleteIncentive> extractCompositeWriter,
                              ItemReadListener<AugmentedIncentiveData> extractReadListener) {

        final Step incentiveStep = stepBuilderFactory.get("incentiveStep")
                                 .<AugmentedIncentiveData, CompleteIncentive>chunk(chunk_size)
                                .listener(extractReadListener) // To handle errors during read incentives
                                .reader(incentivesReader)
                                .processor(incentiveProcessor)
                                .writer(extractCompositeWriter)
                                .build();

        return new FlowBuilder<Flow>("incentiveFlow").start(incentiveStep).build();
    }

    /**
     * Extract composite writer to execute the provide writer concurrently
     * @param incentiveWriter dealer's incentive writer
     * @param stackabilityWriter dealer's stackability writer
     * @param vehicleIncentiveWriter dealer's vehicle incentive writer
     * @param vehicleWriter dealer's vehicle writer
     * @param vehicleStyleWriter dealer's vehicle style writer
     * @param geographyWriter dealer's geography writer
     * @param incentiveWriterToConsumer consumer's incentive writer
     * @param stackabilityWriterToConsumer consumer's stackability writer
     * @param vehicleIncentiveWriterToConsumer consumer's vehicle incentive writer
     * @param vehicleStyleWriterToConsumer consumer's vehicle style writer
     * @param vehicleWriterToConsumer consumer's vehicle writer
     * @param geographyWriterToConsumer consumer's geography writer
     * @return {@link ParallelCompositeItemWriter}
     */
    @Bean
    public ItemWriter<CompleteIncentive> extractCompositeWriter(ItemWriter<CompleteIncentive> incentiveWriter, ItemWriter<CompleteIncentive> stackabilityWriter,
                                                                ItemWriter<CompleteIncentive> vehicleIncentiveWriter, ItemWriter<CompleteIncentive> vehicleWriter,
                                                                ItemWriter<CompleteIncentive> vehicleStyleWriter , ItemWriter<CompleteIncentive> geographyWriter,

                                                                ItemWriter<CompleteIncentive> incentiveWriterToConsumer, ItemWriter<CompleteIncentive> stackabilityWriterToConsumer,
                                                                ItemWriter<CompleteIncentive> vehicleIncentiveWriterToConsumer, ItemWriter<CompleteIncentive> vehicleStyleWriterToConsumer,
                                                                ItemWriter<CompleteIncentive> vehicleWriterToConsumer , ItemWriter<CompleteIncentive> geographyWriterToConsumer) {
        return new ParallelCompositeItemWriter<>(threadsPerWriter, incentiveWriter, vehicleIncentiveWriter,
                                                 vehicleWriter, vehicleStyleWriter,
                                                 geographyWriter, stackabilityWriter,

                                                 incentiveWriterToConsumer, stackabilityWriterToConsumer,
                                                 vehicleIncentiveWriterToConsumer, vehicleStyleWriterToConsumer,
                                                 vehicleWriterToConsumer, geographyWriterToConsumer
                                                 );
    }

    /**
     * Defines the spring batch flow executing the step for given lookup data tasklet
     * @param lookupDataTasklet lookup data tasklet
     * @return lookup data {@link Flow}
     */
    @Bean
    public Flow lookupDataFlow(Tasklet lookupDataTasklet) {

        final Step lookupDataStep = stepBuilderFactory.get("lookupDataStep")
                                       .tasklet(lookupDataTasklet)
                                        .build();

        return new FlowBuilder<Flow>("lookupDataFlow").start(lookupDataStep).build();
    }

    /**
     * Defines the spring batch flow executing the step for given stackability tasklet
     * @param stackabilityTasklet stackability tasklet
     * @return stackability {@link Flow}
     */
    @Bean
    public Flow stackabilityFlow(Tasklet stackabilityTasklet) {

        final Step stackabilityStep = stepBuilderFactory.get("stackabilityStep")
                                                      .tasklet(stackabilityTasklet)
                                                      .build();

        return new FlowBuilder<Flow>("stackabilityFlow").start(stackabilityStep).build();
    }

    /**
     * Defines the extract parallel flow which executes the provided flow concurrently
     * @param incentiveMasterFlow incentive master flow
     * @param lookupDataFlow lookup data flow
     * @return incentive parallel {@link Flow}
     */
    @Bean
    public Flow incentiveParallelFlow(Flow incentiveMasterFlow, Flow lookupDataFlow) {

        return new FlowBuilder<Flow>("incentiveParallelFlow")
            .split(new SimpleAsyncTaskExecutor())
            .add(incentiveMasterFlow, lookupDataFlow).build();
    }
}
