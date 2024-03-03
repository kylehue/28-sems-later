package utils;

public abstract class GameUtils {
    private static int idAccumulator = 0;
    
    public static String generateId() {
        return "#" + (GameUtils.idAccumulator++);
    }
    
    public static double random(double min, double max) {
        return Math.random() * (max - min) + min;
    }
    
    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
    
    public static double map(double n, double fromMin, double fromMax, double toMin, double toMax) {
        return (n - fromMin) / (fromMax - fromMin) * (toMax - toMin) + toMin;
    }
}
