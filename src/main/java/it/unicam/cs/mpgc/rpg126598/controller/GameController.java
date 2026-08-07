package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class GameController {

    @FXML
    private Pane mainPane;

    @FXML
    private Canvas map;

    private MapBuilderService mapBuilderService;

    @FXML
    public void initialize() {
        mapBuilderService = new MapBuilderService();
        mapBuilderService.generateMap(map, "src/main/resources/it/unicam/cs/mpgc/rpg126598/map/world1.txt");
    }
}
