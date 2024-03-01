package entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import main.GameApplication;
import utils.Vector;

public class Enemy extends Entity {
    private final GameApplication gameApplication;
    
    public Enemy(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
    }
    
    public void render(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Color.web("red"));
        double radius = 20;
        ctx.fillOval(getPosition().getX() - radius, getPosition().getY() - radius, radius * 2, radius * 2);
        ctx.closePath();
    }
    
    public void update(){
        this.getPosition().add(this.getVelocity());
        
        
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
        Vector difference = player.getPosition().clone().subtract(this.getPosition());
        double angleToPlayer = Math.atan2(difference.getY(), difference.getX());
        
        this.getVelocity().setX(Math.cos(angleToPlayer) * 2);
        this.getVelocity().setY(Math.sin(angleToPlayer) * 2);
        //
        
        
    }
}
