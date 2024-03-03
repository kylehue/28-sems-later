package colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.GameUtils;
import utils.Vector;

public class CircleCollider extends Collider {
    private double radius = 7.5;
    
    public CircleCollider(double x, double y, double radius) {
        this.getPosition().set(x, y);
        this.radius = radius;
    }
    
    public CircleCollider() {
    
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setStroke(Paint.valueOf("yellow"));
        ctx.strokeOval(
            getPosition().getX() - radius,
            getPosition().getY() - radius,
            radius * 2,
            radius * 2
        );
        ctx.closePath();
    }
    
    @Override
    public void resolveCollision(Collider otherCollider) {
        if (otherCollider instanceof CircleCollider otherCircleCollider) {
            double distance = this.getPosition().getDistanceFrom(otherCollider.getPosition());
            
            if (distance < this.radius + otherCircleCollider.radius) {
                double overlap = distance - this.radius - otherCircleCollider.radius;
                double angle = this.getPosition().getAngle(otherCollider.getPosition());
                
                if (!this.isStatic()) {
                    this.getVelocity().add(
                        Math.cos(angle) * overlap / 2,
                        Math.sin(angle) * overlap / 2
                    );
                }
                
                otherCircleCollider.getVelocity().subtract(
                    Math.cos(angle) * overlap / (this.isStatic() ? 1 : 2),
                    Math.sin(angle) * overlap / (this.isStatic() ? 1 : 2)
                );
            }
        }
    }
}
