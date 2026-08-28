package it.unicam.cs.mpgc.rpg126598.model;

public class ShieldItem {

    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final double shieldAmount;
    private boolean collected = false;

    public ShieldItem(double x, double y, double shieldAmount) {
        this(x, y, 10.0, 10.0, shieldAmount);
    }

    public ShieldItem(double x, double y, double width, double height, double shieldAmount) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shieldAmount = shieldAmount;
    }

    public Hitbox getHitbox() {
        return new Hitbox(x, y, width, height);
    }

    public boolean checkCollision(Player player) {
        if (collected || player == null || player.isDead()) return false;
        return player.getHitbox().intersects(getHitbox());
    }

    public void collect(Player player) {
        if (collected || player == null) return;
        this.collected = true;
        double newDefense = Math.min(player.getMaxDefense(), player.getDefense() + shieldAmount);
        player.setDefense(newDefense);
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getShieldAmount() {
        return shieldAmount;
    }
}
