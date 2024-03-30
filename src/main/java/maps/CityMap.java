package maps;

import colliders.CircleCollider;
import colliders.Collider;
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
        float wallThickness = 5; // this value doubles so 4 becomes 8-pixel thick
        PolygonCollider bottomWallCollider = new PolygonCollider(new Vector[]{
            new Vector(-halfTile, -wallThickness),
            new Vector(halfTile, -wallThickness),
            new Vector(halfTile, wallThickness),
            new Vector(-halfTile, wallThickness)
        });
        bottomWallCollider.setGroup(CollisionGroup.MAP);
        bottomWallCollider.setStatic(true);
        bottomWallCollider.getPosition().setY(halfTile - wallThickness);
        
        PolygonCollider leftWallCollider = new PolygonCollider(new Vector[]{
            new Vector(-wallThickness, -halfTile),
            new Vector(wallThickness, -halfTile),
            new Vector(wallThickness, halfTile),
            new Vector(-wallThickness, halfTile)
        });
        leftWallCollider.setGroup(CollisionGroup.MAP);
        leftWallCollider.setStatic(true);
        leftWallCollider.getPosition().setX(-halfTile + wallThickness);
        
        PolygonCollider rightWallCollider = leftWallCollider.clone();
        rightWallCollider.getPosition().setX(halfTile - wallThickness);
        
        // TODO: make grouped collider because we don't allow concave shapes
        // PolygonCollider topLeftCorner = new PolygonCollider(new Vector[]{
        //     new Vector(-halfTile, -halfTile),
        //     new Vector(halfTile, -halfTile),
        //     new Vector(halfTile, -halfTile + wallThickness * 2),
        //     new Vector(-halfTile + wallThickness * 2, -halfTile + wallThickness * 2),
        //     new Vector(-halfTile + wallThickness * 2, halfTile),
        //     new Vector(-halfTile, halfTile)
        // });
        // topLeftCorner.getPosition().set(inset, inset);
        // topLeftCorner.setGroup(CollisionGroup.MAP);
        // topLeftCorner.setStatic(true);
        
        // MATERIALS
        Material fenceBackSignedLeft = new Material("/maps/city/tiles/fence-back-signed-left.png");
        fenceBackSignedLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceBackSignedRight1 = new Material("/maps/city/tiles/fence-back-signed-right-1.png");
        fenceBackSignedRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceBackSignedRight = new Material("/maps/city/tiles/fence-back-signed-right.png");
        fenceBackSignedRight.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceFrontSignedLeft = new Material("/maps/city/tiles/fence-front-signed-left.png");
        fenceFrontSignedLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceLeft = new Material("/maps/city/tiles/fence-left.png");
        fenceLeft.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceMiddle = new Material("/maps/city/tiles/fence-middle.png");
        fenceMiddle.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceRight = new Material("/maps/city/tiles/fence-right.png");
        fenceRight.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material fenceSideLeft = new Material("/maps/city/tiles/fence-side-left.png");
        fenceSideLeft.setPositionOrigin(Material.PositionOrigin.LEFT);
        Material fenceSideRight = new Material("/maps/city/tiles/fence-side-right.png");
        fenceSideRight.setPositionOrigin(Material.PositionOrigin.RIGHT);
        Material grass0 = new Material("/maps/city/tiles/grass-0.png");
        Material grass1 = new Material("/maps/city/tiles/grass-1.png");
        Material grass2 = new Material("/maps/city/tiles/grass-2.png");
        Material grass3 = new Material("/maps/city/tiles/grass-3.png");
        Material grass4 = new Material("/maps/city/tiles/grass-4.png");
        Material plankBottomShort = new Material("/maps/city/decorations/plank-bottom-short.png");
        plankBottomShort.setPositionOrigin(Material.PositionOrigin.TOP);
        Material plankBottom = new Material("/maps/city/decorations/plank-bottom.png");
        Material plankCrossed = new Material("/maps/city/decorations/plank-crossed.png");
        Material plankHorizontalMiddle = new Material("/maps/city/decorations/plank-horizontal-middle.png");
        Material plankVerticalMiddle = plankHorizontalMiddle.clone();
        plankVerticalMiddle.setRotation(-90);
        Material plankLeft = new Material("/maps/city/decorations/plank-left.png");
        Material plankRight = new Material("/maps/city/decorations/plank-right.png");
        Material plankTop = new Material("/maps/city/decorations/plank-top.png");
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
        Material roofRustySlantedLeft = new Material("/maps/city/decorations/roof-rusty-slanted-left.png");
        Material roofRustySlantedRight = new Material("/maps/city/decorations/roof-rusty-slanted-right.png");
        Material roofRusty = new Material("/maps/city/decorations/roof-rusty.png");
        Material roofSlantedLeft = new Material("/maps/city/decorations/roof-slanted-left.png");
        Material roofSlantedRight = new Material("/maps/city/decorations/roof-slanted-right.png");
        Material roof = new Material("/maps/city/decorations/roof.png");
        Material roomFloor0 = new Material("/maps/city/tiles/room-floor-0.png");
        Material roomFloor1 = new Material("/maps/city/tiles/room-floor-1.png");
        Material roomFloor2 = new Material("/maps/city/tiles/room-floor-2.png");
        Material roomFloor3 = new Material("/maps/city/tiles/room-floor-3.png");
        Material roomWallCornerBottomLeft0 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-0.png");
        roomWallCornerBottomLeft0.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerBottomLeft1 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-1.png");
        roomWallCornerBottomLeft1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerBottomLeft2 = new Material("/maps/city/tiles/room-wall-corner-bottom-left-2.png");
        roomWallCornerBottomLeft2.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerBottomRight0 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-0.png");
        roomWallCornerBottomRight0.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerBottomRight1 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-1.png");
        roomWallCornerBottomRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerBottomRight2 = new Material("/maps/city/tiles/room-wall-corner-bottom-right-2.png");
        roomWallCornerBottomRight2.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallCornerTopLeft0 = new Material("/maps/city/tiles/room-wall-corner-top-left-0.png");
        roomWallCornerTopLeft0.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft0.setCollider(bottomWallCollider.clone());
        Material roomWallCornerTopLeft1 = new Material("/maps/city/tiles/room-wall-corner-top-left-1.png");
        roomWallCornerTopLeft1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft1.setCollider(bottomWallCollider.clone());
        Material roomWallCornerTopLeft2 = new Material("/maps/city/tiles/room-wall-corner-top-left-2.png");
        roomWallCornerTopLeft2.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopLeft2.setCollider(bottomWallCollider.clone());
        Material roomWallCornerTopRight0 = new Material("/maps/city/tiles/room-wall-corner-top-right-0.png");
        roomWallCornerTopRight0.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight0.setCollider(bottomWallCollider.clone());
        Material roomWallCornerTopRight1 = new Material("/maps/city/tiles/room-wall-corner-top-right-1.png");
        roomWallCornerTopRight1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight1.setCollider(bottomWallCollider.clone());
        Material roomWallCornerTopRight2 = new Material("/maps/city/tiles/room-wall-corner-top-right-2.png");
        roomWallCornerTopRight2.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallCornerTopRight2.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle0 = new Material("/maps/city/tiles/room-wall-middle-0.png");
        roomWallMiddle0.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle0.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle1 = new Material("/maps/city/tiles/room-wall-middle-1.png");
        roomWallMiddle1.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle1.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle2 = new Material("/maps/city/tiles/room-wall-middle-2.png");
        roomWallMiddle2.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle2.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle3 = new Material("/maps/city/tiles/room-wall-middle-3.png");
        roomWallMiddle3.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle3.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle4 = new Material("/maps/city/tiles/room-wall-middle-4.png");
        roomWallMiddle4.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle4.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle5 = new Material("/maps/city/tiles/room-wall-middle-5.png");
        roomWallMiddle5.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle5.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle6 = new Material("/maps/city/tiles/room-wall-middle-6.png");
        roomWallMiddle6.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle6.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle7 = new Material("/maps/city/tiles/room-wall-middle-7.png");
        roomWallMiddle7.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle7.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle8 = new Material("/maps/city/tiles/room-wall-middle-8.png");
        roomWallMiddle8.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        roomWallMiddle8.setCollider(bottomWallCollider.clone());
        Material roomWallMiddle9 = new Material("/maps/city/tiles/room-wall-middle-9.png");
        roomWallMiddle9.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        Material roomWallSideRight = new Material("/maps/city/tiles/room-wall-side.png");
        roomWallSideRight.setPositionOrigin(Material.PositionOrigin.RIGHT);
        roomWallSideRight.setCollider(rightWallCollider.clone());
        Material roomWallSideLeft = roomWallSideRight.clone();
        roomWallSideLeft.setPositionOrigin(Material.PositionOrigin.LEFT);
        roomWallSideLeft.setCollider(leftWallCollider.clone());
        Material sand0 = new Material("/maps/city/tiles/sand-0.png");
        Material sand1 = new Material("/maps/city/tiles/sand-1.png");
        Material sand2 = new Material("/maps/city/tiles/sand-2.png");
        Material sand3 = new Material("/maps/city/tiles/sand-3.png");
        Material sidewalk0 = new Material("/maps/city/tiles/sidewalk-0.png");
        Material sidewalk1 = new Material("/maps/city/tiles/sidewalk-1.png");
        Material sidewalk2 = new Material("/maps/city/tiles/sidewalk-2.png");
        Material sidewalk3 = new Material("/maps/city/tiles/sidewalk-3.png");
        Material stopSign = new Material("/maps/city/decorations/stop-sign.png");
        stopSign.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        stopSign.getPosition().addY(
            (float) -this.getTileSize() / 2
        );
        CircleCollider stopSignCollider = new CircleCollider();
        stopSignCollider.setRadius(3);
        stopSignCollider.setGroup(CollisionGroup.MAP);
        stopSignCollider.setStatic(true);
        stopSignCollider.getPosition().setY(halfTile);
        stopSign.setCollider(stopSignCollider);
        
        
        Material streetLamp = new Material("/maps/city/decorations/street-lamp.png");
        streetLamp.getPosition().add(
            (float) this.getTileSize() / 2,
            (float) -this.getTileSize() / 2
        );
        streetLamp.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        CircleCollider streetLampCollider = new CircleCollider();
        streetLampCollider.setRadius(3);
        streetLampCollider.setGroup(CollisionGroup.MAP);
        streetLampCollider.setStatic(true);
        streetLampCollider.getPosition().set(-halfTile, halfTile);
        streetLamp.setCollider(streetLampCollider);
        
        Material trashCan = new Material("/maps/city/decorations/trash-can.png");
        trashCan.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        trashCan.getOriginOffset().addY(-10);
        CircleCollider trashCanCollider = new CircleCollider();
        trashCanCollider.setRadius(5);
        trashCanCollider.setGroup(CollisionGroup.MAP);
        trashCanCollider.addToGroup(CollisionGroup.MAP);
        trashCanCollider.setMass(100);
        trashCan.setCollider(trashCanCollider);
        
        Material barrel = new Material("/maps/city/decorations/barrel.png");
        barrel.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        barrel.getOriginOffset().addY(-6);
        CircleCollider barrelCollider = new CircleCollider();
        barrelCollider.setRadius(8);
        barrelCollider.setGroup(CollisionGroup.MAP);
        barrelCollider.addToGroup(CollisionGroup.MAP);
        barrelCollider.setMass(400);
        barrel.setCollider(barrelCollider);
        
        Material car = new Material("/maps/city/decorations/car.png");
        float carWidthHalf = (float) car.getImage().getWidth() / 2;
        float carHeightHalf = (float) car.getImage().getHeight() / 3;
        PolygonCollider carCollider = new PolygonCollider(new Vector[]{
            new Vector(-carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, -carHeightHalf),
            new Vector(carWidthHalf, carHeightHalf),
            new Vector(-carWidthHalf, carHeightHalf)
        });
        carCollider.setGroup(CollisionGroup.MAP);
        carCollider.addToGroup(CollisionGroup.MAP);
        carCollider.setMass(20000);
        car.setCollider(carCollider);
        
        // Material barrel = new Material("/maps/city/decorations/barrel.png");
        // barrel.setZIndex(ZIndex.MAP_DECORATIONS);
        // barrel.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        // barrel.getOriginOffset().addY(10);
        //
        // CircleCollider barrelCollider = new CircleCollider();
        // barrelCollider.setRadius(10);
        // barrelCollider.setGroup(CollisionGroup.MAP_TILES);
        // barrelCollider.addToGroup(CollisionGroup.MAP_TILES);
        // barrelCollider.setMass(400);
        // barrel.setCollider(barrelCollider);
        //
        // Material car = new Material("/maps/city/decorations/car.png");
        // car.setZIndex(ZIndex.MAP_DECORATIONS);
        // car.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        // car.getOriginOffset().addY(15);
        //
        // float carWidthHalf = (float) car.getImage().getWidth() / 2;
        // float carHeightHalf = (float) car.getImage().getHeight() / 3;
        // PolygonCollider carCollider = new PolygonCollider(new Vector[]{
        //     new Vector(-carWidthHalf, -carHeightHalf),
        //     new Vector(carWidthHalf, -carHeightHalf),
        //     new Vector(carWidthHalf, carHeightHalf),
        //     new Vector(-carWidthHalf, carHeightHalf)
        // });
        // carCollider.setGroup(CollisionGroup.MAP_TILES);
        // carCollider.setMass(10000);
        // car.setCollider(carCollider);
        //
        // Material stopSign = new Material("/maps/city/decorations/stop-sign.png");
        // stopSign.setZIndex(ZIndex.MAP_DECORATIONS);
        // stopSign.setPositionOrigin(Material.PositionOrigin.BOTTOM);
        //
        // CircleCollider stopSignCollider = new CircleCollider();
        // stopSignCollider.setRadius(2);
        // stopSignCollider.setStatic(true);
        // stopSignCollider.setGroup(CollisionGroup.MAP_TILES);
        // stopSign.setCollider(stopSignCollider);
        
        // LAYERS
        
        // Layer layerLow = this.addLayer();
        // layerLow.setMatrix("/maps/city/layers/low.txt", " ");
        // layerLow.registerMaterial("0", barrel);
        // layerLow.registerMaterial("1", car);
        // layerLow.registerMaterial("2", stopSign);
        // layerLow.distributeMaterials();
        
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
