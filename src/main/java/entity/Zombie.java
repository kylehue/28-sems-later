package entity;

import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import sprites.ZombieSprite;

public class Zombie extends Entity {
    private final GameApplication gameApplication;
    private final ZombieSprite sprite = new ZombieSprite();
    private boolean isFacingOnLeftSide = false;
    private double angleToPlayer = 0;
    private double speed = Math.random() + 0.5;
    
    public Zombie(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.sprite.randomizeFirstFrame();
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
    
    public void update() {
        this.getPosition().add(this.getVelocity());
        this.handleMovements();
        this.updateAngleToPlayer();
    }
    
    private void updateAngleToPlayer() {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
        this.angleToPlayer = this.getPosition().getAngle(player.getPosition());
        this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
    }
    
    private void handleMovements() {
        this.getVelocity().setX(Math.cos(angleToPlayer) * speed);
        this.getVelocity().setY(Math.sin(angleToPlayer) * speed);
    }
}
