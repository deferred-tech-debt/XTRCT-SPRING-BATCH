package com.contribute.xtrct.batch.writer;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.batch.component.ExtractConstants;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;

public abstract class ExtractAbstractWriter<T> implements ItemStreamWriter<T> {

    protected ExecutionContext jobExecutionContext;

    private JobExecution jobExecution;

    private Boolean isClosed = false;

    void beforeStep(final StepExecution stepExecution) {
        this.jobExecution = stepExecution.getJobExecution();
        this.jobExecutionContext = stepExecution.getJobExecution().getExecutionContext();
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {}

    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {}

    @Override
    public void close() throws ItemStreamException {}

    void setClosed(final Boolean closed) {
        isClosed = closed;
    }

    Boolean isClosed() {
        return isClosed;
    }

    void updateJobErrorStatus() {
        CommonComponent.updateJobExecutionCompleteStatus(jobExecution, ExtractConstants.EXIT_STATUS_FAIL);
    }
}
