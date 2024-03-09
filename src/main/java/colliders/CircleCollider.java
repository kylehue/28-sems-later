package colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.GameUtils;
import utils.Vector;

public class CircleCollider extends Collider {
    private double radius = 7.5;
    private double width = radius * 2;
    private double height = radius * 2;
    
    public CircleCollider(double x, double y, double radius) {
        this.getPosition().set(x, y);
        this.setRadius(radius);
    }
    
    public CircleCollider() {
    
    }
    
    private void updateSize() {
        this.width = radius * 2;
        this.height = radius * 2;
    }
    
    public double getRadius() {
        return radius;
    }
    
    public void setRadius(double radius) {
        this.radius = radius;
        this.updateSize();
    }
    
    @Override
    public double getWidth() {
        return this.width;
    }
    
    @Override
    public double getHeight() {
        return this.height;
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
