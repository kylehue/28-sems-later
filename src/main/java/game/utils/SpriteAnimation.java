package game.utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.HashMap;

public class SpriteAnimation {
    private final HashMap<Object, TileLocation[]> registeredAnimations = new HashMap<>();
    private Image spriteSheet;
    private float tileWidth = -1;
    private float tileHeight = -1;
    private int frameAccumulator = 0;
    private int framesElapsed = 0;
    private Object currentAnimation = null;
    private int fps = 12;
    private float angleInRadians = 0;
    
    private boolean isHorizontallyFlipped = false;
    private boolean isVerticallyFlipped = false;
    
    // x, y, w, h in canvas
    private final Vector position = new Vector();
    private final Vector origin = new Vector();
    private float width = -1;
    private float height = -1;
    
    public SpriteAnimation() {
    }
    
    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
    
    public void registerAnimation(Object animationName, TileLocation[] tileLocations) {
        registeredAnimations.put(animationName, tileLocations);
        if (currentAnimation == null) {
            currentAnimation = animationName;
        }
    }
    
    public void render(GraphicsContext ctx) {
        framesElapsed++;
        if (currentAnimation == null) return;
        TileLocation[] tileLocations = registeredAnimations.get(currentAnimation);
        if (tileLocations == null) {
            throw new Error("The animation '" + currentAnimation + "' does not exist. Please make sure that it's been registered;");
        }
        
        int accumulatorFixed = frameAccumulator % tileLocations.length;
        TileLocation tileLocation = tileLocations[accumulatorFixed];
        
        ctx.save();
        ctx.translate(
            position.getX() - (isHorizontallyFlipped ? -width / 2 : width / 2),
            position.getY() - (isVerticallyFlipped ? -height / 2 : height / 2)
        );
        ctx.rotate(Math.toDegrees(angleInRadians));
        float computedWidth = isHorizontallyFlipped ? -width : width;
        float computedHeight = isVerticallyFlipped ? -height : height;
        ctx.drawImage(
            this.spriteSheet,
            tileLocation.column() * this.tileWidth,
            tileLocation.row() * this.tileHeight,
            this.tileWidth,
            this.tileHeight,
            origin.getX(),
            origin.getY(),
            computedWidth,
            computedHeight
        );
        
        ctx.restore();
    }
    
    public void nextFrame() {
        int targetFrameCount = 60 / fps;
        if (framesElapsed % targetFrameCount != 0) return;
        this.frameAccumulator++;
    }
    
    public void setFPS(int fps) {
        this.fps = fps;
    }
    
    public void randomizeFirstFrame() {
        this.frameAccumulator = (int) Common.random(0, getFrameLength() - 1);
    }
    
    public void setAngleInRadians(float angleInRadians) {
        this.angleInRadians = angleInRadians;
    }
    
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }
    
    public void setHorizontallyFlipped(boolean horizontallyFlipped) {
        this.isHorizontallyFlipped = horizontallyFlipped;
    }
    
    public void setVerticallyFlipped(boolean verticallyFlipped) {
        this.isVerticallyFlipped = verticallyFlipped;
    }
    
    public void setTileSize(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        if (this.width == -1) this.width = this.tileWidth;
        if (this.height == -1) this.height = this.tileHeight;
    }
    
    public void setAnimation(Object animationName) {
        currentAnimation = animationName;
    }
    
    public int getFrameLength() {
        TileLocation[] tileLocations = registeredAnimations.get(currentAnimation);
        return tileLocations.length;
    }
    
    public int getCurrentFrameNumber() {
        TileLocation[] tileLocations = registeredAnimations.get(currentAnimation);
        return frameAccumulator % tileLocations.length;
    }
    
    public int getFrameAccumulator() {
        return frameAccumulator;
    }
    
    public void setFrameAccumulator(int frameAccumulator) {
        this.frameAccumulator = frameAccumulator;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public Vector getOrigin() {
        return origin;
    }
    
    public float getAngleInRadians() {
        return angleInRadians;
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    public float getTileWidth() {
        return tileWidth;
    }
    
    public float getTileHeight() {
        return tileHeight;
    }
    
    public boolean isHorizontallyFlipped() {
        return isHorizontallyFlipped;
    }
    
    public boolean isVerticallyFlipped() {
        return isVerticallyFlipped;
    }
    
    public void resetFrames() {
        this.frameAccumulator = 0;
    }
    
    /**
     * Utility class for defining the tile location of a single frame.
     */
    public record TileLocation(int row, int column) {
        public static TileLocation create(int row, int column) {
            return new TileLocation(row, column);
        }
    }
}
