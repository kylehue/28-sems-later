package game.utils;

import javafx.animation.AnimationTimer;

public class GameLoop {
    private static final float timeStep = 0.0166f;
    
    private long previousTime = 0;
    private float accumulatedTime = 0;
    
    private float secondsElapsedSinceLastFpsUpdate = 0f;
    private int framesSinceLastFpsUpdate = 0;
    
    private AnimationTimer timer;
    private int frameCount = 0;
    private float fps = 0;
    
    private void maybeCreateTimer() {
        if (this.timer != null) return;
        this.timer = new AnimationTimer() {
            @Override
            public void handle(long currentTime) {
                if (GameLoop.this.timer == null) return;
                if (previousTime == 0) {
                    previousTime = currentTime;
                    return;
                }
                
                float secondsElapsed = (currentTime - previousTime) / 1e9f;
                float secondsElapsedCapped = Math.min(secondsElapsed, Float.MAX_VALUE);
                accumulatedTime += secondsElapsedCapped;
                previousTime = currentTime;
                
                while (accumulatedTime >= timeStep) {
                    fixedUpdate(timeStep);
                    accumulatedTime -= timeStep;
                }
                update(secondsElapsed);
                // float alpha = accumulatedTime / timeStep;
                render(1);
                frameCount++;
                secondsElapsedSinceLastFpsUpdate += secondsElapsed;
                framesSinceLastFpsUpdate++;
                if (secondsElapsedSinceLastFpsUpdate >= 0.5f) {
                    fps = Math.round(framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate);
                    secondsElapsedSinceLastFpsUpdate = 0;
                    framesSinceLastFpsUpdate = 0;
                }
            }
        };
    }
    
    protected void startLoop() {
        this.maybeCreateTimer();
        this.timer.start();
    }
    
    protected void pauseLoop() {
        this.timer.stop();
    }
    
    public float getFPS() {
        return fps;
    }
    
    public int getFrameCount() {
        return frameCount;
    }
    
    public void resetTimer() {
        this.timer.stop();
        this.timer = null;
        this.fps = 0;
        this.frameCount = 0;
        this.previousTime = 0;
        this.accumulatedTime = 0;
        this.secondsElapsedSinceLastFpsUpdate = 0f;
        this.framesSinceLastFpsUpdate = 0;
    }
    
    // to be overridden
    public void fixedUpdate(float deltaTime) {
    
    }
    
    // to be overridden
    public void update(float deltaTime) {
    
    }
    
    // to be overridden
    public void render(float alpha) {
    
    }
}
