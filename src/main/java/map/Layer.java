package map;

import colliders.Collider;
import javafx.scene.canvas.GraphicsContext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class Layer {
    private final Map map;
    private final HashMap<String, Material> registeredMaterials = new HashMap<>();
    private final ArrayList<Material> materials = new ArrayList<>();
    private String matrixSeparator = " ";
    private String matrix = "";
    private int totalWidth = 0;
    private int totalHeight = 0;
    
    public Layer(Map map) {
        this.map = map;
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
            this.matrix = content.toString();
            this.matrixSeparator = matrixSeparator;
            this.updateTotalSize();
            map.updateTotalSize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void registerMaterial(String tileId, Material material) {
        registeredMaterials.put(tileId, material);
        material.setTileSize(map.tileSize);
    }
    
    public void distributeMaterials() {
        materials.clear();
        String[] rows = matrix.split("\n");
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            String[] row = rows[rowIndex].split(matrixSeparator);
            for (int colIndex = 0; colIndex < row.length; colIndex++) {
                String tileId = row[colIndex];
                Material registedMaterial = registeredMaterials.get(tileId);
                if (registedMaterial == null) continue;
                Material material = registedMaterial.clone();
                float x = colIndex * map.tileSize;
                float y = rowIndex * map.tileSize;
                material.getPosition().add(x, y);
                Collider collider = material.getCollider();
                if (collider != null) {
                    collider.getPosition().set(material.getPosition());
                }
                
                materials.add(material);
            }
        }
    }
    
    public ArrayList<Material> getMaterials() {
        return this.materials;
    }
    
    public void update(float deltaTime) {
        for (Material material : materials) {
            material.update(deltaTime);
        }
    }
    
    private void updateTotalSize() {
        String[] rows = matrix.split("\n");
        int maxWidth = 0;
        for (int i = 0; i < rows.length; i++) {
            String[] row = rows[i].split(matrixSeparator);
            maxWidth = Math.max(maxWidth, row.length);
        }
        
        this.totalWidth = maxWidth * map.tileSize;
        this.totalHeight = rows.length * map.tileSize;
    }
    
    public int getTotalWidth() {
        return this.totalWidth;
    }
    
    public int getTotalHeight() {
        return this.totalHeight;
    }
}
