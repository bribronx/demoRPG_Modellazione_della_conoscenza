package it.unicam.cs.mpgc.rpg126598.controller;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.model.save.EnemySaveData;
import it.unicam.cs.mpgc.rpg126598.model.save.GameSaveData;
import it.unicam.cs.mpgc.rpg126598.model.save.PlayerSaveData;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.CombatService;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.JsonSaveLoadService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.XpService;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.Objects;

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
    private final JsonSaveLoadService saveLoadService = new JsonSaveLoadService();
    private XpService xpService;

    private final String currentMapPath = "src/main/resources/it/unicam/cs/mpgc/rpg126598/map/world1.txt";
    private final File defaultSaveFile = new File("saves/savegame.json");

    private AnimationTimer gameLoop;
    private boolean isPaused = false;

    @FXML
    public void initialize() {
        enemyMovementService.setCombatService(combatService);
        combatService.setParentPane(mainPane);
        combatService.setMapBuilderService(mapBuilderService);

        mapBuilderService.generateMap(map, currentMapPath);

        playerController.setGameController(this);
        playerController.initServices(playerImageView, mapBuilderService, mainPane, map.getWidth(), map.getHeight());
        playerController.setCombatService(combatService);
        playerController.setEnemyMovementService(enemyMovementService);

        xpService = new XpService(playerController.getPlayer());
        playerController.setXpService(xpService);

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
                    if (xpService != null) {
                        xpService.onEnemyDefeated(enemy);
                    }
                } else if (deadEntity instanceof Player) {
                    System.out.println("GAME OVER!");
                    playerController.updateHealthBar();
                    playerController.updateDefenseBar();
                    gameLoop.stop();
                    combatService.deathAnimation(playerController.getPlayer(), () -> {
                        mainPane.getChildren().remove(playerController.getPlayer().getImageView());
                    });
                    playerController.showDeathScreen();
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
                if (!p.isDead() && !isPaused) {
                    playerController.update();
                    enemyMovementService.updateEnemies(p, now);
                    combatService.updateProjectiles(p, mapBuilderService.getCollisionMap());
                }
            }
        };
        gameLoop.start();

        spawnInitialEnemies();
    }

    private void spawnInitialEnemies() {
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
        if (xpService != null) {
            xpService.addEnemy(slime1);
            xpService.addEnemy(slime2);
            xpService.addEnemy(zombie1);
            xpService.addEnemy(skeleton1);
            xpService.addEnemy(skeleton2);
        }
    }

    public void saveDefaultGame() {
        saveGame(defaultSaveFile);
    }

    public boolean loadDefaultGame() {
        if (!defaultSaveFile.exists()) {
            playerController.showNotification("Nessun salvataggio rapido trovato.");
            return false;
        }
        return loadGame(defaultSaveFile);
    }

    private String formatSaveFileName(File file) {
        if (file == null) return "";
        return file.getName().replaceFirst("\\.json$", "");
    }

    public void saveGame(File file) {
        try {
            Player player = playerController.getPlayer();
            GameSaveData saveData = saveLoadService.createSaveData(player, enemyMovementService.getEnemies(), currentMapPath);
            saveLoadService.saveGame(saveData, file);
            playerController.showNotification("Partita salvata: " + formatSaveFileName(file));
        } catch (Exception e) {
            playerController.showNotification("Errore nel salvataggio: " + e.getMessage());
            System.err.println("Errore nel salvataggio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean loadGame(File file) {
        try {
            GameSaveData saveData = saveLoadService.loadGame(file);
            applySaveData(saveData);
            playerController.showNotification("Partita caricata: " + formatSaveFileName(file));
            System.out.println("Partita caricata con successo da: " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            playerController.showNotification("Errore caricamento: " + e.getMessage());
            System.err.println("Errore nel caricamento della partita: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void applySaveData(GameSaveData data) {
        if (data == null) return;

        Player player = playerController.getPlayer();
        PlayerSaveData psd = data.getPlayer();

        if (psd != null) {
            player.setHealth(psd.getHealth());
            player.setMaxHealth(psd.getMaxHealth());
            player.setDefense(psd.getDefense());
            player.setMaxDefense(psd.getMaxDefense());
            player.setDamage(psd.getDamage());
            player.setSpeed(psd.getSpeed());
            player.setLevel(psd.getLevel());
            player.setXp(psd.getXp());
            if (psd.getDirection() != null) {
                player.setDirection(psd.getDirection());
            } else {
                player.setDirection(Direction.DOWN);
            }
            if (psd.getState() != null) {
                player.setState(psd.getState());
            } else {
                player.setState(EntityState.IDLE);
            }

            if (player.getAnimationService() != null) {
                player.getAnimationService().stopAll();
            }

            ImageView pView = player.getImageView();
            if (pView != null) {
                if (!mainPane.getChildren().contains(pView)) {
                    mainPane.getChildren().add(pView);
                }

                pView.setLayoutX(psd.getX());
                pView.setLayoutY(psd.getY());
                pView.setTranslateX(0);
                pView.setTranslateY(0);
                pView.setOpacity(1.0);
                pView.setEffect(null);

                Image idleImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/walk_down/player_walk_down_01.png")));
                pView.setImage(idleImage);
                pView.setViewport(new Rectangle2D(10.0, 10.0, 30.0, 40.0));
            }
        }

        // Rimuovi tutti i nemici correnti dalla scena
        for (Enemy enemy : enemyMovementService.getEnemies()) {
            if (enemy.getAnimationService() != null) {
                enemy.getAnimationService().stopAll();
            }
            mainPane.getChildren().remove(enemy.getImageView());
            mainPane.getChildren().remove(enemy.getShadow());
        }
        enemyMovementService.clearEnemies();
        if (xpService != null) {
            xpService.clearEnemies();
        }

        // Ricrea i nemici salvati
        if (data.getEnemies() != null) {
            for (EnemySaveData esd : data.getEnemies()) {
                if (esd.getHealth() <= 0) continue;
                Enemy enemy = createEnemyFromSaveData(esd);
                if (enemy != null) {
                    enemyMovementService.addEnemy(enemy);
                    if (xpService != null) {
                        xpService.addEnemy(enemy);
                    }
                    if (enemy.getShadow() != null) {
                        mainPane.getChildren().add(enemy.getShadow());
                    }
                    mainPane.getChildren().add(enemy.getImageView());
                }
            }
        }

        playerController.hideDeathScreen();
        playerController.updateHealthBar();
        playerController.updateDefenseBar();
        if (playerController.isPaused()) {
            playerController.resumeGame();
        }
        gameLoop.start();
    }

    private Enemy createEnemyFromSaveData(EnemySaveData esd) {
        if (esd.getType() == null) return null;

        Enemy enemy = switch (esd.getType()) {
            case SLIME -> new Slime();
            case ZOMBIE -> new Zombie();
            case SKELETON -> new Skeleton();
        };

        enemy.setHealth(esd.getHealth());
        enemy.setMaxHealth(esd.getMaxHealth());
        enemy.setDefense(esd.getDefense());
        enemy.setMaxDefense(esd.getMaxDefense());
        enemy.setDamage(esd.getDamage());
        enemy.setSpeed(esd.getSpeed());
        if (esd.getDirection() != null) {
            enemy.setDirection(esd.getDirection());
        }
        if (esd.getState() != null) {
            enemy.setState(esd.getState());
        }

        ImageView eView = enemy.getImageView();
        if (eView != null) {
            eView.setLayoutX(esd.getX());
            eView.setLayoutY(esd.getY());
            eView.setTranslateX(0);
            eView.setTranslateY(0);
            eView.setOpacity(1.0);
            eView.setEffect(null);
        }

        return enemy;
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        for (Enemy enemy : enemyMovementService.getEnemies()) {
            if (enemy.getAnimationService() != null) {
                enemy.getAnimationService().stopAll();
            }
        }
        if (playerController != null && playerController.getPlayer() != null && playerController.getPlayer().getAnimationService() != null) {
            playerController.getPlayer().getAnimationService().stopAll();
        }
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    public XpService getXpService() {
        return xpService;
    }
}
