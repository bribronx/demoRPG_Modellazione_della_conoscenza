package it.unicam.cs.mpgc.rpg126598.model;

public class BoneProjectile {
    private double x;
    private double y;
    private final double vx;
    private final double vy;
    private final double damage;
    private final double maxRange = 150.0;
    private double distanceTraveled = 0;

    public BoneProjectile(double startX, double startY, double vx, double vy, double damage) {
        this.x = startX;
        this.y = startY;
        this.vx = vx;
        this.vy = vy;
        this.damage = damage;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public double getDamage() {
        return damage;
    }

    public double getDistanceTraveled() {
        return distanceTraveled;
    }

    public double getMaxRange() {
        return maxRange;
    }

    public Hitbox getHitbox() {
        return new Hitbox(x, y, 8, 8);
    }

    public void update() {
        x += vx;
        y += vy;
        distanceTraveled += Math.hypot(vx, vy);
    }

    public boolean isExpired() {
        return distanceTraveled >= maxRange;
    }
}
