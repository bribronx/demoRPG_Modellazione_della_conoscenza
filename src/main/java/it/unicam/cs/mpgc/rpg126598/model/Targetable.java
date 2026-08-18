package it.unicam.cs.mpgc.rpg126598.model;

public interface Targetable {
    void takeDamage(int amount);
    boolean isDead();
    int getDefense();

}
