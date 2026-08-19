package it.unicam.cs.mpgc.rpg126598.service;

import java.util.List;
import java.util.ArrayList;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import it.unicam.cs.mpgc.rpg126598.model.Targetable;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.Direction;
import it.unicam.cs.mpgc.rpg126598.model.BoneProjectile;
import it.unicam.cs.mpgc.rpg126598.strategy.AttackStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.SkeletonAttackStrategy;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class CombatService {

    public interface CombatListener {
        void onDamageDealt(Entity attacker, Entity target, int damage);

        void onEntityDeath(Entity deadEntity, Entity killer);
    }

    private final CollisionService collisionService;
    private final LoadFramesService loadFramesService = new LoadFramesService();
    private CombatListener listener;
    
    private Pane parentPane;
    private final List<BoneProjectile> projectiles = new ArrayList<>();
    private Image[] boneFrames;

    public CombatService(CollisionService collisionService) {
        this.collisionService = collisionService;
    }

    public void setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public void setCombatListener(CombatListener listener) {
        this.listener = listener;
    }

    public void executeAttack(Entity attacker, List<? extends Entity> targets, AttackStrategy attackStrategy) {
        if (attackStrategy instanceof SkeletonAttackStrategy) {
            attackAnimation(attacker);

            PauseTransition delay = new PauseTransition(Duration.millis(500));
            delay.setOnFinished(event -> spawnBones(attacker));
            delay.play();
            return;
        }

        List<Entity> targetableEntities = attackStrategy.getTargetableEntitiesInRange(attacker, targets,
                collisionService);
        for (Entity target : targetableEntities) {
            int finalDamage = Math.max(1, attacker.getDamage() - target.getDefense());
            if (target instanceof Targetable targetable) {
                targetable.takeDamage(finalDamage);
                if (listener != null) {
                    listener.onDamageDealt(attacker, target, finalDamage);
                }
                if (targetable.isDead()) {
                    if (listener != null) {
                        listener.onEntityDeath(target, attacker);
                    }
                }
            }
        }
    }

    public void spawnBones(Entity attacker) {
        if (parentPane == null) return;
        
        if (boneFrames == null) {
            boneFrames = loadFramesService.loadFrames("skeleton", "bone_sprites", "bone_frame", 8);
        }

        double startX = attacker.getGlobalX();
        double startY = attacker.getGlobalY();
        int damage = attacker.getDamage();

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

        BoneProjectile proj = new BoneProjectile(startX, startY, vx, vy, damage, boneFrames);
        projectiles.add(proj);
        parentPane.getChildren().add(proj.getImageView());
    }

    public void updateProjectiles(Player player, int[][] collisionMap) {
        List<BoneProjectile> toRemove = new ArrayList<>();
        Bounds playerHitbox = player.getHitbox();

        for (BoneProjectile proj : projectiles) {
            proj.update();

            Bounds projHitbox = new BoundingBox(proj.getX(), proj.getY(), 8, 8);


            if (projHitbox.intersects(playerHitbox)) {
                player.takeDamage(proj.getDamage());
                if (listener != null) {
                    listener.onDamageDealt(null, player, proj.getDamage());
                }
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
            if (parentPane != null) {
                parentPane.getChildren().remove(proj.getImageView());
            }
        }
        projectiles.removeAll(toRemove);
    }

    public void attackAnimation(Entity attacker) {
        String entityName = attacker.getClass().getSimpleName().toLowerCase();
        
        if (attacker instanceof Zombie) {
            return;
        }

        String directionStr = attacker.getDirection().name().toLowerCase();
        String folder2 = "attack/attack_" + directionStr;
        String prefix = entityName + "_attack_" + directionStr;

        int frameCount = 4;
        double frameDuration = 60; // default

        if (attacker instanceof Player) {
            if (attacker.getDirection() == Direction.DOWN) {
                frameCount = 4;
            } else {
                frameCount = 3;
            }
        } else if (attacker instanceof Skeleton) {
            frameCount = 4;
            frameDuration = 250; 
        }

        Image[] frames = loadFramesService.loadFrames(entityName, folder2, prefix, frameCount);
        attacker.getAnimationService().attackAnimation(attacker.getImageView(), frames, frameDuration);
    }

    public void deathAnimation(Entity entity) {
        deathAnimation(entity, null);
    }

    public void deathAnimation(Entity entity, Runnable onFinished) {
        String entityName = entity.getClass().getSimpleName().toLowerCase();
        String folder2 = "death";
        String prefix = entityName + "_death";

        int frameCount = 0;
        double frameDuration = 150;

        if (entity instanceof Player) {
            frameCount = 2;
            frameDuration = 300;
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
        entity.getAnimationService().deathAnimation(entity.getImageView(), frames, frameDuration, onFinished);
    }

}
