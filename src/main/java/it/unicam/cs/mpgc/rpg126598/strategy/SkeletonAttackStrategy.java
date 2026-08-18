package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.Collections;
import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;

public class SkeletonAttackStrategy implements AttackStrategy {

    private double cooldown = 2.0;

    @Override
    public List<Entity> getTargetableEntitiesInRange(Entity attacker, List<? extends Entity> targets,
            CollisionService collisionService) {
        // Il danno dell'osso viene gestito al momento della collisione del proiettile,
        // quindi la strategia restituisce una lista vuota per evitare danni istantanei in corpo a corpo.
        return Collections.emptyList();
    }

    @Override
    public double getCooldown() {
        return this.cooldown;
    }

    @Override
    public double setCooldown(double cooldown) {
        this.cooldown = cooldown;
        return cooldown;
    }
}
