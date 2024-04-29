package game.sprites;

import game.utils.SpriteAnimation;
import utils.Common;

public class FireballSprite extends SpriteAnimation {
    public FireballSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/fireball.png"));
        this.setTileSize(11, 11);
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
            SpriteAnimation.TileLocation.create(0, 5),
            SpriteAnimation.TileLocation.create(0, 6)
        });
    }
}
