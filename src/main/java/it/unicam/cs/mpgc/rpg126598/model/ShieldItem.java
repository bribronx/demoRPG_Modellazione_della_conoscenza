package it.unicam.cs.mpgc.rpg126598.model;

public class ShieldItem extends Item{

    private final double shieldAmount;

    public ShieldItem(double x, double y, double shieldAmount) {
        this(x, y, 10.0, 10.0, shieldAmount);
    }

    public ShieldItem(double x, double y, double width, double height, double shieldAmount) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setCollected(false);
        this.shieldAmount = shieldAmount;
    }

    @Override
    public void collect(Player player) {
        if (this.isCollected() || player == null) return;
        this.setCollected(true);
        double newDefense = Math.min(player.getMaxDefense(), player.getDefense() + shieldAmount);
        player.setDefense(newDefense);
    }

    public double getShieldAmount() {
        return shieldAmount;
    }
}
