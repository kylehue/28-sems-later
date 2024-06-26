package utils;

import event.MouseHandler;
import game.utils.GameLoop;
import game.utils.Common;
import game.utils.Vector;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class Parallax extends GameLoop {
    private final ArrayList<Layer> layers = new ArrayList<>();
    private final Canvas canvas = new Canvas();
    private final GraphicsContext ctx = canvas.getGraphicsContext2D();
    private final MouseHandler mouseHandler = new MouseHandler();
    private final float autoMoveMagnitude = 200;
    private final Vector velocity = new Vector(5f, 5f);
    private final Vector autoMovePosition = new Vector();
    private final Vector autoMoveVelocity = new Vector().randomize(2);
    private float acceleration = 0.1f;
    
    public Parallax(Scene scene) {
        this.mouseHandler.listen(scene);
    }
    
    public void addLayer(int index, Image image) {
        layers.add(index, new Layer(image));
    }
    
    @Override
    public void render(float alpha) {
        ctx.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        ctx.setImageSmoothing(false);
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            Vector computedDepthVector = getComputedDepthVector(i - layers.size());
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
    
    private void handleAutoMove() {
        autoMovePosition.add(autoMoveVelocity);
        if (autoMovePosition.getX() < -autoMoveMagnitude || autoMovePosition.getX() > autoMoveMagnitude) {
            autoMoveVelocity.setX(-autoMoveVelocity.getX());
        }
        
        if (autoMovePosition.getY() < -autoMoveMagnitude || autoMovePosition.getY() > autoMoveMagnitude) {
            autoMoveVelocity.setY(-autoMoveVelocity.getY());
        }
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        this.handleAutoMove();
        float mappedX = Common.map(
            mouseHandler.getPosition().getX() + autoMovePosition.getX(),
            -autoMoveMagnitude,
            (float) canvas.getWidth() + autoMoveMagnitude,
            1,
            -1
        );
        float mappedY = Common.map(
            mouseHandler.getPosition().getY() + autoMovePosition.getY(),
            -autoMoveMagnitude,
            (float) canvas.getHeight() + autoMoveMagnitude,
            1,
            -1
        );
        
        if (Float.isNaN(mappedX) || Float.isNaN(mappedY)) return;
        
        for (int i = layers.size() - 1; i >= 0; i--) {
            Layer layer = layers.get(i);
            Vector computedDepthVector = getComputedDepthVector(i - layers.size());
            layer.position.lerp(
                mappedX * computedDepthVector.getX(),
                mappedY * computedDepthVector.getY(),
                acceleration
            );
        }
    }
    
    public void start() {
        this.startLoop();
    }
    
    public void pause() {
        this.pauseLoop();
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
    
    private Vector getComputedDepthVector(int layerIndex) {
        return new Vector(
            (float) (Math.pow(layerIndex + 1, 2) * velocity.getX()),
            (float) (Math.pow(layerIndex + 1, 2) * velocity.getY())
        );
    }
    
    public Canvas getCanvas() {
        return canvas;
    }
    
    private static class Layer {
        public final Image image;
        public final Vector position = new Vector();
        
        public Layer(Image image) {
            this.image = image;
        }
    }
}
