package colliders;

import javafx.scene.paint.Paint;
import scenes.game.GameScene;
import scenes.game.World;
import utils.Vector;

import java.util.List;

public abstract class CollisionResolvers {
    public static void circleToCircle(
        CircleCollider circleA,
        CircleCollider circleB
    ) {
        if (!circleA.isGroupedWith(circleB)) return;
        
        float distance = circleA
            .getPosition()
            .getDistanceFrom(circleB.getPosition());
        float radiusSum = circleA.getRadius() + circleB.getRadius();
        boolean isColliding = distance <= radiusSum;
        
        if (!isColliding) {
            circleA.getContacts().remove(circleB.getId());
            circleB.getContacts().remove(circleA.getId());
            return;
        }
        
        circleA.getContacts().add(circleB.getId());
        circleB.getContacts().add(circleA.getId());
        
        // Resolve collision
        float overlap = radiusSum - distance;
        float angle = circleA.getPosition().getAngle(circleB.getPosition());
        
        Vector displacement = new Vector(
            (float) Math.cos(angle),
            (float) Math.sin(angle)
        ).scale(overlap);
        
        if (!circleA.isStatic() && !circleB.isStatic()) {
            float totalMass = circleA.getMass() + circleB.getMass();
            float ratioA = circleB.getMass() / totalMass;
            float ratioB = circleA.getMass() / totalMass;
            
            circleA.getPosition().subtract(displacement.clone().scale(ratioA));
            circleB.getPosition().add(displacement.clone().scale(ratioB));
        } else if (!circleA.isStatic()) { // Only circleA moves
            circleA.getPosition().subtract(displacement);
        } else if (!circleB.isStatic()) { // Only circleB moves
            circleB.getPosition().add(displacement);
        }
    }
    
    public static Vector calculatePolygonCentroid(List<Vector> vertices) {
        float sumX = 0;
        float sumY = 0;
        int numVertices = vertices.size();
        
        // Sum up all the vertex positions
        for (Vector vertex : vertices) {
            sumX += vertex.getX();
            sumY += vertex.getY();
        }
        
        // Calculate the average for each component
        float centerX = sumX / numVertices;
        float centerY = sumY / numVertices;
        
        // Return the centroid position
        return new Vector(centerX, centerY);
    }
    
