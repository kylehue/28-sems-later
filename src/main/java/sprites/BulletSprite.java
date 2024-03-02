package sprites;

import utils.LayoutUtils;
import utils.SpriteAnimation;

public class BulletSprite extends SpriteAnimation {
    public BulletSprite() {
        this.setSpriteSheet(LayoutUtils.loadImage("/sprites/Bullet-Sprite-Sheet.png"));
        this.setTileSize(48, 30);
        this.registerAnimations();
        this.set(Animation.Idle);
    }
    
    public enum Animation {
        Idle
    }
    
    public void set(Animation animation) {
        this.setAnimation(animation.name());
    }
    
    private void registerAnimations() {
        // Idle
        this.registerAnimation(Animation.Idle.name(), new SpriteAnimation.TileLocation[]{
            SpriteAnimation.TileLocation.create(0, 0)
        });
    }
}
