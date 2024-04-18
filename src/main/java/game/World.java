package game;

import game.colliders.Collider;
import game.colliders.ColliderWorld;
import game.colliders.GroupedCollider;
import game.entity.Entity;
import game.entity.Player;
import game.entity.Zombie;
import game.projectiles.Bullet;
import game.projectiles.Grenade;
import game.projectiles.InstantBullet;
import game.projectiles.Projectile;
import game.utils.*;
import javafx.scene.canvas.GraphicsContext;
import game.map.Layer;
import game.map.Material;
import game.map.PathFinder;
import game.maps.CityMap;
import game.map.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
    private final Game game;
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Camera camera;
    private final Map map = new CityMap();
    private final Quadtree<Collider> quadtree;
    private final ColliderWorld colliderWorld;
    private Player player;
    private final PathFinder pathFinder;
    
    /* For debugging */
    public static final HashMap<String, DebugRenderCallback> debugRender = new HashMap<>();
    
    public interface DebugRenderCallback {
        void call(GraphicsContext ctx);
    }
    
    public World(Game game) {
        this.game = game;
        Bounds mapBounds = new Bounds(
            (float) 0,
            (float) 0,
            map.getTotalWidth(),
            map.getTotalHeight()
        );
        this.quadtree = new Quadtree<>(
            mapBounds,
            15,
            30
        );
        pathFinder = new PathFinder(
            map.getTileSize() / 2,
            map.getTotalWidth(),
            map.getTotalHeight()
        );
        this.colliderWorld = new ColliderWorld(quadtree);
        this.colliderWorld.setBounds(mapBounds);
        this.camera = new Camera(game.getGraphicsContext());
        map.addCollidersToWorld(colliderWorld);
    }
    
    public void setup() {
        this.player = new Player(this);
        float halfMapWidth = (float) map.getTotalWidth() / 2;
        float halfMapHeight = (float) map.getTotalHeight() / 2;
        player.getCollider().getPosition().set(
            halfMapWidth,
            halfMapHeight
        );
        for (int i = 0; i < 0; i++) {
            Zombie zombie = new Zombie(this);
            zombie.getCollider().getPosition().set(
                Common.random(-halfMapWidth, halfMapWidth),
                Common.random(-halfMapHeight, halfMapHeight)
            );
            zombies.add(zombie);
        }
        map.getLayers().forEach(layer -> {
            for (Material material : layer.getMaterials()) {
                Collider collider = material.getCollider();
                if (collider == null) continue;
                pathFinder.getObstacles().add(collider);
            }
        });
    }
    
    public void renderMeta(GraphicsContext ctx) {
        this.quadtree.render(ctx);
        for (Collider collider : colliderWorld.getColliders()) {
            collider.render(ctx);
        }
    }
    
    public void render(GraphicsContext ctx, float alpha) {
        float renderDistanceOffset = 100;
        
        this.camera.begin();
        
        // render drawables
        ArrayList<Drawable> drawables = new ArrayList<>();
        drawables.add(player);
        drawables.addAll(zombies);
        drawables.addAll(projectiles);
        for (Layer layer : map.getLayers()) {
            drawables.addAll(layer.getMaterials());
        }
        
        // exclude drawables that are not in viewport
        for (int i = drawables.size() - 1; i >= 0; i--) {
            Drawable drawable = drawables.get(i);
            boolean isInViewport = this.camera.isInViewport(
                drawable.getRenderPosition(),
                renderDistanceOffset
            );
            if (!isInViewport) {
                drawables.remove(i);
            }
        }
        
        drawables.sort((a, b) -> {
            float ay = a.getRenderPosition().getY();
            float by = b.getRenderPosition().getY();
            if (a.getZIndex() < b.getZIndex()) return -1;
            else if (a.getZIndex() > b.getZIndex()) return 1;
            if (ay < by) return -1;
            else if (ay > by) return 1;
            return 0;
        });
        
        for (Drawable drawable : drawables) {
            boolean isSeeThrough = drawable.isSeeThrough();
            if (isSeeThrough) {
                ctx.save();
                float distance = camera.getPosition().getDistanceFrom(drawable.getPosition());
                float seeThroughDistance = 100;
                if (distance <= seeThroughDistance) {
                    ctx.setGlobalAlpha(
                        Common.map(
                            distance,
                            0,
                            seeThroughDistance,
                            0.1f,
                            1
                        )
                    );
                }
            }
            
            drawable.render(ctx, alpha);
            if (isSeeThrough) ctx.restore();
        }
        
        // this.renderMeta(ctx);
        
        if (!debugRender.isEmpty()) {
            debugRender.forEach((key, run) -> {
                run.call(ctx);
            });
            debugRender.clear();
        }
        
        this.camera.end();
    }
    
    public void fixedUpdate(float deltaTime) {
        this.quadtree.clear();
        map.fixedUpdate(deltaTime);
        
        player.fixedUpdate(deltaTime);
        
        for (Zombie zombie : zombies) {
            zombie.fixedUpdate(deltaTime);
        }
        
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            if (projectile.isDisposed()) {
                projectiles.remove(i);
            } else {
                projectile.fixedUpdate(deltaTime);
                for (Entity entity : zombies) {
                    projectile.handleEntityCollision(entity);
                }
                
                for (Layer layer : map.getLayers()) {
                    for (Material material : layer.getMaterials()) {
                        Collider collider = material.getCollider();
                        if (collider == null) continue;
                        if (collider instanceof GroupedCollider groupedCollider) {
                            for (Collider _collider : groupedCollider.getColliders()) {
                                projectile.handleObstacleCollision(_collider);
                            }
                        } else {
                            projectile.handleObstacleCollision(collider);
                        }
                    }
                }
            }
        }
        
        colliderWorld.fixedUpdate(deltaTime);
    }
    
    public void update(float deltaTime) {
        player.update(deltaTime);
        
        for (Zombie zombie : zombies) {
            zombie.update(deltaTime);
        }
        
        for (Projectile projectile : projectiles) {
            projectile.update(deltaTime);
        }
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(400);
    }
    
    public Bullet spawnBullet(Vector initialPosition, float angle) {
        Bullet bullet = new Bullet(initialPosition, angle);
        colliderWorld.addCollider(bullet.getCollider());
        projectiles.add(bullet);
        return bullet;
    }
    
    public Grenade spawnGrenade(Vector initialPosition, float angle) {
        Grenade grenade = new Grenade(initialPosition, angle);
        colliderWorld.addCollider(grenade.getCollider());
        projectiles.add(grenade);
        return grenade;
    }
    
    public InstantBullet spawnInstantBullet(Vector initialPosition, float angle) {
        InstantBullet instantBullet = new InstantBullet(initialPosition, angle);
        projectiles.add(instantBullet);
        return instantBullet;
    }
    
    public Game getGame() {
        return game;
    }
    
    public PathFinder getPathFinder() {
        return pathFinder;
    }
    
    public Quadtree<Collider> getQuadtree() {
        return quadtree;
    }
    
    public ColliderWorld getColliderWorld() {
        return colliderWorld;
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    public ArrayList<Zombie> getZombies() {
        return zombies;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public Vector getMousePosition() {
        return camera.screenToWorld(game.getMouseHandler().getPosition());
    }
}
