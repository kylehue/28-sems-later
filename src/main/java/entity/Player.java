package entity;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameApplication;
import utils.KeyHandler;

public class Player extends Entity {
    private final GameApplication gameApplication;
    private final double speed = 4;
    
    public Player(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        
        Canvas canvas = gameApplication.getGameScene().getGraphicsContext().getCanvas();
        this.getPosition().setX(canvas.getWidth() / 2);
        this.getPosition().setY(canvas.getHeight() / 2);
    }
    
    public void render(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Color.web("green"));
        double radius = 30;
        ctx.fillOval(getPosition().getX() - radius, getPosition().getY() - radius, radius * 2, radius * 2);
        ctx.closePath();
    }
    
    public void update() {
        this.getPosition().add(this.getVelocity());
        this.handleMovements();
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
        
        if ((leftPressed || rightPressed) && (upPressed || downPressed)) {
            this.getVelocity().normalize();
            this.getVelocity().scale(speed);
        }
    }
}
