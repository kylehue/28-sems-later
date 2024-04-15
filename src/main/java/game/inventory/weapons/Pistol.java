package game.inventory.weapons;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Pistol extends Gun {
    private float bulletSpeed = 15000;
    private float maxDistance = 200;
    
    public Pistol() {
        super("/weapons/pistol.png");
        setFireRateInMillis(250);
        muzzlePosition.set(14, 3);
        handlePosition.set(3, 6);
    }
    
    public void setBulletSpeed(float bulletSpeed) {
        this.bulletSpeed = bulletSpeed;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public float getBulletSpeed() {
        return bulletSpeed;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
}
