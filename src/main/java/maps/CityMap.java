package maps;

import colliders.CircleCollider;
import colliders.PolygonCollider;
import main.CollisionGroup;
import main.ZIndex;
import map.Layer;
import map.Map;
import map.Material;
import map.SplittedMaterial;
import utils.Vector;

public class CityMap extends Map {
    public CityMap() {
        super(32);
        
        // MATERIALS
        Material barrel = new Material("/maps/city/decorations/barrel.png");
        barrel.setZIndex(ZIndex.MAP_DECORATIONS);
        barrel.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        barrel.getOriginOffset().addY(10);
        
        CircleCollider barrelCollider = new CircleCollider();
        barrelCollider.setRadius(10);
        barrelCollider.setGroup(CollisionGroup.MAP_TILES);
        barrelCollider.addToGroup(CollisionGroup.MAP_TILES);
        barrelCollider.setMass(400);
        barrel.setCollider(barrelCollider);
        
        Material car = new Material("/maps/city/decorations/car.png");
        car.setZIndex(ZIndex.MAP_DECORATIONS);
        car.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        car.getOriginOffset().addY(15);
        
        float carWidthHalf = (float) car.getImage().getWidth() / 2;
        float carHeightHalf = (float) car.getImage().getHeight() / 3;
        PolygonCollider carCollider = new PolygonCollider(new Vector[]{
            new Vector(-carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, carHeightHalf),
            new Vector(-carWidthHalf, carHeightHalf)
        });
        carCollider.setGroup(CollisionGroup.MAP_TILES);
        carCollider.setMass(10000);
        car.setCollider(carCollider);
        
        Material stopSign = new Material("/maps/city/decorations/stop-sign.png");
        stopSign.setZIndex(ZIndex.MAP_DECORATIONS);
        stopSign.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        
        CircleCollider stopSignCollider = new CircleCollider();
        stopSignCollider.setRadius(2);
        stopSignCollider.setStatic(true);
        stopSignCollider.setGroup(CollisionGroup.MAP_TILES);
        stopSign.setCollider(stopSignCollider);
        
        // LAYERS
        // Layer layerTiles = this.addLayer();
        // layerTiles.setMatrix("/maps/city/layers/layer-1.txt", " ");
        // layerTiles.registerMaterial("0", road);
        // layerTiles.registerMaterial("1", sidewalk);
        // layerTiles.distributeMaterials();
        
        Layer layerDecors = this.addLayer();
        layerDecors.setMatrix("/maps/city/layers/layer-2.txt", " ");
        layerDecors.registerMaterial("0", barrel);
        layerDecors.registerMaterial("1", car);
        layerDecors.registerMaterial("2", stopSign);
        layerDecors.distributeMaterials();
    }
}
