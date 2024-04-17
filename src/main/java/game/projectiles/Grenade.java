package game.projectiles;

import game.Game;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.entity.Entity;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

import java.util.List;

public class Grenade extends Projectile {
    private float aoeDistance = 100;
    private int detonationTimeInMillis = 2000;
    private float speed = 10000;
    private final CircleCollider collider = new CircleCollider();
    private final Image image = Common.loadImage("/weapons/grenade.png");
    
    public Grenade(Vector initialPosition, float angle) {
        super(initialPosition, angle);
        initCollider();
    }
    
    private void initCollider() {
        collider.setPosition(initialPosition);
        collider.setGroup(Game.CollisionGroup.PROJECTILES);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.ZOMBIES);
        collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        collider.addToGroup(Game.CollisionGroup.PLAYER);
        collider.setRadius(3);
        collider.setFriction(0.5f);
        collider.setMass(3);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        ctx.save();
        ctx.translate(
            getPosition().getX(),
            getPosition().getY()
        );
        ctx.rotate(Math.toDegrees(angle));
        ctx.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
        ctx.restore();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        handleMovement();
    }
    
    private void handleMovement() {
        position.set(collider.getPosition());
        
        collider.applyForce(
            (float) Math.cos(angle) * speed * collider.getMass(),
            (float) Math.sin(angle) * speed * collider.getMass()
        );
        
        speed *= 0.95f;
    }
    
    @Override
    public void handleEntityCollision(Entity entity) {
    
    }
    
    @Override
    public void handleObstacleCollision(Collider obstacle) {
    
    }
    
    public void setAoeDistance(float aoeDistance) {
        this.aoeDistance = aoeDistance;
    }
    
    public void setDetonationTimeInMillis(int detonationTimeInMillis) {
        this.detonationTimeInMillis = detonationTimeInMillis;
    }
    
    public float getAoeDistance() {
        return aoeDistance;
    }
    
    public int getDetonationTimeInMillis() {
        return detonationTimeInMillis;
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
