package game.ui.components;

import game.Game;
import game.Progress;
import game.inventory.WeaponKind;
import game.utils.Vector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class WeaponSwitch extends Component {
    private record WeaponData(
        WeaponKind weaponKind,
        ImageButton unlocked,
        ImageButton locked
    ) {
    }
    
    private final WeaponData[] weapons;
    
    private final ObjectProperty<WeaponKind> currentWeapon = new SimpleObjectProperty<>();
    private final Vector offset = new Vector();
    private float width = -1;
    
    public WeaponSwitch() {
        this.weapons = new WeaponData[]{
            new WeaponData(
                WeaponKind.PISTOL,
                new ImageButton("/ui/weapons/pistol.png"),
                new ImageButton("/ui/weapons/pistol-locked.png")
            ),
            new WeaponData(
                WeaponKind.RIFLE,
                new ImageButton("/ui/weapons/rifle.png"),
                new ImageButton("/ui/weapons/rifle-locked.png")
            ),
            new WeaponData(
                WeaponKind.SHOTGUN,
                new ImageButton("/ui/weapons/shotgun.png"),
                new ImageButton("/ui/weapons/shotgun-locked.png")
            ),
            new WeaponData(
                WeaponKind.SNIPER,
                new ImageButton("/ui/weapons/sniper.png"),
                new ImageButton("/ui/weapons/sniper-locked.png")
            ),
            new WeaponData(
                WeaponKind.GRENADE_LAUNCHER,
                new ImageButton("/ui/weapons/grenade-launcher.png"),
                new ImageButton("/ui/weapons/grenade-launcher-locked.png")
            )
        };
        
        // Add click listener on weapons
        for (WeaponData weaponData : weapons) {
            weaponData.unlocked.setOnClick(() -> {
                setCurrentWeapon(weaponData.weaponKind);
            });
        }
        
        // Fix component bounds on window resize
        fixBounds();
        Game.canvas.widthProperty().addListener(e -> {
            fixBounds();
        });
        Game.canvas.heightProperty().addListener(e -> {
            fixBounds();
        });
        
        currentWeapon.bindBidirectional(
            Game.world.getPlayer().getInventoryManager().currentWeaponProperty()
        );
        currentWeaponProperty().addListener(e -> {
            fixBounds();
        });
    }
    
    private void fixBounds() {
        if (weapons.length == 0) return;
        if (getCurrentWeapon() == null) return;
        
        Canvas canvas = Game.canvas;
        float spacing = 20;
        float canvasHeight = (float) canvas.getHeight();
        float desiredWidth = width == -1 ? (float) canvas.getWidth() : width;
        
        for (int i = 0; i < weapons.length; i++) {
            WeaponData weaponData = weapons[i];
            
            float properWidth = desiredWidth / weapons.length - spacing + spacing / weapons.length;
            weaponData.locked.setWidth(properWidth);
            weaponData.unlocked.setWidth(properWidth);
            
            float properHeight = weaponData.unlocked.getHeight();
            
            Vector properPosition = new Vector(
                offset.getX() + i * weaponData.unlocked.getWidth() + i * spacing,
                offset.getY() + canvasHeight - properHeight - spacing
            );
            if (weaponData.weaponKind == getCurrentWeapon()) {
                properPosition.addY(-properHeight / 8);
            }
            weaponData.locked.getPosition().set(properPosition);
            weaponData.unlocked.getPosition().set(properPosition);
        }
    }
    
    public void setWidth(float width) {
        this.width = width;
        fixBounds();
    }
    
    public void setCurrentWeapon(WeaponKind currentWeapon) {
        this.currentWeapon.set(currentWeapon);
        setVisible(false);
        fixBounds();
    }
    
    public ObjectProperty<WeaponKind> currentWeaponProperty() {
        return currentWeapon;
    }
    
    public WeaponKind getCurrentWeapon() {
        return currentWeapon.get();
    }
    
    public Vector getOffset() {
        return offset;
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        for (WeaponData weaponData : weapons) {
            boolean isUnlocked = Progress.unlockedWeapons.contains(weaponData.weaponKind);
            weaponData.unlocked.setVisible(isUnlocked && isVisible);
            weaponData.locked.setVisible(!isUnlocked && isVisible);
            if (isUnlocked) {
                weaponData.unlocked.render(ctx);
            } else {
                weaponData.locked.render(ctx);
            }
        }
    }
}
