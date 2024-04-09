package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Async {
    public static final ExecutorService executorService = Executors.newFixedThreadPool(
        Math.min(Runtime.getRuntime().availableProcessors(), 4),
        (runnable) -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    );
}
