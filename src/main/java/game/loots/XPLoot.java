package game.loots;

import game.Game;
import game.Progress;

public class XPLoot extends Loot {
    public XPLoot() {
        super("/loots/xp.png");
        setTimeLimitInMillis(60000);
    }
    
    @Override
    protected void handlePickUp() {
        Progress.currentXp.set(Progress.currentXp.get() + 1);
    }
}
