package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.CombatService;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GameController {

    @FXML
    private Pane mainPane;

    @FXML
    private ImageView playerImageView;

    @FXML
    private Canvas map;

    @FXML
    private PlayerController playerController;

    private final MapBuilderService mapBuilderService = new MapBuilderService();
    private final EnemyMovementService enemyMovementService = new EnemyMovementService(mapBuilderService);
    private final CombatService combatService = new CombatService(new CollisionService());

    private AnimationTimer gameLoop;

    @FXML
    public void initialize() {
        enemyMovementService.setCombatService(combatService);
        combatService.setParentPane(mainPane);
        combatService.setMapBuilderService(mapBuilderService);

        mapBuilderService.generateMap(map, "src/main/resources/it/unicam/cs/mpgc/rpg126598/map/world1.txt");

        playerController.initServices(playerImageView, mapBuilderService, mainPane, map.getWidth(), map.getHeight());
        playerController.setCombatService(combatService);
        playerController.setEnemyMovementService(enemyMovementService);

        combatService.setCombatListener(new CombatService.CombatListener() {
            @Override
            public void onDamageDealt(Entity attacker, Entity target, double damage) {
                String attackerName = attacker != null ? attacker.getClass().getSimpleName() : "Osso";
                System.out.println(attackerName + " infligge " + damage +
                        " danni a " + target.getClass().getSimpleName() +
                        " (HP rimanenti: " + target.getHealth() + ")");
            }

            @Override
            public void onEntityDeath(Entity deadEntity, Entity killer) {
                System.out.println(deadEntity.getClass().getSimpleName() + " e' morto!");
                if (deadEntity instanceof Enemy enemy) {
                    enemyMovementService.removeEnemy(enemy);
                    combatService.deathAnimation(enemy, () -> {
                        mainPane.getChildren().remove(enemy.getImageView());
                        mainPane.getChildren().remove(enemy.getShadow());
                    });
                } else if (deadEntity instanceof Player) {
                    System.out.println("GAME OVER!");
                    playerController.updateHealthBar();
                    playerController.updateDefenseBar();
                    gameLoop.stop();
                    combatService.deathAnimation(playerController.getPlayer(), () -> {
                        mainPane.getChildren().remove(playerController.getPlayer().getImageView());
                        mainPane.getChildren().remove(playerController.getPlayer().getShadow());
                    });
                }
            }
        });

        if (mainPane.getScene() != null) {
            playerController.setupKeyListeners(mainPane.getScene());
        }
        mainPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                playerController.setupKeyListeners(newScene);
            }
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Player p = playerController.getPlayer();
                if (!p.isDead()) {
                    playerController.update();
                    enemyMovementService.updateEnemies(p, now);
                    combatService.updateProjectiles(p, mapBuilderService.getCollisionMap());
                }
            }
        };
        gameLoop.start();

        Slime slime1 = new Slime();
        Slime slime2 = new Slime();
        Zombie zombie1 = new Zombie();
        Skeleton skeleton1 = new Skeleton();
        Skeleton skeleton2 = new Skeleton();
        skeleton2.getImageView().setLayoutX(300);
        skeleton2.getImageView().setLayoutY(150);
        skeleton1.getImageView().setLayoutX(100);
        skeleton1.getImageView().setLayoutY(150);
        slime1.getImageView().setLayoutX(100);
        slime1.getImageView().setLayoutY(100);
        slime2.getImageView().setLayoutX(120);
        slime2.getImageView().setLayoutY(100);
        zombie1.getImageView().setLayoutX(120);
        zombie1.getImageView().setLayoutY(50);
        mainPane.getChildren().addAll(
                slime1.getShadow(), slime1.getImageView(),
                slime2.getShadow(), slime2.getImageView(),
                zombie1.getShadow(), zombie1.getImageView(),
                skeleton1.getShadow(), skeleton1.getImageView(),
                skeleton2.getShadow(), skeleton2.getImageView());
        enemyMovementService.addEnemy(slime1);
        enemyMovementService.addEnemy(slime2);
        enemyMovementService.addEnemy(zombie1);
        enemyMovementService.addEnemy(skeleton1);
        enemyMovementService.addEnemy(skeleton2);
    }
}
