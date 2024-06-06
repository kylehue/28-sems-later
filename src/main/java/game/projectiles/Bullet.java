package game.projectiles;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.entity.Entity;
import game.map.Layer;
import game.map.Material;
import game.utils.Vector;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public class Bullet extends Projectile {
    private final float knockBackForce = 3000;
    private final FloatProperty speed = new SimpleFloatProperty(10000);
    private final FloatProperty maxDistance = new SimpleFloatProperty(200);
    private final FloatProperty penetration = new SimpleFloatProperty(1);
    private final CircleCollider collider = new CircleCollider();
    private final Image image = Common.loadImage("/weapons/bullet-2.png");
    
    public Bullet(World world, Vector initialPosition, float angle) {
        super(world, initialPosition, angle);
        initCollider();
    }
    
    private void initCollider() {
        collider.setPosition(initialPosition);
        collider.setCategory(Game.CollisionCategory.PROJECTILES.get());
        collider.setMask(
            Game.CollisionCategory.MAP.get() |
                Game.CollisionCategory.MOBS.get() |
                Game.CollisionCategory.PROJECTILES.get()
        );
        collider.setSkipResolutionMask(
            Game.CollisionCategory.MAP.get() |
                Game.CollisionCategory.MOBS.get() |
                Game.CollisionCategory.PROJECTILES.get()
        );
        collider.setRadius(2);
        collider.setFriction(0.9f);
        collider.setMass(1);
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
        
        handleEntityCollision();
        handleObstacleCollision();
        handleDisposal();
    }
    
    private void handleMovement() {
        position.set(collider.getPosition());
        collider.applyForce(
            (float) Math.cos(angle) * getSpeed() * collider.getMass(),
            (float) Math.sin(angle) * getSpeed() * collider.getMass()
        );
    }
    
    private void handleDisposal() {
        float currentDistance = initialPosition.getDistanceFrom(position);
        if (
            getPenetration() <= 0 ||
                currentDistance > getMaxDistance() ||
                collider.isCollidingInBounds()
        ) {
            dispose();
        }
    }
    
    private void handleEntityCollision() {
        for (Entity entity : world.getEntities(false)) {
            if (isEntityMarked(entity)) continue;
            boolean isEntityHit = collider.isCollidingWith(entity.getHitBox());
            if (!isEntityHit) continue;
            
            float penetrationPercentage = getPenetration() >= 1 ? 1 : getPenetration();
            float computedDamage = getDamage() * penetrationPercentage;
            
            entity.addHealth(-computedDamage);
            penetration.set(getPenetration() - penetrationPercentage);
            markEntity(entity);
            
            // Add knock back
            float angleToBullet = initialPosition.getAngle(entity.getCollider().getPosition());
            entity.getCollider().applyForce(
                (float) (Math.cos(angleToBullet) * knockBackForce * penetrationPercentage),
                (float) (Math.sin(angleToBullet) * knockBackForce * penetrationPercentage)
            );
        }
    }
    
    private void handleObstacleCollision() {
        for (Layer layer : world.getMap().getLayers()) {
            for (Material material : layer.getMaterials()) {
                Collider obstacle = material.getCollider();
                if (obstacle == null) continue;
                boolean isObstacleHit = obstacle.isCollidingWith(collider);
                if (isObstacleHit) {
                    dispose();
                    return;
                }
            }
        }
    }
    
    @Override
    public void dispose() {
        world.getProjectiles().remove(this);
        world.getColliderWorld().removeCollider(collider);
    }
    
    public void setSpeed(float speed) {
        this.speed.set(speed);
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance.set(maxDistance);
    }
    
    public void setPenetration(float penetration) {
        this.penetration.set(Math.max(0, penetration));
    }
    
    public float getSpeed() {
        return speed.get();
    }
    
    public float getMaxDistance() {
        return maxDistance.get();
    }
    
    public float getPenetration() {
        return penetration.get();
    }
    
    public FloatProperty speedProperty() {
        return speed;
    }
    
    public FloatProperty maxDistanceProperty() {
        return maxDistance;
    }
    
    public FloatProperty penetrationProperty() {
        return penetration;
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
