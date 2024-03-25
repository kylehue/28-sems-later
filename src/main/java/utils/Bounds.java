package utils;

public class Bounds {
    private Vector position = new Vector();
    private float width = 0;
    private float height = 0;
    
    public Bounds() {
    
    }
    
    public Bounds(float x, float y, float width, float height) {
        this.position.set(x, y);
        this.width = width;
        this.height = height;
    }
    
    public Vector getPosition() {
        return position;
    }
    
    public void setX(float x) {
        this.position.setX(x);
    }
    
    public void setY(float y) {
        this.position.setY(y);
    }
    
    public void setWidth(float width) {
        this.width = width;
    }
    
    public void setHeight(float height) {
        this.height = height;
    }
    
    public float getX() {
        return position.getX();
    }
    
    public float getY() {
        return position.getY();
    }
    
    public float getWidth() {
        return width;
    }
    
    public float getHeight() {
        return height;
    }
    
    @Override
    public String toString() {
        return "Bounds{" +
            "position=" + position +
            ", width=" + width +
            ", height=" + height +
            '}';
    }
}
