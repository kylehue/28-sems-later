package game.inventory.weapons;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Sniper extends Gun {
    private float penetration = 15000;
    
    public Sniper() {
        super("/weapons/sniper.png");
        setFireRateInMillis(250);
        muzzlePosition.set(52, 7);
        handlePosition.set(24, 10);
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }
    
    public float getPenetration() {
        return penetration;
    }
}
