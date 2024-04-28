package scenes;

import game.Game;
import game.Progress;
import game.powerups.PowerUpKind;
import game.weapons.WeaponKind;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import main.GameApplication;
import skins.ButtonImageSkin;
import skins.ProgressBarImageSkin;
import utils.Common;

import java.util.ArrayList;

public class GameScene extends GameApplicationScene {
    private final Game game;
    private final BooleanProperty isGameOverComponentVisible = new SimpleBooleanProperty();
    private final BooleanProperty isWeaponSwitchComponentVisible = new SimpleBooleanProperty();
    private final BooleanProperty isPowerUpSelectionComponentVisible = new SimpleBooleanProperty();
    private PowerUpSelectEvent onSelectPowerUp = null;
    
    public GameScene(GameApplication gameApplication, String sceneId) {
        super(gameApplication, sceneId);
        
        game = new Game(this);
        game.initEventHandlers(scene);
        
        // Add canvas to game scene
        defaultRoot.getChildren().add(Game.canvas);
        Game.canvas.widthProperty().bind(scene.widthProperty());
        Game.canvas.heightProperty().bind(scene.heightProperty());
        
        this.setupGameOverComponent();
        this.setupGameComponents();
        this.setupWeaponSwitchComponent();
        this.setupPowerUpSelectionComponent();
    }
    
    private void setupGameOverComponent() {
        GridPane parent = new GridPane();
        parent.visibleProperty().bind(isGameOverComponentVisible);
        defaultRoot.getChildren().add(parent);
        Common.setupGridPane(parent, 2, 1);
        parent.getRowConstraints().get(0).setPercentHeight(70);
        parent.getRowConstraints().get(1).setPercentHeight(30);
        
        ImageView gameOverImage = new ImageView(
            Common.resampleImage(
                Common.loadImage("/ui/game-over/game-over.png"),
                8
            )
        );
        GridPane.setHalignment(gameOverImage, HPos.CENTER);
        GridPane.setValignment(gameOverImage, VPos.CENTER);
        parent.add(gameOverImage, 0, 0);
        
        Button mainMenuButton = new Button("Main Menu");
        GridPane.setHalignment(mainMenuButton, HPos.CENTER);
        GridPane.setValignment(mainMenuButton, VPos.TOP);
        parent.add(mainMenuButton, 0, 1);
        
        mainMenuButton.setOnMouseClicked(e -> {
            gameApplication.getSceneManager().setScene("title");
            game.resetGame();
        });
    }
    
    public void setGameOverComponentsVisible(boolean v) {
        isGameOverComponentVisible.set(v);
    }
    
    private Label createPixelatedLabel(int size) {
        Label label = new Label();
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.color(0, 0, 0, 0.75));
        dropShadow.setRadius(0);
        dropShadow.setSpread(0);
        dropShadow.setOffsetY(5);
        label.setTextFill(Color.WHITE);
        label.setEffect(dropShadow);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setFont(Common.loadFont(
            "/fonts/PIXY.ttf",
            size
        ));
        
