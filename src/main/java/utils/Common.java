package utils;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.text.Font;

import java.util.HashMap;

public class Common {
    /**
     * Nothing special, it just sets up the GridPane defaults.
     */
    public static void setupGridPane(
        GridPane gridPane,
        int rowCount,
        int columnCount
    ) {
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();
        
        for (int i = 0; i < Math.max(rowCount, 1); i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setVgrow(Priority.SOMETIMES);
            rowConstraints.setValignment(VPos.TOP);
            rowConstraints.setFillHeight(true);
            rowConstraints.setMinHeight(Region.USE_COMPUTED_SIZE);
            rowConstraints.setPrefHeight(Region.USE_COMPUTED_SIZE);
            rowConstraints.setMaxHeight(Region.USE_COMPUTED_SIZE);
            gridPane.getRowConstraints().add(rowConstraints);
        }
        
        for (int i = 0; i < Math.max(columnCount, 1); i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setHgrow(Priority.SOMETIMES);
            columnConstraints.setHalignment(HPos.LEFT);
            columnConstraints.setFillWidth(true);
            columnConstraints.setMinWidth(Region.USE_COMPUTED_SIZE);
            columnConstraints.setPrefWidth(Region.USE_COMPUTED_SIZE);
            columnConstraints.setMaxWidth(Region.USE_COMPUTED_SIZE);
            gridPane.getColumnConstraints().add(columnConstraints);
        }
    }
    
    private final static HashMap<String, Image> loadedImages = new HashMap<>();
    
    public static Image loadImage(String url) {
        Image loadedImage = loadedImages.get(url);
        if (loadedImage != null) {
            return loadedImage;
        }
        
        try {
            Image image = new Image(
                Common.class.getResource(url).toURI().toString()
            );
            loadedImages.put(url, image);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Error(e);
        }
    }
    
    private final static HashMap<String, Font> loadedFonts = new HashMap<>();
    
    public static Font loadFont(String url, int size) {
        String key = url + "." + size;
        Font loadedFont = loadedFonts.get(key);
        if (loadedFont != null) {
            return loadedFont;
        }
        
        Font font = Font.loadFont(Common.class.getResourceAsStream(url), size);
        loadedFonts.put(key , font);
        
        return font;
    }
    
    private final static HashMap<String, Media> loadedMedias = new HashMap<>();
    public static Media loadMedia(String url) {
        Media loadedMedia = loadedMedias.get(url);
        if (loadedMedia != null) {
            return loadedMedia;
        }
        
        Media media = new Media(Common.class.getResource(url).toString());
        loadedMedias.put(url , media);
        
        return media;
    }
}
