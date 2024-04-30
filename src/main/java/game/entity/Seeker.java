package game.entity;

import game.Game;
import game.colliders.Collider;
import game.colliders.CollisionResolvers;
import game.map.Layer;
import game.map.Material;
import game.utils.PathFinder;
import game.utils.IntervalMap;
import game.utils.Vector;
import javafx.concurrent.Task;
import utils.Async;

import java.util.ArrayList;

public abstract class Seeker extends Entity {
    private ArrayList<Vector> pathToSeek = new ArrayList<>();
    private float angleToSeek = 0;
    private final Vector positionToSeek = new Vector();
    private boolean isPathClear = false;
    private final IntervalMap intervalMap = new IntervalMap();
    private boolean isFacingOnLeftSide = false;
    
    private enum Interval {
        UPDATE_PATH,
        UPDATE_IS_PATH_CLEAR
    }
    
    public Seeker() {
        intervalMap.registerIntervalFor(
            Interval.UPDATE_PATH,
            150
        );
        intervalMap.registerIntervalFor(
            Interval.UPDATE_IS_PATH_CLEAR,
            100
        );
    }
    
    /**
     * Checks if the straight line from this to player
     * has no obstacles.
     */
    private boolean _isPathClear() {
        for (Layer layer : Game.world.getMap().getLayers()) {
            for (Material material : layer.getMaterials()) {
                Collider obstacle = material.getCollider();
                if (obstacle == null) continue;
                
                Vector intersectionPoint = CollisionResolvers.getLineToColliderIntersectionPoint(
                    position,
                    positionToSeek,
                    obstacle
                );
                
                if (intersectionPoint != null) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    protected boolean isPathClear() {
        return isPathClear;
    }
    
    protected abstract void handleSeek(float angle);
    
    protected void seek(Vector positionToSeek) {
        this.positionToSeek.set(positionToSeek);
        
        maybeUpdateIfPathIsClear();
        maybeUpdatePathToSeek();
        maybeUpdateAngleToSeek();
        handleSeek(angleToSeek);
    }
    
    protected boolean isFacingOnLeftSide() {
        return isFacingOnLeftSide;
    }
    
    private void maybeUpdateAngleToSeek() {
        // Use straightforward angle if path is clear
        if (isPathClear) {
            angleToSeek = position.getAngle(positionToSeek);
        }
        
        // Use pathfinder if path has obstacles
        if (!isPathClear && pathToSeek.size() > 1) {
            Vector step = pathToSeek.get(Math.max(0, pathToSeek.size() - 2));
            angleToSeek = position.getAngle(step);
        }
        
        isFacingOnLeftSide = Math.abs(angleToSeek) > (Math.PI / 2);
    }
    
    private void maybeUpdateIfPathIsClear() {
        if (intervalMap.isIntervalOverFor(Interval.UPDATE_IS_PATH_CLEAR)) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    Seeker.this.isPathClear = Seeker.this._isPathClear();
                    return null;
                }
            };
            
            task.setOnFailed(System.out::println);
            Async.queue1.submit(task);
            
            intervalMap.resetIntervalFor(Interval.UPDATE_IS_PATH_CLEAR);
        }
    }
    
    private void maybeUpdatePathToSeek() {
        if (isPathClear) return;
        
        if (intervalMap.isIntervalOverFor(Interval.UPDATE_PATH)) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    PathFinder pathFinder = Game.world.getPathFinder();
                    pathToSeek = pathFinder.requestPath(
                        position,
                        positionToSeek
                    );
                    return null;
                }
            };
            
            task.setOnFailed(System.out::println);
            Async.queue1.submit(task);
            
            intervalMap.resetIntervalFor(Interval.UPDATE_PATH);
        }
    }
}
