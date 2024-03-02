package scenes.game;

import entity.Bullet;
import entity.Zombie;
import entity.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;
import map.CityMap;
import utils.Camera;
import utils.GameUtils;

import java.util.ArrayList;

public class World {
    private final GameApplication gameApplication;
    private final Player player;
    private final ArrayList<Zombie> zombies = new ArrayList<>();
    private final ArrayList<Bullet> bullets = new ArrayList<>();
    private final Camera camera;
    private final CityMap cityMap = new CityMap();
    
    public World(GameApplication gameApplication) {
        this.gameApplication = gameApplication;
        this.camera = new Camera(gameApplication.getGameScene().getGraphicsContext());
        this.player = new Player(gameApplication);
        
        Canvas canvas = gameApplication.getGameScene().getGraphicsContext().getCanvas();
        for (int i = 0; i < 50; i++) {
            Zombie enemy = new Zombie(gameApplication);
            enemy.getPosition().setX(GameUtils.random(0, canvas.getWidth()));
            enemy.getPosition().setY(GameUtils.random(0, canvas.getHeight()));
            zombies.add(enemy);
        }
    }
    
    public void render(GraphicsContext ctx) {
        this.camera.begin();
        
        cityMap.render(ctx);
        
        for (Zombie zombie : zombies) {
            zombie.render(ctx);
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
        
        for (Zombie zombie : zombies) {
            zombie.update();
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
