package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.strategy.ZombieMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.MeleeAttackStrategy;

public class Zombie extends Enemy {

    public Zombie() {
        super(EnemyType.ZOMBIE, new ZombieMovementStrategy());
        this.setAttackStrategy(new MeleeAttackStrategy());
        this.setSpeed(0.2);
        this.setWidth(14.0);
        this.setHeight(14.0);
        this.setBoundBox(4, 4, 12, 12);
        this.setHealth(100);
        this.setMaxHealth(100);
        this.setDamage(5);
        this.setLevel(1);
        this.setXp(40);
        this.setAggroRange(60.0);
    }
}
