package map;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void registerMaterial(String tileId, Material material) {
        registeredMaterials.put(tileId, material);
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

                float x = colIndex * map.tileSize;
                float y = rowIndex * map.tileSize;
                registedMaterial.getPosition().set(x, y);
                materials.add(registedMaterial.clone());
            }
        }
    }
    
    public ArrayList<Material> getMaterials() {
        return this.materials;
    }
}
