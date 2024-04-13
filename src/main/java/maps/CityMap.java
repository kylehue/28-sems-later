package maps;

import colliders.CircleCollider;
import colliders.Collider;
import colliders.GroupedCollider;
import colliders.PolygonCollider;
import main.CollisionGroup;
import main.ZIndex;
import map.Layer;
import map.Map;
import map.Material;
import utils.Vector;

public class CityMap extends Map {
    public CityMap() {
        super(32);
        // BASIC COLLIDERS
        
        float halfTile = ((float) this.getTileSize()) / 2;
        float roomWallThickness = 5; // this value doubles so 4 becomes 8-pixel thick
        PolygonCollider roomWallBottomCollider = new PolygonCollider(new Vector[]{
            new Vector(-halfTile, -roomWallThickness),
            new Vector(halfTile, -roomWallThickness),
            new Vector(halfTile, roomWallThickness),
            new Vector(-halfTile, roomWallThickness)
        });
        roomWallBottomCollider.setGroup(CollisionGroup.MAP);
        roomWallBottomCollider.setStatic(true);
        roomWallBottomCollider.getPosition().setY(halfTile - roomWallThickness);
        
        PolygonCollider roomWallLeftCollider = new PolygonCollider(new Vector[]{
            new Vector(-roomWallThickness, -halfTile),
            new Vector(roomWallThickness, -halfTile),
            new Vector(roomWallThickness, halfTile),
            new Vector(-roomWallThickness, halfTile)
        });
        roomWallLeftCollider.setGroup(CollisionGroup.MAP);
        roomWallLeftCollider.setStatic(true);
        roomWallLeftCollider.getPosition().setX(-halfTile + roomWallThickness);
        
        PolygonCollider roomWallRightCollider = roomWallLeftCollider.clone();
        roomWallRightCollider.getPosition().setX(halfTile - roomWallThickness);
        
        GroupedCollider roomWallBottomLeftCollider = new GroupedCollider(new Collider[]{
            roomWallLeftCollider.clone(),
            roomWallBottomCollider.clone()
        });
        roomWallBottomLeftCollider.setGroup(CollisionGroup.MAP);
        roomWallBottomLeftCollider.setStatic(true);
        
        GroupedCollider roomWallBottomRightCollider = new GroupedCollider(new Collider[]{
            roomWallRightCollider.clone(),
            roomWallBottomCollider.clone()
        });
        roomWallBottomRightCollider.setGroup(CollisionGroup.MAP);
        roomWallBottomRightCollider.setStatic(true);
        
        PolygonCollider roomWallTurnLeftCollider = new PolygonCollider(new Vector[]{
            new Vector(-roomWallThickness, -roomWallThickness),
            new Vector(roomWallThickness, -roomWallThickness),
            new Vector(roomWallThickness, roomWallThickness),
            new Vector(-roomWallThickness, roomWallThickness)
        });
        roomWallTurnLeftCollider.setGroup(CollisionGroup.MAP);
        roomWallTurnLeftCollider.setStatic(true);
        roomWallTurnLeftCollider.getPosition().setX(halfTile - roomWallThickness);
        roomWallTurnLeftCollider.getPosition().setY(halfTile - roomWallThickness);
        
        PolygonCollider roomWallTurnRightCollider = roomWallTurnLeftCollider.clone();
        roomWallTurnRightCollider.getPosition().setX(-halfTile + roomWallThickness);
        
        float fenceThickness = 2.5f; // this value doubles so 4 becomes 8-pixel thick
        PolygonCollider fenceBottomCollider = new PolygonCollider(new Vector[]{
            new Vector(-halfTile, -fenceThickness),
            new Vector(halfTile, -fenceThickness),
            new Vector(halfTile, fenceThickness),
            new Vector(-halfTile, fenceThickness)
        });
        fenceBottomCollider.setGroup(CollisionGroup.MAP);
        fenceBottomCollider.setStatic(true);
        fenceBottomCollider.getPosition().setY(halfTile - fenceThickness);
        
        PolygonCollider fenceLeftCollider = new PolygonCollider(new Vector[]{
            new Vector(-fenceThickness, -halfTile),
            new Vector(fenceThickness, -halfTile),
            new Vector(fenceThickness, halfTile),
            new Vector(-fenceThickness, halfTile)
        });
        fenceLeftCollider.setGroup(CollisionGroup.MAP);
        fenceLeftCollider.setStatic(true);
        fenceLeftCollider.getPosition().setX(-halfTile + fenceThickness);
        
        PolygonCollider fenceRightCollider = fenceLeftCollider.clone();
        fenceRightCollider.getPosition().setX(halfTile - fenceThickness);
        
        GroupedCollider fenceBottomRightCollider = new GroupedCollider(new Collider[]{
            fenceRightCollider.clone(),
            fenceBottomCollider.clone()
        });
        fenceBottomRightCollider.setGroup(CollisionGroup.MAP);
        fenceBottomRightCollider.setStatic(true);
        
        GroupedCollider fenceTurnLeftCollider = new GroupedCollider(new Collider[]{
            fenceLeftCollider.clone(),
            fenceBottomCollider.clone()
        });
        fenceTurnLeftCollider.setGroup(CollisionGroup.MAP);
        fenceTurnLeftCollider.setStatic(true);
        
        GroupedCollider fenceTurnRightCollider = new GroupedCollider(new Collider[]{
            fenceRightCollider.clone(),
            fenceBottomCollider.clone()
        });
        fenceTurnRightCollider.setGroup(CollisionGroup.MAP);
        fenceTurnRightCollider.setStatic(true);
        
        // MATERIALS (LOW)
        Material grass0 = new Material("/maps/city/tiles/grass-0.png");
        Material grass1 = new Material("/maps/city/tiles/grass-1.png");
        Material grass2 = new Material("/maps/city/tiles/grass-2.png");
        Material grass3 = new Material("/maps/city/tiles/grass-3.png");
        Material grass4 = new Material("/maps/city/tiles/grass-4.png");
        Material roadCornerBottomLeftGrassy = new Material("/maps/city/tiles/road-corner-bottom-left-grassy.png");
        Material roadCornerBottomLeftSandy = new Material("/maps/city/tiles/road-corner-bottom-left-sandy.png");
        Material roadCornerBottomLeft = new Material("/maps/city/tiles/road-corner-bottom-left.png");
        Material roadCornerBottomRightGrassy = new Material("/maps/city/tiles/road-corner-bottom-right-grassy.png");
        Material roadCornerBottomRightSandy = new Material("/maps/city/tiles/road-corner-bottom-right-sandy.png");
        Material roadCornerBottomRight = new Material("/maps/city/tiles/road-corner-bottom-right.png");
        Material roadCornerTopLeftGrassy = new Material("/maps/city/tiles/road-corner-top-left-grassy.png");
        Material roadCornerTopLeftSandy = new Material("/maps/city/tiles/road-corner-top-left-sandy.png");
        Material roadCornerTopLeft = new Material("/maps/city/tiles/road-corner-top-left.png");
        Material roadCornerTopRightGrassy = new Material("/maps/city/tiles/road-corner-top-right-grassy.png");
        Material roadCornerTopRightSandy = new Material("/maps/city/tiles/road-corner-top-right-sandy.png");
        Material roadCornerTopRight = new Material("/maps/city/tiles/road-corner-top-right.png");
        Material roadGrass0 = new Material("/maps/city/tiles/road-grass-0.png");
        Material roadGrass1 = new Material("/maps/city/tiles/road-grass-1.png");
        Material roadMiddleHorizontal = new Material("/maps/city/tiles/road-middle-horizontal.png");
        Material roadMiddleVertical = new Material("/maps/city/tiles/road-middle-vertical.png");
        Material roadPedestrianHorizontal = new Material("/maps/city/tiles/road-pedestrian-horizontal.png");
        Material roadPedestrianVertical = new Material("/maps/city/tiles/road-pedestrian-vertical.png");
        Material roadSand0 = new Material("/maps/city/tiles/road-sand-0.png");
        Material roadSand1 = new Material("/maps/city/tiles/road-sand-1.png");
        Material roadSideBottomGrassy0 = new Material("/maps/city/tiles/road-side-bottom-grassy-0.png");
        Material roadSideBottomGrassy1 = new Material("/maps/city/tiles/road-side-bottom-grassy-1.png");
        Material roadSideBottomGrassy2 = new Material("/maps/city/tiles/road-side-bottom-grassy-2.png");
        Material roadSideBottomGrassy3 = new Material("/maps/city/tiles/road-side-bottom-grassy-3.png");
        Material roadSideBottomSandy0 = new Material("/maps/city/tiles/road-side-bottom-sandy-0.png");
        Material roadSideBottomSandy1 = new Material("/maps/city/tiles/road-side-bottom-sandy-1.png");
        Material roadSideBottomSandy2 = new Material("/maps/city/tiles/road-side-bottom-sandy-2.png");
        Material roadSideBottomSandy3 = new Material("/maps/city/tiles/road-side-bottom-sandy-3.png");
        Material roadSideBottom = new Material("/maps/city/tiles/road-side-bottom.png");
        Material roadSideLeftGrassy0 = new Material("/maps/city/tiles/road-side-left-grassy-0.png");
        Material roadSideLeftGrassy1 = new Material("/maps/city/tiles/road-side-left-grassy-1.png");
        Material roadSideLeftGrassy2 = new Material("/maps/city/tiles/road-side-left-grassy-2.png");
        Material roadSideLeftGrassy3 = new Material("/maps/city/tiles/road-side-left-grassy-3.png");
        Material roadSideLeftSandy0 = new Material("/maps/city/tiles/road-side-left-sandy-0.png");
        Material roadSideLeftSandy1 = new Material("/maps/city/tiles/road-side-left-sandy-1.png");
        Material roadSideLeftSandy2 = new Material("/maps/city/tiles/road-side-left-sandy-2.png");
        Material roadSideLeftSandy3 = new Material("/maps/city/tiles/road-side-left-sandy-3.png");
        Material roadSideLeft = new Material("/maps/city/tiles/road-side-left.png");
        Material roadSideRightGrassy0 = new Material("/maps/city/tiles/road-side-right-grassy-0.png");
        Material roadSideRightGrassy1 = new Material("/maps/city/tiles/road-side-right-grassy-1.png");
        Material roadSideRightGrassy2 = new Material("/maps/city/tiles/road-side-right-grassy-2.png");
        Material roadSideRightGrassy3 = new Material("/maps/city/tiles/road-side-right-grassy-3.png");
        Material roadSideRightSandy0 = new Material("/maps/city/tiles/road-side-right-sandy-0.png");
        Material roadSideRightSandy1 = new Material("/maps/city/tiles/road-side-right-sandy-1.png");
        Material roadSideRightSandy2 = new Material("/maps/city/tiles/road-side-right-sandy-2.png");
        Material roadSideRightSandy3 = new Material("/maps/city/tiles/road-side-right-sandy-3.png");
        Material roadSideRight = new Material("/maps/city/tiles/road-side-right.png");
        Material roadSideTopGrassy0 = new Material("/maps/city/tiles/road-side-top-grassy-0.png");
        Material roadSideTopGrassy1 = new Material("/maps/city/tiles/road-side-top-grassy-1.png");
        Material roadSideTopGrassy2 = new Material("/maps/city/tiles/road-side-top-grassy-2.png");
        Material roadSideTopGrassy3 = new Material("/maps/city/tiles/road-side-top-grassy-3.png");
        Material roadSideTopSandy0 = new Material("/maps/city/tiles/road-side-top-sandy-0.png");
        Material roadSideTopSandy1 = new Material("/maps/city/tiles/road-side-top-sandy-1.png");
        Material roadSideTopSandy2 = new Material("/maps/city/tiles/road-side-top-sandy-2.png");
        Material roadSideTopSandy3 = new Material("/maps/city/tiles/road-side-top-sandy-3.png");
        Material roadSideTop = new Material("/maps/city/tiles/road-side-top.png");
        Material road = new Material("/maps/city/tiles/road.png");
        Material roomFloor0 = new Material("/maps/city/tiles/room-floor-0.png");
        Material roomFloor1 = new Material("/maps/city/tiles/room-floor-1.png");
        Material roomFloor2 = new Material("/maps/city/tiles/room-floor-2.png");
        Material roomFloor3 = new Material("/maps/city/tiles/room-floor-3.png");
        Material sand0 = new Material("/maps/city/tiles/sand-0.png");
        Material sand1 = new Material("/maps/city/tiles/sand-1.png");
        Material sand2 = new Material("/maps/city/tiles/sand-2.png");
        Material sand3 = new Material("/maps/city/tiles/sand-3.png");
        Material sidewalk0 = new Material("/maps/city/tiles/sidewalk-0.png");
        Material sidewalk1 = new Material("/maps/city/tiles/sidewalk-1.png");
        Material sidewalk2 = new Material("/maps/city/tiles/sidewalk-2.png");
        Material sidewalk3 = new Material("/maps/city/tiles/sidewalk-3.png");
        
        // MATERIALS (MID)
        Material fenceBackSignedLeft = new Material("/maps/city/tiles/fence-back-signed-left.png");
        fenceBackSignedLeft.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceBackSignedLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceBackSignedLeft.setCollider(fenceBottomCollider);
        
        Material fenceBackSignedRight1 = new Material("/maps/city/tiles/fence-back-signed-right-1.png");
        fenceBackSignedRight1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceBackSignedRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceBackSignedRight1.setCollider(fenceBottomCollider);
        
        Material fenceBackSignedRight = new Material("/maps/city/tiles/fence-back-signed-right.png");
        fenceBackSignedRight.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceBackSignedRight.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceBackSignedRight.setCollider(fenceBottomCollider);
        
        Material fenceFrontSignedLeft = new Material("/maps/city/tiles/fence-front-signed-left.png");
        fenceFrontSignedLeft.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceFrontSignedLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceFrontSignedLeft.setCollider(fenceBottomCollider);
        
        Material fenceLeft = new Material("/maps/city/tiles/fence-left.png");
        fenceLeft.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceLeft.setCollider(fenceBottomCollider);
        
        Material fenceMiddle = new Material("/maps/city/tiles/fence-middle.png");
        fenceMiddle.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceMiddle.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceMiddle.setCollider(fenceBottomCollider);
        
        Material fenceRight = new Material("/maps/city/tiles/fence-right.png");
        fenceRight.setRenderPosition(Material.PositionOrigin.BOTTOM);
        fenceRight.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        fenceRight.setCollider(fenceBottomCollider);
        
        Material fenceSideLeft = new Material("/maps/city/tiles/fence-side-left.png");
        fenceSideLeft.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        fenceSideLeft.setPositionOrigin(Material.PositionOrigin.TOP_LEFT);
        fenceSideLeft.setCollider(fenceLeftCollider);
        
        Material fenceSideRight = new Material("/maps/city/tiles/fence-side-right.png");
        fenceSideRight.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        fenceSideRight.setPositionOrigin(Material.PositionOrigin.TOP_RIGHT);
        fenceSideRight.setCollider(fenceRightCollider);
        
        Material fenceTurnLeft = new Material("/maps/city/tiles/fence-left-collision.png");
        fenceTurnLeft.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        fenceTurnLeft.setPositionOrigin(Material.PositionOrigin.TOP_LEFT);
        fenceTurnLeft.setCollider(fenceTurnLeftCollider);
        
        Material fenceTurnRight = new Material("/maps/city/tiles/fence-right-collision.png");
        fenceTurnRight.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        fenceTurnRight.setPositionOrigin(Material.PositionOrigin.TOP_LEFT);
        fenceTurnRight.setCollider(fenceTurnRightCollider);
        
        Material roomWallCornerBottomLeft0 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-0.png");
        roomWallCornerBottomLeft0.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomLeft0.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomLeft0.setCollider(roomWallBottomLeftCollider.clone());
        
        Material roomWallCornerBottomLeft1 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-1.png");
        roomWallCornerBottomLeft1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomLeft1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomLeft1.setCollider(roomWallBottomLeftCollider.clone());
        
        Material roomWallCornerBottomLeft2 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-2.png");
        roomWallCornerBottomLeft2.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomLeft2.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomLeft2.setCollider(roomWallBottomLeftCollider.clone());
        
        Material roomWallCornerBottomRight0 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-0.png");
        roomWallCornerBottomRight0.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomRight0.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomRight0.setCollider(roomWallBottomRightCollider);
        
        Material roomWallCornerBottomRight1 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-1.png");
        roomWallCornerBottomRight1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomRight1.setCollider(roomWallBottomRightCollider);
        
        Material roomWallCornerBottomRight2 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-2.png");
        roomWallCornerBottomRight2.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerBottomRight2.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerBottomRight2.setCollider(roomWallBottomRightCollider);
        
        Material roomWallCornerTopLeft0 = new Material("/maps/city/tiles/room-wall-corner-top-left-0.png");
        roomWallCornerTopLeft0.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft0.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopLeft0.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallCornerTopLeft1 = new Material("/maps/city/tiles/room-wall-corner-top-left-1.png");
        roomWallCornerTopLeft1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopLeft1.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallCornerTopLeft2 = new Material("/maps/city/tiles/room-wall-corner-top-left-2.png");
        roomWallCornerTopLeft2.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft2.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopLeft2.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallCornerTopRight0 = new Material("/maps/city/tiles/room-wall-corner-top-right-0.png");
        roomWallCornerTopRight0.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight0.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopRight0.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallCornerTopRight1 = new Material("/maps/city/tiles/room-wall-corner-top-right-1.png");
        roomWallCornerTopRight1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopRight1.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallCornerTopRight2 = new Material("/maps/city/tiles/room-wall-corner-top-right-2.png");
        roomWallCornerTopRight2.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight2.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallCornerTopRight2.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle0 = new Material("/maps/city/tiles/room-wall-middle-0.png");
        roomWallMiddle0.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle0.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle0.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle1 = new Material("/maps/city/tiles/room-wall-middle-1.png");
        roomWallMiddle1.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle1.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle1.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle2 = new Material("/maps/city/tiles/room-wall-middle-2.png");
        roomWallMiddle2.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle2.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle2.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle3 = new Material("/maps/city/tiles/room-wall-middle-3.png");
        roomWallMiddle3.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle3.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle3.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle4 = new Material("/maps/city/tiles/room-wall-middle-4.png");
        roomWallMiddle4.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle4.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle4.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle5 = new Material("/maps/city/tiles/room-wall-middle-5.png");
        roomWallMiddle5.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle5.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle5.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle6 = new Material("/maps/city/tiles/room-wall-middle-6.png");
        roomWallMiddle6.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle6.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle6.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle7 = new Material("/maps/city/tiles/room-wall-middle-7.png");
        roomWallMiddle7.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle7.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle7.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle8 = new Material("/maps/city/tiles/room-wall-middle-8.png");
        roomWallMiddle8.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle8.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        roomWallMiddle8.setCollider(roomWallBottomCollider.clone());
        
        Material roomWallMiddle9 = new Material("/maps/city/tiles/room-wall-middle-9.png");
        roomWallMiddle9.setRenderPosition(Material.PositionOrigin.BOTTOM);
        roomWallMiddle9.setPositionOrigin(Material.PositionOrigin.BOTTOM_LEFT);
        
        Material roomWallSideRight = new Material("/maps/city/tiles/room-wall-side.png");
        roomWallSideRight.setPositionOrigin(Material.PositionOrigin.TOP_RIGHT);
        roomWallSideRight.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        roomWallSideRight.setCollider(roomWallRightCollider);
        
        Material roomWallSideLeft = roomWallSideRight.clone();
        roomWallSideLeft.setPositionOrigin(Material.PositionOrigin.TOP_LEFT);
        roomWallSideLeft.setCollider(roomWallLeftCollider);
        
        Material roomWallSideTurnRight = new Material("/maps/city/tiles/room-wall-side-turn-right.png");
        roomWallSideTurnRight.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        roomWallSideTurnRight.setPositionOrigin(Material.PositionOrigin.TOP_LEFT);
        roomWallSideTurnRight.setCollider(roomWallTurnRightCollider);
        
        Material roomWallSideTurnLeft = new Material("/maps/city/tiles/room-wall-side-turn-left.png");
        roomWallSideTurnLeft.setZIndex(ZIndex.MAP_DECORATIONS + 1);
        roomWallSideTurnLeft.setPositionOrigin(Material.PositionOrigin.TOP_RIGHT);
        roomWallSideTurnLeft.setCollider(roomWallTurnLeftCollider);
        
        // MATERIALS (HIGH)
        Material plankBottomShort = new Material("/maps/city/decorations/plank-bottom-short.png");
        plankBottomShort.setSeeThrough(true);
        Material plankBottom = new Material("/maps/city/decorations/plank-bottom.png");
        plankBottom.setSeeThrough(true);
        Material plankCrossed = new Material("/maps/city/decorations/plank-crossed.png");
        plankCrossed.setSeeThrough(true);
        Material plankHorizontalMiddle = new Material("/maps/city/decorations/plank-horizontal-middle.png");
        plankHorizontalMiddle.setSeeThrough(true);
        Material plankVerticalMiddle = plankHorizontalMiddle.clone();
        plankVerticalMiddle.setRotation(-90);
        plankVerticalMiddle.setSeeThrough(true);
        Material plankLeft = new Material("/maps/city/decorations/plank-left.png");
        plankLeft.setSeeThrough(true);
        Material plankRight = new Material("/maps/city/decorations/plank-right.png");
        plankRight.setSeeThrough(true);
        Material plankTop = new Material("/maps/city/decorations/plank-top.png");
        plankTop.setSeeThrough(true);
        
        // MATERIALS (HIGHER)
        Material roofRustySlantedLeft = new Material("/maps/city/decorations/roof-rusty-slanted-left.png");
        roofRustySlantedLeft.setSeeThrough(true);
        Material roofRustySlantedRight = new Material("/maps/city/decorations/roof-rusty-slanted-right.png");
        roofRustySlantedRight.setSeeThrough(true);
        Material roofRusty = new Material("/maps/city/decorations/roof-rusty.png");
        roofRusty.setSeeThrough(true);
        Material roofSlantedLeft = new Material("/maps/city/decorations/roof-slanted-left.png");
        roofSlantedLeft.setSeeThrough(true);
        Material roofSlantedRight = new Material("/maps/city/decorations/roof-slanted-right.png");
        roofSlantedRight.setSeeThrough(true);
        Material roof = new Material("/maps/city/decorations/roof.png");
        roof.setSeeThrough(true);
        
        // MATERIALS (DECORS)
        Material stopSign = new Material("/maps/city/decorations/stop-sign.png");
        stopSign.setPositionOrigin(7, 37);
        stopSign.setRenderPosition(7, 35);
        CircleCollider stopSignCollider = new CircleCollider();
        stopSignCollider.setRadius(3);
        stopSignCollider.setGroup(CollisionGroup.MAP);
        stopSignCollider.setStatic(true);
        stopSign.setCollider(stopSignCollider);
        
        Material streetLamp = new Material("/maps/city/decorations/street-lamp.png");
        streetLamp.setPositionOrigin(20, 106);
        streetLamp.setRenderPosition(20, 103);
        CircleCollider streetLampCollider = new CircleCollider();
        streetLampCollider.setRadius(3);
        streetLampCollider.setGroup(CollisionGroup.MAP);
        streetLampCollider.setStatic(true);
        streetLamp.setCollider(streetLampCollider);
        
        Material trashCan = new Material("/maps/city/decorations/trash-can.png");
        trashCan.setPositionOrigin(7, 14);
        trashCan.setRenderPosition(7, 12);
        CircleCollider trashCanCollider = new CircleCollider();
        trashCanCollider.setRadius(5);
        trashCanCollider.setGroup(CollisionGroup.MAP);
        trashCanCollider.addToGroup(CollisionGroup.MAP);
        trashCanCollider.setMass(100);
        trashCan.setCollider(trashCanCollider);
        
        Material barrel = new Material("/maps/city/decorations/barrel.png");
        barrel.setPositionOrigin(9, 18);
        barrel.setRenderPosition(9, 18);
        CircleCollider barrelCollider = new CircleCollider();
        barrelCollider.setRadius(8);
        barrelCollider.setGroup(CollisionGroup.MAP);
        barrelCollider.addToGroup(CollisionGroup.MAP);
        barrelCollider.setMass(400);
        barrel.setCollider(barrelCollider);
        
        Material car = new Material("/maps/city/decorations/car.png");
        car.setPositionOrigin(28, 18);
        car.setRenderPosition(28, 12);
        float carWidthHalf = (float) car.getImage().getWidth() / 2;
        float carHeightHalf = 16f / 2f;
        PolygonCollider carCollider = new PolygonCollider(new Vector[]{
            new Vector(-carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, carHeightHalf),
            new Vector(-carWidthHalf, carHeightHalf)
        });
        carCollider.setGroup(CollisionGroup.MAP);
        carCollider.addToGroup(CollisionGroup.MAP);
        carCollider.setStatic(true);
        car.setCollider(carCollider);
        
        // LAYERS
        Layer layerLow = this.addLayer();
        layerLow.setZIndex(ZIndex.MAP_FLOOR);
        layerLow.setMatrix("/maps/city/layers/low.txt", " ");
        layerLow.registerMaterial("grass-0.png", grass0);
        layerLow.registerMaterial("grass-1.png", grass1);
        layerLow.registerMaterial("grass-2.png", grass2);
        layerLow.registerMaterial("grass-3.png", grass3);
        layerLow.registerMaterial("grass-4.png", grass4);
        layerLow.registerMaterial("road-corner-bottom-left-grassy.png", roadCornerBottomLeftGrassy);
        layerLow.registerMaterial("road-corner-bottom-left-sandy.png", roadCornerBottomLeftSandy);
        layerLow.registerMaterial("road-corner-bottom-left.png", roadCornerBottomLeft);
        layerLow.registerMaterial("road-corner-bottom-right-grassy.png", roadCornerBottomRightGrassy);
        layerLow.registerMaterial("road-corner-bottom-right-sandy.png", roadCornerBottomRightSandy);
        layerLow.registerMaterial("road-corner-bottom-right.png", roadCornerBottomRight);
        layerLow.registerMaterial("road-corner-top-left-grassy.png", roadCornerTopLeftGrassy);
        layerLow.registerMaterial("road-corner-top-left-sandy.png", roadCornerTopLeftSandy);
        layerLow.registerMaterial("road-corner-top-left.png", roadCornerTopLeft);
        layerLow.registerMaterial("road-corner-top-right-grassy.png", roadCornerTopRightGrassy);
        layerLow.registerMaterial("road-corner-top-right-sandy.png", roadCornerTopRightSandy);
        layerLow.registerMaterial("road-corner-top-right.png", roadCornerTopRight);
        layerLow.registerMaterial("road-grass-0.png", roadGrass0);
        layerLow.registerMaterial("road-grass-1.png", roadGrass1);
        layerLow.registerMaterial("road-middle-horizontal.png", roadMiddleHorizontal);
        layerLow.registerMaterial("road-middle-vertical.png", roadMiddleVertical);
        layerLow.registerMaterial("road-pedestrian-horizontal.png", roadPedestrianHorizontal);
        layerLow.registerMaterial("road-pedestrian-vertical.png", roadPedestrianVertical);
        layerLow.registerMaterial("road-sand-0.png", roadSand0);
        layerLow.registerMaterial("road-sand-1.png", roadSand1);
        layerLow.registerMaterial("road-side-bottom-grassy-0.png", roadSideBottomGrassy0);
        layerLow.registerMaterial("road-side-bottom-grassy-1.png", roadSideBottomGrassy1);
        layerLow.registerMaterial("road-side-bottom-grassy-2.png", roadSideBottomGrassy2);
        layerLow.registerMaterial("road-side-bottom-grassy-3.png", roadSideBottomGrassy3);
        layerLow.registerMaterial("road-side-bottom-sandy-0.png", roadSideBottomSandy0);
        layerLow.registerMaterial("road-side-bottom-sandy-1.png", roadSideBottomSandy1);
        layerLow.registerMaterial("road-side-bottom-sandy-2.png", roadSideBottomSandy2);
        layerLow.registerMaterial("road-side-bottom-sandy-3.png", roadSideBottomSandy3);
        layerLow.registerMaterial("road-side-bottom.png", roadSideBottom);
        layerLow.registerMaterial("road-side-left-grassy-0.png", roadSideLeftGrassy0);
        layerLow.registerMaterial("road-side-left-grassy-1.png", roadSideLeftGrassy1);
        layerLow.registerMaterial("road-side-left-grassy-2.png", roadSideLeftGrassy2);
        layerLow.registerMaterial("road-side-left-grassy-3.png", roadSideLeftGrassy3);
        layerLow.registerMaterial("road-side-left-sandy-0.png", roadSideLeftSandy0);
        layerLow.registerMaterial("road-side-left-sandy-1.png", roadSideLeftSandy1);
        layerLow.registerMaterial("road-side-left-sandy-2.png", roadSideLeftSandy2);
        layerLow.registerMaterial("road-side-left-sandy-3.png", roadSideLeftSandy3);
        layerLow.registerMaterial("road-side-left.png", roadSideLeft);
        layerLow.registerMaterial("road-side-right-grassy-0.png", roadSideRightGrassy0);
        layerLow.registerMaterial("road-side-right-grassy-1.png", roadSideRightGrassy1);
        layerLow.registerMaterial("road-side-right-grassy-2.png", roadSideRightGrassy2);
        layerLow.registerMaterial("road-side-right-grassy-3.png", roadSideRightGrassy3);
        layerLow.registerMaterial("road-side-right-sandy-0.png", roadSideRightSandy0);
        layerLow.registerMaterial("road-side-right-sandy-1.png", roadSideRightSandy1);
        layerLow.registerMaterial("road-side-right-sandy-2.png", roadSideRightSandy2);
        layerLow.registerMaterial("road-side-right-sandy-3.png", roadSideRightSandy3);
        layerLow.registerMaterial("road-side-right.png", roadSideRight);
        layerLow.registerMaterial("road-side-top-grassy-0.png", roadSideTopGrassy0);
        layerLow.registerMaterial("road-side-top-grassy-1.png", roadSideTopGrassy1);
        layerLow.registerMaterial("road-side-top-grassy-2.png", roadSideTopGrassy2);
        layerLow.registerMaterial("road-side-top-grassy-3.png", roadSideTopGrassy3);
        layerLow.registerMaterial("road-side-top-sandy-0.png", roadSideTopSandy0);
        layerLow.registerMaterial("road-side-top-sandy-1.png", roadSideTopSandy1);
        layerLow.registerMaterial("road-side-top-sandy-2.png", roadSideTopSandy2);
        layerLow.registerMaterial("road-side-top-sandy-3.png", roadSideTopSandy3);
        layerLow.registerMaterial("road-side-top.png", roadSideTop);
        layerLow.registerMaterial("road.png", road);
        layerLow.registerMaterial("room-floor-0.png", roomFloor0);
        layerLow.registerMaterial("room-floor-1.png", roomFloor1);
        layerLow.registerMaterial("room-floor-2.png", roomFloor2);
        layerLow.registerMaterial("room-floor-3.png", roomFloor3);
        layerLow.registerMaterial("sand-0.png", sand0);
        layerLow.registerMaterial("sand-1.png", sand1);
        layerLow.registerMaterial("sand-2.png", sand2);
        layerLow.registerMaterial("sand-3.png", sand3);
        layerLow.registerMaterial("sidewalk-0.png", sidewalk0);
        layerLow.registerMaterial("sidewalk-1.png", sidewalk1);
        layerLow.registerMaterial("sidewalk-2.png", sidewalk2);
        layerLow.registerMaterial("sidewalk-3.png", sidewalk3);
        layerLow.distributeMaterials();
        
        Layer layerMid = this.addLayer();
        layerMid.setZIndex(ZIndex.MAP_DECORATIONS);
        layerMid.setMatrix("/maps/city/layers/mid.txt", " ");
        layerMid.registerMaterial("fence-back-signed-left.png", fenceBackSignedLeft);
        layerMid.registerMaterial("fence-back-signed-right-1.png", fenceBackSignedRight1);
        layerMid.registerMaterial("fence-back-signed-right.png", fenceBackSignedRight);
        layerMid.registerMaterial("fence-front-signed-left.png", fenceFrontSignedLeft);
        layerMid.registerMaterial("fence-left.png", fenceLeft);
        layerMid.registerMaterial("fence-middle.png", fenceMiddle);
        layerMid.registerMaterial("fence-right.png", fenceRight);
        layerMid.registerMaterial("fence-side-left.png", fenceSideLeft);
        layerMid.registerMaterial("fence-side-right.png", fenceSideRight);
        layerMid.registerMaterial("fence-left-collision.png", fenceTurnLeft);
        layerMid.registerMaterial("fence-right-collision.png", fenceTurnRight);
        layerMid.registerMaterial("room-wall-corner-bottom-left-0.png", roomWallCornerBottomLeft0);
        layerMid.registerMaterial("room-wall-corner-bottom-left-1.png", roomWallCornerBottomLeft1);
        layerMid.registerMaterial("room-wall-corner-bottom-left-2.png", roomWallCornerBottomLeft2);
        layerMid.registerMaterial("room-wall-corner-bottom-right-0.png", roomWallCornerBottomRight0);
        layerMid.registerMaterial("room-wall-corner-bottom-right-1.png", roomWallCornerBottomRight1);
        layerMid.registerMaterial("room-wall-corner-bottom-right-2.png", roomWallCornerBottomRight2);
        layerMid.registerMaterial("room-wall-corner-top-left-0.png", roomWallCornerTopLeft0);
        layerMid.registerMaterial("room-wall-corner-top-left-1.png", roomWallCornerTopLeft1);
        layerMid.registerMaterial("room-wall-corner-top-left-2.png", roomWallCornerTopLeft2);
        layerMid.registerMaterial("room-wall-corner-top-right-0.png", roomWallCornerTopRight0);
        layerMid.registerMaterial("room-wall-corner-top-right-1.png", roomWallCornerTopRight1);
        layerMid.registerMaterial("room-wall-corner-top-right-2.png", roomWallCornerTopRight2);
        layerMid.registerMaterial("room-wall-middle-0.png", roomWallMiddle0);
        layerMid.registerMaterial("room-wall-middle-1.png", roomWallMiddle1);
        layerMid.registerMaterial("room-wall-middle-2.png", roomWallMiddle2);
        layerMid.registerMaterial("room-wall-middle-3.png", roomWallMiddle3);
        layerMid.registerMaterial("room-wall-middle-4.png", roomWallMiddle4);
        layerMid.registerMaterial("room-wall-middle-5.png", roomWallMiddle5);
        layerMid.registerMaterial("room-wall-middle-6.png", roomWallMiddle6);
        layerMid.registerMaterial("room-wall-middle-7.png", roomWallMiddle7);
        layerMid.registerMaterial("room-wall-middle-8.png", roomWallMiddle8);
        layerMid.registerMaterial("room-wall-middle-9.png", roomWallMiddle9);
        layerMid.registerMaterial("room-wall-side-right.png", roomWallSideRight);
        layerMid.registerMaterial("room-wall-side-left.png", roomWallSideLeft);
        layerMid.registerMaterial("room-wall-side-turn-left.png", roomWallSideTurnLeft);
        layerMid.registerMaterial("room-wall-side-turn-right.png", roomWallSideTurnRight);
        layerMid.distributeMaterials();
        
        Layer layerDecor = this.addLayer();
        layerDecor.setZIndex(ZIndex.MAP_DECORATIONS);
        layerDecor.setMatrix("/maps/city/layers/decors.txt", " ");
        layerDecor.registerMaterial("barrel.png", barrel);
        layerDecor.registerMaterial("car.png", car);
        layerDecor.registerMaterial("stop-sign.png", stopSign);
        layerDecor.registerMaterial("street-lamp.png", streetLamp);
        layerDecor.registerMaterial("trash-can.png", trashCan);
        layerDecor.distributeMaterials();
        
        Layer layerHigh = this.addLayer();
        layerHigh.setZIndex(ZIndex.MAP_HIGH);
        layerHigh.setMatrix("/maps/city/layers/high.txt", " ");
        layerHigh.registerMaterial("plank-bottom-short.png", plankBottomShort);
        layerHigh.registerMaterial("plank-bottom.png", plankBottom);
        layerHigh.registerMaterial("plank-crossed.png", plankCrossed);
        layerHigh.registerMaterial("plank-horizontal-middle.png", plankHorizontalMiddle);
        layerHigh.registerMaterial("plank-vertical-middle.png", plankVerticalMiddle);
        layerHigh.registerMaterial("plank-left.png", plankLeft);
        layerHigh.registerMaterial("plank-right.png", plankRight);
        layerHigh.registerMaterial("plank-top.png", plankTop);
        layerHigh.distributeMaterials();

        Layer layerHigher = this.addLayer();
        layerHigher.setZIndex(ZIndex.MAP_HIGH + 1);
        layerHigher.setMatrix("/maps/city/layers/higher.txt", " ");
        layerHigher.registerMaterial("roof-rusty-slanted-left.png", roofRustySlantedLeft);
        layerHigher.registerMaterial("roof-rusty-slanted-right.png", roofRustySlantedRight);
        layerHigher.registerMaterial("roof-rusty.png", roofRusty);
        layerHigher.registerMaterial("roof-slanted-left.png", roofSlantedLeft);
        layerHigher.registerMaterial("roof-slanted-right.png", roofSlantedRight);
        layerHigher.registerMaterial("roof.png", roof);
        layerHigher.distributeMaterials();
    }
}
