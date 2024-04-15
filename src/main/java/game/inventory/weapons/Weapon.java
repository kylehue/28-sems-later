package game.inventory.weapons;

import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public abstract class Weapon {
    protected Vector handlePosition = new Vector();
    protected final Image image;
    protected float damage = 1;
    
    public Weapon(String imageUrl) {
        this.image = Common.loadImage(imageUrl);
    }
    
    public Vector getHandlePosition() {
        return handlePosition;
    }
    
    public void setDamage(float damage) {
        this.damage = damage;
    }
    
    public Image getImage() {
        return image;
    }
    
    public float getDamage() {
        return damage;
    }
    
    public void render(GraphicsContext ctx, float alpha) {
        Image gunImage = getImage();
        ctx.drawImage(
            gunImage,
            -getHandlePosition().getX() + 4,
            -getHandlePosition().getY() + 4
        );
    }
}
