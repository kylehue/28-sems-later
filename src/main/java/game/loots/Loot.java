package game.loots;

import game.Drawable;
import game.Game;
import game.entity.Player;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public abstract class Loot implements Drawable {
    protected final Image image;
    protected final Vector position = new Vector();
    private final long startTimeInMillis = System.currentTimeMillis();
    private boolean isAutoPickUp = false;
    private float attractionDistance = 50f;
    private float attractionSpeed = 5f;
    private float pickUpDistance = 10f;
    private int timeLimitInMillis = Integer.MAX_VALUE;
    
    public Loot(String imageUrl) {
        this.image = Common.loadImage(imageUrl);
    }
    
    public void setAttractionSpeed(float attractionSpeed) {
        this.attractionSpeed = attractionSpeed;
    }
    
    public void setAutoPickUp(boolean autoPickUp) {
        isAutoPickUp = autoPickUp;
    }
    
    public void setAttractionDistance(float attractionDistance) {
        this.attractionDistance = attractionDistance;
    }
    
    public void setTimeLimitInMillis(int timeLimitInMillis) {
        this.timeLimitInMillis = timeLimitInMillis;
    }
    
    public boolean isAutoPickUp() {
        return isAutoPickUp;
    }
    
    public float getAttractionDistance() {
        return attractionDistance;
    }
    
    public float getAttractionSpeed() {
        return attractionSpeed;
    }
    
    public int getTimeLimitInMillis() {
        return timeLimitInMillis;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    int frameAccumulator = (int) (Math.random() * 10);
    
    public void fixedUpdate(float deltaTime) {
        position.addY((float) Math.sin(frameAccumulator * 0.1f) * 0.1f);
        frameAccumulator++;
        
        Player player = Game.world.getPlayer();
        float distanceToPlayer = position.getDistanceFrom(player.getPosition());
        if (distanceToPlayer <= attractionDistance) {
            float angleToPlayer = position.getAngle(player.getPosition());
            float speedMultiplier = game.utils.Common.map(
                distanceToPlayer,
                0,
                attractionDistance,
                1,
                0
            );
            float velocityX = (float) (
                Math.cos(angleToPlayer) * attractionSpeed * speedMultiplier
            );
            float velocityY = (float) (
                Math.sin(angleToPlayer) * attractionSpeed * speedMultiplier
            );
            position.add(velocityX, velocityY);
        }
        
        if (distanceToPlayer <= pickUpDistance) {
            handlePickUp();
            dispose();
        }
        
        if (System.currentTimeMillis() - startTimeInMillis > timeLimitInMillis) {
            dispose();
        }
    }
    
    public void render(GraphicsContext ctx, float alpha) {
        ctx.drawImage(
            image,
            position.getX() - image.getHeight() / 2,
            position.getY() - image.getWidth() / 2,
            image.getWidth(),
            image.getHeight()
        );
    }
    
    @Override
    public Vector getRenderPosition() {
        return position;
    }
    
    @Override
    public int getZIndex() {
        return Game.ZIndex.MAP_DECORATIONS;
    }
    
    @Override
    public boolean isSeeThrough() {
        return false;
    }
    
    protected abstract void handlePickUp();
    
    public void dispose() {
        Game.world.getLoots().remove(this);
    }
}
