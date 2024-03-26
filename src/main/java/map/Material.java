package map;

import colliders.Collider;
import entity.Thing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.LayoutUtils;
import utils.Vector;

public class Material implements Thing {
    private final Image image;
    private final Vector position = new Vector();
    private int zIndex = 0;
    private boolean isHorizontallyFlipped = false;
    private boolean isVerticallyFlipped = false;
    private int rotation = 0;
    private PositionOrigin positionOrigin = PositionOrigin.CENTER;
    
    public Material(String imageUrl) {
        this.image = LayoutUtils.loadImage(imageUrl);
    }
    
    public Material(Image image) {
        this.image = image;
    }
    
    public enum PositionOrigin {
        TOP,
        RIGHT,
        BOTTOM,
        LEFT,
        CENTER
    }
    
    public void addCollider(Collider collider) {
    
    }
    
    @Override
    public int getZIndex() {
        return this.zIndex;
    }
    
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    
    public boolean isVerticallyFlipped() {
        return isVerticallyFlipped;
    }
    
    public void setVerticallyFlipped(boolean verticallyFlipped) {
        isVerticallyFlipped = verticallyFlipped;
    }
    
    public boolean isHorizontallyFlipped() {
        return isHorizontallyFlipped;
    }
    
    public void setHorizontallyFlipped(boolean horizontallyFlipped) {
        isHorizontallyFlipped = horizontallyFlipped;
    }
    
    public int getRotation() {
        return rotation;
    }
    
    public void setRotation(int rotationInDegrees) {
        this.rotation = rotationInDegrees;
    }
    
    public PositionOrigin getPositionOrigin() {
        return positionOrigin;
    }
    
    public void setPositionOrigin(PositionOrigin positionOrigin) {
        this.positionOrigin = positionOrigin;
    }
    
    @Override
    public Vector getPosition() {
        return this.position;
    }
    
    @Override
    public void render(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(
            image,
            position.getX(),
            position.getY()
        );
    }
    
    public Material clone() {
        Material clone = new Material(this.image);
        clone.setRotation(this.rotation);
        clone.setZIndex(this.zIndex);
        clone.setHorizontallyFlipped(this.isHorizontallyFlipped);
        clone.setVerticallyFlipped(this.isVerticallyFlipped);
        clone.setPositionOrigin(this.positionOrigin);
        clone.position.set(this.position);
        return clone;
    }
}
