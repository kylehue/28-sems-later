module samplegame {
    requires javafx.base;
    requires javafx.controls;
    
    opens main;
    opens scenes.game;
    opens utils;
    opens map;
}