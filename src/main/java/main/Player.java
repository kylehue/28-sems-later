package main;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import utils.Vector;

public class Player {
    public Vector position = new Vector();
    
    public Player() {
    
    }
    
    public void render(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Color.web("green"));
        double radius = 30;
        ctx.fillOval(position.getX() - radius, position.getY() - radius, radius * 2, radius * 2);
        ctx.closePath();
    }
    
    public void update() {
        this.position.add(1, 0);
    }
}
