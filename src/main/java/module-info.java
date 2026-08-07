module it.unicam.cs.mpgc.rpg126598 {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.unicam.cs.mpgc.rpg126598 to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.controller to javafx.fxml;
    opens it.unicam.cs.mpgc.rpg126598.model to javafx.fxml;
    exports it.unicam.cs.mpgc.rpg126598;
}