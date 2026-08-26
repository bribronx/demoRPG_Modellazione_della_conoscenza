package it.unicam.cs.mpgc.rpg126598.strategy;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;

import java.util.ArrayList;
import java.util.List;


public class ChasingMovementStrategy implements EnemyMovementStrategy {

    @Override
    public void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService, CollisionService collisionService, double deltaTime) {
        if (target == null || target.getImageView() == null) return;

        double enemyX = enemy.getGlobalX();
        double enemyY = enemy.getGlobalY();
        double playerX = target.getGlobalX();
        double playerY = target.getGlobalY();

        double distance = Math.hypot(playerX - enemyX, playerY - enemyY);

        if (distance <= enemy.getAggroRange() && distance > 0) {
            enemy.setState(EntityState.CHASING);
            double dirX = (playerX - enemyX) / distance;
            double dirY = (playerY - enemyY) / distance;

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
    }
}
