package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import java.util.List;
import java.util.Set;



public class PlayerMovementService {

        private final Player player;
        private final LoadFramesService loadFramesService = new LoadFramesService();

        Image[] rightFrames = loadFramesService.loadFrames("player","walk_right", "player_walk_right", 6);

        Image[] leftFrames = loadFramesService.loadFrames("player","walk_left", "player_walk_left", 6);

        Image[] upFrames =loadFramesService.loadFrames("player","walk_up", "player_walk_up", 6);

        Image[] downFrames = loadFramesService.loadFrames("player","walk_down", "player_walk_down", 6);

        private CollisionService collisionService = new CollisionService();
        private MapBuilderService mapBuilderService;

        public PlayerMovementService(Player player, MapBuilderService mapBuilderService) {
                this.player = player;
                this.mapBuilderService = mapBuilderService;
        }

        public void updateMovement(Set<KeyCode> activeKeys, List<Enemy> enemies) {
                AnimationService animationService = player.getAnimationService();
                if (animationService.isAttacking()) {
                        return;
                }

                ImageView imageP = player.getImageView();
                double deltaX = 0;
                double deltaY = 0;

                if (activeKeys.contains(KeyCode.W) || activeKeys.contains(KeyCode.UP)) {
                        deltaY -= player.getSpeed();
                }
                if (activeKeys.contains(KeyCode.S) || activeKeys.contains(KeyCode.DOWN)) {
                        deltaY += player.getSpeed();
                }
                if (activeKeys.contains(KeyCode.A) || activeKeys.contains(KeyCode.LEFT)) {
                        deltaX -= player.getSpeed();
                }
                if (activeKeys.contains(KeyCode.D) || activeKeys.contains(KeyCode.RIGHT)) {
                        deltaX += player.getSpeed();
                }

                if (deltaX != 0 && deltaY != 0) {
                        deltaX *= 0.7;
                        deltaY *= 0.7;
                }

                if (deltaX != 0 || deltaY != 0) {
                        if (deltaY < 0 && (player.getDirection() == Direction.UP || deltaX == 0)) {
                                player.setDirection(Direction.UP);
                        } else if (deltaY > 0 && (player.getDirection() == Direction.DOWN || deltaX == 0)) {
                                player.setDirection(Direction.DOWN);
                        } else if (deltaX < 0) {
                                player.setDirection(Direction.LEFT);
                        } else if (deltaX > 0) {
                                player.setDirection(Direction.RIGHT);
                        } else if (deltaY < 0) {
                                player.setDirection(Direction.UP);
                        } else if (deltaY > 0) {
                                player.setDirection(Direction.DOWN);
                        }

                        switch (player.getDirection()) {
                                case UP -> animationService.walkingAnimation(imageP, upFrames, 100);
                                case DOWN -> animationService.walkingAnimation(imageP, downFrames, 100);
                                case LEFT -> animationService.walkingAnimation(imageP, leftFrames, 100);
                                case RIGHT -> animationService.walkingAnimation(imageP, rightFrames, 100);
                        }

                        tryMovePlayer(deltaX, deltaY, enemies);
                } else {
                        animationService.stopWalking();
                }
        }

        private void tryMovePlayer(double deltaX, double deltaY, List<Enemy> enemies) {
                int[][] collisionMap = mapBuilderService.getCollisionMap();

                if (deltaX != 0 && collisionService.checkCollision(player, deltaX, 0, collisionMap, enemies)) {
                        player.moveX(deltaX);
                }
                if (deltaY != 0 && collisionService.checkCollision(player, 0, deltaY, collisionMap, enemies)) {
                        player.moveY(deltaY);
                }
        }

}
