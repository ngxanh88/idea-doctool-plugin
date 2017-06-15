package de.uni.potsdam.doctool.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by ngxanh88 on 07.06.17.
 */
public class AsyncProcess {
    private static final int WAITING_TIME = 50;

    private AsyncProcess() {
    }

    @Nullable
    public static <T> T asyncResultOf(@NotNull final Callable<T> callable, @Nullable final T defaultValue) {
        try {
            return finished(executeOnPooledThread(callable)).get();

        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static <T> Future<T> executeOnPooledThread(final Callable<T> callable) {
        return ApplicationManager.getApplication().executeOnPooledThread(callable);
    }

    private static <T> Future<T> finished(final Future<T> future) {
        while (!future.isDone() && !future.isCancelled()) {
            ProgressManager.checkCanceled();
            try {
                Thread.sleep(WAITING_TIME);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
        return future;
    }

}
