package scenes.game;

import entity.Player;
import javafx.scene.canvas.GraphicsContext;
import main.GameApplication;

import java.util.ArrayList;

public class World {
    private final ArrayList<Player> players = new ArrayList<>();
    
    public World(GameApplication gameApplication) {
        players.add(new Player(gameApplication));
    }
    
    public void render(GraphicsContext ctx) {
        for (Player player : players) {
            player.render(ctx);
        }
    }
    
    public void update() {
        for (Player player : players) {
            player.update();
        }
    }
}
