package colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.GameUtils;
import utils.Vector;

public class CircleCollider extends Collider {
    private float radius = 7.5f;
    private float width = radius * 2;
    private float height = radius * 2;
    
    public CircleCollider(float x, float y, float radius) {
        this.getPosition().set(x, y);
        this.setRadius(radius);
    }
    
    public CircleCollider() {
    
    }
    
    private void updateSize() {
        this.width = radius * 2;
        this.height = radius * 2;
    }
    
    public float getRadius() {
        return radius;
    }
    
    public void setRadius(float radius) {
        this.radius = radius;
        this.updateSize();
    }
    
    @Override
    public float getWidth() {
        return this.width;
    }
    
    @Override
    public float getHeight() {
        return this.height;
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        ctx.beginPath();
        String color = isAsleep() ? "#000" : isColliding() ? "#ff0000" : "#00ff00";
        ctx.setStroke(Paint.valueOf(color));
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
    
    @Override
    public CircleCollider clone() {
        CircleCollider collider = new CircleCollider();
        this.copyAttributesToCollider(collider);
        collider.setRadius(this.getRadius());
        return collider;
    }
}
