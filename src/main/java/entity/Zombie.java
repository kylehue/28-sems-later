package entity;

import colliders.CircleCollider;
import javafx.concurrent.Task;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import main.CollisionGroup;
import main.GameApplication;
import main.ZIndex;
import scenes.game.World;
import sprites.ZombieSprite;
import utils.Async;
import utils.Bounds;
import utils.GameUtils;
import map.PathFinder;
import utils.Vector;

import java.util.ArrayList;

public class Zombie extends Entity {
    // basic characteristics
    private float speed = GameUtils.random(250, 550);
    private int damage = 1;
    // sprite
    private final ZombieSprite sprite = new ZombieSprite();
    // misc
    private final GameApplication gameApplication;
    private CircleCollider collider = new CircleCollider();
    private float angleToPlayer = 0;
    private boolean isFacingOnLeftSide = false;
    private ArrayList<Vector> pathToPlayer = new ArrayList<>();
    
    public Zombie(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.sprite.randomizeFirstFrame();
        this.gameApplication.getGameScene().getWorld().getColliderWorld().addCollider(
            this.collider
        );
        
        this.collider.setGroup(CollisionGroup.ZOMBIES);
        this.collider.addToGroup(CollisionGroup.MAP);
        this.collider.addToGroup(CollisionGroup.MAP);
        this.collider.addToGroup(CollisionGroup.ZOMBIES);
        this.collider.addToGroup(CollisionGroup.BULLETS);
        this.collider.excludeResolutionToGroup(CollisionGroup.BULLETS);
        
        this.collider.setRadius(5);
        this.collider.setMass(1);
        registerIntervalFor("zombie", 5000);
        registerIntervalFor("pathToPlayerUpdate", (int) GameUtils.random(300, 500));
        this.setZIndex(ZIndex.ZOMBIE);
    }
    
    @Override
    public Vector getRenderPosition() {
        return collider.getPosition();
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        this.sprite.render(ctx);
    }
    
    public void update(float deltaTime) {
        this.handleMovements();
        this.handleSprite();
        this.updateAngleToPlayer();
        this.checkPlayerCollision();
        this.maybeUpdatePathToPlayer();
        
        // put in quadtree
        this.gameApplication.getGameScene().getWorld().getQuadtree().insert(
            collider,
            new Bounds(
                collider.getPosition().getX() - collider.getWidth() / 2,
                collider.getPosition().getY() - collider.getHeight() / 2,
                collider.getWidth(),
                collider.getHeight()
            )
        );
    }
    
    private void checkPlayerCollision() {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
        boolean isCollidingWithPlayer = collider.isCollidingWith(
            player.getCollider()
        );
        if (isIntervalOverFor("zombie") && isCollidingWithPlayer) {
            player.setCurrentHealth(player.getCurrentHealth() - damage);
            resetIntervalFor("zombie");
        }
    }
    
    private void updateAngleToPlayer() {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
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
        if (isIntervalOverFor("pathToPlayerUpdate")) {
            Async.executorService.submit(() -> {
                PathFinder pathFinder = gameApplication
                    .getGameScene()
                    .getWorld()
                    .getPathFinder();
                Player player = gameApplication.getGameScene().getWorld().getPlayer();
                pathToPlayer = pathFinder.requestPath(
                    collider.getPosition(),
                    player.getCollider().getPosition()
                );
                if (!pathToPlayer.isEmpty()) {
                    pathToPlayer.removeLast();
                }
                
                if (!pathToPlayer.isEmpty()) {
                    // add jitter
                    pathToPlayer.getLast().add(
                        GameUtils.random(-10, 10),
                        GameUtils.random(-10, 10)
                    );
                }
            });
            resetIntervalFor("pathToPlayerUpdate");
        }
    }
    
    private void handleMovements() {
        getPosition().set(collider.getPosition().clone().addY(-collider.getRadius()));
        
        // Move to player
        if (!pathToPlayer.isEmpty()) {
            // for (Vector v : pathToPlayer) {
            //     World.debugRender.put((v.toString()), ctx -> {
            //         ctx.beginPath();
            //         ctx.setFill(Paint.valueOf("white"));
            //         ctx.fillOval(v.getX() - 2, v.getY() - 2, 4, 4);
            //         ctx.closePath();
            //     });
            // }
            
            Vector pathToPlayerStep = pathToPlayer.getLast();
            float angle = getPosition().getAngle(pathToPlayerStep);
            this.collider.applyForce(
                (float) (Math.cos(angle) * speed * collider.getMass()),
                (float) (Math.sin(angle) * speed * collider.getMass())
            );
            this.isFacingOnLeftSide = Math.abs(angle) > (Math.PI / 2);
            if (pathToPlayerStep.getDistanceFrom(getPosition()) < 10) {
                pathToPlayer.removeLast();
            }
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
}
