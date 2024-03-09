package colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonCollider extends Collider {
    private final ArrayList<Vector> vertices = new ArrayList<>();
    private float angle = 0;
    private float width = 0;
    private float height = 0;
    
    public PolygonCollider() {
    }
    
    public ArrayList<Vector> getVertices() {
        return vertices;
    }
    
    private void updateSize() {
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
        
        this.width = Math.abs(minX - maxX);
        this.height = Math.abs(minY - maxY);
    }
    
    public void setVertices(Vector[] vertices) {
        this.vertices.clear();
        this.vertices.addAll(Arrays.stream(vertices).toList());
        this.updateSize();
    }
    
    public void addVertex(float x, float y) {
        this.vertices.add(new Vector(x, y));
        this.updateSize();
    }
    
    public void addVertex(Vector vertex) {
        this.addVertex(vertex.getX(), vertex.getY());
    }
    
    public void setAngle(float angle) {
        float d = angle - this.angle;
        for (Vector vertex : vertices) {
            float x = (float) (vertex.getX() * Math.cos(d) - vertex.getY() * Math.sin(d));
            float y = (float) (vertex.getX() * Math.sin(d) + vertex.getY() * Math.cos(d));
            vertex.set(x, y);
        }
        
        this.angle = angle;
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
        if (vertices.isEmpty()) return;
        ctx.beginPath();
        ctx.setStroke(Paint.valueOf("yellow"));
        float x = this.getPosition().getX();
        float y = this.getPosition().getY();
        ctx.moveTo(vertices.get(0).getX() + x, vertices.get(0).getY() + y);
        for (int i = 1; i < vertices.size(); i++) {
            Vector vertex = vertices.get(i);
            ctx.lineTo(vertex.getX() + x, vertex.getY() + y);
        }
        ctx.lineTo(vertices.get(0).getX() + x, vertices.get(0).getY() + y);
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
        }
    }
    
    @Override
    public PolygonCollider clone() {
        PolygonCollider collider = new PolygonCollider();
        this.copyAttributesToCollider(collider);
        collider.setAngle(angle);
        for (Vector vertex : vertices) {
            collider.addVertex(vertex.clone());
        }
        return collider;
    }
}
