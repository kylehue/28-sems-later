package entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameApplication;
import sprites.BulletSprite;
import sprites.PlayerSprite;
import utils.Vector;

public class Bullet extends Entity {
    private final GameApplication gameApplication;
    private float angle = 0;
    private float speed = 10;
    private float maxDistance = 300;
    private Vector initialPosition = new Vector();
    private final BulletSprite sprite = new BulletSprite();
    
    public Bullet(GameApplication gameApplication, float x, float y, float angle) {
        this.gameApplication = gameApplication;
        this.getPosition().set(x, y);
        this.initialPosition.set(x, y);
        this.angle = angle;
    }
    
    public void render(GraphicsContext ctx) {
        /*ctx.beginPath();
        ctx.setFill(Color.web("red"));
        float radius = 10;
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
    
    public void update(float deltaTime) {
        this.getPosition().add(this.getVelocity());
        this.getPosition().add(this.getVelocity().clone().scale(deltaTime));
        this.handleMovement();
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public float getMaxDistance() {
        return maxDistance;
    }
    
    public Vector getInitialPosition() {
        return initialPosition;
    }
    
    public void handleMovement() {
        this.getVelocity().set(
            (float) (Math.cos(this.angle) * this.speed),
            (float) (Math.sin(this.angle) * this.speed)
        );
    }
}
