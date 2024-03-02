package map;

import utils.LayoutUtils;

public class CityMap extends Map {
    public CityMap() {
        this.setTileSheet(LayoutUtils.loadImage("/sprites/Character-and-Zombie.png"));
        this.setMapMatrix(Map.parseStringMatrix(
            """
                0 0 0 0 0
                0 1 1 1 0
                0 1 1 1 0
                0 1 1 1 0
                0 0 0 0 0
                """,
            " "
        ));
        this.setTileSize(48, 30);
        this.registerTile("0", Map.TileLocation.create(0, 0));
        this.registerTile("1", Map.TileLocation.create(4, 0));
    }
}