package entity;

import colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import sprites.ZombieSprite;
import utils.GameUtils;
import utils.Quadtree;

public class Zombie extends Entity {
    private final GameApplication gameApplication;
    private final ZombieSprite sprite = new ZombieSprite();
    private boolean isFacingOnLeftSide = false;
    private double angleToPlayer = 0;
    private double speed = GameUtils.random(0.2, 0.8);
    private CircleCollider collider = new CircleCollider();
    
    public Zombie(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.sprite.randomizeFirstFrame();
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
    }
    
    public void render(GraphicsContext ctx) {
        /*ctx.beginPath();
        ctx.setFill(Color.web("red"));
        double radius = 20;
        ctx.fillOval(getPosition().getX() - radius, getPosition().getY() - radius, radius * 2, radius * 2);
        ctx.closePath();*/
        this.sprite.render(ctx);
        this.sprite.setPosition(
            getPosition().getX(),
            getPosition().getY()
        );
        this.sprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
        this.sprite.nextFrame();
    }
    
    public void update(double deltaTime) {
        collider.getPosition().add(collider.getVelocity().clone().scale(deltaTime));
        this.getPosition().set(collider.getPosition());
        this.handleMovements();
        this.updateAngleToPlayer();
        
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
    
    private void handleMovements() {
        this.collider.getVelocity().setX(Math.cos(angleToPlayer) * speed);
        this.collider.getVelocity().setY(Math.sin(angleToPlayer) * speed);
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
}
