package map;

import colliders.Collider;
import colliders.PolygonCollider;
import entity.Thing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.LayoutUtils;
import utils.Vector;

public class Material implements Thing {
    private final Image image;
    private final Vector position = new Vector();
    private final Vector originOffset = new Vector();
    private Collider collider = null;
    private int zIndex = 0;
    private boolean isHorizontallyFlipped = false;
    private boolean isVerticallyFlipped = false;
    private int rotation = 0;
    private PositionOrigin positionOrigin = PositionOrigin.CENTER;
    private int tileSize = 32;
    
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
    
    public Image getImage() {
        return image;
    }
    
    public int getTileSize() {
        return tileSize;
    }
    
    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }
    
    public Collider getCollider() {
        return collider;
    }
    
    public void setCollider(Collider collider) {
        this.collider = collider;
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
    
    public Vector getOriginOffset() {
        return originOffset;
    }
    
    protected Vector getOrigin() {
        float halfTileSize = (float) tileSize / 2;
        float imgWidth = (float) image.getWidth();
        float imgHeight = (float) image.getHeight();
        float x = -imgWidth / 2;
        float y = -imgHeight / 2;
        switch (positionOrigin) {
            case TOP -> y += halfTileSize;
            case BOTTOM -> y -= halfTileSize;
            case LEFT -> x += halfTileSize;
            case RIGHT -> x -= halfTileSize;
        }
        
        return new Vector(x + originOffset.getX(), y + originOffset.getY());
    }
    
    @Override
    public Vector getPosition() {
        return this.position;
    }
    
    @Override
    public void render(GraphicsContext ctx) {
        ctx.save();
        Vector origin = getOrigin();
        ctx.translate(position.getX(), position.getY());
        ctx.scale(isHorizontallyFlipped ? -1 : 1, isVerticallyFlipped ? -1 : 1);
        ctx.rotate(rotation);
        ctx.drawImage(
            image,
            origin.getX(),
            origin.getY()
        );
        ctx.restore();
    }
    
    public void update(float deltaTime) {
        if (collider != null) {
            position.set(collider.getPosition());
            
            if (collider instanceof PolygonCollider polygonCollider) {
                polygonCollider.setAngle((float) Math.toRadians(rotation));
            }
        }
    }
    
    public Material clone() {
        Material clone = new Material(this.image);
        clone.setTileSize(this.tileSize);
        clone.setRotation(this.rotation);
        clone.setZIndex(this.zIndex);
        clone.setHorizontallyFlipped(this.isHorizontallyFlipped);
        clone.setVerticallyFlipped(this.isVerticallyFlipped);
        clone.setPositionOrigin(this.positionOrigin);
        if (collider != null) clone.setCollider(collider.clone());
        clone.originOffset.set(this.originOffset);
        clone.position.set(this.position);
        return clone;
    }
}
