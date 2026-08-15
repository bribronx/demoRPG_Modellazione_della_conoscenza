package it.unicam.cs.mpgc.rpg126598.strategy;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.LoadFramesService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SlimeMovementStrategy implements EnemyMovementStrategy {

    private final Random random = new Random();

    private double stateTimer = 0;
    private boolean isHopping = false;
    private double lastNow = 0;

    private double hopDuration = 0.18;
    private double pauseDuration = 1.5;

    private double dirX = 0;
    private double dirY = 0;

    private final AnimationService animationService = new AnimationService();
    private final LoadFramesService loadFramesService = new LoadFramesService();

    private final Image[] idleFrames = loadFramesService.loadFrames("slime", "idle", "slime_idle", 4);
    private final Image[] walkDownFrames = loadFramesService.loadFrames("slime", "walk_down", "slime_walk_down", 4);
    private final Image[] walkLeftFrames = loadFramesService.loadFrames("slime", "walk_left", "slime_walk_left", 4);
    private final Image[] walkRightFrames = loadFramesService.loadFrames("slime", "walk_right", "slime_walk_right", 4);
    private final Image[] walkUpFrames = loadFramesService.loadFrames("slime", "walk_up", "slime_walk_up", 4);
    public SlimeMovementStrategy() {}

    public SlimeMovementStrategy(double hopDuration, double pauseDuration) {
        this.hopDuration = hopDuration;
        this.pauseDuration = pauseDuration;
    }

    @Override
    public void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService,
            CollisionService collisionService, double deltaTime) {

        // il primo frame non va considerato, per questo controllo che sia 0
        if (lastNow == 0) {
            lastNow = deltaTime;
            return;
        }
        // calcolo il tempo trascorso tra un frame e l'altro in secondi
        double elapsedSeconds = (deltaTime - lastNow) / 1e9;
        lastNow = deltaTime;

        stateTimer += elapsedSeconds;

        if (isHopping) {
            if (stateTimer >= hopDuration) {
                isHopping = false;
                stateTimer = 0;
                enemy.setState(EntityState.IDLE);
            } else {
                double moveSpeed = enemy.getSpeed() * 1.5;
                double deltaX = dirX * moveSpeed;
                double deltaY = dirY * moveSpeed;

                int[][] collisionMap = mapBuilderService != null ? mapBuilderService.getCollisionMap() : null;

                List<Entity> others = new ArrayList<>();
                if (enemies != null) {
                    others.addAll(enemies);
                }
                if (target != null) {
                    others.add(target);
                }

                //movimento dello slime e nel caso di collisione rimbalzo
                if (deltaX != 0 && (collisionService == null
                        || !collisionService.checkCollision(enemy, deltaX, 0, collisionMap, others))) {
                    enemy.moveX(deltaX);
                } else {
                    dirX = -dirX;
                }

                if (deltaY != 0 && (collisionService == null
                        || !collisionService.checkCollision(enemy, 0, deltaY, collisionMap, others))) {
                    enemy.moveY(deltaY);
                } else {
                    dirY = -dirY;
                }
            }
        } else {
            //gestione della fase di pausa tra un balzo e l'altro
            if (stateTimer >= pauseDuration) {
                isHopping = true;
                stateTimer = 0;
                enemy.setState(EntityState.MOVING);
                if (target != null && target.getImageView() != null) {
                    double enemyX = enemy.getGlobalX();
                    double enemyY = enemy.getGlobalY();
                    double playerX = target.getGlobalX();
                    double playerY = target.getGlobalY();

                    double distance = Math.hypot(playerX - enemyX, playerY - enemyY);

                    //scelta della direzione in base alla posizione del giocatore
                    if (distance <= enemy.getAggroRange() && distance > 0) {
                        dirX = (playerX - enemyX) / distance;
                        dirY = (playerY - enemyY) / distance;
                    } else {
                        chooseRandomDirection();
                    }
                } else {
                    chooseRandomDirection();
                }
            }
        }
        ImageView imageView = enemy.getImageView();
        if (imageView != null) {
            if (isHopping) {
                if (Math.abs(dirX) > Math.abs(dirY)) {
                    if (dirX > 0) {
                        animationService.walkingAnimation(imageView, walkRightFrames, 180);
                    } else {
                        animationService.walkingAnimation(imageView, walkLeftFrames, 180);
                    }
                } else {
                    if (dirY > 0) {
                        animationService.walkingAnimation(imageView, walkDownFrames, 180);
                    } else {
                        animationService.walkingAnimation(imageView, walkUpFrames, 180);
                    }
                }
            } else {
                animationService.walkingAnimation(imageView, idleFrames, 180);
            }
        }
    }

    private void chooseRandomDirection() {
        double angle = random.nextDouble() * 2 * Math.PI;
        dirX = Math.cos(angle);
        dirY = Math.sin(angle);
    }

    public double getHopDuration() {
        return hopDuration;
    }

    public void setHopDuration(double hopDuration) {
        this.hopDuration = hopDuration;
    }

    public double getPauseDuration() {
        return pauseDuration;
    }

    public void setPauseDuration(double pauseDuration) {
        this.pauseDuration = pauseDuration;
    }
}
