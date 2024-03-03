package colliders;

import utils.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class PolygonCollider extends Collider {
    private final ArrayList<Vector> vertices = new ArrayList<>();
    public PolygonCollider(Vector[] vertices) {
        this.vertices.addAll(Arrays.stream(vertices).toList());
    }
}
