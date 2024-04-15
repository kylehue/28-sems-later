package game.map;

import game.colliders.Collider;
import game.colliders.PolygonCollider;
import game.entity.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;
import game.utils.Vector;

public class Material implements Drawable {
    private final Image image;
    private final Vector position = new Vector();
    private final Vector originOffset = new Vector();
    private final Vector renderPositionOffset = new Vector();
    private Collider collider = null;
    private int zIndex = 0;
    private boolean isHorizontallyFlipped = false;
    private boolean isVerticallyFlipped = false;
    private int rotation = 0;
    private int tileSize = 32;
    private boolean isSeeThroughProperty = false;
    
    public enum PositionOrigin {
        TOP,
        LEFT,
        BOTTOM,
        RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        CENTER
    }
    
    public Material(String imageUrl) {
        this.image = Common.loadImage(imageUrl);
    }
    
    public Material(Image image) {
        this.image = image;
    }
    
    public void setSeeThrough(boolean v) {
        isSeeThroughProperty = v;
    }
    
    @Override
    public boolean isSeeThrough() {
        return isSeeThroughProperty;
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
    
    private float[] computePositionOriginScalar(PositionOrigin positionOrigin) {
        float x = 0;
        float y = 0;
        switch (positionOrigin) {
            case TOP -> y = -1;
            case BOTTOM -> y = 1;
            case LEFT -> x = -1;
            case RIGHT -> x = 1;
            case TOP_LEFT -> {
                x = -1;
                y = -1;
            }
            case TOP_RIGHT -> {
                x = 1;
                y = -1;
            }
            case BOTTOM_LEFT -> {
                x = -1;
                y = 1;
            }
            case BOTTOM_RIGHT -> {
                x = 1;
                y = 1;
            }
        }
        
        return new float[]{x, y};
    }
    
    /**
     * Sets origin starting from top-left of the image.
     */
    public void setPositionOrigin(float x, float y) {
        originOffset.set(
            (float) (image.getWidth() / 2) - x,
            (float) (image.getHeight() / 2) - y
        );
    }
    
    public void setPositionOrigin(PositionOrigin positionOrigin) {
        float[] scalar = computePositionOriginScalar(positionOrigin);
        float halfTileSize = (float) tileSize / 2;
        float offsetX = 0;
        float offsetY = 0;
        if (scalar[0] >= 0) offsetX += (float) image.getWidth();
        if (scalar[1] >= 0) offsetY += (float) image.getHeight();
        
        this.setPositionOrigin(
            halfTileSize * -scalar[0] + offsetX,
            halfTileSize * -scalar[1] + offsetY
        );
    }
    
    /**
     * Sets render position starting from top-left of the image.
     */
    public void setRenderPosition(float x, float y) {
        renderPositionOffset.set(
            (float) -(image.getWidth() / 2) + x,
            (float) -(image.getHeight() / 2) + y
        );
    }
    
    public void setRenderPosition(PositionOrigin positionOrigin) {
        float[] scalar = computePositionOriginScalar(positionOrigin);
        float halfTileSize = (float) tileSize / 2;
        this.setRenderPosition(
            halfTileSize + halfTileSize * scalar[0],
            halfTileSize + halfTileSize * scalar[1]
        );
    }
    
    protected Vector getOrigin() {
        float imgWidth = (float) image.getWidth();
        float imgHeight = (float) image.getHeight();
        float x = -imgWidth / 2;
        float y = -imgHeight / 2;
        
        return new Vector(x + originOffset.getX(), y + originOffset.getY());
    }
    
    @Override
    public Vector getRenderPosition() {
        return this.position.clone().add(renderPositionOffset).add(originOffset);
    }
    
    @Override
    public Vector getPosition() {
        return this.position;
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        ctx.save();
        Vector origin = getOrigin();
        ctx.translate(position.getX(), position.getY());
        ctx.scale(isHorizontallyFlipped ? -1 : 1, isVerticallyFlipped ? -1 : 1);
        ctx.rotate(rotation);
        
        // Image
        ctx.drawImage(
            image,
            origin.getX(),
            origin.getY()
        );
        
        // // Bounds
        // ctx.beginPath();
        // ctx.setStroke(Paint.valueOf("red"));
        // ctx.strokeRect(origin.getX(), origin.getY(), image.getWidth(), image.getHeight());
        // ctx.closePath();
        
        // // Origin
        // ctx.beginPath();
        // ctx.setFill(Paint.valueOf("yellow"));
        // ctx.fillOval(
        //     -2,
        //     -2,
        //     4,
        //     4
        // );
        // ctx.closePath();
        
        ctx.restore();
        
        // // Render position
        // ctx.beginPath();
        // ctx.setFill(Paint.valueOf("white"));
        // ctx.fillOval(
        //     getRenderPosition().getX() - 1,
        //     getRenderPosition().getY() - 1,
        //     2,
        //     2
        // );
        // ctx.closePath();
    }
    
    public void fixedUpdate(float deltaTime) {
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
        clone.setSeeThrough(isSeeThrough());
        // clone.setPositionOrigin(this.positionOrigin);
        clone.renderPositionOffset.set(this.renderPositionOffset);
        if (collider != null) clone.setCollider(collider.clone());
        clone.originOffset.set(this.originOffset);
        clone.position.set(this.position);
        return clone;
    }
}
