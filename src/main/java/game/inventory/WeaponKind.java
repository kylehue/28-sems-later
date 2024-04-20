package game.inventory;

import game.inventory.weapons.*;

public enum WeaponKind {
    PISTOL(new Pistol()),
    RIFLE(new Rifle()),
    SHOTGUN(new Shotgun()),
    SNIPER(new Sniper()),
    GRENADE_LAUNCHER(new GrenadeLauncher());
    
    private final Weapon weapon;
    WeaponKind(Weapon weapon) {
        this.weapon = weapon;
    }
    
    public Weapon get() {
        return weapon;
    }
}