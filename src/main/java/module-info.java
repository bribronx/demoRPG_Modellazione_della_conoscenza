module it.unicam.cs.mpgc.rpg126598 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens it.unicam.cs.mpgc.rpg126598 to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.controller to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.model to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.strategy to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.service to javafx.fxml;

    exports it.unicam.cs.mpgc.rpg126598;
    exports it.unicam.cs.mpgc.rpg126598.model;
    exports it.unicam.cs.mpgc.rpg126598.service;
    exports it.unicam.cs.mpgc.rpg126598.strategy;
}