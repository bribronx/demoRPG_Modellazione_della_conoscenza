package it.unicam.cs.mpgc.rpg126598.model;

public class SwordItem extends Item{

    public SwordItem(double x, double y) {
        this(x, y, 10, 10);
    }

    public SwordItem(double x, double y, double width, double height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setCollected(false);
    }
}
