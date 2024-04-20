package game.ui;

import game.Game;
import game.ui.components.Component;
import game.ui.components.ImageProgressBar;
import game.ui.components.WeaponSwitch;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

public class UI {
    private WeaponSwitch weaponSwitch;
    private ImageProgressBar healthBar;
    
    private final ArrayList<Component> components = new ArrayList<>();
    
    public UI() {
        initComponents();
        fixBounds();
        Game.canvas.widthProperty().addListener(e -> {
            fixBounds();
        });
        Game.canvas.heightProperty().addListener(e -> {
            fixBounds();
        });
        
        Game.keyHandler.getKeyPressedProperty("weapon-switch").addListener(e -> {
            if (!Game.keyHandler.isKeyPressed("weapon-switch")) return;
            weaponSwitch.setVisible(!weaponSwitch.isVisible());
        });
    }
    
    private void initComponents() {
        weaponSwitch = new WeaponSwitch();
        addComponent(weaponSwitch);
        
        healthBar = new ImageProgressBar(
            "/ui/health-bar/base.png",
            "/ui/health-bar/bar.png"
        );
        healthBar.getPosition().set(15, 15);
        healthBar.setWidth(350);
        healthBar.currentValueProperty().bindBidirectional(
            Game.world.getPlayer().currentHealthProperty()
        );
        healthBar.maxValueProperty().bindBidirectional(
            Game.world.getPlayer().maxHealthProperty()
        );
        addComponent(healthBar);
    }
    
    private void fixBounds() {
        float canvasWidth = (float) Game.canvas.getWidth();
        float idealWidth = canvasWidth * 0.85f;
        weaponSwitch.setWidth(idealWidth);
        weaponSwitch.getOffset().setX((canvasWidth - idealWidth) / 2);
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
}
