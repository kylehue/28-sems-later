package game.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;

public class HitEffect {
    private float currentHealth = 0;
    private float oldHealth = currentHealth;
    private boolean isDamaged = false;
    private long lastTimeDamagedInMillis = System.currentTimeMillis();
    private float brightnessAccumulator = 0;
    private int effectMinTimeInMillis = 200;
    
    public void updateCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }
    
    public void begin(GraphicsContext ctx) {
        if (!isDamaged) {
            isDamaged = oldHealth != currentHealth;
            lastTimeDamagedInMillis = System.currentTimeMillis();
        }
        
        if (oldHealth != currentHealth) {
            brightnessAccumulator = 0.85f;
        }
        
        if (isDamaged) {
            ctx.save();
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setBrightness(brightnessAccumulator);
            ctx.setEffect(colorAdjust);
        }
    }
    
    public void end(GraphicsContext ctx) {
        if (isDamaged) {
            ctx.restore();
        }
        
        long timeNow = System.currentTimeMillis();
        if (timeNow - lastTimeDamagedInMillis > effectMinTimeInMillis) {
            brightnessAccumulator *= 0.85f; // fade
        }
        
        if (brightnessAccumulator <= 0.01) {
            isDamaged = oldHealth != currentHealth;
            brightnessAccumulator = 0;
        }
        
        oldHealth = currentHealth;
    }
}
