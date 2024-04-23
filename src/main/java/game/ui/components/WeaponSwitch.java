package game.ui.components;

import game.Game;
import game.Progress;
import game.inventory.WeaponKind;
import game.utils.Vector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;

public class WeaponSwitch extends Select<WeaponKind> {
    HashMap<WeaponKind, ImageButton> lockedVersions = new HashMap<>() {
        {
            put(WeaponKind.PISTOL, new ImageButton("/ui/weapons/pistol-locked.png"));
            put(WeaponKind.RIFLE, new ImageButton("/ui/weapons/rifle-locked.png"));
            put(WeaponKind.SHOTGUN, new ImageButton("/ui/weapons/shotgun-locked.png"));
            put(WeaponKind.SNIPER, new ImageButton("/ui/weapons/sniper-locked.png"));
            put(WeaponKind.GRENADE_LAUNCHER, new ImageButton("/ui/weapons/grenade-launcher-locked.png"));
        }
    };
    
    public WeaponSwitch() {
        addOption(WeaponKind.PISTOL, "/ui/weapons/pistol.png");
        addOption(WeaponKind.RIFLE, "/ui/weapons/rifle.png");
        addOption(WeaponKind.SHOTGUN, "/ui/weapons/shotgun.png");
        addOption(WeaponKind.SNIPER, "/ui/weapons/sniper.png");
        addOption(WeaponKind.GRENADE_LAUNCHER, "/ui/weapons/grenade-launcher.png");
        
        selectedOptionProperty().bindBidirectional(
            Game.world.getPlayer().getInventoryManager().currentWeaponProperty()
        );
        selectedOptionProperty().addListener(e -> {
            setVisible(false);
        });
        isVisibleProperty().addListener(e -> {
            fixBounds();
        });
    }
    
    @Override
    protected void subFixBounds() {
        float canvasHeight = (float) Game.canvas.getHeight();
        getOffset().setY(
            canvasHeight - getOptionMaxHeight() - getSpacing()
        );
        
        float canvasWidth = (float) Game.canvas.getWidth();
        float idealWidth = canvasWidth * 0.85f;
        float idealOffsetX = (canvasWidth - idealWidth) / 2;
        setWidth(idealWidth);
        getOffset().setX(idealOffsetX);
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        for (WeaponKind weaponKind : getOptions().keySet()) {
            boolean isUnlocked = Progress.unlockedWeapons.contains(weaponKind);
            ImageButton unlocked = getOptions().get(weaponKind);
            ImageButton locked = lockedVersions.get(weaponKind);
            
            unlocked.setVisible(isUnlocked && isVisible());
            locked.setVisible(!isUnlocked && isVisible());
            
            if (isVisible()) {
                locked.getPosition().set(unlocked.getPosition());
                locked.setWidth(unlocked.getWidth());
                
                if (isUnlocked) {
                    unlocked.render(ctx);
                } else {
                    locked.render(ctx);
                }
            }
        }
    }
}
