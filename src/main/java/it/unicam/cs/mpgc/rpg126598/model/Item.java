package it.unicam.cs.mpgc.rpg126598.model;

public abstract class Item {
    private double x;
    private double y;
    private double width;
    private double height;
    private boolean collected;

    public void collect(Player player) {
        if (collected || player == null) return;
        this.collected = true;
    }

    public boolean checkCollision(Player player) {
        if (collected || player == null || player.isDead()) return false;
        return player.getHitbox().intersects(getHitbox());
    }

    public Hitbox getHitbox() {
        return new Hitbox(x, y, width, height);
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
