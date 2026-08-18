package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service managing enemy movement and updates across the game world.
 */
public class EnemyMovementService {

    private final List<Enemy> enemies = new ArrayList<>();
    private final CollisionService collisionService;
    private MapBuilderService mapBuilderService;
    private CombatService combatService;

    public EnemyMovementService(MapBuilderService mapBuilderService) {
        this.mapBuilderService = mapBuilderService;
        this.collisionService = new CollisionService();
    }

    public EnemyMovementService(MapBuilderService mapBuilderService, CollisionService collisionService) {
        this.mapBuilderService = mapBuilderService;
        this.collisionService = collisionService != null ? collisionService : new CollisionService();
    }

    public void setCombatService(CombatService combatService) {
        this.combatService = combatService;
    }

    public void addEnemy(Enemy enemy) {
        if (enemy != null && !enemies.contains(enemy)) {
            enemies.add(enemy);
        }
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public List<Enemy> getEnemies() {
        return Collections.unmodifiableList(enemies);
    }

    public void setMapBuilderService(MapBuilderService mapBuilderService) {
        this.mapBuilderService = mapBuilderService;
    }

    /**
     * Aggiorna tutti i nemici
     *
     * @param targetPlayer entità player
     * @param deltaTime    timestamp del frame corrente in nanosecondi
     */
    public void updateEnemies(Player targetPlayer, double deltaTime) {
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.update(targetPlayer, enemies, mapBuilderService, collisionService, deltaTime);

                // Esegui attacco del nemico se è a portata del player e se il cooldown è scaduto
                if (enemy.getAttackStrategy() != null && combatService != null && !targetPlayer.isDead()) {
                    double dist = Math.hypot(targetPlayer.getGlobalX() - enemy.getGlobalX(),
                                             targetPlayer.getGlobalY() - enemy.getGlobalY());
                    if (dist <= enemy.getAttackRange()) {
                        long cooldownMillis = (long) (enemy.getAttackStrategy().getCooldown() * 1000.0);
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - enemy.getLastAttackTime() >= cooldownMillis) {
                            combatService.executeAttack(enemy, List.of(targetPlayer), enemy.getAttackStrategy());
                            enemy.setLastAttackTime(currentTime);
                        }
                    }
                }
            }
        }
    }
}
