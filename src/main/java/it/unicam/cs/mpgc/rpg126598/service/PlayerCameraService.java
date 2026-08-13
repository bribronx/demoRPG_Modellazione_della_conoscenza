package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class PlayerCameraService {

    private final Player player;
    private final Pane mainPane;
    private final int zoom;
    private double mapWidth;
    private double mapHeight;

    public PlayerCameraService(Player player, Pane mainPane, int zoom) {
        this(player, mainPane, zoom, 0, 0);
    }

    public PlayerCameraService(Player player, Pane mainPane, int zoom, double mapWidth, double mapHeight) {
        this.player = player;
        this.mainPane = mainPane;
        this.zoom = zoom;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void setMapSize(double mapWidth, double mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    public void updateCamera() {
        ImageView imageP = player.getImageView();
        if (imageP == null) return;

        double playerX = imageP.getLayoutX() + imageP.getTranslateX() + imageP.getFitWidth() / 2.0;
        double playerY = imageP.getLayoutY() + imageP.getTranslateY() + imageP.getFitHeight() / 2.0;

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

        double translateX = sceneWidth / 2.0 - playerX * zoom;
        double translateY = sceneHeight / 2.0 - playerY * zoom;

        if (mapWidth > 0) {
            double scaledMapWidth = mapWidth * zoom;
            if (scaledMapWidth >= sceneWidth) {
                double minTranslateX = sceneWidth - scaledMapWidth;
                translateX = Math.max(minTranslateX, Math.min(0, translateX));
            } else {
                translateX = (sceneWidth - scaledMapWidth) / 2.0;
            }
        }

        if (mapHeight > 0) {
            double scaledMapHeight = mapHeight * zoom;
            if (scaledMapHeight >= sceneHeight) {
                double minTranslateY = sceneHeight - scaledMapHeight;
                translateY = Math.max(minTranslateY, Math.min(0, translateY));
            } else {
                translateY = (sceneHeight - scaledMapHeight) / 2.0;
            }
        }

        Scale scale = new Scale(zoom, zoom, 0, 0);
        Translate translate = new Translate(translateX, translateY);

        mainPane.getTransforms().setAll(translate, scale);
    }
}
