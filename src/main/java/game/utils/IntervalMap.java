package game.utils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class IntervalMap {
    private final HashMap<Object, Interval> registeredIntervals = new HashMap<>();
    
    private static class Interval {
        public int interval = 0;
        public long coolDown = 0;
    }
    
    public boolean isIntervalOverFor(Object key) {
        long timeNow = System.nanoTime();
        Interval intervalAndCoolDown = registeredIntervals.get(key);
        if (intervalAndCoolDown == null) return false;
        int interval = intervalAndCoolDown.interval;
        long coolDown = intervalAndCoolDown.coolDown;
        return timeNow - coolDown > TimeUnit.MILLISECONDS.toNanos(interval);
    }
    
    public void resetIntervalFor(Object key) {
        Interval intervalAndCooldown = registeredIntervals.get(key);
        if (intervalAndCooldown == null) return;
        intervalAndCooldown.coolDown = System.nanoTime();
    }
    
    public void registerIntervalFor(Object key, int intervalRateInMillis) {
        if (registeredIntervals.containsKey(key)) return;
        Interval interval = new Interval();
        interval.interval = intervalRateInMillis;
        registeredIntervals.put(key, interval);
    }
    
    public void changeIntervalFor(Object key, int newIntervalInMillis) {
        registeredIntervals.get(key).interval = newIntervalInMillis;
    }
}
