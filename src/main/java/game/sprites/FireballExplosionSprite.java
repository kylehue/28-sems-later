package game.sprites;

import game.utils.SpriteAnimation;
import utils.Common;

public class FireballExplosionSprite extends SpriteAnimation {
    public FireballExplosionSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/fireball-explosion.png"));
        this.setTileSize(40, 32);
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
            SpriteAnimation.TileLocation.create(0, 0),
            SpriteAnimation.TileLocation.create(0, 1),
            SpriteAnimation.TileLocation.create(0, 2),
            SpriteAnimation.TileLocation.create(0, 3),
            SpriteAnimation.TileLocation.create(0, 4),
            SpriteAnimation.TileLocation.create(0, 5)
        });
    }
}
