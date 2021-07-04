package com.taka.tenpo.config;

import com.taka.tenpo.config.threadpool.ContextAwareRunnablePoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncTaskConfiguration {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAX_POOL_SIZE = 5;
    private static final int KEEP_ALIVE_SEC = 60;
    private static final int QUEUE_CAPACITY = 10;
    private static final String PREFIX = "async-task";

    @Bean(name = "asyncTaskExecutor")
    public Executor createAsyncRunnableExecutor() {
        ContextAwareRunnablePoolExecutor asyncTaskExecutor = new ContextAwareRunnablePoolExecutor();
        asyncTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        asyncTaskExecutor.setKeepAliveSeconds(KEEP_ALIVE_SEC);
        asyncTaskExecutor.setQueueCapacity(QUEUE_CAPACITY);
        asyncTaskExecutor.setThreadNamePrefix(PREFIX);
        return asyncTaskExecutor;
    }
}
