package map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Map {
    private final HashMap<String, TileLocation> registeredTiles = new HashMap<>();
    private Image tileSheet = null;
    private String[][] mapMatrix = {};
    private double tileWidth = 0;
    private double tileHeight = 0;
    
    public void setTileSheet(Image tileSheet) {
        this.tileSheet = tileSheet;
    }
    
    public void setTileSize(double tileWidth, double tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }
    
    public void registerTile(String tileId, TileLocation tileLocation) {
        registeredTiles.put(tileId, tileLocation);
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
        
        for (int rowIndex = 0; rowIndex < mapMatrix.length; rowIndex++) {
            String[] tilesRow = mapMatrix[rowIndex];
            for (int columnIndex = 0; columnIndex < tilesRow.length; columnIndex++) {
                String tileId = tilesRow[columnIndex];
                TileLocation tileLocation = registeredTiles.get(tileId);
                if (tileLocation == null) continue;
                
                ctx.beginPath();
                ctx.drawImage(
                    this.tileSheet,
                    tileLocation.column() * this.tileWidth,
                    tileLocation.row() * this.tileHeight,
                    this.tileWidth,
                    this.tileHeight,
                    columnIndex * this.tileWidth - (this.getTotalWidth() / 2),
                    rowIndex * this.tileHeight - (this.getTotalHeight() / 2),
                    this.tileWidth,
                    this.tileHeight
                );
                ctx.closePath();
            }
        }
    }
    
    public void setMapMatrix(String[][] mapMatrix) {
        this.mapMatrix = mapMatrix;
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
}
