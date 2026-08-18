package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Direction;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import java.util.List;



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

        public void makeMove(KeyEvent e, List<Enemy> enemies) {
                ImageView imageP = player.getImageView();
                double deltaX = 0;
                double deltaY = 0;
                switch (e.getCode()) {
                        case W:
                                animationService.walkingAnimation(imageP, upFrames, 100);
                                deltaY = -player.getSpeed();
                                player.setDirection(Direction.UP);
                                break;
                        case S:
                                animationService.walkingAnimation(imageP, downFrames, 100);
                                deltaY = player.getSpeed();
                                player.setDirection(Direction.DOWN);
                                break;
                        case A:
                                animationService.walkingAnimation(imageP, leftFrames, 100);
                                deltaX = -player.getSpeed();
                                player.setDirection(Direction.LEFT);
                                break;
                        case D:
                                animationService.walkingAnimation(imageP, rightFrames, 100);
                                deltaX = player.getSpeed();
                                player.setDirection(Direction.RIGHT);
                                break;
                }
                if (deltaX != 0 || deltaY != 0) {
                        tryMovePlayer(deltaX, deltaY, enemies);
                }
        }

        private void tryMovePlayer(double deltaX, double deltaY, List<Enemy> enemies) {
                int[][] collisionMap = mapBuilderService.getCollisionMap();

                if (deltaX != 0 && !collisionService.checkCollision(player, deltaX, 0, collisionMap, enemies)) {
                        player.moveX(deltaX);
                }
                if (deltaY != 0 && !collisionService.checkCollision(player, 0, deltaY, collisionMap, enemies)) {
                        player.moveY(deltaY);
                }
        }

}
