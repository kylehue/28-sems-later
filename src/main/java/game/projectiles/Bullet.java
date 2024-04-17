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

public class Bullet extends Projectile {
    private float speed = 10000;
    private float maxDistance = 200;
    private float penetration = 0;
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
        collider.setFriction(0.5f);
        collider.setMass(1);
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
            (float) Math.cos(angle) * speed,
            (float) Math.sin(angle) * speed
        );
    }
    
    @Override
    public void handleEntityCollision(Entity entity) {
        boolean isEntityHit = collider.isCollidingWith(entity.getCollider());
        if (!isEntityHit) return;
        entity.addHealth(-this.damage);
        if (penetration <= 0) {
            dispose();
        } else {
            penetration--;
        }
    }
    
    @Override
    public void handleObstacleCollision(Collider obstacle) {
        boolean isObstacleHit = collider.isCollidingWith(obstacle);
        if (!isObstacleHit) return;
        dispose();
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
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
