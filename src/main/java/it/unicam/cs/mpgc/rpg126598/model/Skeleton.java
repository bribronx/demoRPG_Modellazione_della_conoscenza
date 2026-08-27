package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.strategy.SkeletonMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.SkeletonAttackStrategy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Skeleton extends Enemy{

    public Skeleton(){
        super(EnemyType.SKELETON, new SkeletonMovementStrategy());
        this.setAttackStrategy(new SkeletonAttackStrategy());
        this.setSpeed(0.5);
        this.setBoundBox(4, 4, 8, 8);
        this.setHealth(50);
        this.setMaxHealth(50);
        this.setDamage(10);
        this.setLevel(1);
        this.setXp(60);
        this.setAggroRange(120);
        this.setAttackRange(50.0);
        this.setImageView(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/skeleton/idle/skeleton_idle_01.png")))));
        this.getImageView().setFitHeight(10.0);
        this.getImageView().setFitWidth(10.0);
        this.getImageView().setPreserveRatio(true);
    }

}
