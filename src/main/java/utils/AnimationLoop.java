package utils;

import javafx.animation.AnimationTimer;

public class AnimationLoop {
    private final float targetFps = 60.0f;
    private final float deltaTime = 1 / targetFps;
    private float accumulator = 0.0f;
    private AnimationTimer timer;
    private int frameCount = 0;
    private long lastUpdate = System.nanoTime();
    private float fps = 0;
    
    private void maybeCreateTimer() {
        if (this.timer != null) return;
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long nanosElapsed = now - lastUpdate;
                float frameTime = nanosElapsed / 1e9f;
                if (frameTime > 0.25) {
                    frameTime = 0.25f;
                }
                
                accumulator += frameTime;
                while (accumulator >= deltaTime) {
                    fixedUpdate(deltaTime);
                    accumulator -= deltaTime;
                }
                
                // Update FPS
                if (nanosElapsed > 0) {
                    fps = 1e9f / nanosElapsed;
                }
                
                float alpha = accumulator / deltaTime;
                frameCount++;
                render();
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
    
    // to be overridden
    public void fixedUpdate(float deltaTime) {
    
    }
    
    // to be overridden
    public void render() {
    
    }
}
