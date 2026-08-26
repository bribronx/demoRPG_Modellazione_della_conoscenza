package it.unicam.cs.mpgc.rpg126598.strategy;

import java.util.ArrayList;
import java.util.List;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class MeleeAttackStrategy implements AttackStrategy{

    private double cooldown = 0.5;

    public MeleeAttackStrategy() {
    }

    public MeleeAttackStrategy(double cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public List<Entity> getTargetableEntitiesInRange(Entity attacker, List<? extends Entity> targets,
            CollisionService collisionService) {
        List<Entity> targetableEntities = new ArrayList<>();
        if (attacker == null || targets == null) {
            return targetableEntities;
        }

        Bounds attackerHitbox = attacker.getHitbox();
        if (attackerHitbox == null) {
            return targetableEntities;
        }

        double reach = 5.0;
        double sidePadding = 3.0;

        double minX = attackerHitbox.getMinX();
        double minY = attackerHitbox.getMinY();
        double width = attackerHitbox.getWidth();
        double height = attackerHitbox.getHeight();

        Direction dir = attacker.getDirection();
        if (dir == null) dir = Direction.DOWN;

        Bounds attackBox;
        switch (dir) {
            case UP -> attackBox = new BoundingBox(
                    minX - sidePadding,
                    minY - reach,
                    width + (sidePadding * 2),
                    reach + sidePadding
            );
            case DOWN -> attackBox = new BoundingBox(
                    minX - sidePadding,
                    minY + height - sidePadding,
                    width + (sidePadding * 2),
                    reach + sidePadding
            );
            case LEFT -> attackBox = new BoundingBox(
                    minX - reach,
                    minY - sidePadding,
                    reach + sidePadding,
                    height + (sidePadding * 2)
            );
            case RIGHT -> attackBox = new BoundingBox(
                    minX + width - sidePadding,
                    minY - sidePadding,
                    reach + sidePadding,
                    height + (sidePadding * 2)
            );
            default -> attackBox = new BoundingBox(
                    minX - 3.0,
                    minY - 3.0,
                    width + 6.0,
                    height + 6.0
            );
        }

        for (Entity target : targets) {
            if (target != null && target != attacker && attackBox.intersects(target.getHitbox())) {
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