    public static void circleToPolygon(
        CircleCollider circle,
        PolygonCollider polygon
    ) {
        if (!circle.isGroupedWith(polygon)) return;
        
        int vertexCount = polygon.getVertices().size();
        
        // Minimum vertex to make a polygon is 3, so we return if it's less than 3
        if (vertexCount < 3) return;
        
        // Perform separating axis theorem (polygon axes)
        Vector mtv = null;
        float minOverlap = Float.MAX_VALUE;
        for (int i = 0; i < vertexCount; i++) {
            // Pair 2 points that will act as the line
            Vector pointA = polygon.getVertices()
                .get(i)
                .clone()
                .add(polygon.getPosition());
            Vector pointB = polygon.getVertices()
                .get((i + 1) % vertexCount)
                .clone()
                .add(polygon.getPosition());
            
            // Get normal of the current line
            Vector normal = pointB.clone().subtract(pointA).normal();
            
            // Get projection of circle to normal axis
            float lineLength = pointA.getDistanceFrom(pointB);
            float circleDot = circle.getPosition().clone().dot(normal) / lineLength;
            float circleMin = circleDot - circle.getRadius();
            float circleMax = circleDot + circle.getRadius();
            
            // Get projection of each vertex in a polygon to normal axis
            float polygonMin = Float.MAX_VALUE;
            float polygonMax = -Float.MAX_VALUE;
            for (Vector vertex : polygon.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
                float vertexDot = vertexTransformed.dot(normal) / lineLength;
                polygonMin = Math.min(vertexDot, polygonMin);
                polygonMax = Math.max(vertexDot, polygonMax);
            }
            
            boolean isIntersecting = polygonMax >= circleMin && circleMax >= polygonMin;
            
            /*
             * If there's at least 1 case where the axes don't intersect,
             * it means that both shapes aren't colliding, so we stop.
             */
            if (!isIntersecting) {
                circle.getContacts().remove(polygon.getId());
                polygon.getContacts().remove(circle.getId());
                return;
            }
            
            // Calculate overlap
            float overlap = Math.abs(Math.min(circleMax, polygonMax) - Math.max(circleMin, polygonMin));
            if (overlap < minOverlap) {
                minOverlap = overlap;
                mtv = normal.clone().normalize().scale(overlap);
            }
        }
        
        /**
         * Now let's test the axis of circle:
         * Since it's a circle we only need to test 1 axis and it's the
         * point from circle's center to the closest point in the polygon.
         */
        
        // Get closest the point of polygon to circle
        Vector closestVectorToCircle = null;
        float minDistance = Float.MAX_VALUE;
        for (Vector vertex : polygon.getVertices()) {
            Vector transformedVertex = vertex.clone().add(
                polygon.getPosition()
            );
            float distance = transformedVertex.getDistanceFrom(
                circle.getPosition()
            );
            if (distance < minDistance) {
                closestVectorToCircle = transformedVertex;
                minDistance = distance;
            }
        }
        
        assert closestVectorToCircle != null;
        
        // Get axis points
        Vector pointA = circle.getPosition().clone();
        Vector pointB = closestVectorToCircle.clone();
        
        // Get projection of circle to axis
        Vector normal = pointB.clone().subtract(pointA).normal();
        float lineLength = pointA.getDistanceFrom(pointB);
        float circleDot = circle.getPosition().clone().dot(normal) / lineLength;
        float circleMin = circleDot - circle.getRadius();
        float circleMax = circleDot + circle.getRadius();
        
        // Get projection of each vertex in a polygon to axis
        float polygonMin = Float.MAX_VALUE;
        float polygonMax = -Float.MAX_VALUE;
        for (Vector vertex : polygon.getVertices()) {
            Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
            float vertexDot = vertexTransformed.dot(normal) / lineLength;
            polygonMin = Math.min(vertexDot, polygonMin);
            polygonMax = Math.max(vertexDot, polygonMax);
        }
        
        // Stop if it's not intersecting (2 shapes are not colliding)
        boolean isIntersecting = polygonMax >= circleMin && circleMax >= polygonMin;
        if (!isIntersecting) {
            circle.getContacts().remove(polygon.getId());
            polygon.getContacts().remove(circle.getId());
            return;
        }
        
        /**
         * If we got here, it means that the 2 shapes are colliding.
         * So we resolve the collision:
         */
        
        circle.getContacts().add(polygon.getId());
        polygon.getContacts().add(circle.getId());
        
        // Calculate overlap
        float overlap = Math.abs(Math.min(circleMax, polygonMax) - Math.max(circleMin, polygonMin));
        if (overlap < minOverlap) {
            minOverlap = overlap;
            mtv = normal.clone().normalize().scale(overlap);
        }
        
        // Resolve
        if (mtv != null) {
            Vector polygonCenter = polygon.getPosition().clone().add(
                calculatePolygonCentroid(polygon.getVertices())
            );
            Vector centerToCenter = polygonCenter.clone().subtract(
                circle.getPosition()
            );
            float direction = mtv.dot(centerToCenter) < 0 ? 1 : -1;
            
            mtv.scale(direction);
            if (!circle.isStatic() && !polygon.isStatic()) {
                float totalMass = circle.getMass() + polygon.getMass();
                float ratioA = polygon.getMass() / totalMass;
                float ratioB = circle.getMass() / totalMass;
                
                // Apply MTV based on mass ratios
                circle.getPosition().add(mtv.clone().scale(ratioA));
                polygon.getPosition().subtract(mtv.clone().scale(ratioB));
            } else if (!circle.isStatic()) {
                circle.getPosition().add(mtv);
            } else if (!polygon.isStatic()) {
                polygon.getPosition().subtract(mtv);
            }
        }
    }
    
