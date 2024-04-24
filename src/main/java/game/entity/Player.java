package game.entity;

import game.Config;
import game.Game;
import game.Progress;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.InventoryManager;
import game.weapons.Gun;
import game.weapons.Weapon;
import game.sprites.DashSprite;
import game.sprites.PlayerSprite;
import game.utils.IntervalMap;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.canvas.GraphicsContext;
import event.KeyHandler;
import game.utils.Vector;

public class Player extends Entity {
    // stats
    private final FloatProperty speed = new SimpleFloatProperty();
    private final FloatProperty dashSpeed = new SimpleFloatProperty(
        Config.DEFAULT_PLAYER_DASH_SPEED
    );
    private final IntegerProperty dashIntervalInMillis = new SimpleIntegerProperty();
    
    // control flags
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean dashPressed = false;
    private boolean shootPressed = false;
    
    // misc
    private final CircleCollider collider = new CircleCollider();
    private boolean isFacingOnLeftSide = false;
    private float angleToMouse = 0;
    private final InventoryManager inventoryManager = new InventoryManager(this);
    private final IntervalMap intervals = new IntervalMap();
    private enum Interval {
        DASH
    }
    
    // sprites
    private final PlayerSprite bodySprite = new PlayerSprite();
    
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
        intervals.registerIntervalFor(Interval.DASH, dashIntervalInMillis.get());
        
        // Misc
        this.setZIndex(Game.ZIndex.PLAYER);
        
        maxHealthProperty().bindBidirectional(Progress.maxHealth);
        currentHealthProperty().bindBidirectional(Progress.currentHealth);
        speedProperty().bindBidirectional(Progress.movementSpeed);
        dashIntervalInMillisProperty().bindBidirectional(Progress.dashInterval);
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
        Weapon currentWeapon = inventoryManager.getCurrentWeapon();
        currentWeapon.render(ctx, alpha);
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
        
        if (shootPressed) {
            this.shoot();
        }
    }
    
    @Override
    public void dispose() {
        // ...
    }
    
    public void shoot() {
        Weapon currentWeapon = inventoryManager.getCurrentWeapon();
        
        if (!(currentWeapon instanceof Gun currentGun)) return;
        currentGun.shoot(Game.world, position, angleToMouse);
    }
    
    private void updateAngleToMouse() {
        Vector mouseInWorld = Game.world.getMousePosition();
        this.angleToMouse = position.getAngle(mouseInWorld);
        this.isFacingOnLeftSide = Math.abs(angleToMouse) > (Math.PI / 2);
    }
    
    public void dash() {
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
    }
    
    private void updateControlFlags() {
        KeyHandler keyHandler = Game.keyHandler;
        this.upPressed = keyHandler.isKeyPressed("up");
        this.downPressed = keyHandler.isKeyPressed("down");
        this.leftPressed = keyHandler.isKeyPressed("left");
        this.rightPressed = keyHandler.isKeyPressed("right");
        this.dashPressed = keyHandler.isKeyPressed("dash");
        this.shootPressed = Game.mouseHandler.isMouseLeftPressed();
    }
    
    private void handleMovements() {
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
        if (this.collider.getVelocity().getMagnitude() <= 0.25) {
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
    
    @Override
    public Vector getRenderPosition() {
        return position.clone().addY(collider.getRadius());
    }
    
    @Override
    public Collider getCollider() {
        return collider;
    }
    
    public InventoryManager getInventoryManager() {
        return inventoryManager;
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
    
    public FloatProperty speedProperty() {
        return speed;
    }
    
    public FloatProperty dashSpeedProperty() {
        return dashSpeed;
    }
    
    public IntegerProperty dashIntervalInMillisProperty() {
        return dashIntervalInMillis;
    }
}
