package game.colliders;

import game.utils.Bounds;
import game.utils.Vector;

public abstract class CollisionResolvers {
    public static boolean testAABB(
        float x1,
        float y1,
        float width1,
        float height1,
        float x2,
        float y2,
        float width2,
        float height2
    ) {
        if (x1 + width1 < x2 || x2 + width2 < x1) return false;
        if (y1 + height1 < y2 || y2 + height2 < y1) return false;
        return true;
    }
    
    public static Vector getLineToCircleIntersectionPoint(
        Vector linePointA,
        Vector linePointB,
        Vector circlePosition,
        float circleRadius
    ) {
        // Check if either points are inside the circle
        if (
            linePointA.getDistanceFrom(circlePosition) <= circleRadius ||
                linePointB.getDistanceFrom(circlePosition) <= circleRadius
        ) {
            return null;
        }
        
        float lineLength = linePointA.getDistanceFrom(linePointB);
        
        Vector lineDist = linePointB.clone().subtract(linePointA);
        float dot = (float) (
            circlePosition
                .clone()
                .subtract(linePointA)
                .dot(lineDist) / Math.pow(lineLength, 2)
        );
        
        Vector closestPoint = linePointA.clone().add(lineDist.clone().scale(dot));
        
        // Check if the closest point is in line
        float lineBuffer = 0.1f;
        float closestPointLineDist = closestPoint.getDistanceFrom(linePointA) + closestPoint.getDistanceFrom(linePointB);
        boolean isInLine = closestPointLineDist >= lineLength - lineBuffer && closestPointLineDist <= lineLength + lineBuffer;
        if (!isInLine) {
            return null;
        }
        
        if (closestPoint.getDistanceFrom(circlePosition) > circleRadius) {
            return null;
        }
        
        float closestPointDistance = closestPoint
            .clone()
            .subtract(circlePosition)
            .getMagnitude();
        
        float intersectionDistance = (float) Math.sqrt(
            circleRadius * circleRadius - closestPointDistance * closestPointDistance
        );
        
        return closestPoint.subtract(
            lineDist.normalize().scale(intersectionDistance)
        );
    }
    
    public static Vector getLineToPolygonIntersectionPoint(
        Vector linePointA,
        Vector linePointB,
        Vector polygonPosition,
        Vector[] polygonVertices
    ) {
        int vertexCount = polygonVertices.length;
        float min = Float.MAX_VALUE;
        Vector intersectionPoint = null;
        for (int i = 0; i < vertexCount; i++) {
            // Pair 2 points that will act as the line
            Vector linePointC = polygonVertices[i]
                .clone()
                .add(polygonPosition);
            Vector linePointD = polygonVertices[(i + 1) % vertexCount]
                .clone()
                .add(polygonPosition);
            
            // calculate the distance to intersection point
            // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
            Vector distDC = linePointD.clone().subtract(linePointC);
            Vector distAC = linePointA.clone().subtract(linePointC);
            Vector distBA = linePointB.clone().subtract(linePointA);
            var uA = (distDC.getX() * distAC.getY() - distDC.getY() * distAC.getX()) / (distDC.getY() * distBA.getX() - distDC.getX() * distBA.getY());
            var uB = (distBA.getX() * distAC.getY() - distBA.getY() * distAC.getX()) / (distDC.getY() * distBA.getX() - distDC.getX() * distBA.getY());
            
            // if uA and uB are between 0-1, lines are colliding
            if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
                Vector intersection = new Vector(
                    linePointA.getX() + (uA * distBA.getX()),
                    linePointA.getY() + (uA * distBA.getY())
                );
                float dist = linePointA.getDistanceFrom(intersection);
                if (dist < min) {
                    intersectionPoint = intersection;
                    min = dist;
                }
            }
        }
        
