package game.sprites;

import utils.Common;
import game.utils.SpriteAnimation;

public class DashSprite extends SpriteAnimation {
    public DashSprite() {
        this.setSpriteSheet(Common.loadImage("/sprites/dash-smoke-a.png"));
        this.setTileSize(43, 53);
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
            SpriteAnimation.TileLocation.create(0, 6),
            SpriteAnimation.TileLocation.create(0, 7),
            SpriteAnimation.TileLocation.create(0, 8),
            SpriteAnimation.TileLocation.create(0, 9)
        });
    }
}
