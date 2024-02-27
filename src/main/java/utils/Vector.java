package utils;

public class Vector {
    private double x = 0;
    private double y = 0;
    public Vector() {
    
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
}
