package game;

import game.entity.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import utils.Common;

public class UI {
    private final Game game;
    private final Image healthBarBaseImage = Common.loadImage("/ui/HealthBarBase.png");
    private final Image healthBarImage = Common.loadImage("/ui/HealthBar.png");
    private final int healthBarScale = 3;
    
    
    public UI(Game game) {
        this.game = game;
    }
    
    private void renderFPS(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Paint.valueOf("#00FF00"));
        ctx.setFont(Font.font(null, FontWeight.BOLD, 24));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.fillText(
            String.valueOf((int) game.getFPS()), ctx.getCanvas().getWidth() - 30,
            30,
            100
        );
        ctx.closePath();
    }
    
    public void render(GraphicsContext ctx) {
        Player player = game.getWorld().getPlayer();
        float healthPercentage = (float) player.getCurrentHealth() / (float) player.getMaxHealth();
        ctx.drawImage(
            healthBarBaseImage,
            10,
            10,
            124 * healthBarScale,
            14 * healthBarScale
        );
        ctx.drawImage(
            healthBarImage,
            0,
            0,
            124 * healthPercentage,
            14,
            10,
            10,
            124 * healthBarScale * healthPercentage,
            14 * healthBarScale
        );
        
        renderFPS(ctx);
    }
    
    public void update() {
    
    }
}
