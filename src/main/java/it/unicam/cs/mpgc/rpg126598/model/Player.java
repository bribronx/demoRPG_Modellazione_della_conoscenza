package it.unicam.cs.mpgc.rpg126598.model;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Player extends Entity {
    @FXML
    private ImageView player;

    public Player() {
        this.setSpeed(0.4);
        this.setBoundBox(5, 8, 2, 5);
        this.setHealth(100);
        this.setMaxHealth(100);
        this.setDefense(50);
        this.setMaxDefense(50);
        this.setDamage(20);
        this.setLevel(1);
        this.setXp(0);
    }

}
