package entity;

import colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import sprites.ZombieSprite;
import utils.GameUtils;
import utils.Quadtree;

public class Zombie extends Entity {
    // basic characteristics
    private float speed = /*GameUtils.random(1, 1.5)*/0;
    
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
        this.collider.setRadius(5);
    }
    
    public void render(GraphicsContext ctx) {
        this.sprite.render(ctx);
    }
    
    public void update(float deltaTime) {
        this.handleMovements();
        this.handleSprite();
        this.updateAngleToPlayer();
        
        // put in quadtree
        this.gameApplication.getGameScene().getWorld().getQuadtree().insert(
            collider,
            new Quadtree.Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2,
                collider.getPosition().getY() - collider.getHeight() / 2,
                collider.getWidth(),
                collider.getHeight()
            )
        );
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
        this.collider.getVelocity().setX((float) (Math.cos(angleToPlayer) * speed));
        this.collider.getVelocity().setY((float) (Math.sin(angleToPlayer) * speed));
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
