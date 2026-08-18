package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.strategy.EnemyMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import java.util.List;

public abstract class Enemy extends Entity {

    private EnemyMovementStrategy movementStrategy;
    private AttackStrategy attackStrategy;
    private long lastAttackTime = 0;
    private double aggroRange = 120.0;
    private double attackRange = 20.0;


    public Enemy() {
    }

    public Enemy(EnemyMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public Enemy(EnemyMovementStrategy movementStrategy, AttackStrategy attackStrategy) {
        this.movementStrategy = movementStrategy;
        this.attackStrategy = attackStrategy;
    }

    public EnemyMovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(EnemyMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public double getAggroRange() {
        return aggroRange;
    }

    public void setAggroRange(double aggroRange) {
        this.aggroRange = aggroRange;
    }

    public double getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(double attackRange) {
        this.attackRange = attackRange;
    }

   

    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }

    public void setAttackStrategy(AttackStrategy attackStrategy) {
        this.attackStrategy = attackStrategy;
    }

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public void update(Player target, List<Enemy> enemies, MapBuilderService mapBuilderService, CollisionService collisionService, double deltaTime) {
        if (movementStrategy != null) {
            movementStrategy.move(this, target, enemies, mapBuilderService, collisionService, deltaTime);
        }
    }
}
