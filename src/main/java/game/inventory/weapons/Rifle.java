package game.inventory.weapons;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Rifle extends Gun {
    private float bulletSpeed = 15000;
    private float accuracy = 1; // 0-1 value
    private float maxDistance = 200;
    
    public Rifle() {
        super("/weapons/ak47.png");
        setFireRateInMillis(50);
        muzzlePosition.set(32, 4);
        handlePosition.set(13, 7);
    }
    
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
    
    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getAccuracy() {
        return accuracy;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
}
