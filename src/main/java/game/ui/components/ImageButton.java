package game.ui.components;

import game.Game;
import game.ui.UI;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import utils.Common;

public class ImageButton extends Component {
    private final Image image;
    private final Vector position = new Vector();
    private float width = -1;
    private float height = -1;
    private Effect hoverEffect;
    private Effect pressEffect;
    private Runnable onClick = null;
    
    public ImageButton(String imageUrl) {
        this.image = Common.loadImage(imageUrl);
        setWidth((float) image.getWidth());
        setHeight((float) image.getHeight());
        
        // Setup effects
        ColorAdjust hoverEffect = new ColorAdjust();
        hoverEffect.setSaturation(0.05);
        hoverEffect.setContrast(0.1);
        hoverEffect.setBrightness(0.2);
        this.hoverEffect = hoverEffect;
        ColorAdjust pressEffect = new ColorAdjust();
        pressEffect.setSaturation(0.05);
        pressEffect.setContrast(0.1);
        pressEffect.setBrightness(-0.1);
        this.pressEffect = pressEffect;
        
        Game.mouseHandler.mouseLeftPressedProperty().addListener((e) -> {
            if (!isVisible) return;
            if (!Game.mouseHandler.isMouseLeftPressed()) return;
            if (onClick != null && isMouseOver()) {
                onClick.run();
            }
        });
    }
    
    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }
    
    private boolean isMouseOver() {
        Vector mouse = Game.mouseHandler.getPosition();
        return mouse.getX() >= position.getX() && mouse.getX() <= position.getX() + width && mouse.getY() >= position.getY() && mouse.getY() <= position.getY() + height;
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        if (!isVisible) return;
        boolean isMouseOver = isMouseOver();
        boolean isMousePressed = Game.mouseHandler.isMouseLeftPressed();
        
        if (isMouseOver) {
            ctx.save();
            ctx.setEffect(isMousePressed ? pressEffect : hoverEffect);
        }
        
        ctx.beginPath();
        ctx.drawImage(
            image,
            position.getX(),
            position.getY(),
            width,
            height
        );
        ctx.closePath();
        
        if (isMouseOver) {
            ctx.restore();
        }
    }
    
    public void setWidth(float width) {
        this.width = width;
        
        // preserve ratio
        if (width > 0 && height > 0) {
            float aspectRatio = (float) (image.getWidth() / image.getHeight());
            this.height = width / aspectRatio;
        }
    }
    
    public void setHeight(float height) {
        this.height = height;
        
        // preserve ratio
        if (width > 0 && height > 0) {
            float aspectRatio = (float) (image.getWidth() / image.getHeight());
            this.width = height * aspectRatio;
        }
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public Image getImage() {
        return image;
    }
    
    public Vector getPosition() {
        return position;
    }
}
