package colliders;

import javafx.scene.paint.Paint;
import scenes.game.GameScene;
import utils.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CollisionResolvers {
    public static void circleToCircle(
        CircleCollider circleA,
        CircleCollider circleB
    ) {
        double distance = circleA
            .getPosition()
            .getDistanceFrom(circleB.getPosition());
        
        if (distance < circleA.getRadius() + circleB.getRadius()) {
            double overlap = distance - circleA.getRadius() - circleB.getRadius();
            double angle = circleA.getPosition().getAngle(circleB.getPosition());
            if (!circleA.isStatic()) {
                int div = circleB.isStatic() ? 1 : 2;
                circleA.getPosition().add(
                    Math.cos(angle) * overlap / div,
                    Math.sin(angle) * overlap / div
                );
                circleB.getVelocity().scale(
                    circleA.getVelocity().getMagnitude()
                );
            }
            
            if (!circleB.isStatic()) {
                int div = circleA.isStatic() ? 1 : 2;
                circleB.getPosition().subtract(
                    Math.cos(angle) * overlap / div,
                    Math.sin(angle) * overlap / div
                );
                circleA.getVelocity().scale(
                    circleB.getVelocity().getMagnitude()
                );
            }
        }
    }
    
    private static boolean performSAT(
        List<Vector> objectA,
        List<Vector> objectB
    ) {
        boolean swapped = false;
        for (int i = 0; i < objectA.size(); i++) {
            // Pair 2 points that will act as the line
            Vector pointA = objectA.get(i);
            Vector pointB = objectA.get((i + 1) % objectA.size());
            
            // Get normal of the current line
            Vector lineDiff = pointB.clone().subtract(pointA);
            Vector normalPointA = new Vector(
                -lineDiff.getY(),
                lineDiff.getX()
            );
            Vector normalPointB = new Vector(
                lineDiff.getY(),
                -lineDiff.getX()
            );
            
            // Get projection of objectA to normal
            Vector normalLineDiff = normalPointB.clone().subtract(normalPointA);
            double normalLineLength = normalPointA.getDistanceFrom(normalPointB);
            double objectAMin = Double.MAX_VALUE;
            double objectAMax = -Double.MAX_VALUE;
            for (Vector vertex : objectA) {
                double vertexDot = vertex.clone().subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
                objectAMin = Math.min(vertexDot, objectAMin);
                objectAMax = Math.max(vertexDot, objectAMax);
            }
            
            // Get projection of objectB to normal
            double objectBMin = Double.MAX_VALUE;
            double objectBMax = -Double.MAX_VALUE;
            for (Vector vertex : objectB) {
                double vertexDot = vertex.clone().subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
                objectBMin = Math.min(vertexDot, objectBMin);
                objectBMax = Math.max(vertexDot, objectBMax);
            }
            
            boolean isIntersecting = objectAMax >= objectBMin && objectBMax >= objectAMin;
            
            // If there's at least 1 case where the axes don't intersect,
            // it means that both shapes aren't colliding, so we stop.
            if (!isIntersecting) {
                return false;
            }
            
            if (i == objectA.size() - 1 && !swapped) {
                List<Vector> objectATemp = objectA;
                objectA = objectB;
                objectB = objectATemp;
                i = -1;
                swapped = true;
            }
        }
        
        return true;
    }
    
    public static void circleToPolygon(
        CircleCollider circle,
        PolygonCollider polygon
    ) {
        int vertexCount = polygon.getVertices().size();
        
        // minimum vertex to make a polygon is 3, so we return if it's less than 3
        if (vertexCount < 3) return;
        
        Vector closestVectorToCircle = null;
        double minDistance = Double.MAX_VALUE;
        
        for (Vector vertex : polygon.getVertices()) {
            Vector transformedVertex = vertex.clone().add(
                polygon.getPosition()
            );
            double distance = transformedVertex.getDistanceFrom(
                circle.getPosition()
            );
            if (distance < minDistance) {
                closestVectorToCircle = transformedVertex.clone();
                minDistance = distance;
            }
        }
        
        assert closestVectorToCircle != null;
        
        // perform separating axis theorem
        Vector mtv = null;
        double minOverlap = Double.MAX_VALUE;
        boolean isColliding = true;
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
            Vector lineDiff = pointB.clone().subtract(pointA);
            Vector normalPointA = new Vector(
                -lineDiff.getY(),
                lineDiff.getX()
            );
            Vector normalPointB = new Vector(
                lineDiff.getY(),
                -lineDiff.getX()
            );
            
            // Get projection of circle to normal
            Vector normalLineDiff = normalPointB.clone().subtract(normalPointA);
            double normalLineLength = normalPointA.getDistanceFrom(normalPointB);
            double circleDot = circle.getPosition().clone().subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
            double circleMin = circleDot - circle.getRadius();
            double circleMax = circleDot + circle.getRadius();
            
            // Get projection of each vertex in a polygon to normal
            double polygonMin = Double.MAX_VALUE;
            double polygonMax = -Double.MAX_VALUE;
            for (Vector vertex : polygon.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
                double vertexDot = vertexTransformed.subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
                polygonMin = Math.min(vertexDot, polygonMin);
                polygonMax = Math.max(vertexDot, polygonMax);
            }
            
            boolean isIntersecting = polygonMax >= circleMin && circleMax >= polygonMin;
            
            // If there's at least 1 case where the axes don't intersect,
            // it means that both shapes aren't colliding, so we stop.
            if (!isIntersecting) {
                return;
            }
            
            double overlap = polygonMax - circleMin;
            if (overlap < minOverlap) {
                // Update minimum overlap and corresponding MTV
                minOverlap = overlap;
                mtv = normalLineDiff.normalize().scale(overlap);
            }
        }
        
        Vector pointA = circle.getPosition()
            .clone();
        Vector pointB = closestVectorToCircle.clone();
        
        // Get normal of the current line
        Vector lineDiff = pointB.clone().subtract(pointA);
        Vector normalPointA = pointA.clone();
        Vector normalPointB = pointB.clone();
        
        // Get projection of circle to normal
        Vector normalLineDiff = normalPointB.clone().subtract(normalPointA);
        double normalLineLength = normalPointA.getDistanceFrom(normalPointB);
        double circleDot = circle.getPosition().clone().subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
        double circleMin = circleDot - circle.getRadius();
        double circleMax = circleDot + circle.getRadius();
        
        // Get projection of each vertex in a polygon to normal
        double polygonMin = Double.MAX_VALUE;
        double polygonMax = -Double.MAX_VALUE;
        for (Vector vertex : polygon.getVertices()) {
            Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
            double vertexDot = vertexTransformed.subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
            polygonMin = Math.min(vertexDot, polygonMin);
            polygonMax = Math.max(vertexDot, polygonMax);
        }
        
        boolean isIntersecting = polygonMax >= circleMin && circleMax >= polygonMin;
        
        // If there's at least 1 case where the axes don't intersect,
        // it means that both shapes aren't colliding, so we stop.
        if (!isIntersecting) {
            return;
        }
        
        double overlap = polygonMax - circleMin;
        if (overlap < minOverlap) {
            // Update minimum overlap and corresponding MTV
            minOverlap = overlap;
            mtv = normalLineDiff.normalize().scale(overlap);
        }
        
        if (mtv != null) {
            // Move the circle away from the polygon along the MTV
            
            
            if (!circle.isStatic()) {
                int div = polygon.isStatic() ? 1 : 2;
                circle.getPosition().add(mtv.divide(div));
                polygon.getVelocity().scale(
                    circle.getVelocity().getMagnitude()
                );
            }
            
            if (!polygon.isStatic()) {
                int div = circle.isStatic() ? 1 : 2;
                polygon.getPosition().subtract(mtv.divide(div));
                circle.getVelocity().scale(
                    polygon.getVelocity().getMagnitude()
                );
            }
            
            // Now you can determine which line of the polygon was involved in the collision
            // You can use additional logic to identify the specific line if needed
        }
        
        // for (int i = 0; i < vertexCount; i++) {
        //     // Pair 2 points that will act as the line
        //     Vector pointA = polygon.getVertices()
        //         .get(i)
        //         .clone()
        //         .add(polygon.getPosition());
        //     Vector pointB = polygon.getVertices()
        //         .get((i + 1) % vertexCount)
        //         .clone()
        //         .add(polygon.getPosition());
        //
        //     // Get vector projection through dot product
        //     double lineLength = pointA.getDistanceFrom(pointB);
        //     double dot = circle.getPosition()
        //         .clone()
        //         .subtract(pointA)
        //         .dot(pointB.clone().subtract(pointA)) / Math.pow(lineLength, 2);
        //     Vector projection = pointA.clone()
        //         .add(
        //             pointB.clone()
        //                 .subtract(pointA)
        //                 .scale(dot)
        //         );
        //
        //     // Clamp the projection so that it won't travel outside the line
        //     if (dot < 0) projection = pointA;
        //     else if (dot > 1) projection = pointB;
        //
        //     // Collision check
        //     double circleDistanceFromProjection = projection.getDistanceFrom(circle.getPosition());
        //     boolean isColliding2 = circleDistanceFromProjection <= circle.getRadius();
        //     if (isColliding2) {
        //         double angleToProjection = circle.getPosition().getAngle(projection);
        //         double overlap = Math.abs(circle.getRadius() - circleDistanceFromProjection);
        //
        //         if (!polygon.isStatic()) {
        //             polygon.getPosition().add(
        //                 Math.cos(angleToProjection) * overlap,
        //                 Math.sin(angleToProjection) * overlap
        //             );
        //         }
        //
        //         if (!circle.isStatic()) {
        //             circle.getPosition().subtract(
        //                 Math.cos(angleToProjection) * overlap,
        //                 Math.sin(angleToProjection) * overlap
        //             );
        //         }
        //     }
        // }
    }
    
    public static void polygonToPolygon(
        PolygonCollider polygonA,
        PolygonCollider polygonB
    ) {
        // overlap = Math.min(
        //                 Math.min(circleMax, polygonMax) - Math.max(circleMin, polygonMin),
        //                 overlap
        //             );
        // Vector d = polygon.getPosition().clone().subtract(circle.getPosition());
        // double s = d.getMagnitude();
        // s = s == 0 ? 0.0001 : s;
        // polygon.getPosition().add(
        //     overlap * d.getX() / s,
        //     overlap * d.getY() / s
        // );
    }
}
