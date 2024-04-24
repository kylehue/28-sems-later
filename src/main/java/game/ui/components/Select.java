package game.ui.components;

import game.Game;
import game.utils.Vector;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

public class Select<T> extends Component {
    private final HashMap<T, ImageButton> options = new HashMap<>();
    private final ArrayList<T> optionsOrdered = new ArrayList<>();
    private final ObjectProperty<T> selectedOption = new SimpleObjectProperty<>();
    private final Vector offset = new Vector();
    private float width = -1;
    private float optionMaxHeight = 0;
    private float spacing = 20;

    public Select() {
        // Fix component bounds on window resize
        fixBounds();
        Game.canvas.widthProperty().addListener(e -> {
            fixBounds();
        });
        Game.canvas.heightProperty().addListener(e -> {
            fixBounds();
        });
    }
    
    public void clearOptions() {
        optionsOrdered.clear();
        options.clear();
        selectedOption.set(null);
        optionMaxHeight = 0;
    }
    
    public void addOption(T key, String imageUrl) {
        ImageButton imageButton = new ImageButton(imageUrl);
        addOption(key, imageButton);
    }
    
    public void addOption(T key, ImageButton imageButton) {
        options.put(key, imageButton);
        optionsOrdered.add(key);
        imageButton.setOnClick(() -> {
            selectedOption.set(key);
        });
        fixBounds();
    }
    
    protected void fixBounds() {
        int optionsLength = optionsOrdered.size();
        if (optionsLength == 0) return;
        optionMaxHeight = 0;

        float spacing = getSpacing();
        float desiredWidth = getWidth();

        int index = 0;
        for (T key : optionsOrdered) {
            ImageButton imageButton = options.get(key);
            float properWidth = desiredWidth / optionsLength - spacing + spacing / optionsLength;
            imageButton.setWidth(properWidth);
            float properHeight = imageButton.getHeight();
            optionMaxHeight = Math.max(properHeight, optionMaxHeight);
            Vector properPosition = new Vector(
                offset.getX() + index * properWidth + index * spacing,
                offset.getY()
            );
            
            imageButton.getPosition().set(properPosition);
            index++;
        }
        subFixBounds();
    }
    
    // to be overridden
    protected void subFixBounds() {
    
    }
    
    public void setSpacing(float spacing) {
        if (this.spacing == spacing) return;
        this.spacing = spacing;
        fixBounds();
    }
    
    public void setWidth(float width) {
        if (this.width == width) return;
        this.width = width;
        fixBounds();
    }
    
    public float getWidth() {
        Canvas canvas = Game.canvas;
        return width == -1 ? (float) canvas.getWidth() : width;
    }
    
    public ObjectProperty<T> selectedOptionProperty() {
        return selectedOption;
    }
    
    public T getSelectedOption() {
        return selectedOption.get();
    }
    
    public HashMap<T, ImageButton> getOptions() {
        return options;
    }
    
    public float getSpacing() {
        return spacing;
    }
    
    public float getOptionMaxHeight() {
        return optionMaxHeight;
    }

    public Vector getOffset() {
        return offset;
    }

    @Override
    public void render(GraphicsContext ctx) {
        for (T key : optionsOrdered) {
            ImageButton imageButton = options.get(key);
            imageButton.render(ctx);
            imageButton.setVisible(isVisible());
        }
        
        subRender(ctx);
    }
    
    public void subRender(GraphicsContext ctx) {
    
    }
}
