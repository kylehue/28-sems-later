package map;

import colliders.Collider;
import colliders.PolygonCollider;
import entity.Thing;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Paint;
import scenes.game.World;
import utils.LayoutUtils;
import utils.Vector;

public class Material implements Thing {
    private final Image image;
    private final Vector position = new Vector();
    private final Vector originOffset = new Vector();
    private final Vector colliderPositionOffset = new Vector();
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
        float halfTileSize = (float) tileSize / 2;
        switch (positionOrigin) {
            case TOP -> originOffset.setY(-halfTileSize);
            case BOTTOM -> originOffset.setY(halfTileSize);
            case LEFT -> originOffset.setX(-halfTileSize);
            case RIGHT -> originOffset.setX(halfTileSize);
        }
    }
    
    public Vector getOriginOffset() {
        return originOffset;
    }
    
    public Vector getColliderPositionOffset() {
        return colliderPositionOffset;
    }
    
    protected Vector getOrigin() {
        float imgWidth = (float) image.getWidth();
        float imgHeight = (float) image.getHeight();
        float x = -imgWidth / 2;
        float y = -imgHeight / 2;
        switch (positionOrigin) {
            case TOP -> y += imgHeight / 2;
            case BOTTOM -> y -= imgHeight / 2;
            case LEFT -> x += imgWidth / 2;
            case RIGHT -> x -= imgWidth / 2;
        }
        
        return new Vector(x + originOffset.getX(), y + originOffset.getY());
    }
    
    @Override
    public Vector getRenderPosition() {
        return this.position.clone().add(originOffset.getX(), originOffset.getY());
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
            if (!collider.isStatic()) {
                position.set(collider.getPosition());
            }
            
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
