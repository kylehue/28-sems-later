package utils;

import javafx.animation.AnimationTimer;

import java.util.concurrent.TimeUnit;

public class AnimationLoop {
    private double deltaTime = 0;
    private AnimationTimer timer;
    private int frameCount = 0;
    private long lastUpdate = 0;
    
    private void maybeCreateTimer() {
        if (this.timer != null) return;
        this.timer = new AnimationTimer() {
            
            @Override
            public void handle(long now) {
                deltaTime = (now - lastUpdate) / 1e9;
                update(deltaTime);
                render();
                frameCount++;
                lastUpdate = now;
            }
        };
    }
    
    public void startLoop() {
        this.maybeCreateTimer();
        this.timer.start();
    }
    
    public void pauseLoop() {
        this.timer.stop();
    }
    
    public int getFrameCount() {
        return frameCount;
    }
    
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    public double getDeltaTime() {
        return deltaTime;
    }
    
    public void update(double deltaTime) {
    }
    
    public void render() {
    }
}
