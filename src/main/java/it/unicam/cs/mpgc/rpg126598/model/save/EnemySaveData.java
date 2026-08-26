package it.unicam.cs.mpgc.rpg126598.model.save;

import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;

public class EnemySaveData {
    private EnemyType type;
    private double x;
    private double y;
    private double health;
    private double maxHealth;
    private double defense;
    private double maxDefense;
    private double damage;
    private double speed;
    private Direction direction;
    private EntityState state;

    public EnemySaveData() {
    }

    public EnemySaveData(EnemyType type, double x, double y, double health, double maxHealth, double defense,
                         double maxDefense, double damage, double speed, Direction direction, EntityState state) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.health = health;
        this.maxHealth = maxHealth;
        this.defense = defense;
        this.maxDefense = maxDefense;
        this.damage = damage;
        this.speed = speed;
        this.direction = direction;
        this.state = state;
    }

    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getMaxDefense() {
        return maxDefense;
    }

    public void setMaxDefense(double maxDefense) {
        this.maxDefense = maxDefense;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }
}
