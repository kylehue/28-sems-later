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
        Progress.PLAYER_CURRENT_XP.set((int) (Progress.PLAYER_CURRENT_XP.get() + Common.random(3, 10)));
        Game.world.addPlayerDistanceAwareAudio(
            "/sounds/xp.mp3",
            position,
            200
        );
    }
}
