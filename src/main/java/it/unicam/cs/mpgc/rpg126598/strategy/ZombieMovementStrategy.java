package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.LoadFramesService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ZombieMovementStrategy implements EnemyMovementStrategy {

    private final LoadFramesService loadFramesService = new LoadFramesService();

    private final Image[] idleFrames = loadFramesService.loadFrames("zombie", "idle", "zombie_idle", 8);
    private final Image[] walkDownFrames = loadFramesService.loadFrames("zombie", "walk_down", "zombie_walk_down", 8);
    private final Image[] walkLeftFrames = loadFramesService.loadFrames("zombie", "walk_left", "zombie_walk_left", 8);
    private final Image[] walkRightFrames = loadFramesService.loadFrames("zombie", "walk_right", "zombie_walk_right", 8);
    private final Image[] walkUpFrames = loadFramesService.loadFrames("zombie", "walk_up", "zombie_walk_up", 8);

    double dirX=0;
    double dirY=0;


    @Override
    public void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService,
            CollisionService collisionService, double deltaTime) {
        if (target == null || target.getImageView() == null)
            return;

        double enemyX = enemy.getGlobalX();
        double enemyY = enemy.getGlobalY();
        double playerX = target.getGlobalX();
        double playerY = target.getGlobalY();

        double distance = Math.hypot(playerX - enemyX, playerY - enemyY);

        if (distance <= enemy.getAggroRange() && distance > 0) {
            enemy.setState(EntityState.CHASING);

            dirX = (playerX - enemyX) / distance;
            dirY = (playerY - enemyY) / distance;

            double deltaX = dirX * enemy.getSpeed();
            double deltaY = dirY * enemy.getSpeed();

            int[][] collisionMap = mapBuilderService != null ? mapBuilderService.getCollisionMap() : null;

            List<Entity> others = new ArrayList<>();
            if (enemies != null) {
                others.addAll(enemies);
            }
            if (target != null) {
                others.add(target);
            }

            if (deltaX != 0
                    && (collisionService == null
                            || !collisionService.checkCollision(enemy, deltaX, 0, collisionMap, others))) {
                enemy.moveX(deltaX);
            }
            if (deltaY != 0
                    && (collisionService == null
                            || !collisionService.checkCollision(enemy, 0, deltaY, collisionMap, others))) {
                enemy.moveY(deltaY);
            }
        } else {
            enemy.setState(EntityState.IDLE);
        }

        ImageView imageView = enemy.getImageView();
        if (imageView != null) {
            AnimationService animationService = enemy.getAnimationService();
            if (enemy.getState() == EntityState.CHASING) {
                if (Math.abs(dirX) > Math.abs(dirY)) {
                    if (dirX > 0) {
                        animationService.walkingAnimation(imageView, walkRightFrames, 150);
                    } else {
                        animationService.walkingAnimation(imageView, walkLeftFrames, 150);
                    }
                } else {
                    if (dirY > 0) {
                        animationService.walkingAnimation(imageView, walkDownFrames, 150);
                    } else {
                        animationService.walkingAnimation(imageView, walkUpFrames, 150);
                    }
                }
            } else {
                animationService.walkingAnimation(imageView, idleFrames, 150);
            }
        }
        
    }

}
