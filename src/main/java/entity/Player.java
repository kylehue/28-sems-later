package entity;

import colliders.CircleCollider;
import colliders.Collider;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import main.CollisionGroup;
import main.ZIndex;
import scenes.game.GameScene;
import sprites.DashSprite;
import sprites.PlayerSprite;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import event.KeyHandler;
import utils.Bounds;
import utils.GameUtils;
import utils.LayoutUtils;
import utils.Vector;

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
    private final GameApplication gameApplication;
    private final CircleCollider collider = new CircleCollider();
    private boolean isFacingOnLeftSide = false;
    private float angleToMouse = 0;
    
    // sprites
    private final PlayerSprite bodySprite = new PlayerSprite();
    private final Image gunImage = LayoutUtils.loadImage("/weapons/pistol.png");
    private final DashSprite dashSprite = new DashSprite();
    // private final WeaponManager weaponManager = new WeaponManager();
    
    public Player(GameApplication gameApplication) {
        // weaponManager.setWeapon(WeaponManager.Weapons.PISTOL);
        this.gameApplication = gameApplication;
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
        
        this.collider.setGroup(CollisionGroup.PLAYER);
        this.collider.addToGroup(CollisionGroup.MAP);
        this.collider.addToGroup(CollisionGroup.MAP);
        this.collider.addToGroup(CollisionGroup.ZOMBIES);
        this.collider.setMass(50);
        
        this.collider.setRadius(5);
        dashSprite.setFrameAccumulator(dashSprite.getFrameLength(DashSprite.Animation.Default.name()));
        registerIntervalFor("shoot", 250);
        registerIntervalFor("dash", 1000);
        this.setZIndex(ZIndex.PLAYER);
        
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        // render dash smoke
        if (this.dashSprite.getFrameAccumulator() < this.dashSprite.getFrameLength(DashSprite.Animation.Default.name())) {
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
            getPosition().getX(),
            getPosition().getY()
        );
        this.bodySprite.nextFrame();
        
        // render gun
        ctx.save();
        ctx.translate(
            getPosition().getX(),
            getPosition().getY()
        );
        ctx.rotate(Math.toDegrees(angleToMouse));
        if (isFacingOnLeftSide) {
            ctx.scale(1, -1);
        }
        ctx.drawImage(gunImage, gunImage.getWidth() / 2, -gunImage.getHeight() / 2);
        ctx.restore();
    }
    
    @Override
    public Vector getRenderPosition() {
        return getPosition().clone().addY(collider.getRadius());
    }
    
    public void fixedUpdate(float deltaTime) {
        // put in quadtree
        this.gameApplication.getGameScene().getWorld().getQuadtree().insert(
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
        this.handleMovements();
        this.handleControls();
        this.updateAngleToMouse();
        this.handleSpriteAnimations();
        
        if (shootPressed) {
            this.shoot();
        }
    }
    
    public Collider getCollider() {
        return collider;
    }
    
    public void shoot() {
        if (isIntervalOverFor("shoot")) {
            float offset = (float) (gunImage.getWidth() + gunImage.getWidth() / 2);
            this.gameApplication.getGameScene().getWorld().spawnBullet(
                (float) (getPosition().getX() + Math.cos(this.angleToMouse) * offset),
                (float) (getPosition().getY() + Math.sin(this.angleToMouse) * offset),
                this.angleToMouse
            );
            resetIntervalFor("shoot");
        }
    }
    
    
    private void updateAngleToMouse() {
        GameScene gameScene = this.gameApplication.getGameScene();
        Vector mouseInWorld = gameScene.getWorld().getCamera().screenToWorld(
            gameScene.getMouseHandler().getPosition()
        );
        this.angleToMouse = getPosition().getAngle(mouseInWorld);
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
    
    private void handleControls() {
        KeyHandler keyHandler = gameApplication.getGameScene().getKeyHandler();
        this.upPressed = keyHandler.isKeyPressed("up");
        this.downPressed = keyHandler.isKeyPressed("down");
        this.leftPressed = keyHandler.isKeyPressed("left");
        this.rightPressed = keyHandler.isKeyPressed("right");
        this.dashPressed = keyHandler.isKeyPressed("dash");
        this.shootPressed = gameApplication.getGameScene().getMouseHandler().isMouseLeftPressed();
    }
    
    private void handleMovements() {
        getPosition().lerp(collider.getPosition().clone().addY(-collider.getRadius()), 0.25f);
        
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
}
