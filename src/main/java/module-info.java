module it.unicam.cs.mpgc.rpg126598 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive com.google.gson;

    opens it.unicam.cs.mpgc.rpg126598 to javafx.fxml, com.google.gson;
    opens it.unicam.cs.mpgc.rpg126598.controller to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.model to javafx.fxml, com.google.gson;
    opens it.unicam.cs.mpgc.rpg126598.model.enums to javafx.fxml, com.google.gson;
    opens it.unicam.cs.mpgc.rpg126598.model.save to com.google.gson;
    opens it.unicam.cs.mpgc.rpg126598.strategy to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.service to javafx.fxml, com.google.gson;

    exports it.unicam.cs.mpgc.rpg126598;
    exports it.unicam.cs.mpgc.rpg126598.model;
    exports it.unicam.cs.mpgc.rpg126598.model.enums;
    exports it.unicam.cs.mpgc.rpg126598.model.save;
    exports it.unicam.cs.mpgc.rpg126598.service;
    exports it.unicam.cs.mpgc.rpg126598.strategy;
}