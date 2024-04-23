package game.colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import game.utils.Bounds;
import game.utils.Vector;

public class PolygonCollider extends Collider {
    private final Vector[] vertices;
    private float angle = 0;
    private float width = 0;
    private float height = 0;
    
    public PolygonCollider(Vector[] vertices) {
        this.vertices = vertices;
        if (vertices.length == 0) {
            throw new Error("You cannot create a polygon with no vertices!");
        }
        
        Bounds bounds = this.getVerticesBounds();
        for (Vector vertex : vertices) {
            vertex.subtract(bounds.getX(), bounds.getY());
        }
        
        this.updateSize();
    }
    
    public Vector[] getVertices() {
        return vertices;
    }
    
    private Bounds getVerticesBounds() {
        float minX = Float.MAX_VALUE;
        float maxX = -Float.MAX_VALUE;
        float minY = Float.MAX_VALUE;
        float maxY = -Float.MAX_VALUE;
        for (Vector vertex : vertices) {
            minX = Math.min(minX, vertex.getX());
            maxX = Math.max(maxX, vertex.getX());
            minY = Math.min(minY, vertex.getY());
            maxY = Math.max(maxY, vertex.getY());
        }
        
        minX = minX == Float.MAX_VALUE ? 0 : minX;
        minY = minY == Float.MAX_VALUE ? 0 : minY;
        maxX = maxX == -Float.MAX_VALUE ? 0 : maxX;
        maxY = maxY == -Float.MAX_VALUE ? 0 : maxY;
        
        return new Bounds(
            (minX + maxX) / 2,
            (minY + maxY) / 2,
            maxX - minX,
            maxY - minY
        );
    }
    
    private void updateSize() {
        Bounds bounds = this.getVerticesBounds();
        this.width = bounds.getWidth();
        this.height = bounds.getHeight();
    }
    
    public void setAngle(float angleInRadians) {
        float deltaAngle = angleInRadians - this.angle;
        float cos = (float) Math.cos(deltaAngle);
        float sin = (float) Math.sin(deltaAngle);
        
        for (Vector vertex : vertices) {
            float x = vertex.getX() * cos - vertex.getY() * sin;
            float y = vertex.getX() * sin + vertex.getY() * cos;
            vertex.set(x, y);
        }
        
        this.angle = angleInRadians;
        this.updateSize();
    }
    
    public float getAngle() {
        return angle;
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
        if (vertices.length == 0) return;
        ctx.beginPath();
        String color = isAsleep() ? "#000" : isColliding() ? "#ff0000" : "#00ff00";
        ctx.setStroke(Paint.valueOf(color));
        float x = this.getPosition().getX();
        float y = this.getPosition().getY();
        ctx.moveTo(vertices[0].getX() + x, vertices[0].getY() + y);
        for (int i = 1; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            ctx.lineTo(vertex.getX() + x, vertex.getY() + y);
        }
        ctx.lineTo(vertices[0].getX() + x, vertices[0].getY() + y);
        ctx.stroke();
        ctx.closePath();
    }
    
    @Override
    public void resolveCollision(Collider otherCollider) {
        if (otherCollider instanceof PolygonCollider) {
            CollisionResolvers.polygonToPolygon(
                this,
                (PolygonCollider) otherCollider
            );
        } else if (otherCollider instanceof CircleCollider) {
            CollisionResolvers.circleToPolygon(
                (CircleCollider) otherCollider,
                this
            );
        } else if (otherCollider instanceof GroupedCollider groupedCollider) {
            for (Collider collider : groupedCollider.getColliders()) {
                resolveCollision(collider);
            }
        }
    }
    
    @Override
    public PolygonCollider clone() {
        Vector[] verticesClone = new Vector[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            Vector vertex = vertices[i];
            verticesClone[i] = vertex.clone();
        }
        
        PolygonCollider collider = new PolygonCollider(verticesClone);
        this.copyAttributesToCollider(collider);
        collider.setAngle(angle);
        return collider;
    }
}
