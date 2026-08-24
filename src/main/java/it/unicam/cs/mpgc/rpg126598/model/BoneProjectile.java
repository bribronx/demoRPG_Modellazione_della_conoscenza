package it.unicam.cs.mpgc.rpg126598.model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BoneProjectile {
    private double x;
    private double y;
    private final double vx;
    private final double vy;
    private final double damage;
    private final double maxRange = 150.0;
    private double distanceTraveled = 0;
    private final ImageView imageView;
    private final Image[] frames;
    private int currentFrame = 0;
    private long lastFrameTime = 0;

    public BoneProjectile(double startX, double startY, double vx, double vy, double damage2, Image[] frames) {
        this.x = startX;
        this.y = startY;
        this.vx = vx;
        this.vy = vy;
        this.damage = damage2;
        this.frames = frames;

        this.imageView = new ImageView();
        if (frames != null && frames.length > 0) {
            this.imageView.setImage(frames[0]);
        }
        this.imageView.setFitWidth(10.0);
        this.imageView.setFitHeight(10.0);
        this.imageView.setPreserveRatio(true);
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
    public double getDamage() { return damage; }
    public ImageView getImageView() { return imageView; }

    public void update() {
        x += vx;
        y += vy;
        distanceTraveled += Math.hypot(vx, vy);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);

        // Rotazione dell'osso ogni 100ms
        if (frames != null && frames.length > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastFrameTime >= 100 || lastFrameTime == 0) {
                currentFrame = (currentFrame + 1) % frames.length;
                imageView.setImage(frames[currentFrame]);
                lastFrameTime = currentTime;
            }
        }
    }

    public boolean isExpired() {
        return distanceTraveled >= maxRange;
    }
}
