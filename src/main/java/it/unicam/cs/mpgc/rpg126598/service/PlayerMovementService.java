package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import javafx.scene.input.KeyCode;

import java.util.List;
import java.util.Set;

public class PlayerMovementService {

    private final Player player;
    private final CollisionService collisionService;
    private final MapBuilderService mapBuilderService;

    public PlayerMovementService(Player player, MapBuilderService mapBuilderService) {
        this(player, mapBuilderService, new CollisionService());
    }

    public PlayerMovementService(Player player, MapBuilderService mapBuilderService, CollisionService collisionService) {
        this.player = player;
        this.mapBuilderService = mapBuilderService;
        this.collisionService = collisionService != null ? collisionService : new CollisionService();
    }

    public void updateMovement(Set<KeyCode> activeKeys, List<Enemy> enemies) {
        if (player == null || player.isDead()) return;

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
            player.setState(EntityState.MOVING);
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

            tryMovePlayer(deltaX, deltaY, enemies);
        } else {
            player.setState(EntityState.IDLE);
        }
    }

    private void tryMovePlayer(double deltaX, double deltaY, List<Enemy> enemies) {
        int[][] collisionMap = mapBuilderService != null ? mapBuilderService.getCollisionMap() : null;

        if (deltaX != 0 && collisionService.checkCollision(player, deltaX, 0, collisionMap, enemies)) {
            player.moveX(deltaX);
        }
        if (deltaY != 0 && collisionService.checkCollision(player, 0, deltaY, collisionMap, enemies)) {
            player.moveY(deltaY);
        }
    }
}
