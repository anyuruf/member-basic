package net.anyuruf.memberbasic.config;



import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.AsyncTaskExecutor;

public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor {

    private static final Logger LOG =
        LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

    private final AsyncTaskExecutor executor;

    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(Runnable task) {
        executor.execute(handle(task));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executor.submit(handle(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executor.submit(() -> {
            try {
                return task.call();
            } catch (Exception exception) {
                LOG.error("Exception occurred during asynchronous task execution", exception);
                throw exception;
            }
        });
    }

    private Runnable handle(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception exception) {
                LOG.error("Exception occurred during asynchronous task execution", exception);
                throw exception;
            }
        };
    }
}
