package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CombatService;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerCameraService;
import it.unicam.cs.mpgc.rpg126598.service.PlayerMovementService;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.MeleeAttackStrategy;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashSet;
import java.util.Set;

public class PlayerController {

    @FXML
    private Pane hudPane;

    @FXML
    private VBox stats;

    @FXML
    private ProgressBar hpBar;


    private Player player;
    private PlayerMovementService movementService;
    private PlayerCameraService cameraService;
    private AttackStrategy attackStrategy;

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private CombatService combatService;
    private EnemyMovementService enemyMovementService;

    private static final double HP_SCALE = 2.0;

    public PlayerController() {
        this.player = new Player();
        this.attackStrategy = new MeleeAttackStrategy();
    }

    @FXML
    public void initialize() {
        if (hpBar != null && player != null) {
            hpBar.setPrefWidth(player.getMaxHealth() * HP_SCALE);
            hpBar.setProgress(player.getMaxHealth() > 0 ? player.getHealth() / player.getMaxHealth() : 0);
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

        this.cameraService.updateCamera();
    }

    public void setCombatService(CombatService combatService) {
        this.combatService = combatService;
    }

    public void setEnemyMovementService(EnemyMovementService enemyMovementService) {
        this.enemyMovementService = enemyMovementService;
    }

    public void setupKeyListeners(Scene scene) {
        if (scene == null)
            return;

        scene.setOnKeyPressed(event -> {
            pressedKeys.add(event.getCode());

            if (event.getCode() == KeyCode.SPACE && !player.isDead()) {
                handleAttack();
            }
        });

        scene.setOnKeyReleased(event -> {
            pressedKeys.remove(event.getCode());
        });
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
    }

    public void updateHealthBar() {
        if (hpBar == null || player == null) return;
        hpBar.setPrefWidth(player.getMaxHealth() * HP_SCALE);

        double progress = (player.getMaxHealth() > 0 && !player.isDead())
                ? Math.max(0.0, Math.min(1.0, player.getHealth() / player.getMaxHealth()))
                : 0.0;

        hpBar.setProgress(progress);
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