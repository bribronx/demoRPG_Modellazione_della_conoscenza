package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;
import it.unicam.cs.mpgc.rpg126598.strategy.EnemyMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import java.util.List;

public abstract class Enemy extends Entity {

    private EnemyType enemyType;
    private EnemyMovementStrategy movementStrategy;
    private AttackStrategy attackStrategy;
    private double aggroRange = 120.0;
    private double attackRange = 20.0;


    public Enemy() {
    }

    public Enemy(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public Enemy(EnemyMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }

    public Enemy(EnemyType enemyType, EnemyMovementStrategy movementStrategy) {
        this.enemyType = enemyType;
        this.movementStrategy = movementStrategy;
    }

    public Enemy(EnemyMovementStrategy movementStrategy, AttackStrategy attackStrategy) {
        this.movementStrategy = movementStrategy;
        this.attackStrategy = attackStrategy;
    }

    public Enemy(EnemyType enemyType, EnemyMovementStrategy movementStrategy, AttackStrategy attackStrategy) {
        this.enemyType = enemyType;
        this.movementStrategy = movementStrategy;
        this.attackStrategy = attackStrategy;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
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

    public void update(Player target, List<Enemy> enemies, MapBuilderService mapBuilderService, CollisionService collisionService, double deltaTime) {
        if (movementStrategy != null) {
            movementStrategy.move(this, target, enemies, mapBuilderService, collisionService, deltaTime);
        }
    }
}
