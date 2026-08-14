package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;



public class PlayerMovementService {

        private final Player player;
        private final LoadFramesService loadFramesService = new LoadFramesService();

        Image[] rightFrames = loadFramesService.loadFrames("player","walk_right", "player_walk_right", 6);

        Image[] leftFrames = loadFramesService.loadFrames("player","walk_left", "player_walk_left", 6);

        Image[] upFrames =loadFramesService.loadFrames("player","walk_up", "player_walk_up", 6);

        Image[] downFrames = loadFramesService.loadFrames("player","walk_down", "player_walk_down", 6);

        private CollisionService collisionService = new CollisionService();
        private MapBuilderService mapBuilderService;
        private AnimationService animationService;

        public PlayerMovementService(Player player, MapBuilderService mapBuilderService) {
                this.player = player;
                this.mapBuilderService = mapBuilderService;
                this.animationService = new AnimationService();
        }

        public void makeMove(KeyEvent e) {
                ImageView imageP = player.getImageView();
                double deltaX = 0;
                double deltaY = 0;
                switch (e.getCode()) {
                        case W:
                                animationService.walkingAnimation(imageP, upFrames);
                                deltaY = -player.getSpeed();
                                break;
                        case S:
                                animationService.walkingAnimation(imageP, downFrames);
                                deltaY = player.getSpeed();
                                break;
                        case A:
                                animationService.walkingAnimation(imageP, leftFrames);
                                deltaX = -player.getSpeed();
                                break;
                        case D:
                                animationService.walkingAnimation(imageP, rightFrames);
                                deltaX = player.getSpeed();
                                break;
                        default:
                                animationService.walkingAnimation(imageP, downFrames);
                                break;
                }
                if (deltaX != 0 || deltaY != 0) {
                        tryMovePlayer(deltaX, deltaY);
                }
        }

        private void tryMovePlayer(double deltaX, double deltaY) {
                int[][] collisionMap = mapBuilderService.getCollisionMap();

                if (deltaX != 0 && !collisionService.checkCollision(player, deltaX, 0, collisionMap, null)) {
                        player.moveX(deltaX);
                }
                if (deltaY != 0 && !collisionService.checkCollision(player, 0, deltaY, collisionMap, null)) {
                        player.moveY(deltaY);
                }
        }

}
