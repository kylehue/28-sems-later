package entity;

import colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import sprites.ZombieSprite;
import utils.GameUtils;
import utils.Quadtree;

public class Zombie extends Entity {
    // basic characteristics
    private double speed = GameUtils.random(0.2, 1);
    
    // sprite
    private final ZombieSprite sprite = new ZombieSprite();
    
    // misc
    private final GameApplication gameApplication;
    private CircleCollider collider = new CircleCollider();
    private double angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    
    public Zombie(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.sprite.randomizeFirstFrame();
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
    }
    
    public void render(GraphicsContext ctx) {
        this.sprite.render(ctx);
    }
    
    public void update(double deltaTime) {
        collider.getPosition().add(collider.getVelocity().clone().scale(deltaTime));
        this.handleMovements();
        this.handleSprite();
        this.updateAngleToPlayer();
        
        // put in quadtree
        this.gameApplication.getGameScene().getWorld().getQuadtree().insert(
            this.collider,
            new Quadtree.Bounds(
                this.collider.getPosition().getX() - 9,
                this.collider.getPosition().getY() - 12,
                18,
                25
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
        this.getPosition().set(collider.getPosition());
        this.collider.getVelocity().setX(Math.cos(angleToPlayer) * speed);
        this.collider.getVelocity().setY(Math.sin(angleToPlayer) * speed);
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
