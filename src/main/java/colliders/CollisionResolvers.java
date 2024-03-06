package colliders;

import javafx.scene.paint.Paint;
import scenes.game.GameScene;
import utils.Vector;

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
                circleA.getPosition().add(
                    Math.cos(angle) * overlap,
                    Math.sin(angle) * overlap
                );
            }

            if (!circleB.isStatic()) {
                circleB.getPosition().subtract(
                    Math.cos(angle) * overlap,
                    Math.sin(angle) * overlap
                );
            }
        }
    }
    
    public static void circleToPolygon(
        CircleCollider circle,
        PolygonCollider polygon
    ) {
        int vertexCount = polygon.getVertices().size();
        
        // minimum vertex to make a polygon is 3, so we return if it's less than 3
        if (vertexCount < 3) return;
        
        // https://www.desmos.com/calculator/whwiyjeqof
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
            
            // Get vector projection through dot product
            double lineLength = pointA.getDistanceFrom(pointB);
            double dot = circle.getPosition()
                .clone()
                .subtract(pointA)
                .dot(pointB.clone().subtract(pointA)) / Math.pow(lineLength, 2);
            Vector projection = pointA.clone()
                .add(
                    pointB.clone()
                        .subtract(pointA)
                        .scale(dot)
                );
            
            // Clamp the projection so that it won't travel outside the line
            if (dot < 0) projection = pointA;
            else if (dot > 1) projection = pointB;
            
            // Collision check
            double circleDistanceFromProjection = projection.getDistanceFrom(circle.getPosition());
            boolean isColliding = circleDistanceFromProjection <= circle.getRadius();
            if (isColliding) {
                double angleToProjection = circle.getPosition().getAngle(projection);
                double overlap = Math.abs(circle.getRadius() - circleDistanceFromProjection);
                
                if (!polygon.isStatic()) {
                    polygon.getPosition().add(
                        Math.cos(angleToProjection) * overlap,
                        Math.sin(angleToProjection) * overlap
                    );
                }
                
                if (!circle.isStatic()) {
                    circle.getPosition().subtract(
                        Math.cos(angleToProjection) * overlap,
                        Math.sin(angleToProjection) * overlap
                    );
                }
            }
        }
    }
    
    public static void polygonToPolygon(
        PolygonCollider polygonA,
        PolygonCollider polygonB
    ) {
    
    }
}
