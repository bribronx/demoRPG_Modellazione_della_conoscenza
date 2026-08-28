package it.unicam.cs.mpgc.rpg126598.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

import java.util.Objects;

public class ShieldItem {

    private final ImageView imageView;
    private final Ellipse shadow;
    private final double shieldAmount;
    private boolean collected = false;
    private final double x;
    private final double y;

    public ShieldItem(double x, double y, double shieldAmount) {
        this.x = x;
        this.y = y;
        this.shieldAmount = shieldAmount;

        this.imageView = new ImageView(new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/shield.png"))));
        this.imageView.setFitWidth(10);
        this.imageView.setFitHeight(10);
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);

        this.shadow = new Ellipse();
        this.shadow.setRadiusX(4.0);
        this.shadow.setRadiusY(2.0);
        this.shadow.setFill(Color.color(0, 0, 0, 0.25));
        this.shadow.setLayoutX(x + 5.0);
        this.shadow.setLayoutY(y + 9.5);
    }


    public Bounds getHitbox() {
        return new BoundingBox(x, y, 10, 10);
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

    public ImageView getImageView() {
        return imageView;
    }

    public Ellipse getShadow() {
        return shadow;
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

    public double getShieldAmount() {
        return shieldAmount;
    }
}
