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
import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.CombatService;
import it.unicam.cs.mpgc.rpg126598.service.EnemyMovementService;
import it.unicam.cs.mpgc.rpg126598.service.ItemService;
import it.unicam.cs.mpgc.rpg126598.service.JsonSaveLoadService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.XpService;
import it.unicam.cs.mpgc.rpg126598.view.EnemyAnimationRenderer;
import it.unicam.cs.mpgc.rpg126598.view.EntityView;
import it.unicam.cs.mpgc.rpg126598.view.EntityViewFactory;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
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

    private static final int TILE_SIZE = 16;

    private final MapBuilderService mapBuilderService = new MapBuilderService();
    private final EnemyMovementService enemyMovementService = new EnemyMovementService(mapBuilderService);
    private final CombatService combatService = new CombatService(new CollisionService());
    private final ItemService itemService = new ItemService();
    private final JsonSaveLoadService saveLoadService = new JsonSaveLoadService();
    private final EnemyAnimationRenderer enemyAnimationRenderer = new EnemyAnimationRenderer();
    private final Map<Enemy, EntityView> enemyViews = new HashMap<>();

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
        itemService.setParentPane(mainPane);

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

                if (target instanceof Player && playerController.getPlayerEntityView() != null) {
                    playerController.getPlayerEntityView().playHitEffect();
                } else if (target instanceof Enemy enemy) {
                    EntityView view = enemyViews.get(enemy);
                    if (view != null) {
                        view.playHitEffect();
                    }
                }
            }

            @Override
            public void onEntityDeath(Entity deadEntity, Entity killer) {
                System.out.println(deadEntity.getClass().getSimpleName() + " e' morto!");
                if (deadEntity instanceof Enemy enemy) {
                    enemyMovementService.removeEnemy(enemy);
                    EntityView view = enemyViews.remove(enemy);
                    if (view != null) {
                        combatService.deathAnimation(enemy, view.getImageView(), enemyAnimationRenderer.getAnimationService(enemy), () -> {
                            mainPane.getChildren().remove(view.getImageView());
                            if (view.getShadow() != null) {
                                mainPane.getChildren().remove(view.getShadow());
                            }
                            enemyAnimationRenderer.removeEnemy(enemy);
                        });
                    }
                    if (xpService != null) {
                        xpService.onEnemyDefeated(enemy);
                    }
                } else if (deadEntity instanceof Player) {
                    System.out.println("GAME OVER!");
                    playerController.updateHealthBar();
                    playerController.updateDefenseBar();
                    gameLoop.stop();
                    combatService.deathAnimation(playerController.getPlayer(), playerController.getPlayerImageView(), playerController.getAnimationService(), () -> {
                        mainPane.getChildren().remove(playerController.getPlayerImageView());
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

                    // Sincronizza posizioni e animazioni di tutti i nemici
                    for (Enemy enemy : enemyMovementService.getEnemies()) {
                        EntityView view = enemyViews.get(enemy);
                        if (view != null) {
                            view.updatePosition(enemy.getX(), enemy.getY());
                            enemyAnimationRenderer.updateAnimation(enemy, view.getImageView());
                        }
                    }

                    combatService.updateProjectiles(p, mapBuilderService.getCollisionMap());
                    itemService.update(p, playerController);
                }
            }
        };
        gameLoop.start();

        spawnInitialEnemies();
        spawnInitialItems();

        map.setOnMouseClicked(event -> {
            int tileX = (int) (event.getX() / TILE_SIZE); // Colonna matrice (X)
            int tileY = (int) (event.getY() / TILE_SIZE); // Riga matrice (Y)

            System.out.println("Casella cliccata: TileX=" + tileX + ", TileY=" + tileY +
                    " (Pixel: X=" + (int) event.getX() + ", Y=" + (int) event.getY() + ")");
        });
    }

    public void spawnEnemyAtTile(EnemyType type, int tileX, int tileY) {
        if (type == null) return;
        Enemy enemy = switch (type) {
            case SLIME -> new Slime();
            case ZOMBIE -> new Zombie();
            case SKELETON -> new Skeleton();
        };
        spawnEnemyAtTile(enemy, tileX, tileY);
    }

    public void spawnEnemyAtTile(Enemy enemy, int tileX, int tileY) {
        if (enemy == null) return;

        double pixelX = tileX * TILE_SIZE;
        double pixelY = tileY * TILE_SIZE;
        enemy.setPosition(pixelX, pixelY);

        EntityView view = EntityViewFactory.createEnemyView(enemy);
        enemyViews.put(enemy, view);

        if (view.getShadow() != null && !mainPane.getChildren().contains(view.getShadow())) {
            mainPane.getChildren().add(view.getShadow());
        }
        if (view.getImageView() != null && !mainPane.getChildren().contains(view.getImageView())) {
            mainPane.getChildren().add(view.getImageView());
        }

        enemyMovementService.addEnemy(enemy);
        if (xpService != null) {
            xpService.addEnemy(enemy);
        }
    }

    private void spawnInitialEnemies() {
        spawnEnemyAtTile(EnemyType.SLIME, 11, 4);
        spawnEnemyAtTile(EnemyType.SLIME, 6, 7);
        spawnEnemyAtTile(EnemyType.ZOMBIE, 47, 4);
        spawnEnemyAtTile(EnemyType.SKELETON, 45, 22);  
        spawnEnemyAtTile(EnemyType.SKELETON, 46, 25);  
    }

    private void spawnInitialItems() {
        itemService.spawnShieldItemAtTile(53, 5, 20.0);
        itemService.spawnShieldItemAtTile(41, 19, 20.0);
        itemService.spawnShieldItemAtTile(21, 4, 20.0);
        itemService.spawnSwordItemAtTile(5,24);
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
            player.setPosition(psd.getX(), psd.getY());
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

            if (playerController.getAnimationService() != null) {
                playerController.getAnimationService().stopAll();
            }

            ImageView pView = playerController.getPlayerImageView();
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
        for (Map.Entry<Enemy, EntityView> entry : enemyViews.entrySet()) {
            EntityView view = entry.getValue();
            if (view != null) {
                mainPane.getChildren().remove(view.getImageView());
                if (view.getShadow() != null) {
                    mainPane.getChildren().remove(view.getShadow());
                }
            }
        }
        enemyAnimationRenderer.clear();
        enemyViews.clear();
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
                    enemy.setPosition(esd.getX(), esd.getY());
                    EntityView view = EntityViewFactory.createEnemyView(enemy);
                    enemyViews.put(enemy, view);
                    enemyMovementService.addEnemy(enemy);
                    if (xpService != null) {
                        xpService.addEnemy(enemy);
                    }
                    if (view.getShadow() != null) {
                        mainPane.getChildren().add(view.getShadow());
                    }
                    if (view.getImageView() != null) {
                        mainPane.getChildren().add(view.getImageView());
                    }
                }
            }
        }

        itemService.clear();
        spawnInitialItems();

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

        return enemy;
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
        enemyAnimationRenderer.clear();
        if (playerController != null && playerController.getAnimationService() != null) {
            playerController.getAnimationService().stopAll();
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

    public ItemService getItemService() {
        return itemService;
    }
}