        return intersectionPoint;
    }
    
    public static Vector getLineToBoundsIntersectionPoint(
        Vector linePointA,
        Vector linePointB,
        Bounds bounds
    ) {
        float x = bounds.getX();
        float y = bounds.getY();
        float width = bounds.getWidth();
        float height = bounds.getHeight();
        
        Vector[] boundsVertices = {
            new Vector(),
            new Vector(width, 0),
            new Vector(width, height),
            new Vector(0, height)
        };
        
        return getLineToPolygonIntersectionPoint(
            linePointA,
            linePointB,
            new Vector(x, y),
            boundsVertices
        );
    }
    
    public static Vector getLineToColliderIntersectionPoint(
        Vector linePointA,
        Vector linePointB,
        Collider collider
    ) {
        if (collider instanceof CircleCollider circleCollider) {
            return getLineToCircleIntersectionPoint(
                linePointA,
                linePointB,
                circleCollider.getPosition(),
                circleCollider.getRadius()
            );
        } else if (collider instanceof PolygonCollider polygonCollider) {
            return getLineToPolygonIntersectionPoint(
                linePointA,
                linePointB,
                polygonCollider.getPosition(),
                polygonCollider.getVertices()
            );
        } else if (collider instanceof GroupedCollider groupedCollider) {
            for (Collider collider1 : groupedCollider.getColliders()) {
                Vector intersectionPoint = getLineToColliderIntersectionPoint(linePointA, linePointB, collider1);
                if (intersectionPoint != null) {
                    return intersectionPoint;
                }
            }
        }
        
        return null;
    }
    
    public static void circleToCircle(
        CircleCollider circleA,
        CircleCollider circleB
    ) {
        if (!circleA.isGroupedWith(circleB) && !circleB.isGroupedWith(circleA)) return;
        
        // Detect (AABB)
        boolean isCollidingAABB = testAABB(
            circleA.getPosition().getX() - circleA.getWidth() / 2,
            circleA.getPosition().getY() - circleA.getHeight() / 2,
            circleA.getWidth(),
            circleA.getHeight(),
            circleB.getPosition().getX() - circleB.getWidth() / 2,
            circleB.getPosition().getY() - circleB.getHeight() / 2,
            circleB.getWidth(),
            circleB.getHeight()
        );
        if (!isCollidingAABB) {
            return;
        }
        
        // Detect (Pythagorean Theorem)
        float distance = circleA
            .getPosition()
            .getDistanceFrom(circleB.getPosition());
        float radiusSum = circleA.getRadius() + circleB.getRadius();
        boolean isColliding = distance < radiusSum;
        
        if (!isColliding) {
            return;
        }
        
        circleA.getContacts().add(circleB.getId());
        circleB.getContacts().add(circleA.getId());
        
        if (circleB.isResolutionExcludedFromGroup(circleA.getGroup()) || circleA.isResolutionExcludedFromGroup(circleB.getGroup())) {
            return;
        }
        
        // Resolve collision
        float overlap = radiusSum - distance;
        float angle = circleA.getPosition().getAngle(circleB.getPosition());
        
        Vector displacement = new Vector(
            (float) Math.cos(angle),
            (float) Math.sin(angle)
        ).scale(overlap);
        
        if (!circleA.isStatic() && !circleB.isStatic()) {
            float totalMass = (circleA.getMass() + circleB.getMass()) * (circleA.getContacts().size() + circleB.getContacts().size());
            float ratioA = (circleB.getMass() * circleB.getContacts().size()) / totalMass;
            float ratioB = (circleA.getMass() * circleA.getContacts().size()) / totalMass;
            
            circleA.getPosition().subtract(displacement.clone().scale(ratioA));
            circleB.getPosition().add(displacement.clone().scale(ratioB));
        } else if (!circleA.isStatic()) { // Only circleA moves
            circleA.getPosition().subtract(displacement);
        } else if (!circleB.isStatic()) { // Only circleB moves
            circleB.getPosition().add(displacement);
        }
    }
    
    public static void circleToPolygon(
        CircleCollider circle,
        PolygonCollider polygon
    ) {
        if (!circle.isGroupedWith(polygon) && !polygon.isGroupedWith(circle)) return;
        
        // Detect (AABB)
        boolean isCollidingAABB = testAABB(
            circle.getPosition().getX() - circle.getWidth() / 2,
            circle.getPosition().getY() - circle.getHeight() / 2,
            circle.getWidth(),
            circle.getHeight(),
            polygon.getPosition().getX() - polygon.getWidth() / 2,
            polygon.getPosition().getY() - polygon.getHeight() / 2,
            polygon.getWidth(),
            polygon.getHeight()
        );
        if (!isCollidingAABB) {
            return;
        }
        
        // Detect (Separating Axes Theorem)
        Vector mtv = null;
        float minOverlap = Float.MAX_VALUE;
        int vertexCount = polygon.getVertices().length;
        for (int i = 0; i < vertexCount; i++) {
            // Pair 2 points that will act as the line
            Vector pointA = polygon
                .getVertices()[i]
                .clone()
                .add(polygon.getPosition());
            Vector pointB = polygon
                .getVertices()[(i + 1) % vertexCount]
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
                return;
            }
            
            // Calculate overlap
            float overlap = Math.abs(Math.min(circleMax, polygonMax) - Math.max(circleMin, polygonMin));
            if (overlap < minOverlap) {
                minOverlap = overlap;
                mtv = normal.clone().normalize().scale(minOverlap);
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
        Vector normal = pointB.clone().subtract(pointA);
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
            return;
        }
        
        /**
         * If we got here, it means that the 2 shapes are colliding.
         * So we resolve the collision:
         */
        
        circle.getContacts().add(polygon.getId());
        polygon.getContacts().add(circle.getId());
        
        if (circle.isResolutionExcludedFromGroup(polygon.getGroup()) || polygon.isResolutionExcludedFromGroup(circle.getGroup())) {
            return;
        }
        
        // Calculate overlap
        float overlap = Math.abs(Math.min(circleMax, polygonMax) - Math.max(circleMin, polygonMin));
        if (overlap < minOverlap) {
            minOverlap = overlap;
            mtv = normal.clone().normalize().scale(minOverlap);
        }
        
        // Resolve
        if (mtv != null) {
            Vector centerToCenter = polygon.getPosition().clone().subtract(
                circle.getPosition()
            );
            float direction = mtv.dot(centerToCenter) < 0 ? 1 : -1;
            
            mtv.scale(direction);
            if (!circle.isStatic() && !polygon.isStatic()) {
                float totalMass = (circle.getMass() + polygon.getMass()) * (circle.getContacts().size() + polygon.getContacts().size());
                float ratioA = (polygon.getMass() * polygon.getContacts().size()) / totalMass;
                float ratioB = (circle.getMass() * circle.getContacts().size()) / totalMass;
                
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
        if (!polygonA.isGroupedWith(polygonB) && !polygonB.isGroupedWith(polygonA)) return;
        
        // Detect (AABB)
        boolean isCollidingAABB = testAABB(
            polygonA.getPosition().getX() - polygonA.getWidth() / 2,
            polygonA.getPosition().getY() - polygonA.getHeight() / 2,
            polygonA.getWidth(),
            polygonA.getHeight(),
            polygonB.getPosition().getX() - polygonB.getWidth() / 2,
            polygonB.getPosition().getY() - polygonB.getHeight() / 2,
            polygonB.getWidth(),
            polygonB.getHeight()
        );
        if (!isCollidingAABB) {
            return;
        }
        
        // Detect (Separating Axes Theorem)
        Vector mtv = null;
        float minOverlap = Float.MAX_VALUE;
        boolean swapped = false;
        for (int i = 0; i < polygonA.getVertices().length; i++) {
            // Pair 2 points that will act as the line
            Vector pointA = polygonA
                .getVertices()[i]
                .clone()
                .add(polygonA.getPosition());
            Vector pointB = polygonA
                .getVertices()[(i + 1) % polygonA.getVertices().length]
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
                return;
            }
            
            // Calculate overlap
            float overlap = Math.abs(Math.min(polygonAMax, polygonBMax) - Math.max(polygonAMin, polygonBMin));
            if (overlap < minOverlap) {
                minOverlap = overlap;
                mtv = normal.clone().normalize().scale(minOverlap);
            }
            
            // Check other way around if the last axis still intersects
            boolean isLast = i == polygonA.getVertices().length - 1;
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
        
        if (polygonA.isResolutionExcludedFromGroup(polygonB.getGroup()) || polygonB.isResolutionExcludedFromGroup(polygonA.getGroup())) {
            return;
        }
        
        // Resolve
        if (mtv != null) {
            Vector centerToCenter = polygonB.getPosition().clone().subtract(
                polygonA.getPosition()
            );
            float direction = mtv.dot(centerToCenter) < 0 ? 1 : -1;
            mtv.scale(direction);
            
            if (!polygonA.isStatic() && !polygonB.isStatic()) {
                float totalMass = (polygonA.getMass() + polygonB.getMass()) * (polygonA.getContacts().size() + polygonB.getContacts().size());
                float ratioA = (polygonB.getMass() * polygonB.getContacts().size()) / totalMass;
                float ratioB = (polygonA.getMass() * polygonA.getContacts().size()) / totalMass;
                
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