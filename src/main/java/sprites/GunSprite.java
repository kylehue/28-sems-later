package sprites;

import utils.LayoutUtils;
import utils.SpriteAnimation;

public class GunSprite extends SpriteAnimation {
    public GunSprite() {
        this.setSpriteSheet(LayoutUtils.loadImage("/sprites/Gun-Sprite-Sheet.png"));
        this.setTileSize(48, 30);
        this.registerAnimations();
        this.set(Animation.Idle);
    }
    
    public enum Animation {
        Idle,
        Reload,
        Shoot
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation.name());
    }
    
    private void registerAnimations() {
        // Shoot
        this.registerAnimation(Animation.Shoot.name(), new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(0, 0),
            SpriteAnimation.TileLocation.create(0, 1),
            SpriteAnimation.TileLocation.create(0, 2),
            SpriteAnimation.TileLocation.create(0, 3),
            SpriteAnimation.TileLocation.create(0, 4),
            SpriteAnimation.TileLocation.create(0, 5),
            SpriteAnimation.TileLocation.create(0, 6)
        });
        
        // Reload
        this.registerAnimation(Animation.Reload.name(), new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(1, 0),
            SpriteAnimation.TileLocation.create(1, 1),
            SpriteAnimation.TileLocation.create(1, 2),
            SpriteAnimation.TileLocation.create(1, 3),
            SpriteAnimation.TileLocation.create(1, 4),
            SpriteAnimation.TileLocation.create(1, 5),
            SpriteAnimation.TileLocation.create(1, 6),
            SpriteAnimation.TileLocation.create(1, 7),
            SpriteAnimation.TileLocation.create(1, 8),
            SpriteAnimation.TileLocation.create(1, 9),
            SpriteAnimation.TileLocation.create(1, 10),
            SpriteAnimation.TileLocation.create(1, 11),
            SpriteAnimation.TileLocation.create(1, 12),
            SpriteAnimation.TileLocation.create(1, 13)
        });
        
        // Idle
        this.registerAnimation(Animation.Idle.name(), new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(2, 0),
            SpriteAnimation.TileLocation.create(2, 1),
            SpriteAnimation.TileLocation.create(2, 2),
            SpriteAnimation.TileLocation.create(2, 3)
        });
    }
}
