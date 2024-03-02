package utils;

public class Vector {
    private double x = 0;
    private double y = 0;
    
    public Vector() {
    }
    
    public Vector(double x, double y) {
        this.set(x, y);
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public Vector setX(double x) {
        this.x = x;
        return this;
    }
    
    public Vector setY(double y) {
        this.y = y;
        return this;
    }
    
    public Vector set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }
    
    public Vector set(Vector vector) {
        this.set(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector add(double x, double y) {
        this.x += x;
        this.y += y;
        return this;
    }
    
    public Vector add(Vector vector) {
        this.add(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector subtract(double x, double y) {
        this.x -= x;
        this.y -= y;
        return this;
    }
    
    public Vector subtract(Vector vector) {
        this.subtract(vector.getX(), vector.getY());
        return this;
    }
    
    public Vector scale(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    
    public double getAngle() {
        return Math.atan2(this.getY(), this.getX());
    }
    
    public double getAngle(double x, double y) {
        return Math.atan2(y - this.getY(), x - this.getX());
    }
    
    public double getAngle(Vector vector) {
        return this.getAngle(vector.getX(), vector.getY());
    }
    
    public void setMagnitude(double magnitude) {
        double currentMagnitude = this.getMagnitude();
        currentMagnitude = currentMagnitude == 0 ? 0.00001 : currentMagnitude;
        this.x /= currentMagnitude * magnitude;
        this.y /= currentMagnitude * magnitude;
    }
    
    public double getMagnitude() {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
    }
    
    public void normalize() {
        this.setMagnitude(1);
    }
    
    public double getDistanceFrom(double x, double y) {
        return Math.sqrt(Math.pow(x - this.getX(), 2) + Math.pow(y - this.getY(), 2));
    }
    
    public double getDistanceFrom(Vector vector) {
        return this.getDistanceFrom(vector.getX(), vector.getY());
    }
    
    public Vector clone() {
        return new Vector(this.x, this.y);
    }
    
    @Override
    public String toString() {
        return "Vector{" +
            "x=" + x +
            ", y=" + y +
            '}';
    }
}
