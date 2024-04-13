package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Async {
    public static final ExecutorService queue1 = Executors.newSingleThreadExecutor(
        (runnable) -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    );
    public static final ExecutorService queue2 = Executors.newFixedThreadPool(
        2,
        (runnable) -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        }
    );
}
