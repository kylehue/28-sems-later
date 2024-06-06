package game.projectiles;

import game.Config;
import game.Game;
import game.World;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.entity.Entity;
import game.sprites.ExplosionSprite;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

import java.util.HashSet;

public class Grenade extends Projectile {
    private final float knockBackForce = 15000;
    private float aoeDistance = Config.DEFAULT_GRENADE_AOE_DISTANCE;
    private int detonationTimeInMillis = Config.DEFAULT_GRENADE_DETONATION_TIME_MILLIS;
    private final long startTimeInMillis = System.currentTimeMillis();
    private float speed = 5000;
    private final CircleCollider collider = new CircleCollider();
    private final Image image = Common.loadImage("/weapons/grenade.png");
    private final CircleCollider aoeCollider = new CircleCollider();
    
    public Grenade(World world, Vector initialPosition, float angle) {
        super(world, initialPosition, angle);
        initCollider();
    }
    
    private void initCollider() {
        collider.setPosition(initialPosition);
        collider.setCategory(Game.CollisionCategory.PROJECTILES.get());
        collider.setMask(
            Game.CollisionCategory.MAP.get() |
                Game.CollisionCategory.MOBS.get() |
                Game.CollisionCategory.PROJECTILES.get() |
                Game.CollisionCategory.PLAYER.get()
        );
        collider.setRadius(3);
        collider.setFriction(0.05f);
        collider.setMass(1);
        aoeCollider.setRadius(aoeDistance);
        aoeCollider.setSkipResolutionMask(0xffff); // skip all collision
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
    
    @Override
    public void update(float deltaTime) {
        long timeNow = System.currentTimeMillis();
        if (timeNow - startTimeInMillis > detonationTimeInMillis) {
            detonate();
        }
    }
    
    private void handleMovement() {
        position.set(collider.getPosition());
        aoeCollider.setPosition(collider.getPosition());
        
        collider.applyForce(
            (float) Math.cos(angle) * speed * collider.getMass(),
            (float) Math.sin(angle) * speed * collider.getMass()
        );
        
        speed *= 0.7f;
    }
    
    public void detonate() {
        // Detect which colliders are affected by the explosion
        HashSet<Integer> affectedColliders = new HashSet<>();
        for (Collider _collider : aoeCollider.getAndUpdateNearCollidersImmediate(world.getHashgrid())) {
            if (_collider.isStatic()) continue;
            float distanceToBomb = _collider.getPosition().getDistanceFrom(position);
            if (distanceToBomb > aoeDistance) continue;
            affectedColliders.add(_collider.getId());
            
            // Add some knock back
            float angleToBomb = position.getAngle(_collider.getPosition());
            _collider.applyForce(
                (float) (Math.cos(angleToBomb) * (aoeDistance - distanceToBomb)) * knockBackForce / aoeDistance,
                (float) (Math.sin(angleToBomb) * (aoeDistance - distanceToBomb)) * knockBackForce / aoeDistance
            );
        }
        
        ExplosionSprite explosionSprite = new ExplosionSprite();
        explosionSprite.getPosition().set(position);
        float spriteSize = Math.max(64, aoeDistance);
        explosionSprite.setSize(spriteSize, spriteSize);
        world.addOneTimeSpriteAnimation(explosionSprite);
        
        // Handle affected entities
        for (Entity entity : world.getEntities(false)) {
            if (isEntityMarked(entity)) continue;
            boolean isEntityAffected = affectedColliders.contains(entity.getCollider().getId());
            if (!isEntityAffected) continue;
            
            // Damage should depend on how close they are on epicenter
            float distance = entity
                .getCollider()
                .getPosition()
                .getDistanceFrom(position);
            float distancePercentage = (aoeDistance - distance) / aoeDistance;
            float computedDamage = getDamage() * distancePercentage;
            entity.addHealth(-computedDamage);
            markEntity(entity);
        }
        
        dispose();
        
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/explosion.mp3",
            initialPosition,
            aoeDistance * 2 + 300
        );
    }
    
    @Override
    public void dispose() {
        world.getProjectiles().remove(this);
        world.getColliderWorld().removeCollider(collider);
        world.getColliderWorld().removeCollider(aoeCollider);
    }
    
    @Override
    public int getZIndex() {
        return Game.ZIndex.MAP_DECORATIONS;
    }
    
    public void setAoeDistance(float aoeDistance) {
        this.aoeDistance = aoeDistance;
        aoeCollider.setRadius(aoeDistance);
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
    
    public CircleCollider getAoeCollider() {
        return aoeCollider;
    }
}
