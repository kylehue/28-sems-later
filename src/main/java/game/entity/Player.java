package game.entity;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.inventory.InventoryManager;
import game.inventory.weapons.Gun;
import game.inventory.weapons.Weapon;
import game.sprites.DashSprite;
import game.sprites.PlayerSprite;
import javafx.scene.canvas.GraphicsContext;
import event.KeyHandler;
import game.utils.Bounds;
import game.utils.Vector;

public class Player extends Entity {
    // basic characteristics
    private final float speed = 1000;
    
    // dash
    private float dashSpeed = 15000;
    private float dashAngle = 0;
    private final Vector dashPosition = new Vector();
    
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
    
    // sprites
    private final PlayerSprite bodySprite = new PlayerSprite();
    private final DashSprite dashSprite = new DashSprite();
    
    public Player(World world) {
        super(world);
        this.initCollider();
        this.initIntervals();
        
        dashSprite.setFrameAccumulator(
            dashSprite.getFrameLength()
        );
        
        this.setZIndex(Game.ZIndex.PLAYER);
        
        // inventoryManager.useWeapon(Weapons.AK47);
        // inventoryManager.usePowerUp(PowerUps.AdditionalSpeed);
        // inventoryManager.useItem(Items.Grenade);
    }
    
    private void initIntervals() {
        registerIntervalFor("shoot", 250);
        registerIntervalFor("dash", 1000);
    }
    
    private void initCollider() {
        this.collider.setGroup(Game.CollisionGroup.PLAYER);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MOBS);
        this.collider.setMass(50);
        this.collider.setRadius(5);
        world.getColliderWorld().addCollider(
            this.collider
        );
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        // render dash smoke
        if (this.dashSprite.getFrameAccumulator() < this.dashSprite.getFrameLength()) {
            ctx.save();
            ctx.translate(
                dashPosition.getX(),
                dashPosition.getY()
            );
            ctx.rotate(Math.toDegrees(dashAngle) - 90);
            this.dashSprite.render(ctx);
            ctx.restore();
            this.dashSprite.nextFrame();
            this.dashSprite.setPosition(
                0,
                -this.dashSprite.getHeight() / 2
            );
        }
        
        // render body
        this.bodySprite.render(ctx);
        this.bodySprite.setPosition(
            position.getX(),
            position.getY()
        );
        
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
        world.getQuadtree().insert(
            collider,
            new Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2f,
                collider.getPosition().getY() - collider.getHeight() / 2f,
                collider.getWidth(),
                collider.getHeight()
            )
        );
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
        
        if (!isIntervalOverFor("shoot")) return;
        if (!(currentWeapon instanceof Gun currentGun)) return;
        
        float xOffset = currentGun.getMuzzlePosition().getX() - currentGun.getHandlePosition().getX() + 4;
        Vector initialPosition = new Vector(
            (float) (position.getX() + Math.cos(this.angleToMouse) * xOffset),
            (float) (position.getY() + Math.sin(this.angleToMouse) * xOffset)
        );
        world.spawnGrenade(
            initialPosition,
            this.angleToMouse
        );
        
        resetIntervalFor("shoot");
        changeIntervalFor("shoot", currentGun.getFireRateInMillis());
    }
    
    private void updateAngleToMouse() {
        Vector mouseInWorld = world.getMousePosition();
        this.angleToMouse = position.getAngle(mouseInWorld);
        this.isFacingOnLeftSide = Math.abs(angleToMouse) > (Math.PI / 2);
    }
    
    public void dash() {
        if (!isIntervalOverFor("dash")) return;
        
        float computedSpeed = dashSpeed * collider.getMass();
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
        
        dashAngle = collider.getAcceleration().getAngle();
        
        this.dashSprite.resetFrames();
        this.dashPosition.set(getRenderPosition());
        resetIntervalFor("dash");
    }
    
    private void updateControlFlags() {
        KeyHandler keyHandler = world.getGame().getKeyHandler();
        this.upPressed = keyHandler.isKeyPressed("up");
        this.downPressed = keyHandler.isKeyPressed("down");
        this.leftPressed = keyHandler.isKeyPressed("left");
        this.rightPressed = keyHandler.isKeyPressed("right");
        this.dashPressed = keyHandler.isKeyPressed("dash");
        this.shootPressed = world.getGame().getMouseHandler().isMouseLeftPressed();
    }
    
    private void handleMovements() {
        position.lerp(collider.getPosition().clone().addY(-collider.getRadius()), 0.25f);
        
        // x controls
        float computedSpeed = speed * collider.getMass();
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
    
    @Override
    public Vector getRenderPosition() {
        return position.clone().addY(collider.getRadius());
    }
    
    public Collider getCollider() {
        return collider;
    }
}
