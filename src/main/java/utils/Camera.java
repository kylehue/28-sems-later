package utils;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

// https://github.com/robashton/camera/blob/master/camera.js
public class Camera {
    private final GraphicsContext context;
    private double distance = 1000;
    private Vector position = new Vector();
    private double fieldOfView = Math.PI / 4;
    private double aspectRatio = 0;
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
        this.aspectRatio = canvas.getWidth() / canvas.getHeight();
        this.viewport.width = this.distance * Math.tan(this.fieldOfView);
        this.viewport.height = this.viewport.width / this.aspectRatio;
        this.viewport.left = this.position.getX() - (this.viewport.width / 2.0);
        this.viewport.top = this.position.getY() - (this.viewport.height / 2.0);
        this.viewport.right = this.viewport.left + this.viewport.width;
        this.viewport.bottom = this.viewport.top + this.viewport.height;
        this.viewport.scaleX = canvas.getWidth() / this.viewport.width;
        this.viewport.scaleY = canvas.getHeight() / this.viewport.height;
    }
    
    public void zoomTo(double z) {
        this.distance = GameUtils.lerp(this.distance, z, 0.15);
        this.updateViewport();
    }
    
    public void moveTo(double x, double y) {
        this.position.lerp(x, y, 0.15);
        this.updateViewport();
    }
    
    public void moveTo(Vector vector) {
        this.moveTo(vector.getX(), vector.getY());
        this.updateViewport();
    }
    
    public Vector screenToWorld(double x, double y) {
        return new Vector(
            (x / this.viewport.scaleX) + this.viewport.left,
            (y / this.viewport.scaleY) + this.viewport.top
        );
    }
    
    public Vector screenToWorld(Vector vector) {
        return this.screenToWorld(vector.getX(), vector.getY());
    }
    
    public Vector worldToScreen(double x, double y) {
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
    
    public boolean isInViewport(double x, double y, double offset) {
        return x >= this.viewport.left - offset &&
            x <= this.viewport.right + offset &&
            y >= this.viewport.top - offset &&
            y <= this.viewport.bottom + offset;
    }
    
    public boolean isInViewport(double x, double y) {
        return this.isInViewport(x, y, 0);
    }
    
    public boolean isInViewport(Vector vector, double offset) {
        return this.isInViewport(vector.getX(), vector.getY(), offset);
    }
    
    public boolean isInViewport(Vector vector) {
        return this.isInViewport(vector, 0);
    }
    
    public static class Viewport {
        private double left = 0;
        private double right = 0;
        private double top = 0;
        private double bottom = 0;
        private double width = 0;
        private double height = 0;
        private double scaleX = 1;
        private double scaleY = 1;
        
        public double getLeft() {
            return left;
        }
        
        public double getRight() {
            return right;
        }
        
        public double getTop() {
            return top;
        }
        
        public double getBottom() {
            return bottom;
        }
        
        public double getWidth() {
            return width;
        }
        
        public double getHeight() {
            return height;
        }
        
        public double getScaleX() {
            return scaleX;
        }
        
        public double getScaleY() {
            return scaleY;
        }
    }
}
