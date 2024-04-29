package game.sprites;

import game.utils.SpriteAnimation;
import utils.Common;

public class AcidSprite extends SpriteAnimation {
    public AcidSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/acid-a.png"));
        this.setTileSize(32, 32);
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
            // SpriteAnimation.TileLocation.create(0, 0),
            // SpriteAnimation.TileLocation.create(0, 1),
            // SpriteAnimation.TileLocation.create(0, 2),
            // SpriteAnimation.TileLocation.create(0, 3),
            // SpriteAnimation.TileLocation.create(0, 4),
            // SpriteAnimation.TileLocation.create(0, 5),
            // SpriteAnimation.TileLocation.create(0, 6),
            // SpriteAnimation.TileLocation.create(0, 7),
            // SpriteAnimation.TileLocation.create(0, 8),
            // SpriteAnimation.TileLocation.create(0, 9),
            // SpriteAnimation.TileLocation.create(0, 10),
            // SpriteAnimation.TileLocation.create(0, 11),
            // SpriteAnimation.TileLocation.create(0, 12),
            // SpriteAnimation.TileLocation.create(0, 13),
            // SpriteAnimation.TileLocation.create(0, 14)
            SpriteAnimation.TileLocation.create(0, 9),
            SpriteAnimation.TileLocation.create(0, 10),
            SpriteAnimation.TileLocation.create(0, 11),
            SpriteAnimation.TileLocation.create(0, 12),
            SpriteAnimation.TileLocation.create(0, 13),
            SpriteAnimation.TileLocation.create(0, 14)
        });
    }
}
