package sprites;

import utils.LayoutUtils;
import utils.SpriteAnimation;

public class ZombieSprite extends SpriteAnimation {
    public ZombieSprite() {
        this.setSpriteSheet(LayoutUtils.loadImage("/sprites/Character-and-Zombie.png"));
        this.setTileSize(48, 30);
        this.registerAnimations();
        this.set(Animation.Walk);
    }
    
    public enum Animation {
        Walk
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation.name());
    }
    
    private void registerAnimations() {
        this.registerAnimation(Animation.Walk.name(), new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(4, 0),
            SpriteAnimation.TileLocation.create(4, 1),
            SpriteAnimation.TileLocation.create(4, 2),
            SpriteAnimation.TileLocation.create(4, 3),
            SpriteAnimation.TileLocation.create(4, 4),
            SpriteAnimation.TileLocation.create(4, 5),
            SpriteAnimation.TileLocation.create(4, 6)
        });
    }
}
