package sprites;

import utils.LayoutUtils;
import utils.SpriteAnimation;

public class PlayerSprite extends SpriteAnimation {
    public PlayerSprite() {
        this.setSpriteSheet(LayoutUtils.loadImage("/sprites/Character-and-Zombie.png"));
        this.setTileSize(48, 30);
        // this.setSize(80, 80);
        this.registerAnimations();
        this.setAnimation("idle");
    }
    
    public enum Animation {
        Walk,
        Idle,
        Reload,
        Shoot
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation.name().toLowerCase());
    }
    
    private void registerAnimations() {
        // Idle
        this.registerAnimation("idle", new TileLocation[]{
            SpriteAnimation.TileLocation.create(0, 0),
            SpriteAnimation.TileLocation.create(0, 1),
            SpriteAnimation.TileLocation.create(0, 2),
            SpriteAnimation.TileLocation.create(0, 3),
            SpriteAnimation.TileLocation.create(0, 4),
            SpriteAnimation.TileLocation.create(0, 5)
        });
        
        // Reload
        this.registerAnimation("reload", new TileLocation[]{
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
            SpriteAnimation.TileLocation.create(1, 10)
        });
        
        // Shoot
        this.registerAnimation("shoot", new TileLocation[]{
            SpriteAnimation.TileLocation.create(2, 0),
            SpriteAnimation.TileLocation.create(2, 1),
            SpriteAnimation.TileLocation.create(2, 2),
            SpriteAnimation.TileLocation.create(2, 3)
        });
        
        // Walk
        this.registerAnimation("walk", new TileLocation[]{
            SpriteAnimation.TileLocation.create(3, 0),
            SpriteAnimation.TileLocation.create(3, 1),
            SpriteAnimation.TileLocation.create(3, 2),
            SpriteAnimation.TileLocation.create(3, 3),
            SpriteAnimation.TileLocation.create(3, 4),
            SpriteAnimation.TileLocation.create(3, 5),
            SpriteAnimation.TileLocation.create(3, 6)
        });
    }
}
