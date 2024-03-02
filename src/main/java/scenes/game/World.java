package scenes.game;

import entity.Bullet;
import entity.Enemy;
import entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import utils.Camera;
import utils.GameUtils;

import java.util.ArrayList;

public class World {
    private final GameApplication gameApplication;
    private final Player player;
    private final ArrayList<Enemy> enemies = new ArrayList<>();
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final Camera camera;
    
    public World(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.camera = new Camera(gameApplication.getGameScene().getGraphicsContext());
        this.player = new Player(gameApplication);
        
        Canvas canvas = gameApplication.getGameScene().getGraphicsContext().getCanvas();
        for (int i = 0; i < 50; i++) {
            Enemy enemy = new Enemy(gameApplication);
            enemy.getPosition().setX(GameUtils.random(0, canvas.getWidth()));
            enemy.getPosition().setY(GameUtils.random(0, canvas.getHeight()));
            enemies.add(enemy);
        }
    }
    
    public void render(GraphicsContext ctx) {
        this.camera.begin();
        
        for (Enemy enemy : enemies) {
            enemy.render(ctx);
        }
        
        for (Bullet bullet : bullets) {
            bullet.render(ctx);
        }
        
        player.render(ctx);
        
        this.camera.end();
    }
    
    public void update() {
        this.handleBulletDisposal();
        
        player.update();
        
        for (Enemy enemy : enemies) {
            // enemy.update();
        }
        
        for (Bullet bullet : bullets) {
            bullet.update();
        }
        
        this.camera.moveTo(player.getPosition());
        this.camera.zoomTo(700);
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    
    public Bullet spawnBullet(double x, double y, double angle) {
        Bullet bullet = new Bullet(this.gameApplication, x, y, angle);
        this.bullets.add(bullet);
        return bullet;
    }
    
    private void handleBulletDisposal() {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            // dispose bullets when reached max distance
            double distance = bullet.getPosition().getDistanceFrom(bullet.getInitialPosition());
            if (distance > bullet.getMaxDistance()) {
                bullets.remove(i);
            }
        }
    }
    
    public Camera getCamera() {
        return camera;
    }
}
