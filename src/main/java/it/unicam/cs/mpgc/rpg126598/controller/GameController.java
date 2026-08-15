package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerCameraService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerMovementService;
import javafx.animation.AnimationTimer;
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
    private final EnemyMovementService enemyMovementService = new EnemyMovementService(mapBuilderService);

    private AnimationTimer gameLoop;

    @FXML
    public void initialize() {
        p.setImageView(player);
        mapBuilderService.generateMap(map, "src/main/resources/it/unicam/cs/mpgc/rpg126598/map/world1.txt");
        camera = new PlayerCameraService(p, mainPane, 4, map.getWidth(), map.getHeight());
        camera.updateCamera();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                enemyMovementService.updateEnemies(p, now);
            }
        };
        gameLoop.start();

        Slime slime1 = new Slime();
        Slime slime2 = new Slime();
        slime1.getImageView().setLayoutX(100);
        slime1.getImageView().setLayoutY(100);
        slime2.getImageView().setLayoutX(120);
        slime2.getImageView().setLayoutY(100);
        mainPane.getChildren().addAll(slime1.getImageView(), slime2.getImageView());
        enemyMovementService.addEnemy(slime1);
        enemyMovementService.addEnemy(slime2);
    }

    @FXML
    public void update(KeyEvent event) {
        movementP.makeMove(event);
        camera.updateCamera();
    }
}
