package game.projectiles;

import game.Config;
import game.World;
import game.colliders.Collider;
import game.colliders.CollisionResolvers;
import game.entity.Entity;
import game.map.Layer;
import game.map.Material;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;

public class InstantBullet extends Projectile {
    private float knockBackForce = Config.DEFAULT_INSTANT_BULLET_KNOCK_BACK_FORCE;
    private float penetration = Config.DEFAULT_INSTANT_BULLET_PENETRATION;
    private float opacity = 1;
    private boolean shouldStopTravelling = false;
    private final Vector travelledPosition = new Vector();
    
    public InstantBullet(World world, Vector initialPosition, float angle) {
        super(world, initialPosition, angle);
        travelledPosition.set(initialPosition);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        ctx.save();
        ctx.setGlobalAlpha(opacity);
        ctx.setStroke(Paint.valueOf("white"));
        ctx.setLineWidth(Math.pow(opacity + 1, 2));
        ctx.beginPath();
        ctx.moveTo(initialPosition.getX(), initialPosition.getY());
        ctx.lineTo(
            travelledPosition.getX(),
            travelledPosition.getY()
        );
        ctx.closePath();
        ctx.stroke();
        ctx.restore();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        if (opacity >= 0.001) {
            opacity *= 0.97f;
        } else {
            opacity = 0;
            dispose();
        }
        
        if (!shouldStopTravelling) {
            shouldStopTravelling = penetration <= 0;
            
            travelledPosition.set(
                (float) (position.getX() + Math.cos(angle) * 5000),
                (float) (position.getY() + Math.sin(angle) * 5000)
            );
            
            handleObstacleCollision();
            handleEntityCollision();
        }
    }
    
    private void handleEntityCollision() {
        // Get entities that intersects the trajectory of the bullet
        HashMap<String, Vector> entitiesIntersectionMap = new HashMap<>();
        ArrayList<Entity> entities = new ArrayList<>();
        for (Entity entity : world.getZombies()) {
            Vector intersection = CollisionResolvers.getLineToBoundsIntersectionPoint(
                initialPosition,
                travelledPosition,
                entity.getHitBox()
            );
            boolean isEntityHit = intersection != null;
            if (!isEntityHit) continue;
            entities.add(entity);
            entitiesIntersectionMap.put(entity.getId(), intersection);
        }
        
        // Sort entities by distance (nearest to furthest)
        entities.sort((a, b) -> {
            int distanceA = (int) a
                .getCollider()
                .getPosition()
                .getDistanceFrom(initialPosition);
            int distanceB = (int) b
                .getCollider()
                .getPosition()
                .getDistanceFrom(initialPosition);
            return distanceA - distanceB;
        });
        
        // Hit entities
        Entity lastEntityHit = null;
        for (Entity entity : entities) {
            if (isEntityMarked(entity)) continue;
            if (penetration <= 0) continue;
            float penetrationPercentage = penetration >= 1 ? 1 : penetration;
            float computedDamage = damage * penetrationPercentage;
            
            entity.addHealth(-computedDamage);
            penetration -= penetrationPercentage;
            markEntity(entity);
            lastEntityHit = entity;
            
            // Add knock back
            float angleToBullet = initialPosition.getAngle(entity.getCollider().getPosition());
            entity.getCollider().applyForce(
                (float) (Math.cos(angleToBullet) * knockBackForce * entity.getCollider().getMass()),
                (float) (Math.sin(angleToBullet) * knockBackForce * entity.getCollider().getMass())
            );
        }
        
        if (penetration <= 0 && lastEntityHit != null) {
            travelledPosition.set(
                entitiesIntersectionMap.get(lastEntityHit.getId())
            );
        }
    }
    
    private void handleObstacleCollision() {
        for (Layer layer : world.getMap().getLayers()) {
            for (Material material : layer.getMaterials()) {
                Collider obstacle = material.getCollider();
                if (obstacle == null) continue;
                
                Vector intersectionPoint = CollisionResolvers.getLineToColliderIntersectionPoint(
                    position,
                    travelledPosition,
                    obstacle
                );
                
                if (intersectionPoint != null) {
                    travelledPosition.set(intersectionPoint);
                    shouldStopTravelling = true;
                }
                
                boolean isInsideMapBounds = travelledPosition.getX() >= 0 &&
                    travelledPosition.getX() <= world.getMap().getTotalWidth() &&
                    travelledPosition.getY() >= 0 &&
                    travelledPosition.getY() <= world.getMap().getTotalHeight();
                if (!isInsideMapBounds) {
                    shouldStopTravelling = true;
                }
            }
        }
    }
    
    @Override
    public void dispose() {
        world.getProjectiles().remove(this);
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }
    
    public float getPenetration() {
        return penetration;
    }
}
