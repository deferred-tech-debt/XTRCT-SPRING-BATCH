package com.chromedata.incentives.extract.batch.listener;

import com.chromedata.incentives.extract.batch.component.CommonComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.EXIT_STATUS_FAIL;

@Component("extractReadListener")
public class ExtractReadListener<T> implements ItemReadListener<T> {

    private static final Logger LOG = LogManager.getLogger(ExtractReadListener.class);
    private JobExecution jobExecution;

    // This must be called under before job listener
    void setJobExecution(final JobExecution jobExecution) {
        this.jobExecution = jobExecution;
    }

    @Override
    public void beforeRead() {
        // Nothing to do...
    }

    @Override
    public void afterRead(final T item) {
        // Nothing to do...
    }

    @Override
    public void onReadError(final Exception ex) {
        LOG.error("Read Error...", ex);
        if(jobExecution == null) {
            System.exit(EXIT_STATUS_FAIL);
        }
        CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
    }
}
