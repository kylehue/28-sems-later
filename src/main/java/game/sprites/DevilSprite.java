package game.sprites;

import game.utils.SpriteAnimation;
import utils.Common;

public class DevilSprite extends SpriteAnimation {
    public DevilSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/devil.png"));
        this.setTileSize(32, 33);
        this.registerAnimations();
        this.set(Animation.WALK);
    }
    
    public enum Animation {
        WALK,
        SHOOT
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
        
        this.registerAnimation(Animation.SHOOT, new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(1, 0),
            SpriteAnimation.TileLocation.create(1, 1),
            SpriteAnimation.TileLocation.create(1, 2),
            SpriteAnimation.TileLocation.create(1, 3),
            SpriteAnimation.TileLocation.create(1, 4),
            SpriteAnimation.TileLocation.create(1, 5),
            SpriteAnimation.TileLocation.create(1, 6)
        });
    }
}
