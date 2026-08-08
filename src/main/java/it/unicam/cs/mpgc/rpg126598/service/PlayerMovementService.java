package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

import java.util.Objects;

public class PlayerMovementService {

    private final Player player;

    public PlayerMovementService(Player player) {
        this.player = player;
    }

    public void makeMove(KeyEvent e){
        ImageView imageP= player.getPlayer();
        switch (e.getCode()) {
            case W -> imageP.setTranslateY(imageP.getTranslateY()-player.getSpeed());
            case S -> imageP.setTranslateY(imageP.getTranslateY()+player.getSpeed());
            case A -> imageP.setTranslateX(imageP.getTranslateX()-player.getSpeed());
            case D -> imageP.setTranslateX(imageP.getTranslateX()+player.getSpeed());
        }
    }

}
