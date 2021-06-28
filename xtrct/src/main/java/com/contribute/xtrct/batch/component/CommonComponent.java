package com.contribute.xtrct.batch.component;

import com.contribute.xtrct.CommandLineArgs;
import com.contribute.xtrct.business.util.ApplicationVersion;
import com.contribute.xtrct.dao.model.SignatureStackability;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * A common component to handle shared data across multi threaded batch environment
 */
@Component
public class CommonComponent {

    @Autowired
    private ApplicationVersion applicationVersion;

    private CommandLineArgs commandLineArgs;

    private Set<SignatureStackability> cachedStackabilitiesToDealer = new HashSet<>();
    private Set<Integer> cachedSignatureIdsToDealer = new HashSet<>();
    private Set<Integer> cachedSignatureHistoryIdsToDealer = new HashSet<>();

    private Set<SignatureStackability> cachedStackabilitiesToConsumer = new HashSet<>();
    private Set<Integer> cachedSignatureIdsToConsumer = new HashSet<>();
    private Set<Integer> cachedSignatureHistoryIdsToConsumer = new HashSet<>();

    public ApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    public CommandLineArgs getCommandLineArgs() {
        return commandLineArgs;
    }

    public void setCommandLineArgs(final CommandLineArgs commandLineArgs) {
        this.commandLineArgs = commandLineArgs;
    }

    public Set<SignatureStackability> getCachedStackabilitiesToDealer() {
        return cachedStackabilitiesToDealer;
    }

    public synchronized void addCachedStackabilitiesToDealer(final Set<SignatureStackability> cachedStackabilities) {
        this.cachedStackabilitiesToDealer.addAll(cachedStackabilities);
    }

    public Set<SignatureStackability> getCachedStackabilitiesToConsumer() {
        return cachedStackabilitiesToConsumer;
    }

    public synchronized void addCachedStackabilitiesToConsumer(final Set<SignatureStackability> cachedStackabilities) {
        this.cachedStackabilitiesToConsumer.addAll(cachedStackabilities);
    }

    public Set<Integer> getCachedSignatureIdsToDealer() {
        return cachedSignatureIdsToDealer;
    }

    public synchronized void addCachedSignatureIdsToDealer(final Set<Integer> cachedSignatureIds) {
        this.cachedSignatureIdsToDealer.addAll(cachedSignatureIds);
    }

    public Set<Integer> getCachedSignatureHistoryIdsToDealer() {
        return cachedSignatureHistoryIdsToDealer;
    }

    public synchronized void addCachedSignatureHistoryIdsToDealer(final Set<Integer> cachedSignatureHistoryIds) {
        this.cachedSignatureHistoryIdsToDealer.addAll(cachedSignatureHistoryIds);
    }

    public Set<Integer> getCachedSignatureIdsToConsumer() {
        return cachedSignatureIdsToConsumer;
    }

    public synchronized void addCachedSignatureIdsToConsumer(final Set<Integer> cachedSignatureIds) {
        this.cachedSignatureIdsToConsumer.addAll(cachedSignatureIds);
    }

    public Set<Integer> getCachedSignatureHistoryIdsToConsumer() {
        return cachedSignatureHistoryIdsToConsumer;
    }

    public synchronized void addCachedSignatureHistoryIdsToConsumer(final Set<Integer> cachedSignatureHistoryIds) {
        this.cachedSignatureHistoryIdsToConsumer.addAll(cachedSignatureHistoryIds);
    }

    public static synchronized void updateJobExecutionCompleteStatus(final JobExecution jobExecution, final int status) {
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        Integer completeStatus = (Integer)executionContext.get(ExtractConstants.COMPLETE_STATUS);
        completeStatus = completeStatus == null ? -1 : completeStatus;
        if(completeStatus < status) {
            executionContext.put(ExtractConstants.COMPLETE_STATUS, status);
        }

        if(status == ExtractConstants.EXIT_STATUS_FAIL) {
            jobExecution.stop();
        }
    }
}