    public static void polygonToPolygon(
        PolygonCollider polygonA,
        PolygonCollider polygonB
    ) {
        if (!polygonA.isGroupedWith(polygonB)) return;
        
        Vector mtv = null;
        float minOverlap = Float.MAX_VALUE;
        boolean swapped = false;
        for (int i = 0; i < polygonA.getVertices().size(); i++) {
            // Pair 2 points that will act as the line
            Vector pointA = polygonA.getVertices()
                .get(i)
                .clone()
                .add(polygonA.getPosition());
            Vector pointB = polygonA.getVertices()
                .get((i + 1) % polygonA.getVertices().size())
                .clone()
                .add(polygonA.getPosition());
            
            // Get normal of the current line
            Vector normal = pointB.clone().subtract(pointA).normal();
            
            // Get projection of polygonA to normal
            float lineLength = pointA.getDistanceFrom(pointB);
            float polygonAMin = Float.MAX_VALUE;
            float polygonAMax = -Float.MAX_VALUE;
            for (Vector vertex : polygonA.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygonA.getPosition());
                float vertexDot = vertexTransformed.dot(normal) / lineLength;
                polygonAMin = Math.min(vertexDot, polygonAMin);
                polygonAMax = Math.max(vertexDot, polygonAMax);
            }
            
            // Get projection of polygonB to normal
            float polygonBMin = Float.MAX_VALUE;
            float polygonBMax = -Float.MAX_VALUE;
            for (Vector vertex : polygonB.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygonB.getPosition());
                float vertexDot = vertexTransformed.dot(normal) / lineLength;
                polygonBMin = Math.min(vertexDot, polygonBMin);
                polygonBMax = Math.max(vertexDot, polygonBMax);
            }
            
            boolean isIntersecting = polygonAMax >= polygonBMin && polygonBMax >= polygonAMin;
            
            // If there's at least 1 case where the projections don't
            // intersect, it means that both shapes aren't colliding.
            if (!isIntersecting) {
                polygonA.getContacts().remove(polygonB.getId());
                polygonB.getContacts().remove(polygonA.getId());
                return;
            }
            
            // Calculate overlap
            float overlap = Math.abs(Math.min(polygonAMax, polygonBMax) - Math.max(polygonAMin, polygonBMin));
            if (overlap < minOverlap) {
                minOverlap = overlap;
                mtv = normal.clone().normalize().scale(overlap);
            }
            
            // Check other way around if the last axis still intersects
            boolean isLast = i == polygonA.getVertices().size() - 1;
            if (isLast && !swapped) {
                PolygonCollider polygonATemp = polygonA;
                polygonA = polygonB;
                polygonB = polygonATemp;
                i = -1;
                swapped = true;
            }
        }
        
        /**
         * If we got here, it means that the 2 polygons are colliding.
         * So we resolve the collision:
         */
        
        polygonA.getContacts().add(polygonB.getId());
        polygonB.getContacts().add(polygonA.getId());
        
        // Resolve
        if (mtv != null) {
            Vector polygonACenter = polygonA.getPosition().clone().add(
                calculatePolygonCentroid(polygonA.getVertices())
            );
            Vector polygonBCenter = polygonB.getPosition().clone().add(
                calculatePolygonCentroid(polygonB.getVertices())
            );
            Vector centerToCenter = polygonBCenter.clone().subtract(
                polygonACenter
            );
            float direction = mtv.dot(centerToCenter) < 0 ? 1 : -1;
            mtv.scale(direction);
            
            if (!polygonA.isStatic() && !polygonB.isStatic()) {
                float totalMass = polygonA.getMass() + polygonB.getMass();
                float ratioA = polygonB.getMass() / totalMass;
                float ratioB = polygonA.getMass() / totalMass;
                
                // Apply MTV based on mass ratios
                polygonA.getPosition().add(mtv.clone().scale(ratioA));
                polygonB.getPosition().subtract(mtv.clone().scale(ratioB));
            } else if (!polygonA.isStatic()) {
                polygonA.getPosition().add(mtv);
            } else if (!polygonB.isStatic()) {
                polygonB.getPosition().subtract(mtv);
            }
        }
    }
}