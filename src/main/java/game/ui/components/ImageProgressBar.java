package game.ui.components;

import game.ui.UI;
import game.utils.Vector;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public class ImageProgressBar extends Component {
    private final Image baseImage;
    private final Image barImage;
    private final Vector position = new Vector();
    private final FloatProperty currentValue = new SimpleFloatProperty();
    private final FloatProperty maxValue = new SimpleFloatProperty();
    private float width = 0;
    private float height = 0;
    
    public ImageProgressBar(String baseImageUrl, String barImageUrl) {
        baseImage = Common.loadImage(baseImageUrl);
        barImage = Common.loadImage(barImageUrl);
        width = (float) baseImage.getWidth();
        height = (float) baseImage.getHeight();
    }
    
    public void setWidth(float width) {
        this.width = width;
        
        // preserve ratio
        this.height = (float) ((width * baseImage.getHeight()) / baseImage.getWidth());
    }
    
    public void setHeight(float height) {
        this.height = height;
        
        // preserve ratio
        this.width = (float) ((height * baseImage.getWidth()) / baseImage.getHeight());
    }
    
    public void setCurrentValue(float currentValue) {
        this.currentValue.set(currentValue);
    }
    
    public void setMaxValue(float maxValue) {
        this.maxValue.set(maxValue);
    }
    
    public float getCurrentValue() {
        return currentValue.get();
    }
    
    public FloatProperty currentValueProperty() {
        return currentValue;
    }
    
    public float getMaxValue() {
        return maxValue.get();
    }
    
    public FloatProperty maxValueProperty() {
        return maxValue;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public float getPercentage() {
        return getCurrentValue() / getMaxValue();
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        float percentage = getPercentage();
        ctx.drawImage(
            baseImage,
            position.getX(),
            position.getY(),
            width,
            height
        );
        ctx.drawImage(
            barImage,
            0,
            0,
            barImage.getWidth() * percentage,
            barImage.getHeight(),
            position.getX(),
            position.getY(),
            width * percentage,
            height
        );
    }
}
