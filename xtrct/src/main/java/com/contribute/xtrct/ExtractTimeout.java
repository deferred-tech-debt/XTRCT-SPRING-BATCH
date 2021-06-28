package com.contribute.xtrct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExtractTimeout extends Thread {

    private static final Logger LOG = LogManager.getLogger(ExtractTimeout.class);
    private static final long SLEEP_INTERVAL = 60 * 1_000L;

    private long timeoutMilliseconds;

    public ExtractTimeout(int timeoutMinutes) {
        // Convert minutes to milliseconds
        this.timeoutMilliseconds = timeoutMinutes * 60 * 1_000L;
    }

    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            while ((System.currentTimeMillis() - startTime) < timeoutMilliseconds) {
                Thread.sleep(SLEEP_INTERVAL);
            }
            LOG.fatal("ExtractTimeout has noticed that this process has taken too long to run.  Terminating application!");
            System.exit(13);
        } catch(InterruptedException e) {
            LOG.fatal("Thread was interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
