package utils;

import event.MouseHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;

public class Parallax extends AnimationLoop {
    private final ArrayList<Layer> layers = new ArrayList<>();
    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();
    private final MouseHandler mouseHandler;
    private Vector velocity = new Vector(10, 5);
    private float acceleration = 0.1f;
    
    public Parallax(Scene scene) {
        this.mouseHandler = new MouseHandler(scene);
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    public void addLayer(int index, Image image) {
        layers.add(index, new Layer(image));
    }
    
    private Vector getComputedDepthVector(int layerIndex) {
        return new Vector(
            (float) (Math.pow(layerIndex + 1, 2) * velocity.getX()),
            (float) (Math.pow(layerIndex + 1, 2) * velocity.getY())
        );
    }
    
    @Override
    public void render() {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            Vector computedDepthVector = getComputedDepthVector(i);
            float offsetX = computedDepthVector.getX() * 2;
            float offsetY = computedDepthVector.getY() * 2;
            ctx.beginPath();
            ctx.drawImage(
                layer.image,
                layer.position.getX() - offsetX / 2,
                layer.position.getY() - offsetY / 2,
                canvas.getWidth() + offsetX,
                canvas.getHeight() + offsetY
            );
            ctx.closePath();
        }
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        float mappedX = GameUtils.map(
            mouseHandler.getPosition().getX(),
            0.0f,
            (float) canvas.getWidth(),
            1,
            -1
        );
        float mappedY = GameUtils.map(
            mouseHandler.getPosition().getY(),
            0.0f,
            (float) canvas.getHeight(),
            1,
            -1
        );
        
        if (Float.isNaN(mappedX) || Float.isNaN(mappedY)) return;
        
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            Vector computedDepthVector = getComputedDepthVector(i);
            layer.position.lerp(
                mappedX * computedDepthVector.getX(),
                mappedY * computedDepthVector.getY(),
                acceleration
            );
        }
    }
    
    public Vector getVelocity() {
        return velocity;
    }
    
    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }
    
    public float getAcceleration() {
        return acceleration;
    }
    
    private static class Layer {
        public final Image image;
        public final Vector position = new Vector();
        
        public Layer(Image image) {
            this.image = image;
        }
    }
}
