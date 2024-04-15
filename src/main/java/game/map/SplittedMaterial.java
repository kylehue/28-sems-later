package game.map;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

import java.util.HashMap;

import game.utils.Common;

public class SplittedMaterial {
    private final Image image;
    private final PixelReader pixelReader;
    private final int splitSize;
    private final HashMap<Integer, Material> cachedMaterials = new HashMap<>();
    
    public SplittedMaterial(String imageUrl, int splitSize) {
        this.splitSize = splitSize;
        this.image = utils.Common.loadImage(imageUrl);
        this.pixelReader = this.image.getPixelReader();
    }
    
    public SplittedMaterial(Image image, int splitSize) {
        this.splitSize = splitSize;
        this.image = image;
        this.pixelReader = this.image.getPixelReader();
    }
    
    public Material get(int rowIndex, int columnIndex) {
        // If it's cached, return it
        int pairId = Common.cantor(rowIndex, columnIndex);
        Material cachedMaterial = cachedMaterials.get(pairId);
        if (cachedMaterial != null) return cachedMaterial;
        
        WritableImage img = new WritableImage(
            pixelReader,
            columnIndex * this.splitSize,
            rowIndex * this.splitSize,
            this.splitSize,
            this.splitSize
        );
        
        Material material = new Material(img);
        cachedMaterials.put(pairId, material);
        
        return material;
    }
}
