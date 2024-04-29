package game.sprites;

import utils.Common;
import game.utils.SpriteAnimation;

public class ZombieSprite extends SpriteAnimation {
    public ZombieSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/zombie-a.png"));
        this.setTileSize(22, 23);
        this.registerAnimations();
        this.set(Animation.WALK);
    }
    
    public enum Animation {
        WALK
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation);
    }
    
    private void registerAnimations() {
        this.registerAnimation(Animation.WALK, new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(0, 0),
            SpriteAnimation.TileLocation.create(0, 1),
            SpriteAnimation.TileLocation.create(0, 2),
            SpriteAnimation.TileLocation.create(0, 3),
            SpriteAnimation.TileLocation.create(0, 4),
            SpriteAnimation.TileLocation.create(0, 5),
            SpriteAnimation.TileLocation.create(0, 6)
        });
    }
}
