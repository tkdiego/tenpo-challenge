package com.taka.tenpo.config.threadpool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Future;

public class ContextAwareRunnablePoolExecutor extends ThreadPoolTaskExecutor {

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(new ContextAwareRunnable(task, RequestContextHolder.currentRequestAttributes()));
    }
}
