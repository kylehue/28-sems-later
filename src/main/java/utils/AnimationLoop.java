package utils;

import javafx.animation.AnimationTimer;

import java.util.concurrent.TimeUnit;

public class AnimationLoop {
    private float deltaTime = 0;
    private AnimationTimer timer;
    private int frameCount = 0;
    private long lastUpdate = System.nanoTime();
    private float fps = 0;
    
    private void maybeCreateTimer() {
        if (this.timer != null) return;
        this.timer = new AnimationTimer() {
            
            @Override
            public void handle(long now) {
                if (lastUpdate > 0) {
                    long nanosElapsed = now - lastUpdate;
                    fps = 1e9f / nanosElapsed;
                }
                deltaTime = (now - lastUpdate) / 1e9f;
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
    
    public float getFPS() {
        return fps;
    }
    
    public int getFrameCount() {
        return frameCount;
    }
    
    public long getLastUpdate() {
        return lastUpdate;
    }
    
    public float getDeltaTime() {
        return deltaTime;
    }
    
    public void update(float deltaTime) {
    }
    
    public void render() {
    }
}
