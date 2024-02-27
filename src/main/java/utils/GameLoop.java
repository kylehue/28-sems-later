package utils;

import javafx.animation.AnimationTimer;

public class GameLoop {
    private double deltaTime = 0;
    
    public void startLoop() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;
            
            @Override
            public void handle(long now) {
                deltaTime = (now - lastUpdate) / 1e9;
                render();
                update(deltaTime);
                lastUpdate = now;
            }
        };
        timer.start();
    }
    
    public void update(double deltaTime) {
    }
    
    public void render() {
    }
}
