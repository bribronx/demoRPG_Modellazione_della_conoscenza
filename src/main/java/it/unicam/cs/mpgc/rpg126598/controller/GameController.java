package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerCameraService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerMovementService;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

public class GameController {

    @FXML
    private Pane mainPane;

    @FXML
    private ImageView player;

    @FXML
    private Canvas map;

    private final Player p = new Player();
    private PlayerCameraService camera;
    MapBuilderService mapBuilderService = new MapBuilderService();
    private final PlayerMovementService movementP = new PlayerMovementService(p, mapBuilderService);

    @FXML
    public void initialize() {
        p.setImageView(player);
        camera = new PlayerCameraService(p, mainPane, 4);
        mapBuilderService.generateMap(map, "src/main/resources/it/unicam/cs/mpgc/rpg126598/map/world1.txt");
    }

    @FXML
    public void update(KeyEvent event) {
        movementP.makeMove(event);
        camera.updateCamera();
    }
}
