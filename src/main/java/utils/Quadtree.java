package utils;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

import java.util.HashSet;

// https://github.com/timohausmann/quadtree-js/blob/master/quadtree.js
public class Quadtree<T> {
    private final Bounds bounds;
    private int maxObjects = 10;
    private double minWidth = 100;
    private double minHeight = 100;
    
    private final HashSet<QObject<T>> objects = new HashSet<>();
    private Subnodes<T> subnodes = null;
    
    public Quadtree(Bounds bounds) {
        this.bounds = bounds;
    }
    
    public Quadtree(Bounds bounds, int maxObjects) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
    }
    
    public Quadtree(Bounds bounds, int maxObjects, double minSize) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
        this.minWidth = minSize;
        this.minHeight = minSize;
    }
    
    public Quadtree(Bounds bounds, int maxObjects, double minWidth, double minHeight) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
        this.minWidth = minWidth;
        this.minHeight = minHeight;
    }
    
    public void render(GraphicsContext ctx) {
        for (QObject<T> obj : this.objects) {
            ctx.beginPath();
            ctx.setStroke(Paint.valueOf("blue"));
            ctx.strokeRect(
                obj.position.getX(),
                obj.position.getY(),
                obj.width,
                obj.height
            );
            ctx.closePath();
        }
        
        ctx.beginPath();
        ctx.setStroke(Paint.valueOf("red"));
        ctx.strokeRect(
            this.bounds.position.getX(),
            this.bounds.position.getY(),
            this.bounds.width,
            this.bounds.height
        );
        ctx.closePath();
        
        if (this.subnodes != null) {
            this.subnodes.topLeft.render(ctx);
            this.subnodes.topRight.render(ctx);
            this.subnodes.bottomLeft.render(ctx);
            this.subnodes.bottomRight.render(ctx);
        }
    }
    
    /**
     * Splits the node into 4 sub-nodes.
     */
    private void split() {
        double x = this.bounds.position.getX();
        double y = this.bounds.position.getY();
        double subWidth = this.bounds.width / 2;
        double subHeight = this.bounds.height / 2;
        
        this.subnodes = new Subnodes<T>(
            new Quadtree<T>(
                new Bounds(
                    x,
                    y,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.minWidth,
                this.minHeight
            ),
            new Quadtree<T>(
                new Bounds(
                    x + subWidth,
                    y,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.minWidth,
                this.minHeight
            ),
            new Quadtree<T>(
                new Bounds(
                    x,
                    y + subHeight,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.minWidth,
                this.minHeight
            ),
            new Quadtree<T>(
                new Bounds(
                    x + subWidth,
                    y + subHeight,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.minWidth,
                this.minHeight
            )
        );
    }
    
    private Quadtree<T> getSubnode(SubnodeLocation subnodeLocation) {
        return switch (subnodeLocation) {
            case SubnodeLocation.TOP_LEFT -> this.subnodes.topLeft;
            case SubnodeLocation.TOP_RIGHT -> this.subnodes.topRight;
            case SubnodeLocation.BOTTOM_LEFT -> this.subnodes.bottomLeft;
            case SubnodeLocation.BOTTOM_RIGHT -> this.subnodes.bottomRight;
            default -> throw new Error("Invalid sub-node location.");
        };
    }
    
    /**
     * Determine which node the object belongs to.
     */
    public HashSet<SubnodeLocation> getNodeLocation(Bounds bounds) {
        HashSet<SubnodeLocation> locations = new HashSet<>();
        double verticalMidpoint = this.bounds.position.getX() + (this.bounds.width / 2);
        double horizontalMidpoint = this.bounds.position.getY() + (this.bounds.height / 2);
        
        boolean startIsNorth = bounds.position.getY() < horizontalMidpoint;
        boolean startIsWest = bounds.position.getX() < verticalMidpoint;
        boolean endIsEast = bounds.position.getX() + bounds.width > verticalMidpoint;
        boolean endIsSouth = bounds.position.getY() + bounds.height > horizontalMidpoint;
        
        // Top left
        if (startIsWest && startIsNorth) {
            locations.add(SubnodeLocation.TOP_LEFT);
        }
        
        // Top right
        if (startIsNorth && endIsEast) {
            locations.add(SubnodeLocation.TOP_RIGHT);
        }
        
        // Bottom left
        if (startIsWest && endIsSouth) {
            locations.add(SubnodeLocation.BOTTOM_LEFT);
        }
        
        // Bottom right
        if (endIsEast && endIsSouth) {
            locations.add(SubnodeLocation.BOTTOM_RIGHT);
        }
        
        return locations;
    }
    
    /**
     * Insert the object into the node. If the node
     * exceeds the capacity, it will split and add all
     * objects to their corresponding sub-nodes.
     */
    public void insert(T quadtreeObject, Bounds bounds) {
        // If we have sub-nodes, call insert on matching sub-nodes
        if (this.subnodes != null) {
            HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(bounds);
            for (SubnodeLocation subnodeLocation : subnodeLocations) {
                getSubnode(subnodeLocation).insert(quadtreeObject, bounds);
            }
            return;
        }
        
        // otherwise, store object here
        this.objects.add(
            new QObject<>(quadtreeObject, bounds)
        );
        
        // Max objects reached
        if (
            this.objects.size() > this.maxObjects &&
                this.bounds.width / 2 > this.minWidth &&
                this.bounds.height / 2 > this.minHeight
        ) {
            // Split if we don't already have sub-nodes
            this.split();
            
            // Add all objects to their corresponding sub-node
            for (QObject<T> _quadtreeObject : this.objects) {
                HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(
                    _quadtreeObject
                );
                for (SubnodeLocation subnodeLocation : subnodeLocations) {
                    getSubnode(subnodeLocation).insert(
                        _quadtreeObject.object,
                        _quadtreeObject
                    );
                }
            }
            
            // Clean up this node
            this.objects.clear();
        }
    }
    
    /**
     * Return all objects that could collide with the given object.
     */
    public HashSet<QObject<T>> retrieve(Bounds bounds) {
        HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(bounds);
        HashSet<QObject<T>> returnObjects = new HashSet<>(this.objects);
        
        // If we have sub-nodes, retrieve their objects
        if (this.subnodes != null) {
            for (SubnodeLocation subnodeLocation : subnodeLocations) {
                returnObjects.addAll(this.getSubnode(subnodeLocation).retrieve(bounds));
            }
        }
        
        return returnObjects;
    }
    
    public HashSet<QObject<T>> retrieve(
        double x,
        double y,
        double width,
        double height
    ) {
        return this.retrieve(new Bounds(x, y, width, height));
    }
    
    /**
     * Clear the quadtree.
     */
    public void clear() {
        this.objects.clear();
        if (this.subnodes != null) {
            this.subnodes.topRight.clear();
            this.subnodes.topLeft.clear();
            this.subnodes.bottomLeft.clear();
            this.subnodes.bottomRight.clear();
        }
        this.subnodes = null;
    }
    
    public enum SubnodeLocation {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_RIGHT
    }
    
    public static class Subnodes<T> {
        public final Quadtree<T> topLeft;
        public final Quadtree<T> topRight;
        public final Quadtree<T> bottomLeft;
        public final Quadtree<T> bottomRight;
        
        public Subnodes(
            Quadtree<T> topLeft,
            Quadtree<T> topRight,
            Quadtree<T> bottomLeft,
            Quadtree<T> bottomRight
        ) {
            this.topLeft = topLeft;
            this.topRight = topRight;
            this.bottomLeft = bottomLeft;
            this.bottomRight = bottomRight;
        }
    }
    
    public static class Bounds {
        public final Vector position = new Vector();
        public double width = 0;
        public double height = 0;
        
        public Bounds() {
        }
        
        public Bounds(double x, double y, double width, double height) {
            this.position.set(x, y);
            this.width = width;
            this.height = height;
        }
    }
    
    public static class QObject<T> extends Bounds {
        public final T object;
        
        public QObject(T object, double x, double y, double width, double height) {
            super(x, y, width, height);
            this.object = object;
        }
        
        public QObject(T object, Bounds bounds) {
            super(
                bounds.position.getX(),
                bounds.position.getY(),
                bounds.width,
                bounds.height
            );
            this.object = object;
        }
    }
}
