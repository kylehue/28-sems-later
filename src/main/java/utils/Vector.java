package utils;

public class Vector {
    private double x = 0;
    private double y = 0;
    
    public Vector() {
    }
    
    public Vector(double x, double y) {
        this.setX(x);
        this.setY(y);
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public void add(double x, double y) {
        this.x += x;
        this.y += y;
    }
    
    public void add(Vector vector) {
        this.x += vector.getX();
        this.y += vector.getY();
    }
    
    public void scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }
    
    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
    
    public void setMagnitude(double magnitude) {
        double currentMagnitude = this.getMagnitude();
        currentMagnitude = currentMagnitude == 0 ? 0.000001 : currentMagnitude; // limit
        this.x /= currentMagnitude * magnitude;
        this.y /= currentMagnitude * magnitude;
    }
    
    public void normalize() {
        this.setMagnitude(1);
    }
    
    public Vector clone() {
        return new Vector(this.x, this.y);
    }
}
