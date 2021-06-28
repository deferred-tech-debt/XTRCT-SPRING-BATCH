package com.contribute.xtrct.batch.writer;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.batch.component.ExtractConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Composite writer for given type <T> which executes the N number item writer concurrently
 * @param <T> Type of Item Writer
 *
 * @author Vivek Gupta
 */
public class ParallelCompositeItemWriter<T> implements ItemStreamWriter<T>, InitializingBean {

    private static final Logger LOG = LogManager.getLogger(ParallelCompositeItemWriter.class);

    private List<ItemWriter<? super T>> delegates;

    private ExecutorService executorService;

    private List<Future<Boolean>> allTasks;

    @SafeVarargs
    public ParallelCompositeItemWriter(int threadsPerWriter, ItemWriter<? super T> ... delegates) {
        this.setDelegates(Stream.of(delegates).collect(Collectors.toList()));
        this.setTaskExecutor(Executors.newFixedThreadPool(threadsPerWriter, Executors.defaultThreadFactory()));
        this.allTasks = new ArrayList<>();
    }

    @Override
    public void write(final List<? extends T> item) {

        if(CollectionUtils.isEmpty(item)) {
            return;
        }

        List<Future<Boolean>> tasks = delegates.parallelStream()
                                               .map(writer -> (Callable<Boolean>)
                                                        () -> {
                                                                try{
                                                                    if(writer instanceof ConsumerMarkerInterface) {
                                                                        writer.write(new ArrayList<>(item));
                                                                    } else {
                                                                        writer.write(item);
                                                                    }
                                                                    return true;
                                                                } catch (Exception e) {
                                                                    LOG.error("Error", e);
                                                                    ((ExtractAbstractWriter) writer).updateJobErrorStatus();
                                                                    throw e;
                                                                }
                                                            }
                                                    )
                                               .map(executorService::submit)
                                               .collect(Collectors.toList());

        this.allTasks.addAll(tasks);
        LOG.debug("Adding {} more writer instance. Total active writers's instance - {} / {}", tasks.size(),
                 this.allTasks.parallelStream().filter(task-> !task.isDone() && !task.isCancelled()).count(), this.allTasks.size());

        // Removing the completed task
        this.allTasks.removeIf(task-> task.isDone() || task.isCancelled());
    }

    void setTaskExecutor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void setDelegates(List<ItemWriter<? super T>> delegates) {
        this.delegates = delegates;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(executorService, "Task executor needs to be set");
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        for (final ItemWriter<? super T> writer : delegates) {
            if(writer instanceof ExtractAbstractWriter) {
                ((ExtractAbstractWriter) writer).beforeStep(stepExecution);
            }
        }
    }

    @AfterStep
    public void afterStep(final StepExecution stepExecution) {
        LOG.debug("Waiting for all pending writers to complete. Total active writers's instance - {} / {}",
                 this.allTasks.parallelStream().filter(task-> !task.isDone() && !task.isCancelled()).count(),
                 this.allTasks.size());
        this.allTasks.parallelStream().forEach(future -> {
            try {
                 future.get();
            } catch (InterruptedException | ExecutionException interruptedException) {
                LOG.error("A pending writer interrupted with errors", interruptedException);
                CommonComponent.updateJobExecutionCompleteStatus(stepExecution.getJobExecution(), ExtractConstants.EXIT_STATUS_FAIL);
            }
        });

        LOG.info("All writers are completed.");
    }

    @Override
    public void close() throws ItemStreamException {
        for (ItemWriter<? super T> writer : delegates) {
            if (writer instanceof ExtractAbstractWriter) {
                ExtractAbstractWriter abstractWriter = ((ExtractAbstractWriter) writer);
                if (! abstractWriter.isClosed()) {
                    try {
                        abstractWriter.close();
                        abstractWriter.setClosed(true);
                    } catch (Exception e) {
                        LOG.error("Error...", e);
                        abstractWriter.setClosed(false);
                        abstractWriter.updateJobErrorStatus();
                    }
                }
            }
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        for (ItemWriter<? super T> writer : delegates) {
            if (writer instanceof ExtractAbstractWriter) {
                ExtractAbstractWriter abstractWriter = ((ExtractAbstractWriter) writer);
                try {
                    abstractWriter.open(executionContext);
                } catch (Exception e) {
                    LOG.error("Error...", e);
                    abstractWriter.updateJobErrorStatus();
                }
            }
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        for (ItemWriter<? super T> writer : delegates) {
            if (writer instanceof ItemStream) {
                ((ItemStream) writer).update(executionContext);
            }
        }
    }
}
