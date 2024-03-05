package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;

public class SpriteAnimation {
    private final HashMap<String, TileLocation[]> registeredAnimations = new HashMap<>();
    private Image spriteSheet;
    private double tileWidth = -1;
    private double tileHeight = -1;
    private int frameAccumulator = 0;
    private int framesElapsed = 0;
    private String currentAnimation;
    private int fps = 12;
    
    private boolean horizontallyFlipped = false;
    private boolean verticallyFlipped = false;
    
    // x, y, w, h in canvas
    private double x = 0;
    private double y = 0;
    private double width = -1;
    private double height = -1;
    
    public SpriteAnimation() {
    }
    
    public void setSpriteSheet(Image spriteSheet) {
        this.spriteSheet = spriteSheet;
    }
    
    public void registerAnimation(String animationName, TileLocation[] tileLocations) {
        registeredAnimations.put(animationName, tileLocations);
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
        ctx.drawImage(
            this.spriteSheet,
            tileLocation.column() * this.tileWidth,
            tileLocation.row() * this.tileHeight,
            this.tileWidth,
            this.tileHeight,
            x - (horizontallyFlipped ? -width / 2 : width / 2),
            y - (verticallyFlipped ? -height / 2 : height / 2),
            horizontallyFlipped ? -width : width,
            verticallyFlipped ? -height : height
        );
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
        this.frameAccumulator = (int) GameUtils.random(0, 10);
    }
    
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }
    
    public void setHorizontallyFlipped(boolean horizontallyFlipped) {
        this.horizontallyFlipped = horizontallyFlipped;
    }
    
    public void setVerticallyFlipped(boolean verticallyFlipped) {
        this.verticallyFlipped = verticallyFlipped;
    }
    
    public void setTileSize(double tileWidth, double tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        if (this.width == -1) this.width = this.tileWidth;
        if (this.height == -1) this.height = this.tileHeight;
    }
    
    public void setAnimation(String animationName) {
        currentAnimation = animationName;
    }
    
    public int getFrameLength(String animationName) {
        TileLocation[] tileLocations = registeredAnimations.get(currentAnimation);
        return tileLocations.length;
    }
    
    public int getCurrentFrameNumber(String animationName) {
        TileLocation[] tileLocations = registeredAnimations.get(currentAnimation);
        return frameAccumulator % tileLocations.length;
    }
    
    public int getFrameAccumulator() {
        return frameAccumulator;
    }
    
    public void setFrameAccumulator(int frameAccumulator) {
        this.frameAccumulator = frameAccumulator;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public double getTileWidth() {
        return tileWidth;
    }
    
    public double getTileHeight() {
        return tileHeight;
    }
    
    public boolean isHorizontallyFlipped() {
        return horizontallyFlipped;
    }
    
    public boolean isVerticallyFlipped() {
        return verticallyFlipped;
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
