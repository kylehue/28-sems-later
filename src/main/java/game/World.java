package game;

import game.colliders.Collider;
import game.colliders.ColliderWorld;
import game.entity.Player;
import game.entity.Zombie;
import game.loots.Loot;
import game.loots.XPLoot;
import game.projectiles.Bullet;
import game.projectiles.Grenade;
import game.projectiles.InstantBullet;
import game.projectiles.Projectile;
import game.utils.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.canvas.GraphicsContext;
import game.map.Layer;
import game.map.Material;
import game.map.PathFinder;
import game.maps.CityMap;
import game.map.Map;
import javafx.scene.media.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class World {
    private Player player;
    private final HashSet<DistanceAwareAudio> audios = new HashSet<>();
    private final HashMap<String, ArrayList<DistanceAwareAudio>> reusableAudiosMap = new HashMap<>();
    private final ArrayList<Loot> loots = new ArrayList<>();
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private final ArrayList<Projectile> projectiles = new ArrayList<>();
    private final Camera camera;
    private final Map map = new CityMap();
    private final Quadtree<Collider> quadtree;
    private final ColliderWorld colliderWorld;
    private final PathFinder pathFinder;
    private final ArrayList<SpriteAnimation> oneTimeSpriteAnimations = new ArrayList<>();
    private BooleanProperty isPaused = new SimpleBooleanProperty();
    
    // audios
    private final MediaPlayer ambienceAudio = new MediaPlayer(
        utils.Common.loadMedia("/sounds/ambience.mp3")
    );
    
    /* For debugging */
    public static final HashMap<String, DebugRenderCallback> debugRender = new HashMap<>();
    
    public interface DebugRenderCallback {
        void call(GraphicsContext ctx);
    }
    
    public World() {
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
        this.camera = new Camera(Game.graphicsContext);
        map.addCollidersToWorld(colliderWorld);
        map.getLayers().forEach(layer -> {
            for (Material material : layer.getMaterials()) {
                Collider collider = material.getCollider();
                if (collider == null) continue;
                pathFinder.getObstacles().add(collider);
            }
        });
        
        ambienceAudio.setCycleCount(Integer.MAX_VALUE);
        ambienceAudio.play();
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
        drawables.addAll(loots);
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
        
        for (int i = oneTimeSpriteAnimations.size() - 1; i >= 0; i--) {
            SpriteAnimation anim = oneTimeSpriteAnimations.get(i);
            anim.render(ctx);
        }
        
        this.camera.end();
    }
    
    public void fixedUpdate(float deltaTime) {
        if (isPaused()) return;
        this.quadtree.clear();
        map.fixedUpdate(deltaTime);
        
        player.fixedUpdate(deltaTime);
        
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Zombie zombie = zombies.get(i);
            zombie.fixedUpdate(deltaTime);
        }
        
        for (int i = loots.size() - 1; i >= 0; i--) {
            Loot loot = loots.get(i);
            loot.fixedUpdate(deltaTime);
        }
        
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.fixedUpdate(deltaTime);
        }
        
        for (int i = oneTimeSpriteAnimations.size() - 1; i >= 0; i--) {
            SpriteAnimation anim = oneTimeSpriteAnimations.get(i);
            anim.nextFrame();
            if (anim.getCurrentFrameNumber() >= anim.getFrameLength() - 1) {
                oneTimeSpriteAnimations.remove(i);
            }
        }
        
        colliderWorld.fixedUpdate(deltaTime);
    }
    
    public void update(float deltaTime) {
        if (isPaused()) return;
        player.update(deltaTime);
        
        for (int i = zombies.size() - 1; i >= 0; i--) {
            Zombie zombie = zombies.get(i);
            zombie.update(deltaTime);
        }
        
        for (int i = projectiles.size() - 1; i >= 0; i--) {
            Projectile projectile = projectiles.get(i);
            projectile.update(deltaTime);
        }
        
        for (DistanceAwareAudio audio : audios) {
            audio.update();
        }
        
        Mechanics.update();
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(400);
    }
    
    public Bullet spawnBullet(Vector initialPosition, float angle) {
        Bullet bullet = new Bullet(this, initialPosition, angle);
        colliderWorld.addCollider(bullet.getCollider());
        projectiles.add(bullet);
        return bullet;
    }
    
    public Grenade spawnGrenade(Vector initialPosition, float angle) {
        Grenade grenade = new Grenade(this, initialPosition, angle);
        colliderWorld.addCollider(grenade.getCollider());
        colliderWorld.addCollider(grenade.getAoeCollider());
        projectiles.add(grenade);
        return grenade;
    }
    
    public InstantBullet spawnInstantBullet(Vector initialPosition, float angle) {
        InstantBullet instantBullet = new InstantBullet(this, initialPosition, angle);
        projectiles.add(instantBullet);
        return instantBullet;
    }
    
    public Zombie spawnZombie(Vector initialPosition) {
        Zombie zombie = new Zombie();
        zombie.getCollider().setPosition(initialPosition);
        zombies.add(zombie);
        return zombie;
    }
    
    public Zombie spawnZombie() {
        return spawnZombie(generateRandomPosition());
    }
    
    public XPLoot spawnXPLoot(Vector initialPosition) {
        XPLoot xpLoot = new XPLoot();
        xpLoot.getPosition().set(initialPosition);
        loots.add(xpLoot);
        return xpLoot;
    }
    
    public boolean isPaused() {
        return isPaused.get();
    }
    
    public BooleanProperty isPausedProperty() {
        return isPaused;
    }
    
    public void pause() {
        isPaused.set(true);
        for (DistanceAwareAudio audio : audios) {
            audio.getMediaPlayer().pause();
        }
    }
    
    public void play() {
        isPaused.set(false);
        for (DistanceAwareAudio audio : audios) {
            audio.getMediaPlayer().play();
        }
    }
    
    public void start() {
        this.player = new Player();
        float halfMapWidth = (float) map.getTotalWidth() / 2;
        float halfMapHeight = (float) map.getTotalHeight() / 2;
        player.getCollider().setPosition(
            halfMapWidth,
            halfMapHeight
        );
    }
    
    public void dispose() {
        for (DistanceAwareAudio audio : audios) {
            audio.getMediaPlayer().dispose();
        }
        ambienceAudio.dispose();
    }
    
    public void addOneTimeSpriteAnimation(SpriteAnimation spriteAnimation) {
        oneTimeSpriteAnimations.add(spriteAnimation);
    }
    
    public DistanceAwareAudio addPlayerDistanceAwareAudio(
        String url,
        Vector position,
        float awarenessDistance
    ) {
        ArrayList<DistanceAwareAudio> reusableAudios = reusableAudiosMap.computeIfAbsent(url, (s) -> new ArrayList<>());
        
        DistanceAwareAudio distanceAwareAudio;
        if (!reusableAudios.isEmpty()) {
            distanceAwareAudio = reusableAudios.removeFirst();
            distanceAwareAudio.getMediaPlayer().seek(
                distanceAwareAudio.getMediaPlayer().getStartTime()
            );
            distanceAwareAudio.getPosition().set(position);
        } else {
            distanceAwareAudio = new DistanceAwareAudio(
                url,
                position
            );
            distanceAwareAudio.getMediaPlayer().setOnEndOfMedia(() -> {
                audios.remove(distanceAwareAudio);
                reusableAudios.add(distanceAwareAudio);
            });
        }
        
        distanceAwareAudio.getMediaPlayer().play();
        distanceAwareAudio.setAwarenessPosition(player.getPosition());
        distanceAwareAudio.setAwarenessDistance(awarenessDistance);
        audios.add(distanceAwareAudio);
        
        return distanceAwareAudio;
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
    
    public ArrayList<Loot> getLoots() {
        return loots;
    }
    
    public Camera getCamera() {
        return camera;
    }
    
    public Vector getMousePosition() {
        return camera.screenToWorld(Game.mouseHandler.getPosition());
    }
    
    public Vector generateRandomPosition() {
        return new Vector(
            Common.random(0, map.getTotalWidth()),
            Common.random(0, map.getTotalHeight())
        );
    }
    
    public Map getMap() {
        return map;
    }
}
