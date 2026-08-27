package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;

import java.util.ArrayList;
import java.util.List;

public class XpService {

    public interface XpListener {
        void onXpGained(double amount, double currentXp, double requiredXp);
        void onLevelUp(int newLevel, Player player);
    }

    private final Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private XpListener listener;

    private static final double BASE_XP_REQUIRED = 50.0;
    private static final double XP_GROWTH_FACTOR = 1.5;

    public XpService(Player player) {
        this.player = player;
    }

    public void setListener(XpListener listener) {
        this.listener = listener;
    }

    public void addEnemy(Enemy enemy) {
        if (enemy != null && !enemies.contains(enemy)) {
            enemies.add(enemy);
        }
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void clearEnemies() {
        enemies.clear();
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public double getXpRequiredForNextLevel() {
        if (player == null) return BASE_XP_REQUIRED;
        int currentLevel = (int) Math.max(1, player.getLevel());
        return BASE_XP_REQUIRED * Math.pow(XP_GROWTH_FACTOR, currentLevel - 1);
    }

    public void addXp(double amount) {
        if (player == null || amount <= 0 || player.isDead()) return;

        player.setXp(player.getXp() + amount);

        while (player.getXp() >= getXpRequiredForNextLevel()) {
            double required = getXpRequiredForNextLevel();
            player.setXp(player.getXp() - required);
            levelUp();
        }

        if (listener != null) {
            listener.onXpGained(amount, player.getXp(), getXpRequiredForNextLevel());
        }
    }

    public void onEnemyDefeated(Enemy enemy) {
        if (enemy == null) return;
        removeEnemy(enemy);
        double xpReward = enemy.getXp();
        addXp(xpReward);
    }

    public void levelUp() {
        if (player == null) return;

        int newLevel = (int) (player.getLevel() + 1);
        player.setLevel(newLevel);

        scaleEnemiesStats();

        if (listener != null) {
            listener.onLevelUp(newLevel, player);
        }
    }

    public void scaleEnemiesStats() {
        for (Enemy enemy : enemies) {
            if (enemy != null && !enemy.isDead()) {
                scaleEnemy(enemy);
            }
        }
    }

    public void scaleEnemy(Enemy enemy) {
        if (enemy == null) return;
        enemy.setLevel(enemy.getLevel() + 1);
        enemy.setMaxHealth(enemy.getMaxHealth() + 5.0);
        enemy.setHealth(Math.min(enemy.getHealth() + 5.0, enemy.getMaxHealth()));
        enemy.setDamage(enemy.getDamage() + 1.5);
        enemy.setXp(enemy.getXp() + 5.0);
    }

    public Player getPlayer() {
        return player;
    }
}
