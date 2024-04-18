package game.projectiles;

import game.Game;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.entity.Entity;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public class Bullet extends Projectile {
    private float speed = 20000;
    private float maxDistance = 200;
    private float penetration = 1;
    private final CircleCollider collider = new CircleCollider();
    private final Image image = Common.loadImage("/weapons/bullet-2.png");
    
    public Bullet(Vector initialPosition, float angle) {
        super(initialPosition, angle);
        initCollider();
    }
    
    private void initCollider() {
        collider.setPosition(initialPosition);
        collider.setGroup(Game.CollisionGroup.PROJECTILES);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.ZOMBIES);
        collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        collider.excludeResolutionToGroup(Game.CollisionGroup.MAP);
        collider.excludeResolutionToGroup(Game.CollisionGroup.ZOMBIES);
        collider.excludeResolutionToGroup(Game.CollisionGroup.PROJECTILES);
        collider.setRadius(2);
        collider.setFriction(0.9f);
        collider.setMass(5);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        ctx.save();
        ctx.translate(
            position.getX(),
            position.getY()
        );
        ctx.rotate(Math.toDegrees(angle));
        ctx.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
        ctx.restore();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        handleMovement();
        handleDisposal();
    }
    
    private void handleMovement() {
        position.set(collider.getPosition());
        collider.applyForce(
            (float) Math.cos(angle) * speed * collider.getMass(),
            (float) Math.sin(angle) * speed * collider.getMass()
        );
    }
    
    private void handleDisposal() {
        float currentDistance = initialPosition.getDistanceFrom(position);
        if (currentDistance > maxDistance) {
            dispose();
        }
        
        if (penetration <= 0) {
            dispose();
        }
    }
    
    @Override
    public void handleEntityCollision(Entity entity) {
        if (isEntityMarked(entity)) return;
        boolean isEntityHit = collider.isCollidingWith(entity.getCollider());
        if (!isEntityHit) return;
        
        float penetrationPercentage = penetration >= 1 ? 1 : penetration;
        float computedDamage = damage * penetrationPercentage;
        
        entity.addHealth(-computedDamage);
        penetration -= penetrationPercentage;
        markEntity(entity);
    }
    
    @Override
    public void handleObstacleCollision(Collider obstacle) {
        boolean isObstacleHit = collider.isCollidingWith(obstacle);
        if (isObstacleHit || collider.isCollidingInBounds()) {
            dispose();
        }
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public void setPenetration(float penetration) {
        this.penetration = Math.max(0, penetration);
    }
    
    public float getSpeed() {
        return speed;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    public float getPenetration() {
        return penetration;
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
