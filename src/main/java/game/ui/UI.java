package game.ui;

import game.Game;
import game.ui.components.Component;
import game.ui.components.ImageProgressBar;
import game.ui.components.PowerUpSelect;
import game.ui.components.WeaponSwitch;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class UI {
    private final WeaponSwitch weaponSwitch = new WeaponSwitch();
    private final PowerUpSelect powerUpSelect = new PowerUpSelect();
    private final ImageProgressBar healthBar = new ImageProgressBar(
        "/ui/health-bar/base.png",
        "/ui/health-bar/bar.png"
    );
    
    private final ArrayList<Component> components = new ArrayList<>();
    
    public UI() {
        initComponents();
        Game.keyHandler.getKeyPressedProperty("weapon-switch").addListener(e -> {
            if (!Game.keyHandler.isKeyPressed("weapon-switch")) return;
            weaponSwitch.setVisible(!weaponSwitch.isVisible());
            // showPowerUpSelect();
        });
    }
    
    private void initComponents() {
        addComponent(weaponSwitch);
        addComponent(powerUpSelect);
        addComponent(healthBar);
        
        healthBar.getPosition().set(15, 15);
        healthBar.setWidth(350);
        healthBar.currentValueProperty().bindBidirectional(
            Game.world.getPlayer().currentHealthProperty()
        );
        healthBar.maxValueProperty().bindBidirectional(
            Game.world.getPlayer().maxHealthProperty()
        );
    }
    
    private void renderFPS(GraphicsContext ctx) {
        ctx.beginPath();
        ctx.setFill(Paint.valueOf("#00FF00"));
        ctx.setFont(Font.font(null, FontWeight.BOLD, 24));
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.fillText(
            String.valueOf(Game.FPS), ctx.getCanvas().getWidth() - 30,
            30,
            100
        );
        ctx.closePath();
    }
    
    public void addComponent(Component component) {
        components.add(component);
    }
    
    public void render(GraphicsContext ctx) {
        renderFPS(ctx);
        for (Component component : components) {
            component.render(ctx);
        }
    }
    
    public void showPowerUpSelect() {
        powerUpSelect.show();
    }
}
