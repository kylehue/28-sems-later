package maps;

import main.ZIndex;
import map.Layer;
import map.Map;
import map.Material;
import map.SplittedMaterial;

public class CityMap extends Map {
    public CityMap() {
        super(32);
        
        // // single materials
        // Material grassMaterial = new Material("./images/grass.png");
        // grassMaterial.setRotation(Material.Rotation.DEGREES_0);
        // grassMaterial.setHorizontallyFlipped(false);
        // grassMaterial.setVerticallyFlipped(false);
        // grassMaterial.setPositionOrigin(Material.PositionOrigin.CENTER);
        // // grassMaterial.addCollider(collider);
        // grassMaterial.clone();
        
        SplittedMaterial tileSheet = new SplittedMaterial(
            "/maps/city/tilesheet.png",
            32
        );
        
        Material sidewalk = tileSheet.get(0, 0);
        sidewalk.setZIndex(ZIndex.MAP_TILES);
        Material road = tileSheet.get(0, 9);
        road.setZIndex(ZIndex.MAP_TILES);
        Material barrel = new Material("/maps/city/decorations/barrel.png");
        barrel.setZIndex(ZIndex.MAP_DECORATIONS);
        Material car = new Material("/maps/city/decorations/car.png");
        car.setZIndex(ZIndex.MAP_DECORATIONS);
        Material stopSign = new Material("/maps/city/decorations/stop-sign.png");
        stopSign.setZIndex(ZIndex.MAP_DECORATIONS);
        
        Layer layerTiles = this.addLayer();
        layerTiles.setMatrix("/maps/city/layers/layer-1.txt", " ");
        layerTiles.registerMaterial("0", road);
        layerTiles.registerMaterial("1", sidewalk);
        layerTiles.distributeMaterials();
        
        Layer layerDecors = this.addLayer();
        layerDecors.setMatrix("/maps/city/layers/layer-2.txt", " ");
        layerDecors.registerMaterial("0", barrel);
        layerDecors.registerMaterial("1", car);
        layerDecors.registerMaterial("2", stopSign);
        layerDecors.distributeMaterials();
    }
}
