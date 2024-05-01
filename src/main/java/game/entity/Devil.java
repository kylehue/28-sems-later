package game.entity;

import game.Config;
import game.Game;
import game.Progress;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.projectiles.Fireball;
import game.sprites.BloodSprite;
import game.sprites.DevilSprite;
import game.sprites.SpiritSprite;
import game.utils.Bounds;
import game.utils.Common;
import game.utils.IntervalMap;
import game.utils.Vector;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;

public class Devil extends Seeker {
    // stats
    private final FloatProperty damage = new SimpleFloatProperty();
    private final FloatProperty speed = new SimpleFloatProperty();
    private final float readyShootDistance = 100;
    
    // misc
    private final DevilSprite sprite = new DevilSprite();
    private final CircleCollider collider = new CircleCollider();
    private final IntervalMap intervals = new IntervalMap();
    
    private enum Interval {
        SHOOT_FIREBALL,
        PATH_UPDATE
    }
    
    public Devil() {
        // Defaults
        setDamage(Config.DEFAULT_DEVIL_DAMAGE);
        setCurrentHealth(Config.DEFAULT_DEVIL_HEALTH);
        setMaxHealth(Config.DEFAULT_DEVIL_MAX_HEALTH);
        
        // Initialize colliders
        collider.setGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        collider.setRadius(6);
        collider.setMass(100);
        Game.world.getColliderWorld().addCollider(collider);
        
        // Initialize intervals
        intervals.registerIntervalFor(Interval.SHOOT_FIREBALL, 5000);
        intervals.registerIntervalFor(
            Interval.PATH_UPDATE,
            (int) Common.random(70, 200)
        );
        
        // Misc
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.MOBS);
        
        // Stats
        setMaxHealth(Progress.DEVIL_HEALTH.get());
        setCurrentHealth(Progress.DEVIL_HEALTH.get());
        setSpeed(
            Common.random(
                Progress.DEVIL_SPEED.get() / 2,
                Progress.DEVIL_SPEED.get()
            )
        );
        this.bind();
    }
    
    public void bind() {
        damageProperty().bind(Progress.DEVIL_DAMAGE);
        currentHealthProperty().addListener(this::currentHealthListener);
    }
    
    public void unbind() {
        damageProperty().unbind();
        currentHealthProperty().removeListener(this::currentHealthListener);
    }
    
    private void currentHealthListener(
        ObservableValue<?> o, Number oldHealth, Number newHealth
    ) {
        if (newHealth.floatValue() > oldHealth.floatValue()) return;
        BloodSprite bloodSprite = new BloodSprite();
        bloodSprite.getPosition().set(getPosition());
        Game.world.addOneTimeSpriteAnimation(bloodSprite);
    }
    
    public void setDamage(float damage) {
        this.damage.set(damage);
    }
    
    public float getDamage() {
        return damage.get();
    }
    
    public FloatProperty damageProperty() {
        return damage;
    }
    
    public void setSpeed(float speed) {
        this.speed.set(speed);
    }
    
    public float getSpeed() {
        return speed.get();
    }
    
    public FloatProperty speedProperty() {
        return speed;
    }
    
    private void handleSprite() {
        sprite.getPosition().set(position);
        sprite.setHorizontallyFlipped(isFacingOnLeftSide());
        
        if (isReadyToShootPlayer()) {
            sprite.set(DevilSprite.Animation.SHOOT);
        } else {
            sprite.set(DevilSprite.Animation.WALK);
        }
    }
    
    private boolean isReadyToShootPlayer() {
        float distanceToPlayer = position.getDistanceFrom(
            Game.world.getPlayer().getCollider().getPosition()
        );
        return isPathClear() && distanceToPlayer < readyShootDistance;
    }
    
    private void handleMovements() {
        position.set(collider.getPosition().clone().addY(-collider.getRadius()));
        seek(Game.world.getPlayer().getCollider().getPosition());
    }
    
    private void handleShootFireball() {
        if (
            intervals.isIntervalOverFor(Interval.SHOOT_FIREBALL) && isReadyToShootPlayer()
        ) {
            Bounds playerHitBox = Game.world.getPlayer().getHitBox();
            float targetX = playerHitBox.getX() + playerHitBox.getWidth() / 2;
            float targetY = playerHitBox.getY() + playerHitBox.getHeight() / 2;
            float angleToPlayer = position.getAngle(
                targetX,
                targetY
            );
            Fireball fireball = Game.world.spawnFireball(
                collider.getPosition(),
                angleToPlayer
            );
            fireball.setDamage(getDamage());
            intervals.resetIntervalFor(Interval.SHOOT_FIREBALL);
            
            Game.world.addPlayerDistanceAwareAudio(
                "/sounds/fireball-shot.mp3",
                position,
                250
            );
        }
    }
    
    @Override
    protected void handleSeek(float angle) {
        if (!isReadyToShootPlayer()) {
            collider.applyForce(
                (float) (Math.cos(angle) * getSpeed() * collider.getMass()),
                (float) (Math.sin(angle) * getSpeed() * collider.getMass())
            );
        }
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        this.sprite.render(ctx);
    }
    
    @Override
    public Vector getRenderPosition() {
        return collider.getPosition();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        handleMovements();
        sprite.nextFrame();
    }
    
    @Override
    public void update(float deltaTime) {
        this.handleSprite();
        this.handleShootFireball();
        
        if (getCurrentHealth() <= 0) {
            dispose();
            Progress.PLAYER_ZOMBIE_KILLS.set(
                Progress.PLAYER_ZOMBIE_KILLS.get() + 1
            );
        }
    }
    
    @Override
    public void dispose() {
        Game.world.getDevils().remove(this);
        Game.world.getColliderWorld().removeCollider(collider);
        
        // Death animation
        SpiritSprite spiritSprite = new SpiritSprite();
        spiritSprite.getPosition().set(position);
        Game.world.addOneTimeSpriteAnimation(spiritSprite);
        
        // Death sound
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/devil-death.mp3",
            position,
            250
        );
        
        // Drop XP
        for (int i = 0; i < Common.random(20, 30); i++) {
            Game.world.spawnXPLoot(
                position.clone().add(
                    Common.random(-10, 10),
                    Common.random(-10, 10)
                )
            );
        }
        
        this.unbind();
    }
    
    @Override
    public Collider getCollider() {
        return collider;
    }
    
    @Override
    public Bounds getHitBox() {
        float width = sprite.getWidth() * 0.55f;
        float height = sprite.getHeight() * 0.7f;
        return new Bounds(
            position.getX() - width / 2,
            position.getY() - height / 2,
            width,
            height
        );
    }
}
