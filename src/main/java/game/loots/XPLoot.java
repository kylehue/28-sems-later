package game.loots;

import game.Game;
import game.Progress;
import game.utils.Common;

public class XPLoot extends Loot {
    public XPLoot() {
        super("/loots/xp.png");
        setTimeLimitInMillis(120000);
        setAttractionDistance(100f);
    }
    
    @Override
    protected void handlePickUp() {
        Progress.currentXp.set((int) (Progress.currentXp.get() + Common.random(3, 10)));
    }
}
