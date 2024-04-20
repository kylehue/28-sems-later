package game;

import game.inventory.WeaponKind;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public abstract class Progress {
    public static final ObservableSet<WeaponKind> unlockedWeapons = FXCollections.observableSet(WeaponKind.PISTOL, WeaponKind.SHOTGUN);
}
