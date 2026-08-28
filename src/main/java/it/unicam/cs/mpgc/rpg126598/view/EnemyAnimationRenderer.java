package it.unicam.cs.mpgc.rpg126598.view;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;
import it.unicam.cs.mpgc.rpg126598.service.LoadFramesService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;


public class EnemyAnimationRenderer {

    private final LoadFramesService loadFramesService = new LoadFramesService();
    private final Map<Enemy, AnimationService> animationServices = new HashMap<>();

    // Skeleton frames
    private final Image[] skeletonIdle;
    private final Image[] skeletonWalkDown;
    private final Image[] skeletonWalkLeft;
    private final Image[] skeletonWalkRight;
    private final Image[] skeletonWalkUp;

    // Slime frames
    private final Image[] slimeIdle;
    private final Image[] slimeWalkDown;
    private final Image[] slimeWalkLeft;
    private final Image[] slimeWalkRight;
    private final Image[] slimeWalkUp;

    // Zombie frames
    private final Image[] zombieIdle;
    private final Image[] zombieWalkDown;
    private final Image[] zombieWalkLeft;
    private final Image[] zombieWalkRight;
    private final Image[] zombieWalkUp;

    public EnemyAnimationRenderer() {
        skeletonIdle = loadFramesService.loadFrames("skeleton", "idle", "skeleton_idle", 4);
        skeletonWalkDown = loadFramesService.loadFrames("skeleton", "walk_down", "skeleton_walk_down", 4);
        skeletonWalkLeft = loadFramesService.loadFrames("skeleton", "walk_left", "skeleton_walk_left", 4);
        skeletonWalkRight = loadFramesService.loadFrames("skeleton", "walk_right", "skeleton_walk_right", 4);
        skeletonWalkUp = loadFramesService.loadFrames("skeleton", "walk_up", "skeleton_walk_up", 4);

        slimeIdle = loadFramesService.loadFrames("slime", "idle", "slime_idle", 4);
        slimeWalkDown = loadFramesService.loadFrames("slime", "walk_down", "slime_walk_down", 4);
        slimeWalkLeft = loadFramesService.loadFrames("slime", "walk_left", "slime_walk_left", 4);
        slimeWalkRight = loadFramesService.loadFrames("slime", "walk_right", "slime_walk_right", 4);
        slimeWalkUp = loadFramesService.loadFrames("slime", "walk_up", "slime_walk_up", 4);

        zombieIdle = loadFramesService.loadFrames("zombie", "idle", "zombie_idle", 8);
        zombieWalkDown = loadFramesService.loadFrames("zombie", "walk_down", "zombie_walk_down", 8);
        zombieWalkLeft = loadFramesService.loadFrames("zombie", "walk_left", "zombie_walk_left", 8);
        zombieWalkRight = loadFramesService.loadFrames("zombie", "walk_right", "zombie_walk_right", 8);
        zombieWalkUp = loadFramesService.loadFrames("zombie", "walk_up", "zombie_walk_up", 8);
    }

    public AnimationService getAnimationService(Enemy enemy) {
        return animationServices.computeIfAbsent(enemy, k -> new AnimationService());
    }

    public void removeEnemy(Enemy enemy) {
        AnimationService as = animationServices.remove(enemy);
        if (as != null) {
            as.stopAll();
        }
    }

    public void clear() {
        for (AnimationService as : animationServices.values()) {
            as.stopAll();
        }
        animationServices.clear();
    }

    public void updateAnimation(Enemy enemy, ImageView imageView) {
        if (enemy == null || imageView == null) return;

        AnimationService as = getAnimationService(enemy);
        if (as.isAttacking()) {
            return;
        }

        if (enemy instanceof Skeleton) {
            renderSkeletonAnimation(enemy, imageView, as);
        } else if (enemy instanceof Slime) {
            renderSlimeAnimation(enemy, imageView, as);
        } else if (enemy instanceof Zombie) {
            renderZombieAnimation(enemy, imageView, as);
        }
    }

    private void renderSkeletonAnimation(Enemy enemy, ImageView imageView, AnimationService as) {
        if (enemy.getState() == EntityState.CHASING) {
            switch (enemy.getDirection()) {
                case DOWN -> as.walkingAnimation(imageView, skeletonWalkDown, 150);
                case LEFT -> as.walkingAnimation(imageView, skeletonWalkLeft, 150);
                case RIGHT -> as.walkingAnimation(imageView, skeletonWalkRight, 150);
                case UP -> as.walkingAnimation(imageView, skeletonWalkUp, 150);
            }
        } else {
            as.walkingAnimation(imageView, skeletonIdle, 150);
        }
    }

    private void renderSlimeAnimation(Enemy enemy, ImageView imageView, AnimationService as) {
        if (enemy.getState() == EntityState.MOVING) {
            switch (enemy.getDirection()) {
                case DOWN -> as.walkingAnimation(imageView, slimeWalkDown, 180);
                case LEFT -> as.walkingAnimation(imageView, slimeWalkLeft, 180);
                case RIGHT -> as.walkingAnimation(imageView, slimeWalkRight, 180);
                case UP -> as.walkingAnimation(imageView, slimeWalkUp, 180);
            }
        } else {
            as.walkingAnimation(imageView, slimeIdle, 180);
        }
    }

    private void renderZombieAnimation(Enemy enemy, ImageView imageView, AnimationService as) {
        if (enemy.getState() == EntityState.CHASING) {
            switch (enemy.getDirection()) {
                case DOWN -> as.walkingAnimation(imageView, zombieWalkDown, 150);
                case LEFT -> as.walkingAnimation(imageView, zombieWalkLeft, 150);
                case RIGHT -> as.walkingAnimation(imageView, zombieWalkRight, 150);
                case UP -> as.walkingAnimation(imageView, zombieWalkUp, 150);
            }
        } else {
            as.walkingAnimation(imageView, zombieIdle, 150);
        }
    }
}
