package game.utils;

public abstract class Common {
    private static int idAccumulator = 0;
    
    public static int generateId() {
        return (Common.idAccumulator++);
    }
    
    public static float random(float min, float max) {
        return (float) (Math.random() * (max - min) + min);
    }
    
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public static float map(float n, float fromMin, float fromMax, float toMin, float toMax) {
        return (n - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }
    
    public static float lerp(float start, float stop, float weight) {
        return weight * (stop - start) + start;
    }
    
    public static float clamp(float n, float min, float max) {
        return Math.max(min, Math.min(max, n));
    }
    
    public static int cantor(int x, int y) {
        return ((x + y) * (x + y + 1)) / 2 + y;
    }
}
