package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CombatService;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerCameraService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerMovementService;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.MeleeAttackStrategy;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PlayerController {

    @FXML
    private Pane hudPane;

    @FXML
    private VBox stats;

    @FXML
    private ProgressBar hpBar;

    @FXML
    private ProgressBar defenseBar;

    @FXML
    private VBox deathScreen;

    @FXML
    private VBox pauseMenu;

    @FXML
    private Label notificationLabel;


    private final Player player;
    private PlayerMovementService movementService;
    private PlayerCameraService cameraService;
    private AttackStrategy attackStrategy;

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CombatService combatService;
    private EnemyMovementService enemyMovementService;
    private GameController gameController;

    private PauseTransition notificationTimer;
    private boolean isPaused = false;

    private static final double HP_SCALE = 2.0;

    public PlayerController() {
        this.player = new Player();
        this.attackStrategy = new MeleeAttackStrategy();
    }

    @FXML
    public void initialize() {
        if (hpBar != null) {
            hpBar.setPrefWidth(player.getMaxHealth() * HP_SCALE);
            hpBar.setProgress(player.getMaxHealth() > 0 ? player.getHealth() / player.getMaxHealth() : 0);
        }
        if (defenseBar != null) {
            defenseBar.setPrefWidth(player.getMaxDefense() * HP_SCALE);
            defenseBar.setProgress(player.getMaxDefense() > 0 ? player.getDefense() / player.getMaxDefense() : 0);
        }
    }

    public void initServices(ImageView playerImageView, MapBuilderService mapBuilderService, Pane mainPane,
            double mapWidth, double mapHeight) {
        this.player.setImageView(playerImageView);
        this.movementService = new PlayerMovementService(this.player, mapBuilderService);
        this.cameraService = new PlayerCameraService(this.player, mainPane, 4, mapWidth, mapHeight);

        if (this.hpBar != null) {
            this.hpBar.setPrefWidth(player.getMaxHealth() * HP_SCALE);
            this.hpBar.setProgress(player.getMaxHealth() > 0 ? player.getHealth() / player.getMaxHealth() : 0);
        }
        if (this.defenseBar != null) {
            this.defenseBar.setPrefWidth(player.getMaxDefense() * HP_SCALE);
            this.defenseBar.setProgress(player.getMaxDefense() > 0 ? player.getDefense() / player.getMaxDefense() : 0);
        }

        this.cameraService.updateCamera();
    }

    public void setCombatService(CombatService combatService) {
        this.combatService = combatService;
    }

    public void setEnemyMovementService(EnemyMovementService enemyMovementService) {
        this.enemyMovementService = enemyMovementService;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setupKeyListeners(Scene scene) {
        if (scene == null)
            return;

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                togglePauseMenu();
                event.consume();
                return;
            } else if (event.getCode() == KeyCode.F11) {
                if (scene.getWindow() instanceof Stage stage) {
                    stage.setFullScreen(!stage.isFullScreen());
                    event.consume();
                    return;
                }
            }

            if (isPaused) {
                // Scorciatoie rapide
                if (event.getCode() == KeyCode.F5) {
                    handleQuickSave();
                } else if (event.getCode() == KeyCode.F9) {
                    handleQuickLoad();
                }
                return;
            }

            pressedKeys.add(event.getCode());

            if (event.getCode() == KeyCode.SPACE && !player.isDead()) {
                handleAttack();
            } else if (event.getCode() == KeyCode.F5) {
                handleQuickSave();
            } else if (event.getCode() == KeyCode.F9) {
                handleQuickLoad();
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
        });
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void togglePauseMenu() {
        if (player.isDead()) return;
        if (isPaused) {
            resumeGame();
        } else {
            pauseGame();
        }
    }

    public void pauseGame() {
        isPaused = true;
        pressedKeys.clear();
        if (pauseMenu != null) {
            pauseMenu.setVisible(true);
        }
        if (gameController != null) {
            gameController.setPaused(true);
        }
    }

    public void resumeGame() {
        isPaused = false;
        pressedKeys.clear();
        if (pauseMenu != null) {
            pauseMenu.setVisible(false);
        }
        if (gameController != null) {
            gameController.setPaused(false);
        }
    }

    @FXML
    public void handleResumeGame() {
        resumeGame();
    }

    @FXML
    public void handleExitGame() {
        javafx.application.Platform.exit();
        System.exit(0);
    }

    @FXML
    public void handleQuickSave() {
        if (gameController != null) {
            gameController.saveDefaultGame();
        }
    }

    @FXML
    public void handleQuickLoad() {
        if (gameController != null) {
            boolean loaded = gameController.loadDefaultGame();
            if (loaded) {
                resumeGame();
            }
        }
    }

    @FXML
    public void handleLoadGame() {
        if (gameController != null) {
            boolean loaded = gameController.loadDefaultGame();
            if (loaded) {
                resumeGame();
            }
        }
    }

    @FXML
    public void handleMainMenu(ActionEvent event) {
        if (gameController != null) {
            gameController.stopGameLoop();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/unicam/cs/mpgc/rpg126598/fxml/main-menu.fxml"));
            Parent root = loader.load();

            Stage stage = null;
            if (event != null && event.getSource() instanceof Node node && node.getScene() != null) {
                stage = (Stage) node.getScene().getWindow();
            } else if (hudPane != null && hudPane.getScene() != null) {
                stage = (Stage) hudPane.getScene().getWindow();
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
            root.requestFocus();
        } catch (IOException e) {
            System.err.println("Errore durante il ritorno al menu principale: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showNotification(String message) {
        if (notificationLabel == null) return;

        notificationLabel.setText(message);
        notificationLabel.setOpacity(1.0);
        notificationLabel.toFront();
        notificationLabel.setVisible(true);

        if (notificationTimer != null) {
            notificationTimer.stop();
        }

        notificationTimer = new PauseTransition(Duration.seconds(2.5));
        notificationTimer.setOnFinished(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(500), notificationLabel);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(fadeEvent -> notificationLabel.setVisible(false));
            fade.play();
        });
        notificationTimer.play();
    }

    public void handleAttack() {
        if (player.isDead() || attackStrategy == null)
            return;

        long cooldownMillis = (long) (attackStrategy.getCooldown() * 1000.0);
        long currentTime = System.currentTimeMillis();
        if (currentTime - player.getLastAttackTime() >= cooldownMillis) {
            if (combatService != null && enemyMovementService != null) {
                combatService.executeAttack(player, enemyMovementService.getEnemies(), attackStrategy);
                combatService.attackAnimation(player);
                player.setLastAttackTime(currentTime);
            }
        }
    }

    public void update() {
        if (!player.isDead()) {
            if (movementService != null && enemyMovementService != null) {
                movementService.updateMovement(pressedKeys, enemyMovementService.getEnemies());
            }

            if (cameraService != null) {
                cameraService.updateCamera();
            }
        }
        updateHealthBar();
        updateDefenseBar();
    }

    public void updateHealthBar() {
        if (hpBar == null) return;
        hpBar.setPrefWidth(player.getMaxHealth() * HP_SCALE);

        double progress = (player.getMaxHealth() > 0 && !player.isDead())
                ? Math.clamp(player.getHealth() / player.getMaxHealth(), 0.0, 1.0)
                : 0.0;

        hpBar.setProgress(progress);
    }

    public void updateDefenseBar() {
        if (defenseBar == null) return;
        if (player.getMaxDefense() > 0) {
            defenseBar.setPrefWidth(player.getMaxDefense() * HP_SCALE);
            double progress = (player.isDead() || player.getDefense() <= 0)
                    ? 0.0
                    : Math.clamp(player.getDefense() / player.getMaxDefense(), 0.0, 1.0);
            defenseBar.setProgress(progress);
        } else {
            defenseBar.setProgress(0);
        }
    }

    public void showDeathScreen() {
        deathScreen.setVisible(true);
    }

    public void hideDeathScreen() {
        deathScreen.setVisible(false);
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerMovementService getMovementService() {
        return movementService;
    }

    public PlayerCameraService getCameraService() {
        return cameraService;
    }

    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }
}
