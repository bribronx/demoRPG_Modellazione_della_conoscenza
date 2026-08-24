package it.unicam.cs.mpgc.rpg126598.model;

public interface Targetable {
    void takeDamage(double amount);
    boolean isDead();
    double getDefense();

}
