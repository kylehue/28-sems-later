package scenes.game;

import colliders.CircleCollider;
import colliders.Collider;
import colliders.ColliderWorld;
import colliders.GroupedCollider;
import entity.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import main.CollisionGroup;
import main.GameApplication;
import map.Layer;
import map.Material;
import map.PathFinder;
import maps.CityMap;
import map.Map;
import utils.*;

import java.util.ArrayList;
import java.util.HashMap;

public class World {
    private final GameApplication gameApplication;
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final Camera camera;
    private final Map map = new CityMap();
    private final Quadtree<Collider> quadtree;
    private final ColliderWorld colliderWorld = new ColliderWorld();
    private Player player;
    private final UI ui;
    private final PathFinder pathFinder;
    
    /* For debugging */
    public static final HashMap<String, DebugRenderCallback> debugRender = new HashMap<>();
    
    public interface DebugRenderCallback {
        void call(GraphicsContext ctx);
    }
    
    public World(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.ui = new UI(gameApplication);
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
        this.camera = new Camera(gameApplication.getGameScene().getGraphicsContext());
        map.addCollidersToWorld(colliderWorld);
    }
    
    GroupedCollider test;
    
    CircleCollider a = new CircleCollider();
    CircleCollider b = new CircleCollider();
    public void setup() {
        this.player = new Player(gameApplication);
        float halfMapWidth = (float) map.getTotalWidth() / 2;
        float halfMapHeight = (float) map.getTotalHeight() / 2;
        player.getCollider().getPosition().set(
            halfMapWidth,
            halfMapHeight
        );
        for (int i = 0; i < 0; i++) {
            Zombie zombie = new Zombie(gameApplication);
            zombie.getCollider().getPosition().set(
                GameUtils.random(-halfMapWidth, halfMapWidth),
                GameUtils.random(-halfMapHeight, halfMapHeight)
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
        a.setRadius(12);
        a.getPosition().set(12, 12);
        b.setRadius(12);
        b.getPosition().set(-12, -12);
        test = new GroupedCollider(new Collider[]{
            a,
            b
        });
        test.setGroup(CollisionGroup.MAP);
        test.addToGroup(CollisionGroup.MAP);
        test.setMass(500);
        
        test.getPosition().set(
            halfMapWidth,
            halfMapHeight
        );
        
        colliderWorld.addCollider(test);
        colliderWorld.addCollider(a);
        colliderWorld.addCollider(b);
    }
    
    public void render(GraphicsContext ctx) {
        float renderDistanceOffset = 100;
        
        this.camera.begin();
        
        // render things
        ArrayList<Thing> things = new ArrayList<>();
        things.add(player);
        things.addAll(zombies);
        ArrayList<Layer> layers = map.getLayers();
        for (Layer layer : layers) {
            things.addAll(layer.getMaterials());
        }
        
        // exclude things that are not in viewport
        for (int i = things.size() - 1; i >= 0; i--) {
            Thing thing = things.get(i);
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
        
        for (Thing thing : things) {
            thing.render(ctx);
        }
        
        for (Bullet bullet : bullets) {
            boolean isInViewport = this.camera.isInViewport(
                bullet.getPosition(),
                renderDistanceOffset
            );
            if (isInViewport) {
                bullet.render(ctx);
            }
        }
        
        this.renderMeta(ctx);
        
        if (!debugRender.isEmpty()) {
            debugRender.forEach((key, run) -> {
                run.call(ctx);
            });
            debugRender.clear();
        }
        
        this.camera.end();
        ui.render(ctx);
        this.renderFPS(ctx);
    }
    
    private void renderFPS(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Paint.valueOf("#00FF00"));
        ctx.setFont(Font.font(null, FontWeight.BOLD, 24));
        ctx.fillText(
            String.valueOf((int) gameApplication.getGameScene().getGameLoop().getFPS()), 15,
            30,
            100
        );
        ctx.closePath();
    }
    
    public void renderMeta(GraphicsContext ctx) {
        this.quadtree.render(ctx);
        for (Collider collider : colliderWorld.getColliders()) {
            collider.render(ctx);
        }
    }
    
    public void update(float deltaTime) {
        this.quadtree.clear();
        this.handleBulletDisposal();
        map.putCollidersInQuadtree(this.quadtree);
        map.update(deltaTime);
        this.quadtree.insert(test, new Bounds(
            test.getPosition().getX() - test.getWidth() / 2, test.getPosition().getY() - test.getHeight() / 2, test.getWidth(), test.getHeight()
        ));
        this.quadtree.insert(a, new Bounds(
            a.getPosition().getX() - 25, a.getPosition().getY() - 25, 50, 50
        ));
        this.quadtree.insert(b, new Bounds(
            b.getPosition().getX() - 25, b.getPosition().getY() - 25, 50, 50
        ));
        
        player.update(deltaTime);
        
        for (Zombie zombie : zombies) {
            zombie.update(deltaTime);
        }
        
        for (Bullet bullet : bullets) {
            bullet.update(deltaTime);
        }
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(400);
        colliderWorld.update(deltaTime);
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
    
    public Bullet spawnBullet(float x, float y, float angle) {
        Bullet bullet = new Bullet(this.gameApplication, x, y, angle);
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
    
    public Camera getCamera() {
        return camera;
    }
}
