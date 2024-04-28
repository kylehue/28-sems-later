package game.entity;

import game.Game;
import game.Progress;
import game.colliders.CircleCollider;
import game.sprites.AcidSprite;
import game.sprites.BloodGreenSprite;
import game.utils.*;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;
import game.sprites.ZombieSprite;
import utils.Async;
import game.map.PathFinder;

import java.util.ArrayList;

public class Zombie extends Entity {
    // stats
    private final FloatProperty speed = new SimpleFloatProperty();
    private FloatProperty damage = new SimpleFloatProperty();
    
    // misc
    private final ZombieSprite sprite = new ZombieSprite();
    private final CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    private ArrayList<Vector> pathToPlayer = new ArrayList<>();
    private final HitEffect hitEffect = new HitEffect();
    private final IntervalMap intervals = new IntervalMap();
    private final static IntervalMap generalIntervals = new IntervalMap();
    private float distanceToPlayer = 0;
    
    private enum Interval {
        BITE,
        PATH_UPDATE,
        EMIT_SOUND_GROAN,
        EMIT_SOUND_DEATH
    }
    
    public Zombie() {
        // Initialize colliders
        collider.setGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        collider.setRadius(5);
        collider.setMass(1);
        Game.world.getColliderWorld().addCollider(collider);
        
        // Initialize intervals
        intervals.registerIntervalFor(Interval.BITE, 1000);
        intervals.registerIntervalFor(
            Interval.PATH_UPDATE,
            (int) Common.random(70, 200)
        );
        generalIntervals.registerIntervalFor(Interval.EMIT_SOUND_DEATH, 300);
        generalIntervals.registerIntervalFor(Interval.EMIT_SOUND_GROAN, 2000);
        
        // Misc
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.ZOMBIE);
        
        // Stats
        setSpeed(
            Common.random(
                Progress.ZOMBIE_SPEED.get(),
                Progress.ZOMBIE_SPEED.get() + 100
            )
        );
        Progress.ZOMBIE_SPEED.addListener(e -> {
            setSpeed(
                Common.random(
                    Progress.ZOMBIE_SPEED.get(),
                    Progress.ZOMBIE_SPEED.get() + 100
                )
            );
        });
        damageProperty().bind(Progress.ZOMBIE_DAMAGE);
        
        currentHealthProperty().addListener((o, oldHealth, newHealth) -> {
            if (newHealth.floatValue() > oldHealth.floatValue()) return;
            BloodGreenSprite bloodGreenSprite = new BloodGreenSprite();
            bloodGreenSprite.getPosition().set(getPosition());
            Game.world.addOneTimeSpriteAnimation(bloodGreenSprite);
        });
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        // hitEffect.begin(ctx);
        this.sprite.render(ctx);
        // hitEffect.end(ctx);
        
        // // draw path to player
        // for (Vector vector : pathToPlayer) {
        //     ctx.beginPath();
        //     ctx.setFill(Paint.valueOf("rgba(250, 120, 250, 0.85)"));
        //     ctx.fillOval(vector.getX(), vector.getY(), 8, 8);
        //     ctx.closePath();
        // }
        
        // // render hit box
        // ctx.beginPath();
        // ctx.setFill(Paint.valueOf("rgba(250, 120, 250, 0.85)"));
        // ctx.fillRect(
        //     getHitBox().getX(),
        //     getHitBox().getY(),
        //     getHitBox().getWidth(),
        //     getHitBox().getHeight()
        // );
        // ctx.closePath();
    }
    
    public void fixedUpdate(float deltaTime) {
        this.handleMovements();
        this.checkPlayerCollision();
        this.maybeUpdatePathToPlayer();
        this.sprite.nextFrame();
        hitEffect.updateCurrentHealth(getCurrentHealth());
    }
    
    public void update(float deltaTime) {
        this.handleSprite();
        this.updateAngleToPlayer();
        this.updateDistanceToPlayer();
        this.handleGroan();
        
        if (getCurrentHealth() <= 0) {
            dispose();
        }
    }
    
    private void handleGroan() {
        if (!generalIntervals.isIntervalOverFor(Interval.EMIT_SOUND_GROAN)) {
            return;
        }
        final int SOUND_DISTANCE = 400;
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
    
    private void updateDistanceToPlayer() {
        Player player = Game.world.getPlayer();
        this.distanceToPlayer = player.getPosition().getDistanceFrom(position);
    }
    
    private void updateAngleToPlayer() {
        Player player = Game.world.getPlayer();
        this.angleToPlayer = this.position.getAngle(player.getPosition());
        this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
    }
    
    private void handleSprite() {
        this.sprite.getPosition().set(position);
        this.sprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
    }
    
    private void maybeUpdatePathToPlayer() {
        Player player = Game.world.getPlayer();
        intervals.changeIntervalFor(Interval.PATH_UPDATE, (int) distanceToPlayer);
        if (intervals.isIntervalOverFor(Interval.PATH_UPDATE)) {
            Async.queue1.submit(() -> {
                PathFinder pathFinder = Game.world.getPathFinder();
                pathToPlayer = pathFinder.requestPath(
                    collider.getPosition(),
                    player.getCollider().getPosition()
                );
            });
            intervals.resetIntervalFor(Interval.PATH_UPDATE);
        }
    }
    
    private void handleMovements() {
        position.set(collider.getPosition().clone().addY(-collider.getRadius()));
        
        // Move to player
        if (pathToPlayer.size() > 4) {
            Vector stepNext = pathToPlayer.get(Math.max(0, pathToPlayer.size() - 2));
            float angle = collider.getPosition().getAngle(stepNext);
            this.collider.applyForce(
                (float) (Math.cos(angle) * getSpeed() * collider.getMass()),
                (float) (Math.sin(angle) * getSpeed() * collider.getMass())
            );
            this.isFacingOnLeftSide = Math.abs(angle) > (Math.PI / 2);
        } else {
            this.collider.applyForce(
                (float) (Math.cos(angleToPlayer) * getSpeed() * collider.getMass()),
                (float) (Math.sin(angleToPlayer) * getSpeed() * collider.getMass())
            );
            this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
        }
    }
    
    public void setDamage(float damage) {
        this.damage.set(damage);
    }
    
    public void setSpeed(float speed) {
        this.speed.set(speed);
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
    
    @Override
    public Vector getRenderPosition() {
        return collider.getPosition();
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
}
