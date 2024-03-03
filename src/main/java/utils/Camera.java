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
        this.distance = z;
        this.updateViewport();
    }
    
    public void moveTo(double x, double y) {
        this.position.set(x, y);
        this.updateViewport();
    }
    
    public void moveTo(Vector vector) {
        this.position.set(vector);
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
    
    private static class Viewport {
        public double left = 0;
        public double right = 0;
        public double top = 0;
        public double bottom = 0;
        public double width = 0;
        public double height = 0;
        public double scaleX = 1;
        public double scaleY = 1;
    }
}
