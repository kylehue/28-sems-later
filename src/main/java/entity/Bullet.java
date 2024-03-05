package entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameApplication;
import sprites.BulletSprite;
import sprites.PlayerSprite;
import utils.Vector;

public class Bullet extends Entity {
    private final GameApplication gameApplication;
    private double angle = 0;
    private double speed = 10;
    private double maxDistance = 300;
    private Vector initialPosition = new Vector();
    private final BulletSprite sprite = new BulletSprite();
    
    public Bullet(GameApplication gameApplication, double x, double y, double angle) {
        this.gameApplication = gameApplication;
        this.getPosition().set(x, y);
        this.initialPosition.set(x, y);
        this.angle = angle;
    }
    
    public void render(GraphicsContext ctx) {
        /*ctx.beginPath();
        ctx.setFill(Color.web("red"));
        double radius = 10;
        ctx.fillOval(getPosition().getX() - radius, getPosition().getY() - radius, radius * 2, radius * 2);
        ctx.closePath();*/
        ctx.save();
        ctx.translate(
            getPosition().getX(),
            getPosition().getY()
        );
        ctx.rotate(Math.toDegrees(this.angle));
        sprite.render(ctx);
        ctx.restore();
    }
    
    public void update(double deltaTime) {
        this.getPosition().add(this.getVelocity());
        this.getPosition().add(this.getVelocity().clone().scale(deltaTime));
        this.handleMovement();
    }
    
    public void setAngle(double angle) {
        this.angle = angle;
    }
    
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public double getMaxDistance() {
        return maxDistance;
    }
    
    public Vector getInitialPosition() {
        return initialPosition;
    }
    
    public void handleMovement() {
        this.getVelocity().set(
            Math.cos(this.angle) * this.speed,
            Math.sin(this.angle) * this.speed
        );
    }
}
