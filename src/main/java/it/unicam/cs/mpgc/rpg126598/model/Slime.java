package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.strategy.SlimeMovementStrategy;
import it.unicam.cs.mpgc.rpg126598.strategy.SlimeAttackStrategy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public class Slime extends Enemy {

    public Slime() {
        super(EnemyType.SLIME, new SlimeMovementStrategy());
        this.setAttackStrategy(new SlimeAttackStrategy());
        this.setSpeed(0.8);
        this.setBoundBox(4, 4, 12, 12);
        this.setHealth(10);
        this.setMaxHealth(10);
        this.setDamage(2);
        this.setLevel(1);
        this.setXp(25);
        this.setAggroRange(60.0);
        this.setImageView(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/slime/idle/slime_idle_01.png")))));
        this.getImageView().setFitHeight(12.0);
        this.getImageView().setFitWidth(12.0);
        this.getImageView().setPreserveRatio(true);

    }

   
}
