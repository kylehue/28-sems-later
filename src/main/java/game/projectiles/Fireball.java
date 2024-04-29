package game.projectiles;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.entity.Player;
import game.map.Layer;
import game.map.Material;
import game.sprites.FireballExplosionSprite;
import game.sprites.FireballSprite;
import game.utils.Vector;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;

public class Fireball extends Projectile {
    private final float knockBackForce = 5000;
    private final FloatProperty speed = new SimpleFloatProperty(2500);
    private final FloatProperty maxDistance = new SimpleFloatProperty(300);
    private final FloatProperty penetration = new SimpleFloatProperty(1);
    private final CircleCollider collider = new CircleCollider();
    private final FireballSprite sprite = new FireballSprite();
    
    public Fireball(World world, Vector initialPosition, float angle) {
        super(world, initialPosition, angle);
        initCollider();
    }
    
    private void initCollider() {
        collider.setPosition(initialPosition);
        collider.setGroup(Game.CollisionGroup.PROJECTILES);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.PLAYER);
        collider.excludeResolutionToGroup(Game.CollisionGroup.MAP);
        collider.excludeResolutionToGroup(Game.CollisionGroup.PLAYER);
        collider.excludeResolutionToGroup(Game.CollisionGroup.MOBS);
        collider.excludeResolutionToGroup(Game.CollisionGroup.PROJECTILES);
        collider.setRadius(4);
        collider.setFriction(0.9f);
        collider.setMass(5);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        sprite.render(ctx);
        sprite.getPosition().set(position);
        // ctx.save();
        // ctx.translate(
        //     position.getX(),
        //     position.getY()
        // );
        // // ctx.rotate(Math.toDegrees(angle));
        // ctx.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
        // ctx.restore();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        handleMovement();
        
        handlePlayerCollision();
        handleObstacleCollision();
        handleDisposal();
        
        sprite.nextFrame();
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
    
    private void handlePlayerCollision() {
        Player player = world.getPlayer();
        
        if (isEntityMarked(player)) return;
        boolean isEntityHit = collider.isCollidingWith(player.getHitBox());
        if (!isEntityHit) return;
        
        float penetrationPercentage = getPenetration() >= 1 ? 1 : getPenetration();
        float computedDamage = getDamage() * penetrationPercentage;
        
        player.addHealth(-computedDamage);
        penetration.set(getPenetration() - penetrationPercentage);
        markEntity(player);
        
        // Add knock back
        float angleToBullet = initialPosition.getAngle(player.getCollider().getPosition());
        player.getCollider().applyForce(
            (float) (Math.cos(angleToBullet) * knockBackForce * penetrationPercentage * player.getCollider().getMass()),
            (float) (Math.sin(angleToBullet) * knockBackForce * penetrationPercentage * player.getCollider().getMass())
        );
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
        
        FireballExplosionSprite fireballExplosion = new FireballExplosionSprite();
        fireballExplosion.getPosition().set(position);
        world.addOneTimeSpriteAnimation(fireballExplosion);
        
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/fireball-hit.mp3",
            position,
            250
        );
    }
    
    @Override
    public int getZIndex() {
        return Game.ZIndex.MAP_DECORATIONS;
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
