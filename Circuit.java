package com.goldengekko.lhr.util.CircuitBreaker;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.PostConstruct;

public class Circuit {

    private static long FIVE_MINS_IN_MILLIS = 60000 * 5;

    private static int MAX_FAILURES = 5;

    protected AtomicInteger failureCounter;

    private Long lastExceptionOccuredMillis;

    @PostConstruct
    public void init() {
        this.failureCounter = new AtomicInteger(0);
    }

    public void newException() {
        if (System.currentTimeMillis() - lastExceptionOccuredMillis > FIVE_MINS_IN_MILLIS) {
            reset();
        }
        lastExceptionOccuredMillis = System.currentTimeMillis();
        this.failureCounter.incrementAndGet();
    }


    public boolean isOpen() {

        return (this.failureCounter.get() >= MAX_FAILURES);
    }

    public void reset() {
        failureCounter.set(0);
    }

}
