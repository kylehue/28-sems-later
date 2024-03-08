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
                9 s p p p p p p p p t 9 9 s p p p p p p p p p p p p p p p p p p p p p p p p p p p p p p p t 9
                9 o 0 0 0 0 1 1 1 0 q 9 9 o 0 0 1 1 1 0 0 1 1 1 0 0 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 q 9
                9 o 0 0 0 0 1 1 1 0 q 9 9 o 0 H H H H H H H H H H H H H I H H H H H H H H H H I H H H H 0 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 H H H H H H I H H H H H H H H H H H H H H H H H H H H H H 1 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 H H I H H H H H H H H I H H H H H H H H I H H H H H H H H 1 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 H H H H H H H H H H H H H H H H H H H H H H H H H H H H H 0 q 9
                9 o 0 0 0 0 1 1 1 0 q 9 9 o 0 H H H H H H H H H H H H H H H H H H H H H H I H H H H H H 1 q 9
                9 o 0 0 0 0 1 1 1 0 q 9 9 o 0 I H H H H H H H H H H H H I H H H H H H H H H H H H H H H 0 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 H H I H H H H H I H H H H H H H H H H H H H H H H H H H H 0 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 H H H H H H H H H H H H H I H H H H H H H H H H I H H H H 1 q 9
                9 o 1 1 0 0 1 1 1 0 q 9 9 o 0 0 1 1 1 0 0 1 1 1 0 0 1 1 K 0 0 0 0 0 0 0 0 0 0 0 1 0 0 1 0 q 9
                9 o 0 0 0 0 0 0 0 0 q 9 9 r 6 6 6 6 6 6 6 6 6 6 6 6 6 6 J 6 6 6 J J 6 6 6 6 6 6 6 6 6 6 6 u 9
                9 o 0 0 0 0 1 1 1 0 q 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 J 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 9
                9 r 6 6 6 6 6 6 6 6 u 9 9 9 K 1 1 0 1 1 1 0 0 0 K 0 0 1 0 0 1 1 0 0 0 0 0 0 0 1 0 0 0 1 1 0 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 8 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 K 9
                9 s p p p p p p p p p t 9 9 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1 8
                9 o K 1 0 0 1 2 1 0 2 q 9 8 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 o 1 k c c c c c l 1 q 9 9 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 o 0 m j j j j j n 0 q 9 9 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 7
                9 o 0 m j j j j j n K y y y 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 o 1 m j j j j j n 3 y y y 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 8
                9 o 2 m j j j j j n 3 q 7 8 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 7
                9 o 0 m j j j j j n 1 q 9 8 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1 7
                9 o 0 m j j j j j n 0 q 9 9 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1 9
                9 o 2 m j j j j j n K q 9 7 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1 9
                9 o 1 m j j j j j n 2 q 9 9 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 1 7
                9 o K m j j j j j n 1 q 9 8 0 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 o 1 m j j j j j n 1 q 9 9 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 o 0 a c c c c c b 0 q 9 9 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 8
                9 o 0 0 1 2 K 0 0 0 0 q 9 8 1 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 4 0 9
                9 r 6 6 6 6 6 6 6 6 6 u 9 7 1 0 0 0 K 0 0 1 1 0 0 0 0 0 0 1 0 1 0 1 0 0 0 K K 1 0 0 0 1 0 K 9
                9 9 9 9 9 9 9 9 9 9 9 9 9 9 9 7 9 7 9 9 8 9 9 7 8 8 9 9 7 7 9 9 9 8 9 9 9 7 9 9 8 8 9 9 7 7 9
                """,
            " "
        ));
        this.setTileSize(32, 32);
        this.registerTile("0", Map.TileLocation.create(0, 0)); // Plain Sidewalk
        this.registerTile("1", Map.TileLocation.create(0, 1)); // Cracked Sidewalk
        this.registerTile("2", Map.TileLocation.create(0, 2)); // Slightly-Cracked Sidewalk
        this.registerTile("3", Map.TileLocation.create(0, 3)); // Overgrown-Cracked Sidewalk
        this.registerTile("4", Map.TileLocation.create(0, 4)); // Plain Grass
        this.registerTile("5", Map.TileLocation.create(0, 5)); // Overgrown Roadside South
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
        this.registerTile("o", Map.TileLocation.create(0, 6),90); // Roadside West
        this.registerTile("p", Map.TileLocation.create(0, 6),180); // Roadside North
        this.registerTile("q", Map.TileLocation.create(0, 6),270); // Roadside East
        this.registerTile("r", Map.TileLocation.create(2, 4)); // Corner SideRoad South-West
        this.registerTile("s", Map.TileLocation.create(2, 4),90); // Corner SideRoad North-West
        this.registerTile("t", Map.TileLocation.create(2, 4),180); // Corner SideRoad North-East
        this.registerTile("u", Map.TileLocation.create(2, 4),270); // Corner SideRoad South-East
        this.registerTile("v", Map.TileLocation.create(2, 5)); // PedXing South
        this.registerTile("w", Map.TileLocation.create(2, 5),90); // PedXing West
        this.registerTile("x", Map.TileLocation.create(2, 5),180); // PedXing North
        this.registerTile("y", Map.TileLocation.create(2, 5),270); // PedXing East
        this.registerTile("z", Map.TileLocation.create(2, 6)); // Corner Overgrown SideRoad South-West
        this.registerTile("A", Map.TileLocation.create(2, 6),90); // Corner Overgrown SideRoad North-West
        this.registerTile("B", Map.TileLocation.create(2, 6),180); // Corner Overgrown SideRoad North-East
        this.registerTile("C", Map.TileLocation.create(2, 6),270); // Corner Overgrown SideRoad South-East
        this.registerTile("D", Map.TileLocation.create(0, 5),90); // Overgrown Roadside West
        this.registerTile("E", Map.TileLocation.create(0, 5),180); // Overgrown Roadside North
        this.registerTile("F", Map.TileLocation.create(0, 5),270); // Overgrown Roadside East
        this.registerTile("G", Map.TileLocation.create(2,7)); // Damaged Grass
        this.registerTile("H", Map.TileLocation.create(2,8)); // Dirt
        this.registerTile("I", Map.TileLocation.create(2,9)); // Damaged Dirt
        this.registerTile("J", Map.TileLocation.create(3,0)); // Dirty Road
        this.registerTile("K", Map.TileLocation.create(3,1)); // Damaged Roadside
    }
}
