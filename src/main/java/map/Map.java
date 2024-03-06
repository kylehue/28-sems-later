package map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Map {
    private final HashMap<String, TileLocation> registeredTiles = new HashMap<>();
    private final HashMap<String, Integer> registeredTileAngles = new HashMap<>();
    private Image tileSheet = null;
    private String[][] mapMatrix = {};
    private double tileWidth = 0;
    private double tileHeight = 0;
    private Viewport viewport = null;
    private int renderTileViewportOffsetX = 0;
    private int renderTileViewportOffsetY = 0;
    
    public void setViewport(double top, double bottom, double left, double right) {
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
    
    public void setTileSize(double tileWidth, double tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }
    
    public double getTileWidth() {
        return tileWidth;
    }
    
    public double getTileHeight() {
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
    
    public double getTotalWidth() {
        return (mapMatrix[0] == null ? 0 : mapMatrix[0].length) * this.tileWidth;
    }
    
    public double getTotalHeight() {
        return mapMatrix.length * this.tileHeight;
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
                TileLocation tileLocation = registeredTiles.get(tileId);
                if (tileLocation == null) continue;
                
                int tileAngle = this.registeredTileAngles.get(tileId);
                double x = columnIndex * this.tileWidth - (this.getTotalWidth() / 2);
                double y = rowIndex * this.tileHeight - (this.getTotalHeight() / 2);
                ctx.save();
                ctx.translate(x, y);
                ctx.rotate(tileAngle);
                ctx.beginPath();
                ctx.drawImage(
                    this.tileSheet,
                    tileLocation.column() * this.tileWidth,
                    tileLocation.row() * this.tileHeight,
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
    
    public record Viewport(double top, double bottom, double left, double right) {
    
    }
}
