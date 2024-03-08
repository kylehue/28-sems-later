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
    
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
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
        if (otherCollider instanceof CircleCollider) {
            CollisionResolvers.circleToCircle(
                this,
                (CircleCollider) otherCollider
            );
        } else if (otherCollider instanceof PolygonCollider) {
            CollisionResolvers.circleToPolygon(
                this,
                (PolygonCollider) otherCollider
            );
        }
    }
}
