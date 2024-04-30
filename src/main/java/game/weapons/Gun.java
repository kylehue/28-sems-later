package game.weapons;

import game.World;
import game.utils.IntervalMap;
import game.utils.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import utils.Common;

public abstract class Gun extends Weapon {
    private final static int MIN_SOUND_INTERVAL_MILLIS = 60;
    private final Vector muzzlePosition = new Vector();
    protected int fireRateInMillis = 1000;
    private final Image muzzleFlashImage = Common.loadImage("/weapons/muzzle-flash.png");
    private float muzzleFlashOpacity = 0;
    private boolean isMuzzleFlashEnabled = true;
    private final IntervalMap intervals = new IntervalMap();
    
    private enum Interval {
        EMIT_SOUND,
        SHOOT
    }
    
    public Gun(String imageUrl) {
        super(imageUrl);
        
        intervals.registerIntervalFor(Interval.EMIT_SOUND, MIN_SOUND_INTERVAL_MILLIS);
        intervals.registerIntervalFor(Interval.SHOOT, fireRateInMillis);
    }
    
    public void setMuzzlePosition(Vector vector) {
        muzzlePosition.set(vector);
    }
    
    public void setFireRateInMillis(int fireRateInMillis) {
        this.fireRateInMillis = fireRateInMillis;
    }
    
    public int getFireRateInMillis() {
        return fireRateInMillis;
    }
    
    public Vector getMuzzlePosition() {
        return muzzlePosition.clone();
    }
    
    protected abstract void handleShoot(
        World world,
        Vector initialPosition,
        float angle
    );
    
    protected abstract void handleSound(Vector initialPosition);
    
    public void shoot(World world, Vector initialPosition, float angle) {
        if (!intervals.isIntervalOverFor(Interval.SHOOT)) return;
        float xOffset = muzzlePosition.getX() - getHandlePosition().getX() + 4;
        Vector computedInitialPosition = initialPosition.clone().add(
            (float) (Math.cos(angle) * xOffset),
            (float) (Math.sin(angle) * xOffset)
        );
        
        setHandlePosition(
            getOrigHandlePosition().addX(
                (float) Math.pow(fireRateInMillis, 0.33f)
            )
        );
        muzzleFlashOpacity = 1;
        
        handleShoot(world, computedInitialPosition, angle);
        
        if (intervals.isIntervalOverFor(Interval.EMIT_SOUND)) {
            handleSound(initialPosition);
            intervals.resetIntervalFor(Interval.EMIT_SOUND);
        }
        
        intervals.resetIntervalFor(Interval.SHOOT);
        intervals.changeIntervalFor(Interval.SHOOT, fireRateInMillis);
    }
    
    public void setMuzzleFlashEnabled(boolean muzzleFlashEnabled) {
        isMuzzleFlashEnabled = muzzleFlashEnabled;
    }
    
    @Override
    public void render(GraphicsContext ctx, float alpha) {
        super.render(ctx, alpha);
        if (!isMuzzleFlashEnabled) return;
        muzzleFlashOpacity = game.utils.Common.lerp(muzzleFlashOpacity, 0, 0.4f);
        ctx.save();
        ctx.setGlobalAlpha(muzzleFlashOpacity);
        ctx.drawImage(
            muzzleFlashImage,
            -muzzleFlashImage.getWidth() / 2 - (getOrigHandlePosition().getX() - getMuzzlePosition().getX()) + 2,
            -muzzleFlashImage.getHeight() / 2 - (getOrigHandlePosition().getY() - getMuzzlePosition().getY()) + 2,
            muzzleFlashImage.getWidth(),
            muzzleFlashImage.getHeight()
        );
        ctx.restore();
    }
}
