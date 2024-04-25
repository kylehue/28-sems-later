package game.map;

import game.colliders.Collider;
import game.utils.Vector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Layer {
    private final Map map;
    private final HashMap<String, Material> registeredMaterials = new HashMap<>();
    private final ArrayList<Material> materials = new ArrayList<>();
    private String matrixSeparator = " ";
    private String[][] matrix = {};
    private int totalWidth = 0;
    private int totalHeight = 0;
    private int zIndex = 0;
    private int rowCount = 0;
    private int columnCount = 0;
    
    public Layer(Map map) {
        this.map = map;
    }
    
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }
    
    public void setMatrix(String url, String matrixSeparator) {
        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream(url))
            );
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();
            this.matrix = Arrays.stream(
                content.toString().split("\n")).map(v -> v.split(matrixSeparator)
            ).toArray(String[][]::new);
            this.matrixSeparator = matrixSeparator;
            this.updateTotalSize();
            map.updateTotalSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getTileIdAt(Vector position) {
        int xIndex = (int) (position.getX() / map.tileSize);
        int yIndex = (int) (position.getY() / map.tileSize);
        return matrix[yIndex][xIndex];
    }
    
    public void registerMaterial(String tileId, Material material) {
        registeredMaterials.put(tileId, material);
        material.setTileSize(map.tileSize);
    }
    
    public void distributeMaterials() {
        materials.clear();
        for (int rowIndex = 0; rowIndex < matrix.length; rowIndex++) {
            String[] row = matrix[rowIndex];
            for (int colIndex = 0; colIndex < row.length; colIndex++) {
                String tileId = row[colIndex];
                Material registedMaterial = registeredMaterials.get(tileId);
                if (registedMaterial == null) continue;
                Material material = registedMaterial.clone();
                float x = colIndex * map.tileSize + (float) map.tileSize / 2;
                float y = rowIndex * map.tileSize + (float) map.tileSize / 2;
                material.getPosition().add(x, y);
                if (material.getZIndex() == 0) {
                    material.setZIndex(this.zIndex);
                }
                Collider collider = material.getCollider();
                if (collider != null) {
                    collider.getPosition().add(material.getPosition());
                }
                
                materials.add(material);
            }
        }
    }
    
    public ArrayList<Material> getMaterials() {
        return this.materials;
    }
    
    public void fixedUpdate(float deltaTime) {
        for (Material material : materials) {
            material.fixedUpdate(deltaTime);
        }
    }
    
    private void updateTotalSize() {
        int maxWidth = 0;
        for (int i = 0; i < matrix.length; i++) {
            String[] row = matrix[i];
            maxWidth = Math.max(maxWidth, row.length);
        }
        
        this.totalWidth = maxWidth * map.tileSize;
        this.totalHeight = matrix.length * map.tileSize;
        this.rowCount = matrix.length;
        this.columnCount = maxWidth;
    }
    
    public int getTotalWidth() {
        return this.totalWidth;
    }
    
    public int getTotalHeight() {
        return this.totalHeight;
    }
}
