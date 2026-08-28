package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.strategy.SlimeMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.SlimeAttackStrategy;

public class Slime extends Enemy {

    public Slime() {
        super(EnemyType.SLIME, new SlimeMovementStrategy());
        this.setAttackStrategy(new SlimeAttackStrategy());
        this.setSpeed(0.8);
        this.setWidth(12.0);
        this.setHeight(12.0);
        this.setBoundBox(4, 4, 12, 12);
        this.setHealth(10);
        this.setMaxHealth(10);
        this.setDamage(2);
        this.setLevel(1);
        this.setXp(25);
        this.setAggroRange(60.0);
    }
}
