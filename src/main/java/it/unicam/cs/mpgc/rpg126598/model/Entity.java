package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;

public abstract class Entity implements Targetable {
    private double x;
    private double y;
    private double width = 16.0;
    private double height = 16.0;
    private double hitboxOffsetX = 0.0;
    private double hitboxOffsetY = 0.0;
    private double hitboxWidth = 16.0;
    private double hitboxHeight = 16.0;

    private double speed;
    private double health;
    private double maxHealth;
    private double defense;
    private double maxDefense;
    private double damage;
    private double level;
    private double xp;
    private EntityState state = EntityState.IDLE;
    private Direction direction = Direction.DOWN;
    private long lastAttackTime;

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
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

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getGlobalX() {
        return x;
    }

    public double getGlobalY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setBoundBox(double offsetX, double offsetY, double width, double height) {
        this.hitboxOffsetX = offsetX;
        this.hitboxOffsetY = offsetY;
        this.hitboxWidth = width;
        this.hitboxHeight = height;
    }

    public double getHitboxOffsetX() {
        return hitboxOffsetX;
    }

    public double getHitboxOffsetY() {
        return hitboxOffsetY;
    }

    public double getHitboxWidth() {
        return hitboxWidth;
    }

    public double getHitboxHeight() {
        return hitboxHeight;
    }

    public Hitbox getHitboxAt(double deltaX, double deltaY) {
        double hitboxX = this.x + this.hitboxOffsetX + deltaX;
        double hitboxY = this.y + this.hitboxOffsetY + deltaY;
        return new Hitbox(hitboxX, hitboxY, this.hitboxWidth, this.hitboxHeight);
    }

    public Hitbox getHitbox() {
        return getHitboxAt(0, 0);
    }

    public void moveX(double deltaX) {
        this.x += deltaX;
    }

    public void moveY(double deltaY) {
        this.y += deltaY;
    }

    public void move(double deltaX, double deltaY) {
        this.x += deltaX;
        this.y += deltaY;
    }

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
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

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    @Override
    public void takeDamage(double amount) {
        if (this.defense > 0) {
            if (amount <= this.defense) {
                this.defense -= amount;
                amount = 0;
            } else {
                amount -= this.defense;
                this.defense = 0;
            }
        }
        if (amount > 0) {
            this.setHealth(Math.max(0, this.getHealth() - amount));
        }
    }

    @Override
    public boolean isDead() {
        return this.getHealth() <= 0;
    }
}
