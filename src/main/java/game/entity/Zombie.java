package game.entity;

import game.Config;
import game.Game;
import game.Progress;
import game.colliders.CircleCollider;
import game.sprites.AcidSprite;
import game.sprites.BloodGreenSprite;
import game.utils.*;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import game.sprites.ZombieSprite;

public class Zombie extends Seeker {
    // stats
    private final FloatProperty speed = new SimpleFloatProperty();
    private final FloatProperty damage = new SimpleFloatProperty();
    
    // misc
    private final ZombieSprite sprite = new ZombieSprite();
    private final CircleCollider collider = new CircleCollider();
    private final IntervalMap intervals = new IntervalMap();
    private final static IntervalMap generalIntervals = new IntervalMap();
    
    private enum Interval {
        BITE,
        EMIT_SOUND_GROAN,
        EMIT_SOUND_DEATH
    }
    
    public Zombie() {
        // Defaults
        setCurrentHealth(Config.DEFAULT_ZOMBIE_HEALTH);
        setMaxHealth(Config.DEFAULT_ZOMBIE_MAX_HEALTH);
        
        // Initialize colliders
        collider.setCategory(Game.CollisionCategory.MOBS.get());
        collider.setMask(
            Game.CollisionCategory.MAP.get() |
                Game.CollisionCategory.MOBS.get() |
                Game.CollisionCategory.PROJECTILES.get()
        );
        collider.setRadius(5);
        collider.setMass(1);
        Game.world.getColliderWorld().addCollider(collider);
        
        // Initialize intervals
        intervals.registerIntervalFor(Interval.BITE, 1000);
        generalIntervals.registerIntervalFor(Interval.EMIT_SOUND_DEATH, 300);
        generalIntervals.registerIntervalFor(Interval.EMIT_SOUND_GROAN, 2000);
        
        // Misc
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.MOBS);
        
        // Stats
        setMaxHealth(Progress.ZOMBIE_HEALTH.get());
        setCurrentHealth(Progress.ZOMBIE_HEALTH.get());
        setSpeed(
            Common.random(
                Progress.ZOMBIE_SPEED.get() / 2,
                Progress.ZOMBIE_SPEED.get()
            )
        );
        
        this.bind();
    }
    
    public void bind() {
        damageProperty().bind(Progress.ZOMBIE_DAMAGE);
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
        BloodGreenSprite bloodGreenSprite = new BloodGreenSprite();
        bloodGreenSprite.getPosition().set(getPosition());
        Game.world.addOneTimeSpriteAnimation(bloodGreenSprite);
    }
    
    private void emitGroanSound() {
        if (!generalIntervals.isIntervalOverFor(Interval.EMIT_SOUND_GROAN)) {
            return;
        }
        final int SOUND_DISTANCE = 400;
        Player player = Game.world.getPlayer();
        float distanceToPlayer = player.getPosition().getDistanceFrom(position);
        if (distanceToPlayer >= SOUND_DISTANCE) return;
        String[] pathsToGroan = {
            "/sounds/zombie-groan-1.mp3",
            "/sounds/zombie-groan-2.mp3",
            "/sounds/zombie-groan-brains-1.mp3",
            "/sounds/zombie-groan-brains-2.mp3"
        };
        Game.world.addPlayerDistanceAwareAudio(
            pathsToGroan[(int) Math.floor(Math.random() * pathsToGroan.length)],
            position,
            SOUND_DISTANCE
        );
        generalIntervals.changeIntervalFor(
            Interval.EMIT_SOUND_GROAN,
            (int) Common.random(1000, 3000)
        );
        generalIntervals.resetIntervalFor(Interval.EMIT_SOUND_GROAN);
    }
    
    private void checkPlayerCollision() {
        Player player = Game.world.getPlayer();
        boolean isCollidingWithPlayer = collider.isCollidingWith(
            player.getCollider()
        );
        if (intervals.isIntervalOverFor(Interval.BITE) && isCollidingWithPlayer) {
            player.addHealth(-getDamage());
            intervals.resetIntervalFor(Interval.BITE);
        }
    }
    
    private void handleSprite() {
        this.sprite.getPosition().set(position);
        this.sprite.setHorizontallyFlipped(isFacingOnLeftSide());
    }
    
    private void handleMovements() {
        position.set(collider.getPosition().clone().addY(-collider.getRadius()));
        seek(Game.world.getPlayer().getCollider().getPosition());
    }
    
    public void setDamage(float damage) {
        this.damage.set(damage);
    }
    
    public void setSpeed(float speed) {
        this.speed.set(speed);
    }
    
    public float getDamage() {
        return damage.get();
    }
    
    public float getSpeed() {
        return speed.get();
    }
    
    public FloatProperty speedProperty() {
        return speed;
    }
    
    public FloatProperty damageProperty() {
        return damage;
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
        this.handleMovements();
        this.checkPlayerCollision();
        this.sprite.nextFrame();
    }
    
    @Override
    public void update(float deltaTime) {
        this.handleSprite();
        this.emitGroanSound();
        
        if (getCurrentHealth() <= 0) {
            dispose();
            Progress.PLAYER_ZOMBIE_KILLS.set(
                Progress.PLAYER_ZOMBIE_KILLS.get() + 1
            );
        }
    }
    
    @Override
    public void dispose() {
        Game.world.getZombies().remove(this);
        Game.world.getColliderWorld().removeCollider(collider);
        
        // Death animation
        AcidSprite acidSprite = new AcidSprite();
        acidSprite.getPosition().set(position);
        Game.world.addOneTimeSpriteAnimation(acidSprite);
        
        // Drop XP
        for (int i = 0; i < 1; i++) {
            Game.world.spawnXPLoot(
                position.clone().add(
                    Common.random(-10, 10),
                    Common.random(-10, 10)
                )
            );
        }
        
        // Audio
        if (generalIntervals.isIntervalOverFor(Interval.EMIT_SOUND_DEATH)) {
            Game.world.addPlayerDistanceAwareAudio(
                "/sounds/zombie-death.mp3",
                position,
                200
            );
            generalIntervals.resetIntervalFor(Interval.EMIT_SOUND_DEATH);
        }
        
        this.unbind();
    }
    
    @Override
    protected void handleSeek(float angle) {
        this.collider.applyForce(
            (float) (Math.cos(angle) * getSpeed() * collider.getMass()),
            (float) (Math.sin(angle) * getSpeed() * collider.getMass())
        );
    }
    
    @Override
    public CircleCollider getCollider() {
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
