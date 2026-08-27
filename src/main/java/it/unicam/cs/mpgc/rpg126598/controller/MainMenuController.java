package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.service.AnimationService;
import it.unicam.cs.mpgc.rpg126598.service.LoadFramesService;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainMenuController {

    @FXML
    private StackPane rootPane;

    @FXML
    private ImageView backgroundImageView;

    @FXML
    private VBox pauseMenu;

    @FXML
    private ImageView menup;

    private final LoadFramesService loadFramesService = new LoadFramesService();
    private static final String DEFAULT_SAVE_PATH = "saves/savegame.json";
    private static final String GAME_VIEW_FXML = "/it/unicam/cs/mpgc/rpg126598/fxml/game-view.fxml";

    Image[] downFrames = loadFramesService.loadFrames("player","walk_down", "player_walk_down", 6);
    private final AnimationService animationService = new AnimationService();

    @FXML
    public void initialize() {
        if (rootPane != null && backgroundImageView != null) {
            backgroundImageView.fitWidthProperty().bind(rootPane.widthProperty());
            backgroundImageView.fitHeightProperty().bind(rootPane.heightProperty());
        }

        AnimationTimer loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                animationService.walkingAnimation(menup, downFrames, 150);
            }
        };
        loop.start();

    }

    @FXML
    public void handleNewGame(ActionEvent event) {
        loadGameScene(event, false);
    }

    @FXML
    public void handleQuickLoad(ActionEvent event) {
        File saveFile = new File(DEFAULT_SAVE_PATH);
        if (!saveFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Continua Partita");
            alert.setHeaderText(null);
            alert.setContentText("Nessun salvataggio trovato.");
            alert.showAndWait();
            return;
        }
        loadGameScene(event, true);
    }

    @FXML
    public void handleExitGame(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void loadGameScene(ActionEvent event, boolean loadSave) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(GAME_VIEW_FXML));
            Parent root = loader.load();

            GameController gameController = loader.getController();

            Stage stage = null;
            if (event != null && event.getSource() instanceof Node node && node.getScene() != null) {
                stage = (Stage) node.getScene().getWindow();
            } else if (pauseMenu != null && pauseMenu.getScene() != null) {
                stage = (Stage) pauseMenu.getScene().getWindow();
            }

            if (stage != null) {
                Scene scene = stage.getScene();
                if (scene == null) {
                    scene = new Scene(root);
                    stage.setScene(scene);
                } else {
                    scene.setRoot(root);
                }
            }

            if (loadSave && gameController != null) {
                gameController.loadDefaultGame();
            }

            root.requestFocus();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento della schermata di gioco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
