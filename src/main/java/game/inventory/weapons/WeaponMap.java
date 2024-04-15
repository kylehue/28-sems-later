package game.inventory.weapons;

import java.util.HashMap;

public abstract class WeaponMap {
    public enum Kind {
        PISTOL,
        RIFLE,
        SNIPER,
    }
    
    private static final HashMap<Kind, Weapon> map = new HashMap<>() {
        {
            put(Kind.RIFLE, new Rifle());
            put(Kind.PISTOL, new Pistol());
            put(Kind.SNIPER, new Sniper());
        }
    };
    
    public static Weapon get(Kind weaponKind) {
        return map.get(weaponKind);
    }
}
