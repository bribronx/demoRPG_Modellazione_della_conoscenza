package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;

public class MeleeAttackStrategy implements AttackStrategy{



    private double cooldown = 1.0;

    @Override
    public List<Entity> getTargetableEntitiesInRange(Entity attacker, List<? extends Entity> targets,
            CollisionService collisionService) {
                List<Entity> targetableEntities = new ArrayList<>();
                javafx.geometry.Bounds attackerHitbox = attacker.getHitbox();
                
                // Espandiamo la hitbox dell'attacco corpo a corpo per raggiungere i bersagli adiacenti
                double range = 12.0; 
                javafx.geometry.Bounds attackBox = new javafx.geometry.BoundingBox(
                    attackerHitbox.getMinX() - range,
                    attackerHitbox.getMinY() - range,
                    attackerHitbox.getWidth() + (range * 2),
                    attackerHitbox.getHeight() + (range * 2)
                );

                for(Entity target:targets){
                    if (target != attacker && attackBox.intersects(target.getHitbox())) {
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
