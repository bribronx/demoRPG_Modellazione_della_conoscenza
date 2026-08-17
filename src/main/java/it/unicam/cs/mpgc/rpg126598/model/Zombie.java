package it.unicam.cs.mpgc.rpg126598.model;

import it.unicam.cs.mpgc.rpg126598.strategy.ZombieMovementStrategy;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Zombie extends Enemy {
    public Zombie() {
        super(new ZombieMovementStrategy());
        this.setSpeed(0.2);
        this.setMaxHealth(100);
        this.setBoundBox(4, 4, 12, 12);
        this.setHealth(100);
        this.setDamage(5);
        this.setAggroRange(60);
        this.setImageView(new ImageView(new Image(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/zombie/idle/zombie_idle_01.png"))));
        this.getImageView().setFitHeight(14.0);
        this.getImageView().setFitWidth(14.0);
        this.getImageView().setPreserveRatio(true);
        this.getImageView().setViewport(new Rectangle2D(4.0, 0.0, 24.0, 24.0));
    }
}
