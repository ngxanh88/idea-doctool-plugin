package de.uni.potsdam.doctool.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * The class create asynchronous Process and parallel execution
 */
public class AsyncProcess {
    private static final int WAITING_TIME = 50;

    private AsyncProcess() {
    }

    /**
     * start and execute a asynchronous Process with {@link Callable} task
     *
     * @param callable the asynchronous processing task instance.
     * @param defaultValue the default result instance of {@code <T>} type.
     * @param <T> the result type of {@link Callable#call()}.
     * @return the result instance of {@code <T>} type.
     */
    @Nullable
    public static <T> T startAsync(@NotNull final Callable<T> callable, @Nullable final T defaultValue) {
        try {
            return execute(callable).get();

        } catch (Exception e) {
            return defaultValue;
        }
    }

    /**
     * execute a asynchronous Process with {@link Callable} task
     *
     * @param callable the asynchronous processing task instance.
     * @param <T> the result type of {@link Callable#call()}.
     * @return the result of an asynchronous computation
     */
    private static <T> Future<T> execute(final Callable<T> callable) {
        final Future<T> future = ApplicationManager.getApplication().executeOnPooledThread(callable);

        // check process per 50 ms. When process is done or cancelled then return result.
        while (!future.isDone() && !future.isCancelled()) {
            ProgressManager.checkCanceled();
            try {
                Thread.sleep(WAITING_TIME);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }
        return future;
    }

}
