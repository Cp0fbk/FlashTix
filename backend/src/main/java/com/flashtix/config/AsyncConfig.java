package com.flashtix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Core pool size: number of threads to keep alive even if idle
        executor.setCorePoolSize(2);

        // Max pool size: maximum number of threads
        executor.setMaxPoolSize(5);

        // Queue capacity: number of tasks to queue before creating new threads
        executor.setQueueCapacity(100);

        // Thread name prefix for easier debugging and monitoring
        executor.setThreadNamePrefix("FlashTix-Email-");

        // Wait for tasks to complete on shutdown
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // Maximum time to wait for task completion on shutdown (in seconds)
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}
