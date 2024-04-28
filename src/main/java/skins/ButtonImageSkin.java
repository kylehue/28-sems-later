package skins;

import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.control.skin.ButtonSkin;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ButtonImageSkin extends ButtonSkin {
    private final ImageView image;
    private final Effect hoverEffect;
    private final Effect pressEffect;
    
    public ButtonImageSkin(Button button, Image image) {
        super(button);
        
        this.image = new ImageView(image);
        button.setText("");
        button.setGraphic(this.image);
        
        ColorAdjust hoverEffect = new ColorAdjust();
        hoverEffect.setSaturation(0.05);
        hoverEffect.setBrightness(0.1);
        this.hoverEffect = hoverEffect;
        ColorAdjust pressEffect = new ColorAdjust();
        pressEffect.setSaturation(0.05);
        pressEffect.setBrightness(-0.1);
        this.pressEffect = pressEffect;
        
        this.initListeners();
    }
    
    private void initListeners() {
        Button button = getSkinnable();
        
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            if (button.isHover()) {
                this.image.setEffect(hoverEffect);
            }
        });
        
        button.pressedProperty().addListener(e -> {
            if (button.isPressed()) {
                this.image.setEffect(pressEffect);
            } else if (!button.isHover()) {
                this.image.setEffect(null);
            }
        });
        
        button.hoverProperty().addListener(e -> {
            if (button.isHover() && !button.isPressed()) {
                this.image.setEffect(hoverEffect);
            } else if (!button.isPressed()) {
                this.image.setEffect(null);
            }
        });
    }
}
