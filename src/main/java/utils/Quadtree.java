package utils;

import java.util.HashSet;

// https://github.com/timohausmann/quadtree-js/blob/master/quadtree.js
public class Quadtree {
    private final Bounds bounds;
    private int maxObjects = 10;
    private int maxLevels = 10;
    private int level = 0;
    
    private final HashSet<Bounds> objects = new HashSet<>();
    private Subnodes subnodes = null;
    
    public Quadtree(Bounds bounds) {
        this.bounds = bounds;
    }
    
    public Quadtree(Bounds bounds, int maxObjects) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
    }
    
    public Quadtree(Bounds bounds, int maxObjects, int maxLevels) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
        this.maxLevels = maxLevels;
    }
    
    public Quadtree(Bounds bounds, int maxObjects, int maxLevels, int level) {
        this.bounds = bounds;
        this.maxObjects = maxObjects;
        this.maxLevels = maxLevels;
        this.level = level;
    }
    
    /**
     * Splits the node into 4 sub-nodes.
     */
    private void split() {
        int nextLevel = this.level + 1;
        double x = this.bounds.position.getX();
        double y = this.bounds.position.getY();
        double subWidth = this.bounds.width / 2;
        double subHeight = this.bounds.height / 2;
        
        this.subnodes = new Subnodes(
            new Quadtree(
                new Bounds(
                    x,
                    y,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.maxLevels,
                nextLevel
            ),
            new Quadtree(
                new Bounds(
                    x + subWidth,
                    y,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.maxLevels,
                nextLevel
            ),
            new Quadtree(
                new Bounds(
                    x,
                    y + subHeight,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.maxLevels,
                nextLevel
            ),
            new Quadtree(
                new Bounds(
                    x + subWidth,
                    y + subHeight,
                    subWidth,
                    subHeight
                ),
                this.maxObjects,
                this.maxLevels,
                nextLevel
            )
        );
    }
    
    private Quadtree getSubnode(SubnodeLocation subnodeLocation) {
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
    public void insert(Bounds bounds) {
        int i = 0;
        
        // If we have sub-nodes, call insert on matching sub-nodes
        if (this.subnodes != null) {
            HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(bounds);
            for (SubnodeLocation subnodeLocation : subnodeLocations) {
                getSubnode(subnodeLocation).insert(bounds);
            }
            return;
        }
        
        // otherwise, store object here
        this.objects.add(bounds);
        
        // Max objects reached
        if (this.objects.size() > this.maxObjects && this.level < this.maxLevels) {
            // Split if we don't already have sub-nodes
            this.split();
            
            // Add all objects to their corresponding sub-node
            for (Bounds object : this.objects) {
                HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(object);
                for (SubnodeLocation subnodeLocation : subnodeLocations) {
                    getSubnode(subnodeLocation).insert(object);
                }
            }
            
            // Clean up this node
            this.objects.clear();
        }
    }
    
    /**
     * Return all objects that could collide with the given object.
     */
    public HashSet<Bounds> retrieve(Bounds bounds) {
        HashSet<SubnodeLocation> subnodeLocations = this.getNodeLocation(bounds);
        HashSet<Bounds> returnObjects = new HashSet<>(this.objects);
        
        // If we have sub-nodes, retrieve their objects
        if (this.subnodes != null) {
            for (SubnodeLocation subnodeLocation : subnodeLocations) {
                returnObjects.addAll(this.getSubnode(subnodeLocation).retrieve(bounds));
            }
        }
        
        return returnObjects;
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
    
    public static class Subnodes {
        public final Quadtree topLeft;
        public final Quadtree topRight;
        public final Quadtree bottomLeft;
        public final Quadtree bottomRight;
        
        public Subnodes(
            Quadtree topLeft,
            Quadtree topRight,
            Quadtree bottomLeft,
            Quadtree bottomRight
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
}
