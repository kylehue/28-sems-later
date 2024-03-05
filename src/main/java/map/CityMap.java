package map;

import utils.LayoutUtils;

public class CityMap extends Map {
    public CityMap() {
        this.setTileSheet(LayoutUtils.loadImage("/sprites/Terrains.png"));
        this.setMapMatrix(Map.parseStringMatrix(
            """
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9
                9 9 1 1 0 0 1 1 1 0 0 7 6 6 6 6 6 8 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 7 6 6 6 6 6 8 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 2 3 3 5 3 3 4 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 0 0 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 p p p p p p p p p 9 9 9 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 1 0 0 1 1 1 0 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 k c c c c c l 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 1 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 1 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 m j j j j j n 0 o 9 9 q 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 1 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 1 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 1 m j j j j j n 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 a c c c c c b 0 o 9 9 q 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 q 0 0 0 0 0 0 0 0 0 o 9 9 q 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 9 9
                9 9 6 6 6 6 6 6 6 6 6 9 9 9 9 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 9 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9
                """,
            " "
        ));
        this.setTileSize(32, 32);
        this.registerTile("0", Map.TileLocation.create(0, 0)); // Plain Sidewalk
        this.registerTile("1", Map.TileLocation.create(0, 1)); // Cracked Sidewalk
        this.registerTile("2", Map.TileLocation.create(0, 2)); // Slightly-Cracked Sidewalk
        this.registerTile("3", Map.TileLocation.create(0, 3)); // Overgrown-Cracked Sidewalk
        this.registerTile("4", Map.TileLocation.create(0, 4)); // Plain Grass
        this.registerTile("5", Map.TileLocation.create(0, 5)); // Roadside Grass
        this.registerTile("6", Map.TileLocation.create(0, 6)); // Roadside South
        this.registerTile("7", Map.TileLocation.create(0, 7)); // Slightly-Overgrown Cracked Road
        this.registerTile("8", Map.TileLocation.create(0, 8)); // Overgrown-Cracked Road
        this.registerTile("9", Map.TileLocation.create(0, 9)); // Plain Road
        this.registerTile("a", Map.TileLocation.create(1,0)); // Front-Left Corner Wall
        this.registerTile("b", Map.TileLocation.create(1,1)); // Front-Right Corner Wall
        this.registerTile("c", Map.TileLocation.create(1,2)); // Front Wall
        this.registerTile("d", Map.TileLocation.create(1,3)); // Slightly-Broken Front Wall
        this.registerTile("e", Map.TileLocation.create(1,4)); // Broken Front Wall
        this.registerTile("f", Map.TileLocation.create(1,5)); // Front Wall Door Frame
        this.registerTile("g", Map.TileLocation.create(1,6)); // Cracked Right Side-Wall
        this.registerTile("h", Map.TileLocation.create(1,7)); // Cracked Left Side-Wall
        this.registerTile("i", Map.TileLocation.create(1,8)); // Cracked Inner-Building Tile
        this.registerTile("j", Map.TileLocation.create(1,9)); // Plain Inner-Building Tile
        this.registerTile("k", Map.TileLocation.create(2,0)); // Back-Left Corner Wall
        this.registerTile("l", Map.TileLocation.create(2,1)); // Back-Right Corner Wall
        this.registerTile("m", Map.TileLocation.create(2,2)); // Plain Left Side-Wall
        this.registerTile("n", Map.TileLocation.create(2,3)); // Plain Right Side-Wall
        this.registerTile("o", Map.TileLocation.create(0, 6),90); // Roadside East
        this.registerTile("p", Map.TileLocation.create(0, 6),180); // Roadside North
        this.registerTile("q", Map.TileLocation.create(0, 6),270); // Roadside West
    }
}
