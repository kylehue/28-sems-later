package game.entity;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import javafx.scene.canvas.GraphicsContext;
import game.sprites.ZombieSprite;
import utils.Async;
import game.utils.Bounds;
import game.utils.Common;
import game.map.PathFinder;
import game.utils.Vector;

import java.util.ArrayList;

public class Zombie extends Entity {
    // stats
    private float speed = Common.random(300, 750);
    private int damage = 1;
    
    // misc
    private final ZombieSprite sprite = new ZombieSprite();
    private CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    private ArrayList<Vector> pathToPlayer = new ArrayList<>();
    
    public Zombie(World world) {
        super(world);
        this.initCollider();
        this.initIntervals();
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.ZOMBIE);
    }
    
    private void initIntervals() {
        registerIntervalFor("zombie", 5000);
        registerIntervalFor(
            "pathToPlayerUpdate",
            (int) Common.random(70, 200)
        );
    }
    
    private void initCollider() {
        this.collider.setGroup(Game.CollisionGroup.ZOMBIES);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.ZOMBIES);
        this.collider.addToGroup(Game.CollisionGroup.BULLETS);
        this.collider.excludeResolutionToGroup(Game.CollisionGroup.BULLETS);
        
        this.collider.setRadius(5);
        this.collider.setMass(1);
        world.getColliderWorld().addCollider(
            this.collider
        );
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        this.sprite.render(ctx);
        
        // // draw path to player
        // for (Vector vector : pathToPlayer) {
        //     ctx.beginPath();
        //     ctx.setFill(Paint.valueOf("rgba(250, 120, 250, 0.85)"));
        //     ctx.fillOval(vector.getX(), vector.getY(), 8, 8);
        //     ctx.closePath();
        // }
    }
    
    public void fixedUpdate(float deltaTime) {
        this.checkPlayerCollision();
        this.maybeUpdatePathToPlayer();
        // put in quadtree
        world.getQuadtree().insert(
            collider,
            new Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2,
                collider.getPosition().getY() - collider.getHeight() / 2,
                collider.getWidth(),
                collider.getHeight()
            )
        );
    }
    
    public void update(float deltaTime) {
        this.handleMovements();
        this.handleSprite();
        this.updateAngleToPlayer();
    }
    
    private void checkPlayerCollision() {
        Player player = world.getPlayer();
        boolean isCollidingWithPlayer = collider.isCollidingWith(
            player.getCollider()
        );
        if (isIntervalOverFor("zombie") && isCollidingWithPlayer) {
            player.setCurrentHealth(player.getCurrentHealth() - damage);
            resetIntervalFor("zombie");
        }
    }
    
    private void updateAngleToPlayer() {
        Player player = world.getPlayer();
        this.angleToPlayer = this.getPosition().getAngle(player.getPosition());
        this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
    }
    
    private void handleSprite() {
        this.sprite.setPosition(
            getPosition().getX(),
            getPosition().getY()
        );
        this.sprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
        this.sprite.nextFrame();
    }
    
    private void maybeUpdatePathToPlayer() {
        Player player = world.getPlayer();
        float distanceToPlayer = player.getPosition().getDistanceFrom(getPosition());
        changeIntervalFor("pathToPlayerUpdate", (int) distanceToPlayer);
        if (isIntervalOverFor("pathToPlayerUpdate")) {
            Async.queue1.submit(() -> {
                PathFinder pathFinder = world.getPathFinder();
                pathToPlayer = pathFinder.requestPath(
                    collider.getPosition(),
                    player.getCollider().getPosition()
                );
            });
            resetIntervalFor("pathToPlayerUpdate");
        }
    }
    
    private void handleMovements() {
        getPosition().set(collider.getPosition().clone().addY(-collider.getRadius()));
        
        // Move to player
        if (pathToPlayer.size() > 1) {
            Vector stepNext = pathToPlayer.get(Math.max(0, pathToPlayer.size() - 2));
            float angle = collider.getPosition().getAngle(stepNext);
            this.collider.applyForce(
                (float) (Math.cos(angle) * speed * collider.getMass()),
                (float) (Math.sin(angle) * speed * collider.getMass())
            );
            this.isFacingOnLeftSide = Math.abs(angle) > (Math.PI / 2);
        } else {
            this.collider.applyForce(
                (float) (Math.cos(angleToPlayer) * speed * collider.getMass()),
                (float) (Math.sin(angleToPlayer) * speed * collider.getMass())
            );
            this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
        }
    }
    
    public CircleCollider getCollider() {
        return collider;
    }
    
    @Override
    public Vector getRenderPosition() {
        return collider.getPosition();
    }
}
