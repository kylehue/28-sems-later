package game.ui.components;

import game.Game;
import game.Progress;
import game.powerups.PowerUpKind;
import javafx.geometry.VPos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import utils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PowerUpSelect extends Select<PowerUpKind> {
    private final HashMap<PowerUpKind, ImageButton> imageButtonHashMap = new HashMap<>() {
        {
            put(
                PowerUpKind.BULLET_DAMAGE,
                new ImageButton("/ui/power-ups/bullet-damage.png")
            );
            put(
                PowerUpKind.BULLET_SPEED,
                new ImageButton("/ui/power-ups/bullet-speed.png")
            );
            put(
                PowerUpKind.BULLET_MAX_DISTANCE,
                new ImageButton("/ui/power-ups/bullet-max-distance.png")
            );
            put(
                PowerUpKind.BULLET_PENETRATION,
                new ImageButton("/ui/power-ups/bullet-penetration.png")
            );
            put(
                PowerUpKind.FIRE_RATE,
                new ImageButton("/ui/power-ups/fire-rate.png")
            );
            put(
                PowerUpKind.GRENADE_AOE,
                new ImageButton("/ui/power-ups/grenade-aoe.png")
            );
            put(
                PowerUpKind.RIFLE_ACCURACY,
                new ImageButton("/ui/power-ups/rifle-accuracy.png")
            );
            put(
                PowerUpKind.SHOTGUN_SPREAD,
                new ImageButton("/ui/power-ups/shotgun-spread.png")
            );
            put(
                PowerUpKind.MAX_HEALTH,
                new ImageButton("/ui/power-ups/max-health.png")
            );
            put(
                PowerUpKind.HEALTH_REGEN,
                new ImageButton("/ui/power-ups/health-regen.png")
            );
            put(
                PowerUpKind.MOVEMENT_SPEED,
                new ImageButton("/ui/power-ups/movement-speed.png")
            );
            put(
                PowerUpKind.DASH_INTERVAL,
                new ImageButton("/ui/power-ups/dash-interval.png")
            );
        }
    };
    private final HashSet<PowerUpKind> powerUps = new HashSet<>();
    
    public PowerUpSelect() {
        powerUps.add(PowerUpKind.HEALTH_REGEN);
        powerUps.add(PowerUpKind.MAX_HEALTH);
        powerUps.add(PowerUpKind.DASH_INTERVAL);
        powerUps.add(PowerUpKind.MOVEMENT_SPEED);
        powerUps.add(PowerUpKind.FIRE_RATE);
        powerUps.add(PowerUpKind.GRENADE_AOE);
        powerUps.add(PowerUpKind.RIFLE_ACCURACY);
        powerUps.add(PowerUpKind.SHOTGUN_SPREAD);
        powerUps.add(PowerUpKind.BULLET_DAMAGE);
        powerUps.add(PowerUpKind.BULLET_MAX_DISTANCE);
        powerUps.add(PowerUpKind.BULLET_PENETRATION);
        powerUps.add(PowerUpKind.BULLET_SPEED);

        isVisibleProperty().addListener(e -> {
            fixBounds();
        });
    }
    
    @Override
    protected void subFixBounds() {
        float canvasHeight = (float) Game.canvas.getHeight();
        getOffset().setY(
            canvasHeight / 2 - getOptionMaxHeight() / 2
        );
        
        float canvasWidth = (float) Game.canvas.getWidth();
        float idealWidth = canvasWidth * 0.85f;
        float idealOffsetX = (canvasWidth - idealWidth) / 2;
        setWidth(idealWidth);
        getOffset().setX(idealOffsetX);
    }
    
    public void randomizePool(int poolSize) {
        clearOptions();
        ArrayList<PowerUpKind> open = new ArrayList<>(
            powerUps
                .stream()
                .filter(v -> v.get().isAllowedToUse())
                .toList()
        );
        for (int i = 0; i < poolSize; i++) {
            int rng = (int) Math.round(Math.random() * (open.size() - 1));
            PowerUpKind pickedPowerUp = open.get(rng);
            addOption(pickedPowerUp, imageButtonHashMap.get(pickedPowerUp));
            open.remove(pickedPowerUp);
        }
        fixBounds();
    }
    
    public void show() {
        randomizePool(4);
        setVisible(true);
    }
    
    public void hide() {
        setVisible(false);
        clearOptions();
    }
    
    @Override
    public void subRender(GraphicsContext ctx) {
        if (isVisible()) {
            // Render text
            ctx.save();
            ctx.setFont(Common.loadFont(
                "/fonts/PIXY.ttf",
                48
            ));
            DropShadow dropShadow = new DropShadow();
            dropShadow.setColor(Color.color(0, 0, 0, 1));
            dropShadow.setRadius(0);
            dropShadow.setSpread(0);
            dropShadow.setOffsetY(4);
            ctx.setTextAlign(TextAlignment.CENTER);
            ctx.setTextBaseline(VPos.BOTTOM);
            ctx.setEffect(dropShadow);
            ctx.setFill(Color.valueOf("white"));
            ctx.fillText(
                "Choose an upgrade",
                getOffset().getX() + getWidth() / 2,
                getOffset().getY() - 20
            );
            ctx.restore();
        }
    }
}
