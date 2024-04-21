package game.entity;

import game.Game;
import game.World;
import game.colliders.CircleCollider;
import game.sprites.AcidSprite;
import game.utils.HitEffect;
import game.utils.IntervalMap;
import javafx.scene.canvas.GraphicsContext;
import game.sprites.ZombieSprite;
import utils.Async;
import game.utils.Common;
import game.map.PathFinder;
import game.utils.Vector;

import java.util.ArrayList;

public class Zombie extends Entity {
    // stats
    private float speed = Common.random(300, 750);
    private float damage = 1;
    
    // misc
    private final ZombieSprite sprite = new ZombieSprite();
    private CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    private ArrayList<Vector> pathToPlayer = new ArrayList<>();
    private HitEffect hitEffect = new HitEffect();
    private final IntervalMap intervals = new IntervalMap();
    
    public Zombie() {
        this.initCollider();
        this.initIntervals();
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.ZOMBIE);
    }
    
    private void initIntervals() {
        intervals.registerIntervalFor("zombie", 5000);
        intervals.registerIntervalFor(
            "pathToPlayerUpdate",
            (int) Common.random(70, 200)
        );
    }
    
    private void initCollider() {
        this.collider.setGroup(Game.CollisionGroup.MOBS);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MAP);
        this.collider.addToGroup(Game.CollisionGroup.MOBS);
        this.collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        // this.collider.excludeResolutionToGroup(Game.CollisionGroup.BULLETS);
        
        this.collider.setRadius(5);
        this.collider.setMass(1);
        Game.world.getColliderWorld().addCollider(
            this.collider
        );
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        // hitEffect.begin(ctx);
        this.sprite.render(ctx);
        // hitEffect.end(ctx);
        
        // // draw path to player
        // for (Vector vector : pathToPlayer) {
        //     ctx.beginPath();
        //     ctx.setFill(Paint.valueOf("rgba(250, 120, 250, 0.85)"));
        //     ctx.fillOval(vector.getX(), vector.getY(), 8, 8);
        //     ctx.closePath();
        // }
    }
    
    public void fixedUpdate(float deltaTime) {
        this.handleMovements();
        this.checkPlayerCollision();
        this.maybeUpdatePathToPlayer();
        this.sprite.nextFrame();
        hitEffect.updateCurrentHealth(getCurrentHealth());
    }
    
    public void update(float deltaTime) {
        this.handleSprite();
        this.updateAngleToPlayer();
        
        if (getCurrentHealth() <= 0) {
            dispose();
        }
    }
    
    @Override
    public void dispose() {
        Game.world.getZombies().remove(this);
        Game.world.getColliderWorld().removeCollider(collider);
        AcidSprite acidSprite = new AcidSprite();
        acidSprite.getPosition().set(position);
        Game.world.addOneTimeSpriteAnimation(acidSprite);
    }
    
    private void checkPlayerCollision() {
        Player player = Game.world.getPlayer();
        boolean isCollidingWithPlayer = collider.isCollidingWith(
            player.getCollider()
        );
        if (intervals.isIntervalOverFor("zombie") && isCollidingWithPlayer) {
            player.addHealth(-damage);
            intervals.resetIntervalFor("zombie");
        }
    }
    
    private void updateAngleToPlayer() {
        Player player = Game.world.getPlayer();
        this.angleToPlayer = this.position.getAngle(player.getPosition());
        this.isFacingOnLeftSide = Math.abs(angleToPlayer) > (Math.PI / 2);
    }
    
    private void handleSprite() {
        this.sprite.getPosition().set(position);
        this.sprite.setHorizontallyFlipped(this.isFacingOnLeftSide);
    }
    
    private void maybeUpdatePathToPlayer() {
        Player player = Game.world.getPlayer();
        float distanceToPlayer = player.getPosition().getDistanceFrom(position);
        intervals.changeIntervalFor("pathToPlayerUpdate", (int) distanceToPlayer);
        if (intervals.isIntervalOverFor("pathToPlayerUpdate")) {
            Async.queue1.submit(() -> {
                PathFinder pathFinder = Game.world.getPathFinder();
                pathToPlayer = pathFinder.requestPath(
                    collider.getPosition(),
                    player.getCollider().getPosition()
                );
            });
            intervals.resetIntervalFor("pathToPlayerUpdate");
        }
    }
    
    private void handleMovements() {
        position.set(collider.getPosition().clone().addY(-collider.getRadius()));
        
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
