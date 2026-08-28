package it.unicam.cs.mpgc.rpg126598.strategy;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;

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

    public SlimeMovementStrategy() {}

    public SlimeMovementStrategy(double hopDuration, double pauseDuration) {
        this.hopDuration = hopDuration;
        this.pauseDuration = pauseDuration;
    }

    @Override
    public void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService,
            CollisionService collisionService, double deltaTime) {

        // il primo frame non va considerato
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

                // movimento dello slime e nel caso di collisione rimbalzo
                if (deltaX != 0 && (collisionService == null
                        || collisionService.checkCollision(enemy, deltaX, 0, collisionMap, others))) {
                    enemy.moveX(deltaX);
                } else {
                    dirX = -dirX;
                }

                if (deltaY != 0 && (collisionService == null
                        || collisionService.checkCollision(enemy, 0, deltaY, collisionMap, others))) {
                    enemy.moveY(deltaY);
                } else {
                    dirY = -dirY;
                }

                if (Math.abs(dirX) > Math.abs(dirY)) {
                    if (dirX > 0) {
                        enemy.setDirection(Direction.RIGHT);
                    } else {
                        enemy.setDirection(Direction.LEFT);
                    }
                } else {
                    if (dirY > 0) {
                        enemy.setDirection(Direction.DOWN);
                    } else {
                        enemy.setDirection(Direction.UP);
                    }
                }
            }
        } else {
            // gestione della fase di pausa tra un balzo e l'altro
            if (stateTimer >= pauseDuration) {
                isHopping = true;
                stateTimer = 0;
                enemy.setState(EntityState.MOVING);
                if (target != null) {
                    double enemyX = enemy.getX();
                    double enemyY = enemy.getY();
                    double playerX = target.getX();
                    double playerY = target.getY();

                    double distance = Math.hypot(playerX - enemyX, playerY - enemyY);

                    // scelta della direzione in base alla posizione del giocatore
                    if (distance <= enemy.getAggroRange() && distance > 0) {
                        dirX = (playerX - enemyX) / distance;
                        dirY = (playerY - enemyY) / distance;
                    } else {
                        chooseRandomDirection();
                    }
                } else {
                    chooseRandomDirection();
                }

                if (Math.abs(dirX) > Math.abs(dirY)) {
                    if (dirX > 0) {
                        enemy.setDirection(Direction.RIGHT);
                    } else {
                        enemy.setDirection(Direction.LEFT);
                    }
                } else {
                    if (dirY > 0) {
                        enemy.setDirection(Direction.DOWN);
                    } else {
                        enemy.setDirection(Direction.UP);
                    }
                }
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
