package game.entity;

import game.Config;
import game.Game;
import game.Progress;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.sprites.BloodSprite;
import game.utils.Common;
import game.weapons.Gun;
import game.weapons.Weapon;
import game.sprites.DashSprite;
import game.sprites.PlayerSprite;
import game.utils.IntervalMap;
import game.weapons.WeaponKind;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.GraphicsContext;
import event.KeyHandler;
import game.utils.Vector;
import javafx.scene.media.MediaPlayer;

public class Player extends Entity {
    private static final float IDLE_VELOCITY_THRESHOLD = 0.25f;
    
    // stats
    private final FloatProperty speed = new SimpleFloatProperty();
    private final FloatProperty dashSpeed = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_DASH_SPEED
    );
    private final IntegerProperty dashIntervalInMillis = new SimpleIntegerProperty();
    private final int healthRegenIntervalInMillis = 50;
    private final FloatProperty healthRegenHealth = new SimpleFloatProperty();
    
    // control flags
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean dashPressed = false;
    private boolean shootPressed = false;
    
    // misc
    private final ObjectProperty<WeaponKind> currentWeapon = new SimpleObjectProperty<>(WeaponKind.PISTOL);
    private final CircleCollider collider = new CircleCollider();
    private boolean isFacingOnLeftSide = false;
    private float angleToMouse = 0;
    private final IntervalMap intervals = new IntervalMap();
    
    private enum Interval {
        DASH,
        HEALTH_REGEN,
        DAMAGE_SOUND
    }
    
    // sprites
    private final PlayerSprite bodySprite = new PlayerSprite();
    
    // audios
    private final MediaPlayer footstepAudio = new MediaPlayer(
        utils.Common.loadMedia("/sounds/footstep.mp3")
    );
    private final MediaPlayer footstepGrassAudio = new MediaPlayer(
        utils.Common.loadMedia("/sounds/footstep-grass.mp3")
    );
    
    public Player() {
        // Initialize collider
        this.collider.setGroup(Game.CollisionGroup.PLAYER);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MOBS);
        this.collider.setMass(50);
        this.collider.setRadius(5);
        Game.world.getColliderWorld().addCollider(
            this.collider
        );
        
        // Initialize intervals
        intervals.registerIntervalFor(Interval.DASH, getDashIntervalInMillis());
        intervals.registerIntervalFor(Interval.HEALTH_REGEN, healthRegenIntervalInMillis);
        
        // Misc
        this.setZIndex(Game.ZIndex.PLAYER);
        
        footstepAudio.setCycleCount(Integer.MAX_VALUE);
        footstepGrassAudio.setCycleCount(Integer.MAX_VALUE);
        footstepAudio.setVolume(0.7f);
        footstepGrassAudio.setVolume(0.7f);
        
        this.bind();
    }
    
    public void bind() {
        maxHealthProperty().bindBidirectional(Progress.PLAYER_MAX_HEALTH);
        currentHealthProperty().bindBidirectional(Progress.PLAYER_CURRENT_HEALTH);
        speedProperty().bindBidirectional(Progress.PLAYER_SPEED);
        dashIntervalInMillisProperty().bindBidirectional(Progress.PLAYER_DASH_INTERVAL);
        healthRegenHealthProperty().bindBidirectional(Progress.PLAYER_HEALTH_REGEN_HEALTH);
        
        dashIntervalInMillisProperty().addListener(this::dashIntervalListener);
        Game.world.isPausedProperty().addListener(this::worldPausedListener);
        currentHealthProperty().addListener(this::currentHealthListener);
    }
    
    public void unbind() {
        maxHealthProperty().unbindBidirectional(Progress.PLAYER_MAX_HEALTH);
        currentHealthProperty().unbindBidirectional(Progress.PLAYER_CURRENT_HEALTH);
        speedProperty().unbindBidirectional(Progress.PLAYER_SPEED);
        dashIntervalInMillisProperty().unbindBidirectional(Progress.PLAYER_DASH_INTERVAL);
        healthRegenHealthProperty().unbindBidirectional(Progress.PLAYER_HEALTH_REGEN_HEALTH);
        
        dashIntervalInMillisProperty().removeListener(this::dashIntervalListener);
        Game.world.isPausedProperty().removeListener(this::worldPausedListener);
        currentHealthProperty().removeListener(this::currentHealthListener);
    }
    
    private void dashIntervalListener(
        ObservableValue<?> o, Number o1, Number dashInterval
    ) {
        intervals.changeIntervalFor(Interval.DASH, dashInterval.intValue());
    }
    
    private void worldPausedListener(
        ObservableValue<?> o, Boolean o1, Boolean isPaused
    ) {
        if (isPaused) {
            footstepAudio.stop();
            footstepGrassAudio.stop();
        }
    }
    
    private void currentHealthListener(
        ObservableValue<?> o, Number oldHealth, Number newHealth
    ) {
        if (newHealth.floatValue() > oldHealth.floatValue()) return;
        BloodSprite bloodSprite = new BloodSprite();
        bloodSprite.getPosition().set(getPosition()).add(
            Common.random(-5, 5),
            Common.random(-7, 7)
        );
        Game.world.addOneTimeSpriteAnimation(bloodSprite);
    }
    
    @Override
    public void dispose() {
        this.unbind();
        footstepAudio.dispose();
        footstepGrassAudio.dispose();
        Game.world.getColliderWorld().removeCollider(collider);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        // render body
        this.bodySprite.render(ctx);
        this.bodySprite.getPosition().set(position);
        
        // render gun
        ctx.save();
        ctx.translate(
            position.getX(),
            position.getY()
        );
        ctx.rotate(Math.toDegrees(angleToMouse));
        if (isFacingOnLeftSide) {
            ctx.scale(1, -1);
        }
        getCurrentWeapon().render(ctx, alpha);
        ctx.restore();
    }
    
    public void fixedUpdate(float deltaTime) {
        this.handleMovements();
        this.handleSpriteAnimations();
        this.bodySprite.nextFrame();
    }
    
    public void update(float deltaTime) {
        this.updateControlFlags();
        this.updateAngleToMouse();
        this.handleHealthRegen();
        this.handleFootstepAudio();
        
        if (shootPressed) {
            this.shoot();
        }
        
    }
    
    private void handleFootstepAudio() {
        String positionTileId = Game.world.getMap().getLayers().get(0).getTileIdAt(position);
        boolean isOnGrass = positionTileId.contains("grass")
            || positionTileId.contains("sand");
        if (collider.getVelocity().getMagnitude() > IDLE_VELOCITY_THRESHOLD) {
            // walking
            if (isOnGrass) {
                footstepGrassAudio.setVolume(1);
                footstepGrassAudio.play();
                footstepAudio.stop();
            } else {
                footstepAudio.setVolume(1);
                footstepAudio.play();
                footstepGrassAudio.stop();
            }
        } else {
            // not walking
            float mappedVolume = Common.map(
                collider.getVelocity().getMagnitude(),
                0,
                IDLE_VELOCITY_THRESHOLD,
                0,
                1
            );
            if (mappedVolume <= 0.05) {
                footstepAudio.stop();
                footstepGrassAudio.stop();
            } else {
                footstepAudio.setVolume(mappedVolume);
                footstepGrassAudio.setVolume(mappedVolume);
            }
        }
    }
    
    public void handleHealthRegen() {
        if (!intervals.isIntervalOverFor(Interval.HEALTH_REGEN)) return;
        addHealth(getHealthRegenHealth());
        intervals.resetIntervalFor(Interval.HEALTH_REGEN);
    }
    
    public void shoot() {
        if (Game.world.isGameOver()) return;
        
        Weapon currentWeapon = getCurrentWeapon();
        
        if (!(currentWeapon instanceof Gun currentGun)) return;
        currentGun.shoot(Game.world, position, angleToMouse);
    }
    
    public void dash() {
        if (Game.world.isGameOver()) return;
        
        intervals.changeIntervalFor(Interval.DASH, dashIntervalInMillis.get());
        if (!intervals.isIntervalOverFor(Interval.DASH)) return;
        
        float computedSpeed = dashSpeed.get() * collider.getMass();
        if (upPressed) {
            collider.applyForceY(-computedSpeed);
        } else if (downPressed) {
            collider.applyForceY(computedSpeed);
        }
        
        if (leftPressed) {
            collider.applyForceX(-computedSpeed);
        } else if (rightPressed) {
            collider.applyForceX(computedSpeed);
        }
        
        // fix dash speed in diagonal movement
        if ((leftPressed || rightPressed) && (upPressed || downPressed)) {
            collider.getAcceleration().limit(
                computedSpeed / collider.getMass()
            );
        }
        
        // if not moving, just dash away from mouse
        if (!upPressed && !downPressed && !leftPressed && !rightPressed) {
            float angle = (float) (Math.PI + angleToMouse);
            this.collider.applyForce(
                (float) (Math.cos(angle) * (computedSpeed)),
                (float) (Math.sin(angle) * (computedSpeed))
            );
        }
        
        DashSprite dashSprite = new DashSprite();
        float dashAngle = collider.getAcceleration().getAngle();
        dashSprite.setAngleInRadians((float) (dashAngle - Math.PI / 2));
        dashSprite.getPosition().set(getRenderPosition().clone().add(
            dashSprite.getWidth() / 2,
            dashSprite.getHeight() / 2
        ));
        dashSprite.getOrigin().set(-dashSprite.getWidth() / 2, -dashSprite.getHeight());
        Game.world.addOneTimeSpriteAnimation(dashSprite);
        intervals.resetIntervalFor(Interval.DASH);
        
        Game.world.addPlayerDistanceAwareAudio("/sounds/dash.mp3", position, 200);
    }
    
    private void updateAngleToMouse() {
        if (Game.world.isGameOver()) return;
        
        Vector mouseInWorld = Game.world.getMousePosition();
        this.angleToMouse = position.getAngle(mouseInWorld);
        this.isFacingOnLeftSide = Math.abs(angleToMouse) > (Math.PI / 2);
    }
    
    private void updateControlFlags() {
        if (Game.world.isGameOver()) return;
        
        KeyHandler keyHandler = Game.keyHandler;
        this.upPressed = keyHandler.isKeyPressed(Game.Control.MOVE_UP);
        this.downPressed = keyHandler.isKeyPressed(Game.Control.MOVE_DOWN);
        this.leftPressed = keyHandler.isKeyPressed(Game.Control.MOVE_LEFT);
        this.rightPressed = keyHandler.isKeyPressed(Game.Control.MOVE_RIGHT);
        this.dashPressed = keyHandler.isKeyPressed(Game.Control.DASH);
        this.shootPressed = Game.mouseHandler.isMouseLeftPressed();
    }
    
    private void handleMovements() {
        if (Game.world.isGameOver()) return;
        
        position.lerp(collider.getPosition().clone().addY(-collider.getRadius()), 0.25f);
        
        // x controls
        float computedSpeed = speed.get() * collider.getMass();
        if (leftPressed || rightPressed) {
            if (leftPressed) {
                collider.applyForceX(-1 * computedSpeed);
            }
            if (rightPressed) {
                collider.applyForceX(1 * computedSpeed);
            }
        }
        
        // y controls
        if (upPressed || downPressed) {
            if (upPressed) {
                collider.applyForceY(-1 * computedSpeed);
            }
            if (downPressed) {
                collider.applyForceY(1 * computedSpeed);
            }
        }
        
        // fix speed in diagonal movement
        if ((leftPressed || rightPressed) && (upPressed || downPressed)) {
            collider.getAcceleration().limit(
                computedSpeed / collider.getMass()
            );
        }
        
        // dash
        if (dashPressed) {
            dash();
        }
    }
    
    private void handleSpriteAnimations() {
        if (this.collider.getVelocity().getMagnitude() <= IDLE_VELOCITY_THRESHOLD) {
            this.bodySprite.set(PlayerSprite.Animation.Idle);
            
            if (shootPressed) {
                this.bodySprite.set(PlayerSprite.Animation.Shoot);
            }
        } else {
            this.bodySprite.set(PlayerSprite.Animation.Walk);
        }
        
        this.bodySprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
    }
    
    public void setDashIntervalInMillis(int dashIntervalInMillis) {
        this.dashIntervalInMillis.set(dashIntervalInMillis);
    }
    
    public void setDashSpeed(float dashSpeed) {
        this.dashSpeed.set(dashSpeed);
    }
    
    public void setSpeed(float speed) {
        this.speed.set(speed);
    }
    
    public void setHealthRegenHealth(float regenHealth) {
        healthRegenHealth.set(regenHealth);
    }
    
    public void setCurrentWeapon(WeaponKind weaponKind) {
        this.currentWeapon.set(weaponKind);
    }
    
    @Override
    public Vector getRenderPosition() {
        return position.clone().addY(collider.getRadius());
    }
    
    @Override
    public Collider getCollider() {
        return collider;
    }
    
    public int getDashIntervalInMillis() {
        return dashIntervalInMillis.get();
    }
    
    public float getDashSpeed() {
        return dashSpeed.get();
    }
    
    public float getSpeed() {
        return speed.get();
    }
    
    public Weapon getCurrentWeapon() {
        return currentWeapon.get().get();
    }
    
    public float getHealthRegenHealth() {
        return healthRegenHealth.get();
    }
    
    public FloatProperty healthRegenHealthProperty() {
        return healthRegenHealth;
    }
    
    public FloatProperty speedProperty() {
        return speed;
    }
    
    public FloatProperty dashSpeedProperty() {
        return dashSpeed;
    }
    
    public IntegerProperty dashIntervalInMillisProperty() {
        return dashIntervalInMillis;
    }
    
    public ObjectProperty<WeaponKind> currentWeaponProperty() {
        return currentWeapon;
    }
}
