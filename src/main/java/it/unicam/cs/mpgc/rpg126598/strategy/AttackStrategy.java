package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;

public interface AttackStrategy {
    List<Entity> getTargetableEntitiesInRange(Entity attacker, List<? extends Entity> targets, CollisionService collisionService);
    double getCooldown();
    double setCooldown(double cooldown);
}
