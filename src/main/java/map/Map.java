package map;

import colliders.Collider;
import colliders.ColliderWorld;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Quadtree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Map {
    private final HashMap<String, TileLocation> registeredTiles = new HashMap<>();
    private final HashMap<String, Integer> registeredTileAngles = new HashMap<>();
    private final HashMap<String, ArrayList<Collider>> registeredTileColliders = new HashMap<>();
    private final HashSet<Collider> tileColliders = new HashSet<>();
    private Image tileSheet = null;
    private String[][] mapMatrix = {};
    private float tileWidth = 0;
    private float tileHeight = 0;
    private Viewport viewport = null;
    private int renderTileViewportOffsetX = 0;
    private int renderTileViewportOffsetY = 0;
    
    public void registerColliderToTile(String tileId, Collider collider) {
        if (!registeredTiles.containsKey(tileId)) {
            throw new Error("Tile #" + tileId + " is not found. Please make sure that you have registered this tile using registerTile().");
        }
        
        ArrayList<Collider> colliders = registeredTileColliders.computeIfAbsent(tileId, k -> new ArrayList<>());
        
        collider.setStatic(true);
        collider.setGroup("tiles");
        colliders.add(collider);
    }
    
    public void putCollidersInQuadtree(Quadtree<Collider> quadtree) {
        for (Collider collider : tileColliders) {
            quadtree.insert(
                collider,
                new Quadtree.Bounds(
                    collider.getPosition().getX() - this.getTileWidth() / 2,
                    collider.getPosition().getY() - this.getTileHeight() / 2,
                    this.getTileWidth(),
                    this.getTileHeight()
                )
            );
        }
    }
    
    public void initializeColliders(ColliderWorld colliderWorld) {
        for (
            int rowIndex = 0;
            rowIndex < mapMatrix.length;
            rowIndex++
        ) {
            String[] tilesRow = mapMatrix[rowIndex];
            for (
                int columnIndex = 0;
                columnIndex < tilesRow.length;
                columnIndex++
            ) {
                String tileId = tilesRow[columnIndex];
                ArrayList<Collider> colliders = registeredTileColliders.get(tileId);
                if (colliders == null || colliders.isEmpty()) continue;
                
                int tileAngle = this.registeredTileAngles.get(tileId);
                float x = getTileX(columnIndex);
                float y = getTileY(rowIndex);
                for (Collider _collider : colliders) {
                    Collider collider = _collider.clone();
                    collider.getPosition().set(x, y);
                    colliderWorld.addCollider(collider);
                    tileColliders.add(collider);
                }
            }
        }
    }
    
    public void setViewport(float top, float bottom, float left, float right) {
        this.viewport = new Viewport(top, bottom, left, right);
    }
    
    public Viewport getViewport() {
        return viewport;
    }
    
    public void setRenderTileViewportOffset(int x, int y) {
        this.renderTileViewportOffsetX = x;
        this.renderTileViewportOffsetY = y;
    }
    
    public void setTileSheet(Image tileSheet) {
        this.tileSheet = tileSheet;
    }
    
    public void setTileSize(float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }
    
    public float getTileWidth() {
        return tileWidth;
    }
    
    public float getTileHeight() {
        return tileHeight;
    }
    
    public void registerTile(String tileId, TileLocation tileLocation) {
        registeredTiles.put(tileId, tileLocation);
        this.registeredTileAngles.put(tileId, 0);
    }
    
    public void registerTile(String tileId, TileLocation tileLocation, int angleInDegrees) {
        this.registerTile(tileId, tileLocation);
        this.registeredTileAngles.put(tileId, angleInDegrees);
    }
    
    public float getTotalWidth() {
        return (mapMatrix[0] == null ? 0 : mapMatrix[0].length) * this.tileWidth;
    }
    
    public float getTotalHeight() {
        return mapMatrix.length * this.tileHeight;
    }
    
    private float getTileX(int columnIndex) {
        return columnIndex * this.tileWidth - (this.getTotalWidth() / 2);
    }
    
    private float getTileY(int rowIndex) {
        return rowIndex * this.tileHeight - (this.getTotalHeight() / 2);
    }
    
    public void render(GraphicsContext ctx) {
        if (this.tileSheet == null) {
            throw new Error("Tile sheet is not found. Please make sure that you have set the tile sheet using setTileSheet().");
        }
        
        // limit render based on viewport
        int rowIndexStart = 0;
        int rowIndexEnd = 0;
        int columnIndexStart = 0;
        int columnIndexEnd = 0;
        if (viewport != null) {
            rowIndexStart = (int) ((viewport.top() + this.getTotalHeight()
                / 2) / this.tileHeight) + 2 - renderTileViewportOffsetY;
            rowIndexEnd = (int) ((viewport.bottom() + this.getTotalHeight()
                / 2) / this.tileHeight) + renderTileViewportOffsetY;
            columnIndexStart = (int) ((viewport.left() + this.getTotalWidth()
                / 2) / this.tileWidth) + 2 - renderTileViewportOffsetX;
            columnIndexEnd = (int) ((viewport.right() + this.getTotalWidth()
                / 2) / this.tileWidth) + renderTileViewportOffsetX;
        }
        
        // render each tile
        for (
            int rowIndex = Math.max(0, rowIndexStart);
            rowIndex < Math.min(mapMatrix.length, rowIndexEnd);
            rowIndex++
        ) {
            String[] tilesRow = mapMatrix[rowIndex];
            
            for (
                int columnIndex = Math.max(0, columnIndexStart);
                columnIndex < Math.min(tilesRow.length, columnIndexEnd);
                columnIndex++
            ) {
                String tileId = tilesRow[columnIndex];
                TileLocation tileLocationFromSprite = registeredTiles.get(tileId);
                if (tileLocationFromSprite == null) continue;
                
                int tileAngle = this.registeredTileAngles.get(tileId);
                float x = getTileX(columnIndex);
                float y = getTileY(rowIndex);
                ctx.save();
                ctx.translate(x, y);
                ctx.rotate(tileAngle);
                ctx.beginPath();
                ctx.drawImage(
                    this.tileSheet,
                    tileLocationFromSprite.column() * this.tileWidth,
                    tileLocationFromSprite.row() * this.tileHeight,
                    this.tileWidth,
                    this.tileHeight,
                    -this.tileWidth / 2,
                    -this.tileHeight / 2,
                    this.tileWidth,
                    this.tileHeight
                );
                ctx.closePath();
                ctx.restore();
            }
        }
    }
    
    public void setMapMatrix(String[][] mapMatrix) {
        this.mapMatrix = mapMatrix;
    }
    
    public void setMapMatrix(String mapMatrixString, String separator) {
        this.mapMatrix = Map.parseStringMatrix(mapMatrixString, separator);
    }
    
    public static String[][] parseStringMatrix(String stringMatrix, String separator) {
        String[] rows = stringMatrix.split("\n");
        ArrayList<String[]> matrix = new ArrayList<>();
        for (String row : rows) {
            matrix.add(row.split(separator));
        }
        
        return matrix.toArray(new String[0][0]);
    }
    
    /**
     * Utility class for defining the tile location of a tile.
     */
    public record TileLocation(int row, int column) {
        public static TileLocation create(int row, int column) {
            return new TileLocation(row, column);
        }
    }
    
    public record Viewport(float top, float bottom, float left, float right) {
    
    }
}
