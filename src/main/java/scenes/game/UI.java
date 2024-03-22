package scenes.game;

import entity.Player;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import main.GameApplication;
import utils.LayoutUtils;

public class UI {
    
    private Image healthBarBaseImage = LayoutUtils.loadImage("/ui/HealthBarBase.png");
    private Image healthBarImage = LayoutUtils.loadImage("/ui/HealthBar.png");
    private int healthBarScale = 3;
    
    private final GameApplication gameApplication;
    public UI(GameApplication gameApplication){
        this.gameApplication = gameApplication;
    }
    
    public void render(GraphicsContext ctx) {
        Player player = gameApplication.getGameScene().getWorld().getPlayer();
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
            0, //x
            0, // y
            124 * healthPercentage, // crop width
            14,  // crop height
            10,
            10,
            124 * healthBarScale * healthPercentage,
            14 * healthBarScale
        );
        
    }
    public void update(){
    
      
    
    }
    
    
    
}
