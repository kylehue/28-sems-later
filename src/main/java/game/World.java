package game;

import game.colliders.Collider;
import game.colliders.ColliderWorld;
import game.entity.Bullet;
import game.entity.Player;
import game.entity.Drawable;
import game.entity.Zombie;
import game.utils.Bounds;
import game.utils.Camera;
import game.utils.Common;
import game.utils.Quadtree;
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
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final Camera camera;
    private final Map map = new CityMap();
    private final Quadtree<Collider> quadtree;
    private final ColliderWorld colliderWorld = new ColliderWorld();
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
        this.colliderWorld.setBounds(mapBounds);
        this.colliderWorld.setQuadtree(this.quadtree);
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
        
        // render things
        ArrayList<Drawable> things = new ArrayList<>();
        things.add(player);
        things.addAll(zombies);
        ArrayList<Layer> layers = map.getLayers();
        for (Layer layer : layers) {
            things.addAll(layer.getMaterials());
        }
        
        // exclude things that are not in viewport
        for (int i = things.size() - 1; i >= 0; i--) {
            Drawable thing = things.get(i);
            boolean isInViewport = this.camera.isInViewport(
                thing.getRenderPosition(),
                renderDistanceOffset
            );
            if (!isInViewport) {
                things.remove(i);
            }
        }
        
        // TODO: project requirements application: apply insertion sort
        things.sort((a, b) -> {
            float ay = a.getRenderPosition().getY();
            float by = b.getRenderPosition().getY();
            if (a.getZIndex() < b.getZIndex()) return -1;
            else if (a.getZIndex() > b.getZIndex()) return 1;
            if (ay < by) return -1;
            else if (ay > by) return 1;
            return 0;
        });
        
        for (Drawable thing : things) {
            if (thing.isSeeThrough()) {
                ctx.save();
                float distance = camera.getPosition().getDistanceFrom(thing.getPosition());
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
            
            thing.render(ctx, alpha);
            if (thing.isSeeThrough()) ctx.restore();
        }
        
        for (Bullet bullet : bullets) {
            boolean isInViewport = this.camera.isInViewport(
                bullet.getPosition(),
                renderDistanceOffset
            );
            if (isInViewport) {
                bullet.render(ctx, alpha);
            }
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
        this.handleBulletDisposal();
        map.putCollidersInQuadtree(this.quadtree);
        map.fixedUpdate(deltaTime);
        
        player.fixedUpdate(deltaTime);
        
        for (Zombie zombie : zombies) {
            zombie.fixedUpdate(deltaTime);
        }
        
        for (Bullet bullet : bullets) {
            bullet.fixedUpdate(deltaTime);
        }
        
        colliderWorld.fixedUpdate(deltaTime);
    }
    
    public void update(float deltaTime) {
        player.update(deltaTime);
        
        for (Zombie zombie : zombies) {
            zombie.update(deltaTime);
        }
        
        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
        }
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(400);
    }
    
    public Bullet spawnBullet(float x, float y, float angle) {
        Bullet bullet = new Bullet(this, x, y, angle);
        this.bullets.add(bullet);
        return bullet;
    }
    
    private void handleBulletDisposal() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            // dispose bullets when reached max distance
            float distance = bullet.getPosition().getDistanceFrom(bullet.getInitialPosition());
            if (distance > bullet.getMaxDistance()) {
                bullets.remove(i);
                colliderWorld.removeCollider(bullet.getCollider());
            }
        }
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
    
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    
    public ArrayList<Zombie> getZombies() {
        return zombies;
    }
    
    public Camera getCamera() {
        return camera;
    }
}
