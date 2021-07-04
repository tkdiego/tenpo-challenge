package com.taka.tenpo.config.threadpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;

import static java.lang.String.format;

public class ContextAwareRunnable implements  Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextAwareRunnable.class);

    private Runnable task;
    private RequestAttributes context;
    private Map mdcContext;

    public ContextAwareRunnable(Runnable task, RequestAttributes context) {
        this.task = task;
        this.context = context;
        this.mdcContext = MDC.getCopyOfContextMap();
    }

    @Override
    public void run() {
        if(this.context != null) {
            RequestContextHolder.setRequestAttributes(this.context);
        }
        if(this.mdcContext != null) {
            MDC.setContextMap(this.mdcContext);
        } else {
            MDC.clear();
        }
        try {
            this.task.run();
        } catch (Exception e) {
            LOGGER.error(format("An error occurs when execute background task. Task: %s - Exception: %s", task, e));
        } finally {
            RequestContextHolder.resetRequestAttributes();
        }
    }

}
