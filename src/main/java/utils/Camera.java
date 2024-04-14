package utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

// https://github.com/robashton/camera/blob/master/camera.js
public class Camera {
    private final GraphicsContext context;
    private float distance = 1000;
    private Vector position = new Vector();
    private float fieldOfView = (float) (Math.PI / 4f);
    private float aspectRatio = 0;
    private Viewport viewport = new Viewport();
    
    public Camera(GraphicsContext context) {
        this.context = context;
        this.updateViewport();
    }
    
    public void begin() {
        this.context.save();
        this.applyScale();
        this.applyTranslation();
    }
    
    public void end() {
        this.context.restore();
    }
    
    public void applyScale() {
        this.context.scale(this.viewport.scaleX, this.viewport.scaleY);
    }
    
    public void applyTranslation() {
        this.context.translate(-this.viewport.left, -this.viewport.top);
    }
    
    public void updateViewport() {
        Canvas canvas = this.context.getCanvas();
        this.aspectRatio = (float) (canvas.getWidth() / canvas.getHeight());
        this.viewport.width = (float) (this.distance * Math.tan(this.fieldOfView));
        this.viewport.height = this.viewport.width / this.aspectRatio;
        this.viewport.left = (float) (this.position.getX() - (this.viewport.width / 2.0));
        this.viewport.top = (float) (this.position.getY() - (this.viewport.height / 2.0));
        this.viewport.right = this.viewport.left + this.viewport.width;
        this.viewport.bottom = this.viewport.top + this.viewport.height;
        this.viewport.scaleX = (float) (canvas.getWidth() / this.viewport.width);
        this.viewport.scaleY = (float) (canvas.getHeight() / this.viewport.height);
    }
    
    public void zoomTo(float z) {
        this.distance = GameUtils.lerp(this.distance, z, 0.15f);
        this.updateViewport();
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public void moveTo(float x, float y) {
        this.position.lerp(x, y, 0.5f);
        this.updateViewport();
    }
    
    public void moveTo(Vector vector) {
        this.moveTo(vector.getX(), vector.getY());
        this.updateViewport();
    }
    
    public Vector screenToWorld(float x, float y) {
        return new Vector(
            (x / this.viewport.scaleX) + this.viewport.left,
            (y / this.viewport.scaleY) + this.viewport.top
        );
    }
    
    public Vector screenToWorld(Vector vector) {
        return this.screenToWorld(vector.getX(), vector.getY());
    }
    
    public Vector worldToScreen(float x, float y) {
        return new Vector(
            (x - this.viewport.left) * (this.viewport.scaleX),
            (y - this.viewport.top) * (this.viewport.scaleY)
        );
    }
    
    public Vector worldToScreen(Vector vector) {
        return this.worldToScreen(vector.getX(), vector.getY());
    }
    
    public Viewport getViewport() {
        return viewport;
    }
    
    public boolean isInViewport(float x, float y, float offset) {
        return x >= this.viewport.left - offset &&
            x <= this.viewport.right + offset &&
            y >= this.viewport.top - offset &&
            y <= this.viewport.bottom + offset;
    }
    
    public boolean isInViewport(float x, float y) {
        return this.isInViewport(x, y, 0);
    }
    
    public boolean isInViewport(Vector vector, float offset) {
        return this.isInViewport(vector.getX(), vector.getY(), offset);
    }
    
    public boolean isInViewport(Vector vector) {
        return this.isInViewport(vector, 0);
    }
    
    public static class Viewport {
        private float left = 0;
        private float right = 0;
        private float top = 0;
        private float bottom = 0;
        private float width = 0;
        private float height = 0;
        private float scaleX = 1;
        private float scaleY = 1;
        
        public float getLeft() {
            return left;
        }
        
        public float getRight() {
            return right;
        }
        
        public float getTop() {
            return top;
        }
        
        public float getBottom() {
            return bottom;
        }
        
        public float getWidth() {
            return width;
        }
        
        public float getHeight() {
            return height;
        }
        
        public float getScaleX() {
            return scaleX;
        }
        
        public float getScaleY() {
            return scaleY;
        }
    }
}
