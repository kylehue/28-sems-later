package game.inventory.weapons;

public class GrenadeLauncher extends Gun {
    private float aoeDistance = 60;
    
    public GrenadeLauncher() {
        super("/weapons/grenade-launcher.png");
        setFireRateInMillis(500);
        muzzlePosition.set(41, 4);
        handlePosition.set(24, 8);
    }
    
    public void setAoeDistance(float aoeDistance) {
        this.aoeDistance = aoeDistance;
    }
    
    public float getAoeDistance() {
        return aoeDistance;
    }
}
