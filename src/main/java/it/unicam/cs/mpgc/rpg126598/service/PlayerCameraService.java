package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class PlayerCameraService {

    private final Player player;

    private final Pane mainPane;

    private final int zoom;

    public PlayerCameraService(Player player, Pane mainPane, int zoom) {
        this.player = player;
        this.mainPane = mainPane;
        this.zoom = zoom;
    }

    public void updateCamera() {
        ImageView imageP = player.getPlayer();
        double playerX = imageP.getLayoutX() + imageP.getTranslateX() + imageP.getFitWidth() / 2;
        double playerY = imageP.getLayoutY() + imageP.getTranslateY() + imageP.getFitHeight() / 2;

        double sceneWidth = mainPane.getPrefWidth();
        double sceneHeight = mainPane.getPrefHeight();

        if (mainPane.getScene() != null) {
            if (mainPane.getScene().getWidth() > 0) {
                sceneWidth = mainPane.getScene().getWidth();
            }
            if (mainPane.getScene().getHeight() > 0) {
                sceneHeight = mainPane.getScene().getHeight();
            }
        }

        Scale scale = new Scale(zoom, zoom, 0, 0);

        Translate translate = new Translate(
                sceneWidth / 2 - playerX * zoom,
                sceneHeight / 2 - playerY * zoom);

        mainPane.getTransforms().setAll(translate, scale);
    }
}
