package game.utils;

import game.World;
import game.colliders.Collider;
import game.colliders.CollisionResolvers;
import game.utils.Bounds;
import game.utils.Common;
import game.utils.Vector;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import utils.Heap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class PathFinder {
    private final int nodeSize;
    private final HashSet<Collider> obstacles = new HashSet<>();
    private Node[][] nodes;
    private int totalWidth = 0;
    private int totalHeight = 0;
    private int gridLengthX = 0;
    private int gridLengthY = 0;
    
    public PathFinder(int nodeSize, int width, int height) {
        this.nodeSize = nodeSize;
        this.totalWidth = width;
        this.totalHeight = height;
        this.gridLengthX = width / nodeSize;
        this.gridLengthY = height / nodeSize;
        this.nodes = new Node[gridLengthX][gridLengthY];
    }
    
    public int getNodeSize() {
        return nodeSize;
    }
    
    public HashSet<Collider> getObstacles() {
        return obstacles;
    }
    
    public ArrayList<Vector> requestPath(Vector start, Vector goal) {
        ArrayList<Vector> path = new ArrayList<>();
        
        Node startNode = getProperNodeIfObstacle(
            getOrCreateNodeFromPosition(start),
            start
        );
        
        Node goalNode = getProperNodeIfObstacle(
            getOrCreateNodeFromPosition(goal),
            goal
        );
        
        // // render start node
        // Node finalStartNode = startNode;
        // World.debugRender.put(startNode.x + "." + startNode.y, ctx -> {
        //     ctx.beginPath();
        //     ctx.setFill(Paint.valueOf("rgba(0, 0, 255, 0.5)"));
        //     ctx.fillRect(finalStartNode.x * nodeSize, finalStartNode.y * nodeSize, nodeSize, nodeSize);
        //     ctx.closePath();
        // });
        //
        // // render goal node
        // Node finalGoalNode = goalNode;
        // World.debugRender.put(goalNode.x + ".." + goalNode.y, ctx -> {
        //     ctx.beginPath();
        //     ctx.setFill(Paint.valueOf(finalGoalNode.isObstacle ? "rgba(255, 0, 255, 0.75)" : "rgba(0, 255, 0, 0.5)"));
        //     ctx.fillRect(finalGoalNode.x * nodeSize, finalGoalNode.y * nodeSize, nodeSize, nodeSize);
        //     ctx.closePath();
        // });
        
        // // render neighbors of goal node
        // ArrayList<Node> neighbors = getNodeNeighbors(goalNode);
        // for (Node node : neighbors) {
        //     World.debugRender.put(node.x + "." + node.y, ctx -> {
        //         ctx.beginPath();
        //         ctx.setFill(Paint.valueOf("rgba(255, 0, 255, 0.5)"));
        //         ctx.fillRect(node.x * nodeSize, node.y * nodeSize, nodeSize, nodeSize);
        //         ctx.closePath();
        //     });
        // }
        
        // // render map
        // for (int x = 0; x < gridLengthX; x++) {
        //     for (int y = 0; y < gridLengthY; y++) {
        //         Node node = getOrCreateNode(x, y);
        //         World.debugRender.put(node.x + ".," + node.y, ctx -> {
        //             ctx.beginPath();
        //             ctx.setFill(Paint.valueOf(node.isObstacle ? "rgba(255, 0, 0, 0.25)" : "rgba(0, 255, 0, 0.25)"));
        //             ctx.fillRect(node.x * nodeSize + 1, node.y * nodeSize + 1, nodeSize - 2, nodeSize - 2);
        //             ctx.closePath();
        //         });
        //     }
        // }
        
        // Perform A* algorithm
        Heap<Node> openNodes = new Heap<>((a, b) -> {
            if (a.fCost == b.fCost) return a.hCost - b.hCost;
            return a.fCost - b.fCost;
        });
        HashSet<Integer> closedNodes = new HashSet<>();
        openNodes.add(startNode);
        
        while (!openNodes.isEmpty()) {
            Node currentNode = openNodes.remove();
            closedNodes.add(currentNode.id);
            
            if (currentNode == goalNode) {
                path = retracePath(startNode, goalNode);
                break;
            }
            
            ArrayList<Node> neighbors = getNodeNeighbors(currentNode);
            for (Node neighborNode : neighbors) {
                if (neighborNode.isObstacle || closedNodes.contains(neighborNode.id)) {
                    continue;
                }
                
                int movementCostToNeighbor = currentNode.gCost + computeNodeDistances(
                    currentNode,
                    neighborNode
                );
                boolean isNeighborInOpen = openNodes.contains(neighborNode);
                if (movementCostToNeighbor < neighborNode.gCost || !isNeighborInOpen) {
                    neighborNode.gCost = movementCostToNeighbor;
                    neighborNode.hCost = computeNodeDistances(neighborNode, goalNode);
                    neighborNode.fCost = neighborNode.gCost + neighborNode.hCost;
                    neighborNode.parent = currentNode;
                    
                    if (!isNeighborInOpen) {
                        openNodes.add(neighborNode);
                    }
                }
            }
        }
        
        return path;
    }
    
    /**
     * If `node` is an obstacle, it computes the last node that is not
     * an obstacle by using the `currentPosition` and `lastPosition` and returns it.
     * If `node` is not an obstacle, it just updates the `lastPosition`.
     */
    private Node getProperNodeIfObstacle(Node node, Vector currentPosition) {
        if (!node.isObstacle) return node;
        
        ArrayList<Node> neighbors = new ArrayList<>(
            getNodeNeighbors(node)
                .stream()
                .filter(v -> !v.isObstacle)
                .toList()
        );
        
        if (neighbors.isEmpty()) {
            return node;
        }
        
        float halfNodeSize = (float) nodeSize / 2;
        neighbors.sort((a, b) -> {
            Vector nodePosA = new Vector(
                a.x * nodeSize + halfNodeSize,
                a.y * nodeSize + halfNodeSize
            );
            int distanceA = (int) nodePosA.getDistanceFrom(
                currentPosition
            );
            Vector nodePosB = new Vector(
                b.x * nodeSize + halfNodeSize,
                b.y * nodeSize + halfNodeSize
            );
            int distanceB = (int) nodePosB.getDistanceFrom(
                currentPosition
            );
            return distanceA - distanceB;
        });
        
        return neighbors.getFirst();
    }
    
    HashSet<String> cachedObstacles = new HashSet<>();
    
    public boolean isObstacle(int x, int y) {
        if (cachedObstacles.contains(x + "." + y)) return true;
        // arbitrary buffer to prevent collision on edge
        float nodeSizeBuffer = 2;
        Bounds bounds = new Bounds(
            x * nodeSize + nodeSizeBuffer / 2,
            y * nodeSize + nodeSizeBuffer / 2,
            nodeSize - nodeSizeBuffer,
            nodeSize - nodeSizeBuffer
        );
        
        for (Collider collider : obstacles) {
            boolean isColliding = collider.isCollidingWith(bounds);
            if (isColliding) {
                // cache if static
                if (collider.isStatic()) {
                    cachedObstacles.add(x + "." + y);
                } else {
                    cachedObstacles.remove(x + "." + y);
                }
                return true;
            }
        }
        
        return false;
    }
    
    public int[] convertWorldPositionToGridPosition(Vector position) {
        float nodeSizeHalf = (float) nodeSize / 2;
        float percentMidX = position.getX() / ((float) totalWidth / 2f);
        float percentMidY = position.getY() / ((float) totalHeight / 2f);
        float percentX = Common.clamp(
            (position.getX() + nodeSizeHalf * percentMidX - nodeSizeHalf) / totalWidth,
            0,
            1
        );
        float percentY = Common.clamp(
            (position.getY() + nodeSizeHalf * percentMidY - nodeSizeHalf) / totalHeight,
            0,
            1
        );
        int gridX = Math.round((gridLengthX - 1) * percentX);
        int gridY = Math.round((gridLengthY - 1) * percentY);
        return new int[]{gridX, gridY};
    }
    
    private Node getOrCreateNodeFromPosition(Vector position) {
        int[] gridPosition = convertWorldPositionToGridPosition(position);
        int gridX = gridPosition[0];
        int gridY = gridPosition[1];
        return getOrCreateNode(gridX, gridY);
    }
    
    private Node getOrCreateNode(int x, int y) {
        // If it exists already, return it
        Node cachedNode = this.nodes[x][y];
        if (cachedNode != null) {
            return cachedNode;
        }
        
        // If it doesn't exist, create it
        Node node = new Node();
        node.x = x;
        node.y = y;
        node.isObstacle = isObstacle(node.x, node.y);
        this.nodes[x][y] = node;
        
        return node;
    }
    
    
    HashMap<Integer, ArrayList<Node>> cachedNeighbors = new HashMap<>();
    
    private ArrayList<Node> getNodeNeighbors(Node node) {
        ArrayList<Node> cached = cachedNeighbors.get(node.id);
        if (cached != null) {
            return cached;
        }
        
        ArrayList<Node> neighbors = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0) continue;
                int possibleX = node.x + x;
                int possibleY = node.y + y;
                
                if (
                    possibleX >= 0 &&
                        possibleX < gridLengthX &&
                        possibleY >= 0 &&
                        possibleY < gridLengthY
                ) {
                    neighbors.add(getOrCreateNode(possibleX, possibleY));
                }
            }
        }
        
        cachedNeighbors.put(node.id, neighbors);
        
        return neighbors;
    }
    
    /**
     * Get distance between 2 nodes.
     */
    private static int computeNodeDistances(Node a, Node b) {
        int distanceX = Math.abs(a.x - b.x);
        int distanceY = Math.abs(a.y - b.y);
        
        if (distanceX > distanceY) return 14 * distanceY + 10 * (distanceX - distanceY);
        return 14 * distanceX + 10 * (distanceY - distanceX);
    }
    
    /**
     * Retrace the correct order of path between 2 nodes.
     */
    private ArrayList<Vector> retracePath(Node startNode, Node goalNode) {
        ArrayList<Vector> path = new ArrayList<>();
        float halfNodeSize = (float) this.nodeSize / 2;
        Node currentNode = goalNode;
        while (currentNode != startNode) {
            path.add(new Vector(
                currentNode.x * this.nodeSize + halfNodeSize,
                currentNode.y * this.nodeSize + halfNodeSize
            ));
            currentNode = currentNode.parent;
        }
        path.add(new Vector(
            startNode.x * this.nodeSize + halfNodeSize,
            startNode.y * this.nodeSize + halfNodeSize
        ));
        
        return path;
    }
    
    private static class Node {
        public final int id = Common.generateId();
        public int x = 0;
        public int y = 0;
        public int fCost = 0;
        public int hCost = 0;
        public int gCost = 0;
        public boolean isObstacle = false;
        public Node parent = null;
    }
}