        return label;
    }
    
    private void setupGameComponents() {
        // Parent of all
        GridPane parent = new GridPane();
        defaultRoot.getChildren().add(parent);
        Common.setupGridPane(parent, 2, 2);
        parent.getRowConstraints().get(0).setPercentHeight(50);
        parent.getRowConstraints().get(1).setPercentHeight(50);
        parent.getColumnConstraints().get(0).setPercentWidth(50);
        parent.getColumnConstraints().get(1).setPercentWidth(50);
        parent.setPadding(new Insets(10));
        
        // Top left parent
        VBox topLeftParent = new VBox();
        parent.add(topLeftParent, 0, 0);
        topLeftParent.setSpacing(10);
        
        // Health bar
        int scaleFactor = 3;
        ProgressBar healthBar = new ProgressBar();
        topLeftParent.getChildren().add(healthBar);
        healthBar.setSkin(
            new ProgressBarImageSkin(
                healthBar,
                Common.resampleImage("/ui/health-bar/base.png", scaleFactor),
                Common.resampleImage("/ui/health-bar/bar.png", scaleFactor),
                21 * scaleFactor,
                8 * scaleFactor
            )
        );
        healthBar.progressProperty().bind(Progress.currentHealth.divide(Progress.maxHealth));
        healthBar.setMouseTransparent(true);
        
        // XP bar
        ProgressBar xpBar = new ProgressBar();
        xpBar.setSkin(
            new ProgressBarImageSkin(
                xpBar,
                Common.resampleImage("/ui/xp-bar/base.png", scaleFactor),
                Common.resampleImage("/ui/xp-bar/bar.png", scaleFactor),
                21 * scaleFactor,
                8 * scaleFactor
            )
        );
        Progress.currentXp.addListener(e -> {
            float currentXp = (float) Progress.currentXp.get();
            float maxXp = (float) Progress.maxXp.get();
            xpBar.setProgress(currentXp / maxXp);
        });
        
        // XP text
        Label xpText = createPixelatedLabel(21 * scaleFactor / 2);
        xpText.textProperty().bind(Progress.currentLevel.asString());
        xpText.setTextFill(Color.valueOf("#5bf4a7"));
        xpText.prefHeightProperty().bind(xpBar.heightProperty());
        xpText.maxHeightProperty().bind(xpBar.heightProperty());
        xpText.setPrefWidth(21 * scaleFactor);
        xpText.setMaxWidth(21 * scaleFactor);
        
        Group xpGroup = new Group(xpBar, xpText);
        xpGroup.setMouseTransparent(true);
        topLeftParent.getChildren().add(xpGroup);
    }
    
    private void setupWeaponSwitchComponent() {
        // Parent of all
        StackPane parent = new StackPane();
        parent.visibleProperty().bind(isWeaponSwitchComponentVisible);
        defaultRoot.getChildren().add(parent);
        parent.setPadding(new Insets(40));
        
        // Parent of weapon switch
        VBox weaponSwitchParent = new VBox();
        parent.getChildren().add(weaponSwitchParent);
        weaponSwitchParent.setAlignment(Pos.BOTTOM_CENTER);
        weaponSwitchParent.setSpacing(10);
        
        // Text
        Label label = createPixelatedLabel(50);
        weaponSwitchParent.getChildren().add(label);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setText("Weapons");
        
        // Parent of weapons
        HBox weaponsParent = new HBox();
        weaponSwitchParent.getChildren().add(weaponsParent);
        weaponsParent.setSpacing(10);
        weaponsParent.setAlignment(Pos.CENTER);
        
        // Setup weapon buttons
        record ButtonInfo(
            WeaponKind weaponKind,
            Button button,
            String unlockedImageUrl,
            String lockedImageUrl
        ) {
        }
        ArrayList<ButtonInfo> buttonInfos = new ArrayList<>() {
            {
                add(new ButtonInfo(
                    WeaponKind.PISTOL,
                    new Button(),
                    "/ui/weapons/pistol.png",
                    "/ui/weapons/pistol-locked.png"
                ));
                add(new ButtonInfo(
                    WeaponKind.RIFLE,
                    new Button(),
                    "/ui/weapons/rifle.png",
                    "/ui/weapons/rifle-locked.png"
                ));
                add(new ButtonInfo(
                    WeaponKind.SHOTGUN,
                    new Button(),
                    "/ui/weapons/shotgun.png",
                    "/ui/weapons/shotgun-locked.png"
                ));
                add(new ButtonInfo(
                    WeaponKind.SNIPER,
                    new Button(),
                    "/ui/weapons/sniper.png",
                    "/ui/weapons/sniper-locked.png"
                ));
                add(new ButtonInfo(
                    WeaponKind.GRENADE_LAUNCHER,
                    new Button(),
                    "/ui/weapons/grenade-launcher.png",
                    "/ui/weapons/grenade-launcher-locked.png"
                ));
            }
        };
        
        // Add weapon buttons
        int scaleFactor = 3;
        for (ButtonInfo buttonInfo : buttonInfos) {
            weaponsParent.getChildren().add(buttonInfo.button);
            
            ButtonImageSkin buttonImageSkin = new ButtonImageSkin(
                buttonInfo.button,
                Common.resampleImage(
                    Progress.unlockedWeapons.contains(buttonInfo.weaponKind) ? buttonInfo.unlockedImageUrl : buttonInfo.lockedImageUrl,
                    scaleFactor
                )
            );
            buttonInfo.button.setSkin(buttonImageSkin);
            
            buttonInfo.button.setOnAction(e -> {
                if (!Progress.unlockedWeapons.contains(buttonInfo.weaponKind)) {
                    return;
                }
                
                Game.world.getPlayer().setCurrentWeapon(
                    buttonInfo.weaponKind
                );
                
                setWeaponSwitchComponentVisible(false);
            });
        }
        
        // Watch lock/unlock weapon buttons
        Progress.unlockedWeapons.addListener(
            (SetChangeListener<? super WeaponKind>) (e) -> {
                e.getElementAdded();
                for (ButtonInfo buttonInfo : buttonInfos) {
                    ButtonImageSkin buttonImageSkin = new ButtonImageSkin(
                        buttonInfo.button,
                        Common.resampleImage(
                            Progress.unlockedWeapons.contains(buttonInfo.weaponKind) ? buttonInfo.unlockedImageUrl : buttonInfo.lockedImageUrl,
                            scaleFactor
                        )
                    );
                    buttonInfo.button.setSkin(buttonImageSkin);
                }
            }
        );
    }
    
    public void setWeaponSwitchComponentVisible(boolean v) {
        isWeaponSwitchComponentVisible.set(v);
    }
    
    public boolean isWeaponSwitchComponentVisible() {
        return isWeaponSwitchComponentVisible.get();
    }
    
    public interface PowerUpSelectEvent {
        void call(PowerUpKind selectedPowerUp);
    }
    
    private void setupPowerUpSelectionComponent() {
        // Parent of all
        VBox parent = new VBox();
        parent.visibleProperty().bind(isPowerUpSelectionComponentVisible);
        defaultRoot.getChildren().add(parent);
        parent.setPadding(new Insets(40));
        parent.setAlignment(Pos.CENTER);
        
        // Setup text
        Label label = createPixelatedLabel(50);
        parent.getChildren().add(label);
        label.setText("Choose an upgrade");
        
        // Parent of power-ups
        HBox powerUpButtonsParent = new HBox();
        parent.getChildren().add(powerUpButtonsParent);
        powerUpButtonsParent.setSpacing(10);
        powerUpButtonsParent.setAlignment(Pos.CENTER);
        
        // Setup power-ups
        record PowerUpInfo(
            PowerUpKind powerUpKind,
            Button button,
            String imageUrl
        ) {
        }
        ArrayList<PowerUpInfo> powerUpInfos = new ArrayList<>() {
            {
                add(new PowerUpInfo(
                    PowerUpKind.BULLET_DAMAGE,
                    new Button(),
                    "/ui/power-ups/bullet-damage.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.BULLET_SPEED,
                    new Button(),
                    "/ui/power-ups/bullet-speed.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.BULLET_MAX_DISTANCE,
                    new Button(),
                    "/ui/power-ups/bullet-max-distance.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.BULLET_PENETRATION,
                    new Button(),
                    "/ui/power-ups/bullet-penetration.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.FIRE_RATE,
                    new Button(),
                    "/ui/power-ups/fire-rate.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.GRENADE_AOE,
                    new Button(),
                    "/ui/power-ups/grenade-aoe.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.RIFLE_ACCURACY,
                    new Button(),
                    "/ui/power-ups/rifle-accuracy.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.SHOTGUN_SPREAD,
                    new Button(),
                    "/ui/power-ups/shotgun-spread.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.MAX_HEALTH,
                    new Button(),
                    "/ui/power-ups/max-health.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.HEALTH_REGEN,
                    new Button(),
                    "/ui/power-ups/health-regen.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.MOVEMENT_SPEED,
                    new Button(),
                    "/ui/power-ups/movement-speed.png"
                ));
                add(new PowerUpInfo(
                    PowerUpKind.DASH_INTERVAL,
                    new Button(),
                    "/ui/power-ups/dash-interval.png"
                ));
            }
        };
        int scaleFactor = 3;
        for (PowerUpInfo powerUpInfo : powerUpInfos) {
            ButtonImageSkin buttonImageSkin = new ButtonImageSkin(
                powerUpInfo.button,
                Common.resampleImage(
                    powerUpInfo.imageUrl,
                    scaleFactor
                )
            );
            powerUpInfo.button.setSkin(buttonImageSkin);
            
            powerUpInfo.button.setOnAction(e -> {
                if (onSelectPowerUp != null) {
                    onSelectPowerUp.call(powerUpInfo.powerUpKind);
                }
            });
        }
        
        // Randomize when shown
        isPowerUpSelectionComponentVisible.addListener(e -> {
            boolean isVisible = isPowerUpSelectionComponentVisible();
            if (!isVisible) return;
            
            powerUpButtonsParent.getChildren().clear();
            final int POOL_SIZE = 4;
            ArrayList<PowerUpInfo> open = new ArrayList<>(
                powerUpInfos
                    .stream()
                    .filter(v -> v.powerUpKind.get().isAllowedToUse())
                    .toList()
            );
            for (int i = 0; i < POOL_SIZE; i++) {
                int rng = (int) Math.round(Math.random() * (open.size() - 1));
                PowerUpInfo powerUpInfo = open.get(rng);
                powerUpButtonsParent.getChildren().add(
                    powerUpInfo.button
                );
                open.remove(powerUpInfo);
            }
        });
    }
    
    public void setPowerUpSelectionComponentVisible(boolean v) {
        isPowerUpSelectionComponentVisible.set(v);
    }
    
    public boolean isPowerUpSelectionComponentVisible() {
        return isPowerUpSelectionComponentVisible.get();
    }
    
    public void setOnSelectPowerUp(PowerUpSelectEvent onSelectPowerUp) {
        this.onSelectPowerUp = onSelectPowerUp;
    }
    
    public Game getGame() {
        return game;
    }
}
