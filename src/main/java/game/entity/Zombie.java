package game.entity;

import game.Config;
import game.Game;
import game.colliders.CircleCollider;
import game.colliders.Collider;
import game.colliders.PolygonCollider;
import game.sprites.AcidSprite;
import game.utils.*;
import javafx.scene.canvas.GraphicsContext;
import game.sprites.ZombieSprite;
import javafx.scene.paint.Paint;
import utils.Async;
import game.map.PathFinder;

import java.util.ArrayList;

public class Zombie extends Entity {
    // stats
    private float speed = Common.random(300, 750);
    private float damage = Config.DEFAULT_ZOMBIE_DAMAGE;
    
    // misc
    private final ZombieSprite sprite = new ZombieSprite();
    private final CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    private ArrayList<Vector> pathToPlayer = new ArrayList<>();
    private final HitEffect hitEffect = new HitEffect();
    private final IntervalMap intervals = new IntervalMap();
    
    public Zombie() {
        // Initialize colliders
        collider.setGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.MAP);
        collider.addToGroup(Game.CollisionGroup.MOBS);
        collider.addToGroup(Game.CollisionGroup.PROJECTILES);
        collider.setRadius(5);
        collider.setMass(1);
        Game.world.getColliderWorld().addCollider(collider);
        
        // Initialize intervals
        intervals.registerIntervalFor("zombie", 5000);
        intervals.registerIntervalFor(
            "pathToPlayerUpdate",
            (int) Common.random(70, 200)
        );
        
        // Misc
        this.sprite.randomizeFirstFrame();
        this.setZIndex(Game.ZIndex.ZOMBIE);
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
        
        // // render hit box
        // ctx.beginPath();
        // ctx.setFill(Paint.valueOf("rgba(250, 120, 250, 0.85)"));
        // ctx.fillRect(
        //     getHitBox().getX(),
        //     getHitBox().getY(),
        //     getHitBox().getWidth(),
        //     getHitBox().getHeight()
        // );
        // ctx.closePath();
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
        
        // Death animation
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
    
    public void setDamage(float damage) {
        this.damage = damage;
    }
    
    @Override
    public CircleCollider getCollider() {
        return collider;
    }
    
    @Override
    public Bounds getHitBox() {
        float width = sprite.getWidth() * 0.55f;
        float height = sprite.getHeight() * 0.7f;
        return new Bounds(
            position.getX() - width / 2,
            position.getY() - height / 2,
            width,
            height
        );
    }
    
    @Override
    public Vector getRenderPosition() {
        return collider.getPosition();
    }
    
    public float getDamage() {
        return damage;
    }
}
