package main;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;

public class World {
    private final ArrayList<Player> players = new ArrayList<>();
    
    public World() {
        players.add(new Player());
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
