package colliders;

import utils.GameUtils;
import utils.Vector;

public abstract class CollisionResolvers {
    public static void circleToCircle(
        CircleCollider circleA,
        CircleCollider circleB
    ) {
        double distance = circleA
            .getPosition()
            .getDistanceFrom(circleB.getPosition());
        double radiusSum = circleA.getRadius() + circleB.getRadius();
        boolean isColliding = distance <= radiusSum;
        
        if (!isColliding) {
            circleA.getContacts().remove(circleB.getId());
            circleB.getContacts().remove(circleA.getId());
            return;
        }
        
        circleA.getContacts().add(circleB.getId());
        circleB.getContacts().add(circleA.getId());
        
        // Resolve collision
        double overlap = distance - radiusSum;
        double angle = circleA.getPosition().getAngle(circleB.getPosition());
        
        Vector displacement = new Vector(Math.cos(angle), Math.sin(angle));
        displacement.scale(overlap);
        if (!circleA.isStatic()) {
            int div = circleB.isStatic() ? 1 : 2;
            circleA.getPosition().add(displacement.clone().divide(div));
            circleA.getVelocity().scale(0);
        }
        
        if (!circleB.isStatic()) {
            int div = circleA.isStatic() ? 1 : 2;
            circleB.getPosition().subtract(displacement.divide(div));
            circleB.getVelocity().scale(0);
        }
    }
    
    public static void circleToPolygon(
        CircleCollider circle,
        PolygonCollider polygon
    ) {
        int vertexCount = polygon.getVertices().size();
        
        // Minimum vertex to make a polygon is 3, so we return if it's less than 3
        if (vertexCount < 3) return;
        
        // Perform separating axis theorem (polygon axes)
        Vector mtv = null;
        double minOverlap = Double.MAX_VALUE;
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
            
            // Get projection of circle to normal axis
            Vector normalLineDiff = normalPointB.clone().subtract(normalPointA);
            double normalLineLength = normalPointA.getDistanceFrom(normalPointB);
            double circleDot = circle.getPosition().clone().subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
            double circleMin = circleDot - circle.getRadius();
            double circleMax = circleDot + circle.getRadius();
            
            // Get projection of each vertex in a polygon to normal axis
            double polygonMin = Double.MAX_VALUE;
            double polygonMax = -Double.MAX_VALUE;
            for (Vector vertex : polygon.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
                double vertexDot = vertexTransformed.subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
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
            double overlap = polygonMax - circleMin;
            if (overlap < minOverlap) {
                minOverlap = overlap;
                mtv = normalLineDiff.normalize().scale(overlap);
            }
        }
        
        /**
         * Now let's test the axis of circle:
         * Since it's a circle we only need to test 1 axis and it's the
         * point from circle's center to the closest point in the polygon.
         */
        
        // Get closest the point of polygon to circle
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
                closestVectorToCircle = transformedVertex;
                minDistance = distance;
            }
        }
        
        assert closestVectorToCircle != null;
        
        // Get axis points
        Vector pointA = circle.getPosition().clone();
        Vector pointB = closestVectorToCircle.clone();
        
        // Get projection of circle to axis
        Vector lineDiff = pointB.clone().subtract(pointA);
        double lineLength = pointA.getDistanceFrom(pointB);
        double circleDot = circle.getPosition().clone().subtract(pointA).dot(lineDiff) / lineLength;
        double circleMin = circleDot - circle.getRadius();
        double circleMax = circleDot + circle.getRadius();
        
        // Get projection of each vertex in a polygon to axis
        double polygonMin = Double.MAX_VALUE;
        double polygonMax = -Double.MAX_VALUE;
        for (Vector vertex : polygon.getVertices()) {
            Vector vertexTransformed = vertex.clone().add(polygon.getPosition());
            double vertexDot = vertexTransformed.subtract(pointA).dot(lineDiff) / lineLength;
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
        double overlap = polygonMax - circleMin;
        if (overlap < minOverlap) {
            minOverlap = overlap;
            mtv = lineDiff.normalize().scale(overlap);
        }
        
        // Resolve
        if (mtv != null) {
            if (!circle.isStatic()) {
                int div = polygon.isStatic() ? 1 : 2;
                circle.getPosition().add(mtv.clone().divide(div));
                circle.getVelocity().scale(0);
            }
            
            if (!polygon.isStatic()) {
                int div = circle.isStatic() ? 1 : 2;
                polygon.getPosition().subtract(mtv.clone().divide(div));
                polygon.getVelocity().scale(0);
            }
        }
    }
    
    public static void polygonToPolygon(
        PolygonCollider polygonA,
        PolygonCollider polygonB
    ) {
        double minOverlap = Double.MAX_VALUE;
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
            Vector lineDiff = pointB.clone().subtract(pointA);
            Vector normalPointA = new Vector(
                -lineDiff.getY(),
                lineDiff.getX()
            );
            Vector normalPointB = new Vector(
                lineDiff.getY(),
                -lineDiff.getX()
            );
            
            // Get projection of polygonA to normal
            Vector normalLineDiff = normalPointB.clone().subtract(normalPointA);
            double normalLineLength = normalPointA.getDistanceFrom(normalPointB);
            double polygonAMin = Double.MAX_VALUE;
            double polygonAMax = -Double.MAX_VALUE;
            for (Vector vertex : polygonA.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygonA.getPosition());
                double vertexDot = vertexTransformed.subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
                polygonAMin = Math.min(vertexDot, polygonAMin);
                polygonAMax = Math.max(vertexDot, polygonAMax);
            }
            
            // Get projection of polygonB to normal
            double polygonBMin = Double.MAX_VALUE;
            double polygonBMax = -Double.MAX_VALUE;
            for (Vector vertex : polygonB.getVertices()) {
                Vector vertexTransformed = vertex.clone().add(polygonB.getPosition());
                double vertexDot = vertexTransformed.subtract(normalPointA).dot(normalLineDiff) / normalLineLength;
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
            double overlap = Math.min(polygonAMax, polygonBMax) - Math.max(polygonAMin, polygonBMin);
            if (overlap < minOverlap) {
                minOverlap = overlap;
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
        
        Vector displacement = polygonB.getPosition()
            .clone().subtract(polygonA.getPosition());
        double displacementLength = displacement.getMagnitude();
        displacement.scale(minOverlap).divide(displacementLength);
        if (!polygonA.isStatic()) {
            int div = polygonB.isStatic() ? 1 : 2;
            polygonA.getPosition().subtract(displacement.divide(div));
            polygonA.getVelocity().scale(0);
        }
        
        if (!polygonB.isStatic()) {
            int div = polygonA.isStatic() ? 1 : 2;
            polygonB.getPosition().add(displacement.divide(div));
            polygonB.getVelocity().scale(0);
        }
    }
}
