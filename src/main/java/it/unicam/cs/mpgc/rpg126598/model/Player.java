package it.unicam.cs.mpgc.rpg126598.model;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class Player extends Entity {
    @FXML
    private ImageView player;

    public Player() {
        this.setSpeed(4);
    }

    public ImageView getPlayer() {
        return player;
    }

    public void setPlayer(ImageView player) {
        this.player = player;
    }
}
