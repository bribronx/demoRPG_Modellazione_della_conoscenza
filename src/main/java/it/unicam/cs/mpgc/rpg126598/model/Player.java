package it.unicam.cs.mpgc.rpg126598.model;

public class Player extends Entity {

    public Player() {
        this.setSpeed(0.4);
        this.setWidth(16.0);
        this.setHeight(16.0);
        this.setBoundBox(5, 8, 2, 5);
        this.setHealth(100);
        this.setMaxHealth(100);
        this.setDefense(50);
        this.setMaxDefense(50);
        this.setDamage(20);
        this.setLevel(1);
        this.setXp(0);
    }
}
