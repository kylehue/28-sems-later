package game.entity;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;
import game.utils.Vector;

import java.util.ArrayList;

public class Bullet extends Entity {
    /* Stats */
    private float speed = 10000;
    private float maxDistance = 300;
    private float damage = 25;
    private int penetration = 25;
    
    /* State */
    private final Vector position = new Vector();
    private final Vector initialPosition = new Vector();
    private float angle = 0;
    
    /* Misc */
    private final CircleCollider collider = new CircleCollider();
    private final Image image = Common.loadImage("/weapons/bullet-1.png");
    
    public Bullet(World world, float x, float y, float angle) {
        super(world);
        this.initCollider(x, y);
        
        this.initialPosition.set(x, y);
        this.angle = angle;
        
        registerIntervalFor("bullet", 25);
    }
    
    private void initCollider(float x, float y) {
        this.collider.getPosition().set(x, y);
        this.collider.setGroup(Game.CollisionGroup.BULLETS);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.ZOMBIES);
        this.collider.addToGroup(Game.CollisionGroup.BULLETS);
        this.collider.excludeResolutionToGroup(Game.CollisionGroup.MAP);
        this.collider.excludeResolutionToGroup(Game.CollisionGroup.ZOMBIES);
        this.collider.excludeResolutionToGroup(Game.CollisionGroup.BULLETS);
        this.collider.setRadius(2);
        this.collider.setFriction(0.5f);
        this.collider.setMass(1);
        world.getColliderWorld().addCollider(
            this.collider
        );
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
        ctx.drawImage(image, -image.getWidth() / 2, -image.getHeight() / 2);
        ctx.restore();
    }
    
    public void update(float deltaTime) {
        this.handleMovement();
        this.checkZombieCollision();
    }
    
    public void handleMovement() {
        this.position.set(this.collider.getPosition());
        this.collider.applyForce(
            (float) Math.cos(angle) * this.speed,
            (float) Math.sin(angle) * this.speed
        );
    }
    
    private void checkZombieCollision() {
        ArrayList<Zombie> zombies = world.getZombies();
        for (Zombie zombie : zombies) {
            if (!this.collider.isCollidingWith(zombie.getCollider())) {
                continue;
            }
            if (isIntervalOverFor("bullet")) {
                zombie.setCurrentHealth(zombie.getCurrentHealth() - damage);
                this.setCurrentHealth(this.getCurrentHealth() - penetration);
                resetIntervalFor("bullet");
            }
            // if(this.collider.isCollidingWith(polygonCollider)){}
        }
    }
    
    public void setAngle(float angle) {
        this.angle = angle;
    }
    
    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
    
    public CircleCollider getCollider() {
        return collider;
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
}
