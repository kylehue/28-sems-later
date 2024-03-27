package entity;

import colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import main.CollisionGroup;
import main.GameApplication;
import sprites.ZombieSprite;
import utils.Bounds;
import utils.GameUtils;

public class Zombie extends Entity {
    // basic characteristics
    private float speed = GameUtils.random(200f, 700f);
    private int damage = 1;
    // sprite
    private final ZombieSprite sprite = new ZombieSprite();
    // misc
    private final GameApplication gameApplication;
    private CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    
    
    public Zombie(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.sprite.randomizeFirstFrame();
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
        
        this.collider.setGroup(CollisionGroup.ZOMBIES);
        this.collider.addToGroup(CollisionGroup.MAP_BOUNDS);
        this.collider.addToGroup(CollisionGroup.MAP_TILES);
        this.collider.addToGroup(CollisionGroup.ZOMBIES);
        this.collider.addToGroup(CollisionGroup.BULLETS);
        this.collider.excludeResolutionToGroup(CollisionGroup.BULLETS);
        
        this.collider.setRadius(5);
        this.collider.setMass(1);
        registerIntervalFor("zombie", 5000);
    }
    
    public void render(GraphicsContext ctx) {
        this.sprite.render(ctx);
    }
    
    public void update(float deltaTime) {
        this.handleMovements();
        this.handleSprite();
        this.updateAngleToPlayer();
        this.checkPlayerCollision();
        
        // put in quadtree
        this.gameApplication.getGameScene().getWorld().getQuadtree().insert(
            collider,
            new Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2,
                collider.getPosition().getY() - collider.getHeight() / 2,
                collider.getWidth(),
                collider.getHeight()
            )
        );
    }
    
    private void checkPlayerCollision() {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
        boolean isCollidingWithPlayer = collider.isCollidingWith(
            player.getCollider()
        );
        if(isIntervalOverFor("zombie") && isCollidingWithPlayer) {
            player.setCurrentHealth(player.getCurrentHealth() - damage);
            resetIntervalFor("zombie");
        }
    }
    
    private void updateAngleToPlayer() {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
        this.angleToPlayer = this.getPosition().getAngle(player.getPosition());
        this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
    }
    
    private void handleSprite() {
        this.sprite.setPosition(
            getPosition().getX(),
            getPosition().getY()
        );
        this.sprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
        this.sprite.nextFrame();
    }
    
    private void handleMovements() {
        this.getPosition().set(
            collider.getPosition().clone().subtract(0, collider.getRadius())
        );
        this.collider.applyForce(
            (float) (Math.cos(angleToPlayer) * speed * collider.getMass()),
            (float) (Math.sin(angleToPlayer) * speed * collider.getMass())
        );
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
