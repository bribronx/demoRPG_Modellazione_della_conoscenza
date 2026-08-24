package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class SlimeAttackStrategy implements AttackStrategy {

    private double cooldown = 0.5;
    // Margine di contatto per rilevare l'impatto del salto
    private double margin = 3.0;

    public SlimeAttackStrategy() {
    }

    public SlimeAttackStrategy(double cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public List<Entity> getTargetableEntitiesInRange(Entity attacker, List<? extends Entity> targets,
            CollisionService collisionService) {
        List<Entity> targetableEntities = new ArrayList<>();
        if (attacker == null || targets == null) return targetableEntities;

        Bounds aBox = attacker.getHitbox();
        if (aBox == null) return targetableEntities;

        
        Bounds contactBox = new BoundingBox(
                aBox.getMinX() - margin,
                aBox.getMinY() - margin,
                aBox.getWidth() + (margin * 2),
                aBox.getHeight() + (margin * 2)
        );

        for (Entity target : targets) {
            if (target != null && target != attacker && contactBox.intersects(target.getHitbox())) {
                targetableEntities.add(target);
            }
        }
        return targetableEntities;
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
