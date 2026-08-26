package it.unicam.cs.mpgc.rpg126598.strategy;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.LoadFramesService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.image.ImageView;


public class SkeletonMovementStrategy implements EnemyMovementStrategy {

       private final LoadFramesService loadFramesService = new LoadFramesService();

    private final Image[] idleFrames = loadFramesService.loadFrames("skeleton", "idle", "skeleton_idle", 4);
    private final Image[] walkDownFrames = loadFramesService.loadFrames("skeleton", "walk_down", "skeleton_walk_down", 4);
    private final Image[] walkLeftFrames = loadFramesService.loadFrames("skeleton", "walk_left", "skeleton_walk_left", 4);
    private final Image[] walkRightFrames = loadFramesService.loadFrames("skeleton", "walk_right", "skeleton_walk_right", 4);
    private final Image[] walkUpFrames = loadFramesService.loadFrames("skeleton", "walk_up", "skeleton_walk_up", 4);

    double dirX=0;
    double dirY=0;

    @Override
    public void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService, CollisionService collisionService, double deltaTime) {
        if (target == null || target.getImageView() == null) return;

        // Blocca il movimento e l'aggiornamento dell'animazione di movimento se lo scheletro sta attaccando
        long timeSinceLastAttack = System.currentTimeMillis() - enemy.getLastAttackTime();
        long attackDurationMillis = 1000L;
        if (timeSinceLastAttack < attackDurationMillis) {
            return;
        }

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

            if (deltaX != 0 && (collisionService == null || collisionService.checkCollision(enemy, deltaX, 0, collisionMap, others))) {
                enemy.moveX(deltaX);
            }
            if (deltaY != 0 && (collisionService == null || collisionService.checkCollision(enemy, 0, deltaY, collisionMap, others))) {
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
                        enemy.setDirection(Direction.RIGHT);
                    } else {
                        animationService.walkingAnimation(imageView, walkLeftFrames, 150);
                        enemy.setDirection(Direction.LEFT);
                    }
                } else {
                    if (dirY > 0) {
                        animationService.walkingAnimation(imageView, walkDownFrames, 150);
                        enemy.setDirection(Direction.DOWN);
                    } else {
                        animationService.walkingAnimation(imageView, walkUpFrames, 150);
                        enemy.setDirection(Direction.UP);
                    }
                }
            } else {
                animationService.walkingAnimation(imageView, idleFrames, 150);
            }
        }
        
    }
}
