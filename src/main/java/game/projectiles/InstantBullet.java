package game.projectiles;

import game.Game;
import game.colliders.CircleCollider;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public class InstantBullet extends Projectile {
    private float penetration = 0;
    private float opacity = 1;
    
    public InstantBullet(Vector initialPosition, float angle) {
        super(initialPosition, angle);
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        ctx.save();
        ctx.setGlobalAlpha(opacity);
        ctx.setStroke(Paint.valueOf("white"));
        ctx.setLineWidth(Math.pow(opacity + 1, 2));
        ctx.beginPath();
        ctx.moveTo(position.getX(), position.getY());
        ctx.lineTo(
            position.getX() + Math.cos(angle) * 5000,
            position.getY() + Math.sin(angle) * 5000
        );
        ctx.closePath();
        ctx.stroke();
        ctx.restore();
    }
    
    @Override
    public void fixedUpdate(float deltaTime) {
        if (opacity >= 0.001) {
            opacity *= 0.97f;
        } else {
            opacity = 0;
            dispose();
        }
    }
    
    public void setPenetration(float penetration) {
        this.penetration = penetration;
    }
    
    public float getPenetration() {
        return penetration;
    }
}
