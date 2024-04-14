package entity;

import colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.CollisionGroup;
import main.GameApplication;
import utils.LayoutUtils;
import utils.Vector;

import java.util.ArrayList;

public class Bullet extends Entity{
    private final GameApplication gameApplication;
    private float angle = 0;
    private float speed = 10000;
    private float maxDistance = 300;
    private Vector initialPosition = new Vector();
    private final Vector position = new Vector();
    private final CircleCollider collider = new CircleCollider();
    private int damage = 25;
    private int penetration = 25;
    private Image image = LayoutUtils.loadImage("/weapons/bullet-1.png");
    
    public Bullet(GameApplication gameApplication, float x, float y, float angle) {
        this.gameApplication = gameApplication;
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
        this.collider.getPosition().set(x, y);
        this.initialPosition.set(x, y);
        this.angle = angle;
        this.collider.setGroup(CollisionGroup.BULLETS);
        this.collider.addToGroup(CollisionGroup.MAP);
        this.collider.addToGroup(CollisionGroup.ZOMBIES);
        this.collider.addToGroup(CollisionGroup.BULLETS);
        this.collider.setRadius(2);
        this.collider.setFriction(0.5f);
        this.collider.setMass(1);
        registerIntervalFor("bullet", 25);
    }
    
    public void render(GraphicsContext ctx, float alpha) {
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
        ctx.drawImage(image, 0, 0);
        ctx.restore();
    }
    
    public void update(float deltaTime) {
        this.handleMovement();
        this.checkZombieCollision();
    }
    
    public CircleCollider getCollider() {
        return collider;
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
    
    public Vector getPosition() {
        return position;
    }
    
    public void handleMovement() {
        this.position.set(this.collider.getPosition());
        this.collider.applyForce(
            (float) Math.cos(angle) * this.speed,
            (float) Math.sin(angle) * this.speed
        );
    }
    
    private void checkZombieCollision() {
        ArrayList<Zombie> zombies = gameApplication.getGameScene().getWorld().getZombies();
        for (Zombie zombie : zombies) {
            if (!this.collider.isCollidingWith(zombie.getCollider())) {
                continue;
            }
            if(isIntervalOverFor("bullet")) {
                zombie.setCurrentHealth(zombie.getCurrentHealth() - damage);
                this.setCurrentHealth(this.getCurrentHealth() - penetration);
                resetIntervalFor("bullet");
            }
            // if(this.collider.isCollidingWith(polygonCollider)){}
        }
    }
}
