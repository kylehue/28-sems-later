package entity;

import scenes.game.GameScene;
import sprites.GunSprite;
import sprites.PlayerSprite;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import event.KeyHandler;
import utils.Vector;

import java.util.concurrent.TimeUnit;

public class Player extends Entity {
    private final GameApplication gameApplication;
    private final double speed = 4;
    private final PlayerSprite bodySprite = new PlayerSprite();
    private final GunSprite gunSprite = new GunSprite();
    private double health = 0;
    private double angleToMouse = 0;
    private boolean isFacingOnLeftSide = false;
    private long lastShootTime = 0;
    private int fireRateInMillis = 250;
    private boolean isShooting = false;
    
    public Player(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
    }
    
    public void render(GraphicsContext ctx) {
        /*// guide
        ctx.beginPath();
        ctx.setFill(Color.web("green"));
        double radius = 30;
        ctx.fillOval(getPosition().getX() - radius, getPosition().getY() - radius, radius * 2, radius * 2);
        ctx.closePath();*/
        
        // render body
        this.bodySprite.render(ctx);
        this.bodySprite.nextFrame();
        this.bodySprite.setPosition(
            getPosition().getX(),
            getPosition().getY()
        );
        
        // render gun
        ctx.save();
        ctx.translate(
            getPosition().getX(),
            getPosition().getY()
        );
        ctx.rotate(Math.toDegrees(angleToMouse));
        this.gunSprite.render(ctx);
        ctx.restore();
        this.gunSprite.nextFrame();
        this.gunSprite.setPosition(15, 0);
    }
    
    public void update() {
        this.getPosition().add(this.getVelocity());
        this.handleMovements();
        this.handleSpriteAnimations();
        this.updateAngleToMouse();
        
        if (this.gameApplication.getGameScene().getMouseHandler().isMouseLeftPressed()) {
            this.shoot();
            isShooting = true;
            this.gunSprite.setFPS(30);
        } else {
            isShooting = false;
            this.gunSprite.setFPS(12);
        }
    }
    
    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getHealth() {
        return health;
    }
    
    public void shoot() {
        long timeNow = System.nanoTime();
        if (timeNow - lastShootTime > TimeUnit.MILLISECONDS.toNanos(fireRateInMillis)) {
            double offset = 30;
            this.gameApplication.getGameScene().getWorld().spawnBullet(
                this.getPosition().getX() + Math.cos(this.angleToMouse) * offset,
                this.getPosition().getY() + Math.sin(this.angleToMouse) * offset,
                this.angleToMouse
            );
            lastShootTime = System.nanoTime();
        }
    }
    
    public void setFireRateInMillis(int fireRateInMillis) {
        this.fireRateInMillis = fireRateInMillis;
    }
    
    private void updateAngleToMouse() {
        GameScene gameScene = this.gameApplication.getGameScene();
        Vector mouseInWorld = gameScene.getWorld().getCamera().screenToWorld(
            gameScene.getMouseHandler().getPosition()
        );
        this.angleToMouse = this.getPosition().getAngle(mouseInWorld);
        this.isFacingOnLeftSide = Math.abs(angleToMouse) > (Math.PI / 2);
    }
    
    private void handleMovements() {
        KeyHandler keyHandler = gameApplication.getGameScene().getKeyHandler();
        boolean upPressed = keyHandler.isKeyPressed("up");
        boolean downPressed = keyHandler.isKeyPressed("down");
        boolean leftPressed = keyHandler.isKeyPressed("left");
        boolean rightPressed = keyHandler.isKeyPressed("right");
        
        // x
        if (leftPressed || rightPressed) {
            if (leftPressed) {
                this.getVelocity().setX(-1 * speed);
            }
            if (rightPressed) {
                this.getVelocity().setX(1 * speed);
            }
        } else {
            this.getVelocity().setX(0);
        }
        
        // y
        if (upPressed || downPressed) {
            if (upPressed) {
                this.getVelocity().setY(-1 * speed);
            }
            if (downPressed) {
                this.getVelocity().setY(1 * speed);
            }
        } else {
            this.getVelocity().setY(0);
        }
        
        // fix speed in diagonal movement
        if ((leftPressed || rightPressed) && (upPressed || downPressed)) {
            this.getVelocity().normalize();
            this.getVelocity().scale(speed);
        }
    }
    
    private void handleSpriteAnimations() {
        if (isShooting) {
            this.gunSprite.set(GunSprite.Animation.Shoot);
        } else {
            this.gunSprite.set(GunSprite.Animation.Idle);
        }
        
        if (this.getVelocity().getX() == 0 && this.getVelocity().getY() == 0) {
            this.bodySprite.set(PlayerSprite.Animation.Idle);
            
            if (isShooting) {
                this.bodySprite.set(PlayerSprite.Animation.Shoot);
            }
        } else {
            this.bodySprite.set(PlayerSprite.Animation.Walk);
        }
        
        this.bodySprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
        this.gunSprite.setVerticallyFlipped(this.isFacingOnLeftSide);
    }
}
