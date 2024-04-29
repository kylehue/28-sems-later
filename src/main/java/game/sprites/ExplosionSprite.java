package game.sprites;

import game.utils.SpriteAnimation;
import utils.Common;

public class ExplosionSprite extends SpriteAnimation {
    public ExplosionSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/explosion-a.png"));
        this.setTileSize(64, 64);
        this.registerAnimations();
        this.set(Animation.DEFAULT);
    }
    
    public enum Animation {
        DEFAULT
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation);
    }
    
    private void registerAnimations() {
        this.registerAnimation(Animation.DEFAULT, new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(0, 1),
            SpriteAnimation.TileLocation.create(0, 2),
            SpriteAnimation.TileLocation.create(0, 3),
            SpriteAnimation.TileLocation.create(1, 0),
            SpriteAnimation.TileLocation.create(1, 1),
            SpriteAnimation.TileLocation.create(1, 2),
            SpriteAnimation.TileLocation.create(1, 3),
            SpriteAnimation.TileLocation.create(2, 0),
            SpriteAnimation.TileLocation.create(2, 1),
            SpriteAnimation.TileLocation.create(2, 2),
            SpriteAnimation.TileLocation.create(2, 3),
            SpriteAnimation.TileLocation.create(3, 0),
            SpriteAnimation.TileLocation.create(3, 1)
        });
    }
}
