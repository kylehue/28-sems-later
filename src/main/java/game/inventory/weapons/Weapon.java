package game.inventory.weapons;

import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public abstract class Weapon {
    private final Vector origHandlePosition = new Vector();
    private final Vector handlePosition = new Vector();
    protected final Image image;
    protected float damage = 1;
    
    public Weapon(String imageUrl) {
        this.image = Common.loadImage(imageUrl);
    }
    
    protected void setHandlePosition(Vector vector) {
        handlePosition.set(vector);
    }
    
    public void setOrigHandlePosition(Vector vector) {
        handlePosition.set(vector);
        origHandlePosition.set(vector);
    }
    
    protected Vector getHandlePosition() {
        return handlePosition.clone();
    }
    
    protected Vector getOrigHandlePosition() {
        return origHandlePosition.clone();
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
            -handlePosition.getX() + 4,
            -handlePosition.getY() + 4
        );
        handlePosition.lerp(origHandlePosition, 0.15f);
        subRender(ctx, alpha);
    }
    
    protected void subRender(GraphicsContext ctx, float alpha) {
    
    }
}
