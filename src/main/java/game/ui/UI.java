package game.ui;

import game.Game;
import game.Progress;
import game.ui.components.Component;
import game.ui.components.ImageProgressBar;
import game.ui.components.PowerUpSelect;
import game.ui.components.WeaponSwitch;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import utils.Common;

import java.util.ArrayList;

public class UI {
    private final WeaponSwitch weaponSwitch = new WeaponSwitch();
    private final PowerUpSelect powerUpSelect = new PowerUpSelect();
    private final ImageProgressBar healthBar = new ImageProgressBar(
        "/ui/health-bar/base.png",
        "/ui/health-bar/bar.png"
    );
    private final ImageProgressBar xpBar = new ImageProgressBar(
        "/ui/xp-bar/base.png",
        "/ui/xp-bar/bar.png"
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
        addComponent(xpBar);
        
        float margin = 10;
        healthBar.getPosition().set(margin, margin);
        healthBar.getBarOffset().set(21, 8);
        healthBar.setWidth(350);
        healthBar.currentValueProperty().bind(Progress.currentHealth);
        healthBar.maxValueProperty().bind(Progress.maxHealth);
        
        xpBar.getPosition().set(
            healthBar.getPosition().getX() + healthBar.getWidth() + margin,
            margin
        );
        xpBar.getBarOffset().set(21, 8);
        xpBar.setWidth(350);
        xpBar.currentValueProperty().bind(Progress.currentXp);
        xpBar.maxValueProperty().bind(Progress.maxXp);
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
        
        // Render level
        ctx.save();
        ctx.setFont(Common.loadFont(
            "/fonts/PIXY.ttf",
            (int) (xpBar.getHeight() / 2)
        ));
        float widthRatio = xpBar.getWidthRadio();
        float heightRatio = xpBar.getHeightRadio();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.5));
        dropShadow.setRadius(0);
        dropShadow.setSpread(0);
        dropShadow.setOffsetY(heightRatio);
        ctx.setTextAlign(TextAlignment.CENTER);
        ctx.setTextBaseline(VPos.CENTER);
        ctx.setEffect(dropShadow);
        ctx.setFill(Color.valueOf("#5bf4a7"));
        ctx.fillText(
            String.valueOf(Progress.currentLevel.get()),
            xpBar.getPosition().getX() + 11 * widthRatio,
            xpBar.getPosition().getY() + 9 * heightRatio,
            18 * widthRatio
        );
        ctx.restore();
    }
    
    public void showPowerUpSelect() {
        powerUpSelect.show();
    }
    
    public PowerUpSelect getPowerUpSelect() {
        return powerUpSelect;
    }
}
