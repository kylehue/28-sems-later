package game.utils;

public class Vector {
    private float x = 0;
    private float y = 0;
    
    public Vector() {
    }
    
    public Vector(float x, float y) {
        this.set(x, y);
    }
    
    public float getX() {
        return this.x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public Vector setX(float x) {
        this.x = x;
        return this;
    }
    
    public Vector setY(float y) {
        this.y = y;
        return this;
    }
    
    public Vector set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector set(Vector vector) {
        this.set(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector addX(float x) {
        this.x += x;
        return this;
    }
    
    public Vector addY(float y) {
        this.y += y;
        return this;
    }
    
    public Vector add(float x, float y) {
        this.addX(x);
        this.addY(y);
        return this;
    }
    
    public Vector add(Vector vector) {
        this.add(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector subtractX(float x) {
        this.x -= x;
        return this;
    }
    
    public Vector subtractY(float y) {
        this.y -= y;
        return this;
    }
    
    public Vector subtract(float x, float y) {
        this.subtractX(x);
        this.subtractY(y);
        return this;
    }
    
    public Vector subtract(Vector vector) {
        this.subtract(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector scale(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    
    public Vector divide(float divisor) {
        this.x /= divisor;
        this.y /= divisor;
        return this;
    }
    
    public float getAngle() {
        return (float) Math.atan2(this.getY(), this.getX());
    }
    
    public float getAngle(float x, float y) {
        return (float) Math.atan2(y - this.getY(), x - this.getX());
    }
    
    public float getAngle(Vector vector) {
        return this.getAngle(vector.getX(), vector.getY());
    }
    
    public Vector setMagnitude(float magnitude) {
        float currentMagnitude = this.getMagnitude();
        if (currentMagnitude == 0) {
            this.x = magnitude;
            this.y = 0;
        } else {
            float scaleFactor = magnitude / currentMagnitude;
            this.x *= scaleFactor;
            this.y *= scaleFactor;
        }
        return this;
    }
    
    public float getMagnitude() {
        return (float) Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
    
    public Vector normalize() {
        return this.setMagnitude(1);
    }
    
    public float getDistanceFrom(float x, float y) {
        return (float) Math.sqrt(Math.pow(x - this.getX(), 2) + Math.pow(y - this.getY(), 2));
    }
    
    public float getDistanceFrom(Vector vector) {
        return this.getDistanceFrom(vector.getX(), vector.getY());
    }
    
    public Vector lerpX(float x, float weight) {
        this.x = weight * (x - this.x) + this.x;
        return this;
    }
    
    public Vector lerpY(float y, float weight) {
        this.y = weight * (y - this.y) + this.y;
        return this;
    }
    
    public Vector lerp(float x, float y, float weightX, float weightY) {
        this.lerpX(x, weightX);
        this.lerpY(y, weightY);
        return this;
    }
    
    public Vector lerp(float x, float y, float weight) {
        return this.lerp(x, y, weight, weight);
    }
    
    public Vector lerp(Vector vector, float weight) {
        return this.lerp(vector.getX(), vector.getY(), weight);
    }
    
    public Vector lerp(Vector vector, float weightX, float weightY) {
        return this.lerp(vector.getX(), vector.getY(), weightX, weightY);
    }
    
    public float dot(float x, float y) {
        return this.x * x + this.y * y;
    }
    
    public float dot(Vector vector) {
        return this.dot(vector.getX(), vector.getY());
    }
    
    public Vector normal() {
        float tempX = this.x;
        this.x = -this.y;
        this.y = tempX;
        return this;
    }
    
    public Vector limit(float n) {
        if (this.getMagnitude() > n) {
            this.setMagnitude(n);
        }
        return this;
    }
    
    public Vector clone() {
        return new Vector(this.x, this.y);
    }
    
    public Vector randomize(float magnitude) {
        return this.set(
            (float) (Math.random() - 0.5),
            (float) (Math.random() - 0.5)
        ).setMagnitude(magnitude);
    }
    
    @Override
    public String toString() {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
