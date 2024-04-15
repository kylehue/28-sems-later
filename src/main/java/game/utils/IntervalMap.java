package game.utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class IntervalMap {
    private final HashMap<String, Interval> registeredIntervals = new HashMap<>();
    
    private static class Interval {
        public int interval = 0;
        public long coolDown = 0;
    }
    
    public boolean isIntervalOverFor(String name) {
        long timeNow = System.nanoTime();
        Interval intervalAndCoolDown = registeredIntervals.get(name);
        if (intervalAndCoolDown == null) return false;
        int interval = intervalAndCoolDown.interval;
        long coolDown = intervalAndCoolDown.coolDown;
        return timeNow - coolDown > TimeUnit.MILLISECONDS.toNanos(interval);
    }
    
    public void resetIntervalFor(String name) {
        Interval intervalAndCooldown = registeredIntervals.get(name);
        if (intervalAndCooldown == null) return;
        intervalAndCooldown.coolDown = System.nanoTime();
    }
    
    public void registerIntervalFor(String name, int intervalRateInMillis) {
        Interval interval = new Interval();
        interval.interval = intervalRateInMillis;
        registeredIntervals.put(name, interval);
    }
    
    public void changeIntervalFor(String name, int newIntervalInMillis) {
        registeredIntervals.get(name).interval = newIntervalInMillis;
    }
}
