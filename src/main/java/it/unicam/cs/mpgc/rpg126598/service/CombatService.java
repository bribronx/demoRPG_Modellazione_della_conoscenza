package it.unicam.cs.mpgc.rpg126598.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Hitbox;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.model.enums.Direction;
import it.unicam.cs.mpgc.rpg126598.model.enums.EntityState;
import it.unicam.cs.mpgc.rpg126598.model.Targetable;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.BoneProjectile;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.SkeletonAttackStrategy;
import it.unicam.cs.mpgc.rpg126598.view.BoneProjectileView;
import it.unicam.cs.mpgc.rpg126598.view.EntityViewFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class CombatService {

    public interface CombatListener {
        void onDamageDealt(Entity attacker, Entity target, double finalDamage);

        void onEntityDeath(Entity deadEntity, Entity killer);
    }

    private final CollisionService collisionService;
    private final LoadFramesService loadFramesService = new LoadFramesService();
    private final AnimationService animationService = new AnimationService();
    private CombatListener listener;
    private MapBuilderService mapBuilderService;

    private Pane parentPane;
    private final List<BoneProjectile> projectiles = new ArrayList<>();
    private final Map<BoneProjectile, BoneProjectileView> projectileViews = new HashMap<>();
    private Image[] boneFrames;

    public CombatService(CollisionService collisionService) {
        this.collisionService = collisionService;
    }

    public void setMapBuilderService(MapBuilderService mapBuilderService) {
        this.mapBuilderService = mapBuilderService;
    }

    public void setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public void setCombatListener(CombatListener listener) {
        this.listener = listener;
    }

    public AnimationService getAnimationService() {
        return animationService;
    }

    public void tryEnemyAttack(Enemy enemy, Player targetPlayer) {
        if (enemy == null || targetPlayer == null || targetPlayer.isDead() || enemy.getAttackStrategy() == null) {
            return;
        }

        boolean inRange = false;
        if (enemy instanceof Slime slime) {
            if (slime.getState() == EntityState.MOVING) {
                List<Entity> targets = slime.getAttackStrategy().getTargetableEntitiesInRange(slime, List.of(targetPlayer), collisionService);
                inRange = !targets.isEmpty();
            }
        } else {
            double dist = Math.hypot(targetPlayer.getX() - enemy.getX(),
                                     targetPlayer.getY() - enemy.getY());
            inRange = (dist <= enemy.getAttackRange());
        }

        if (inRange) {
            long cooldownMillis = (long) (enemy.getAttackStrategy().getCooldown() * 1000.0);
            long currentTime = System.currentTimeMillis();
            if (currentTime - enemy.getLastAttackTime() >= cooldownMillis) {
                executeAttack(enemy, List.of(targetPlayer), enemy.getAttackStrategy());
                enemy.setLastAttackTime(currentTime);
            }
        }
    }

    public void executeAttack(Entity attacker, List<? extends Entity> targets, AttackStrategy attackStrategy) {
        if (attackStrategy instanceof SkeletonAttackStrategy) {
            PauseTransition delay = new PauseTransition(Duration.millis(500));
            delay.setOnFinished(event -> spawnBones(attacker));
            delay.play();
            return;
        }

        List<Entity> targetableEntities = attackStrategy.getTargetableEntitiesInRange(attacker, targets,
                collisionService);
        for (Entity target : targetableEntities) {
            double finalDamage = Math.max(1, attacker.getDamage());
            if (target instanceof Targetable targetable) {
                targetable.takeDamage(finalDamage);
                if (listener != null) {
                    listener.onDamageDealt(attacker, target, finalDamage);
                }
                applyKnockback(attacker, target, 16.0);
                if (targetable.isDead()) {
                    if (listener != null) {
                        listener.onEntityDeath(target, attacker);
                    }
                }
            }
        }
    }

    public void applyKnockback(Entity attacker, Entity target, double distance) {
        if (attacker == null || target == null)
            return;
        double dx = target.getX() - attacker.getX();
        double dy = target.getY() - attacker.getY();
        double len = Math.hypot(dx, dy);

        double dirX = 0;
        double dirY = 0;

        if (len > 0.0001) {
            dirX = dx / len;
            dirY = dy / len;
        } else if (attacker.getDirection() != null) {
            switch (attacker.getDirection()) {
                case UP -> dirY = -1;
                case DOWN -> dirY = 1;
                case LEFT -> dirX = -1;
                case RIGHT -> dirX = 1;
            }
        } else {
            dirY = 1;
        }

        applyKnockbackDirection(target, dirX, dirY, distance);
    }

    public void applyKnockback(double sourceX, double sourceY, Entity target, double distance) {
        if (target == null)
            return;
        double dx = target.getX() - sourceX;
        double dy = target.getY() - sourceY;
        double len = Math.hypot(dx, dy);

        double dirX = 0;
        double dirY = 0;

        if (len > 0.0001) {
            dirX = dx / len;
            dirY = dy / len;
        } else {
            dirY = 1;
        }

        applyKnockbackDirection(target, dirX, dirY, distance);
    }

    private void applyKnockbackDirection(Entity target, double dirX, double dirY, double distance) {
        if (target == null || distance <= 0)
            return;
        int[][] collisionMap = mapBuilderService != null ? mapBuilderService.getCollisionMap() : null;

        double step = 1.0;
        int steps = (int) Math.ceil(distance / step);
        double stepX = dirX * step;
        double stepY = dirY * step;

        for (int i = 0; i < steps; i++) {
            if (stepX != 0 && (collisionService == null || collisionMap == null
                    || !collisionService.checkTileCollision(target, stepX, 0, collisionMap))) {
                target.moveX(stepX);
            }
            if (stepY != 0 && (collisionService == null || collisionMap == null
                    || !collisionService.checkTileCollision(target, 0, stepY, collisionMap))) {
                target.moveY(stepY);
            }
        }
    }

    public void spawnBones(Entity attacker) {
        if (parentPane == null)
            return;

        if (boneFrames == null) {
            boneFrames = loadFramesService.loadFrames("skeleton", "bone_sprites", "bone_frame", 8);
        }

        double startX = attacker.getX();
        double startY = attacker.getY();
        double damage = attacker.getDamage();

        double speed = 1.0;
        double vx = 0;
        double vy = 0;

        switch (attacker.getDirection()) {
            case UP:
                vy = -speed;
                break;
            case DOWN:
                vy = speed;
                break;
            case LEFT:
                vx = -speed;
                break;
            case RIGHT:
                vx = speed;
                break;
        }

        BoneProjectile proj = new BoneProjectile(startX, startY, vx, vy, damage);
        BoneProjectileView view = EntityViewFactory.createBoneProjectileView(proj, boneFrames);
        projectiles.add(proj);
        projectileViews.put(proj, view);
        parentPane.getChildren().add(view.getImageView());
    }

    public void updateProjectiles(Player player, int[][] collisionMap) {
        List<BoneProjectile> toRemove = new ArrayList<>();
        Hitbox playerHitbox = player.getHitbox();

        for (BoneProjectile proj : projectiles) {
            proj.update();
            BoneProjectileView view = projectileViews.get(proj);
            if (view != null) {
                view.update(proj);
            }

            Hitbox projHitbox = proj.getHitbox();

            if (projHitbox.intersects(playerHitbox)) {
                player.takeDamage(proj.getDamage());
                if (listener != null) {
                    listener.onDamageDealt(null, player, proj.getDamage());
                }
                applyKnockback(proj.getX(), proj.getY(), player, 16.0);
                if (player.isDead()) {
                    if (listener != null) {
                        listener.onEntityDeath(player, null);
                    }
                }
                toRemove.add(proj);
            } else if (collisionService.checkTileCollision(projHitbox, collisionMap)) {
                toRemove.add(proj);
            } else if (proj.isExpired()) {
                toRemove.add(proj);
            }
        }

        for (BoneProjectile proj : toRemove) {
            BoneProjectileView view = projectileViews.remove(proj);
            if (view != null && parentPane != null) {
                parentPane.getChildren().remove(view.getImageView());
            }
        }
        projectiles.removeAll(toRemove);
    }

    public void attackAnimation(Entity attacker, ImageView imageView, AnimationService animService) {
        if (attacker == null || imageView == null || animService == null) return;
        String entityName = attacker.getClass().getSimpleName().toLowerCase();

        if (attacker instanceof Zombie) {
            return;
        }

        String directionStr = attacker.getDirection().name().toLowerCase();
        String folder2 = "attack/attack_" + directionStr;
        String prefix = entityName + "_attack_" + directionStr;

        int frameCount = 4;
        double frameDuration = 60;

        if (attacker instanceof Player) {
            if (attacker.getDirection() == Direction.DOWN) {
                frameCount = 4;
            } else {
                frameCount = 3;
            }
            frameDuration = 130;
        } else if (attacker instanceof Skeleton) {
            frameCount = 4;
            frameDuration = 250;
        }

        Image[] frames = loadFramesService.loadFrames(entityName, folder2, prefix, frameCount);
        animService.attackAnimation(imageView, frames, frameDuration);
    }

    public void deathAnimation(Entity entity, ImageView imageView, AnimationService animService, Runnable onFinished) {
        if (entity == null || imageView == null || animService == null) {
            if (onFinished != null) onFinished.run();
            return;
        }
        String entityName = entity.getClass().getSimpleName().toLowerCase();
        String folder2 = "death";
        String prefix = entityName + "_death";

        int frameCount = 0;
        double frameDuration = 150;

        if (entity instanceof Player) {
            frameCount = 2;
            frameDuration = 500;
        } else if (entity instanceof Skeleton) {
            frameCount = 4;
            frameDuration = 150;
        }

        if (frameCount == 0) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        Image[] frames = loadFramesService.loadFrames(entityName, folder2, prefix, frameCount);
        animService.deathAnimation(imageView, frames, frameDuration, onFinished);
    }
}
