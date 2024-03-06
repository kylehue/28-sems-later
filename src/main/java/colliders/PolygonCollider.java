package colliders;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonCollider extends Collider {
    private final ArrayList<Vector> vertices = new ArrayList<>();
    
    public PolygonCollider() {
    }
    
    public ArrayList<Vector> getVertices() {
        return vertices;
    }
    
    public void setVertices(Vector[] vertices) {
        this.vertices.clear();
        this.vertices.addAll(Arrays.stream(vertices).toList());
    }
    
    public void addVertex(double x, double y) {
        this.vertices.add(new Vector(x, y));
    }
    
    public void addVertex(Vector vertex) {
        this.vertices.add(vertex);
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        if (vertices.isEmpty()) return;
        ctx.beginPath();
        ctx.setStroke(Paint.valueOf("yellow"));
        double x = this.getPosition().getX();
        double y = this.getPosition().getY();
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
}
