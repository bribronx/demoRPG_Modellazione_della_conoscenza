package it.unicam.cs.mpgc.rpg126598.model;

import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;

public abstract class Entity implements Targetable {
    private double speed;
    private int health;
    private int maxHealth;
    private int defense;
    private int maxHp;
    private int damage;
    private int level;
    private int xp;
    private ImageView imageView;
    private Rectangle boundBox;
    private Ellipse shadow;
    private EntityState state = EntityState.IDLE;
    private Direction direction = Direction.DOWN;
    private AnimationService animationService;
    private long lastAttackTime;

    public long getLastAttackTime() {
        return lastAttackTime;
    }

    public void setLastAttackTime(long lastAttackTime) {
        this.lastAttackTime = lastAttackTime;
    }

    public AnimationService getAnimationService() {
        if (animationService == null) {
            animationService = new AnimationService();
        }
        return animationService;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Rectangle getBoundBox() {
        return boundBox;
    }

    public void setBoundBox(double x, double y, double width, double height) {
        this.boundBox = new Rectangle(x, y, width, height);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Ellipse getShadow() {
        if (shadow == null && imageView != null) {
            shadow = new Ellipse();
            shadow.radiusXProperty().bind(imageView.fitWidthProperty().divide(2.5));
            shadow.radiusYProperty().bind(imageView.fitWidthProperty().divide(6.0));
            shadow.setFill(Color.color(0, 0, 0, 0.1));

            shadow.layoutXProperty().bind(imageView.layoutXProperty().add(imageView.fitWidthProperty().divide(2)));
            shadow.layoutYProperty().bind(imageView.layoutYProperty().add(imageView.fitHeightProperty().subtract(1)));

            shadow.translateXProperty().bind(imageView.translateXProperty());
            shadow.translateYProperty().bind(imageView.translateYProperty());
        }
        return shadow;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getGlobalX() {
        if (imageView == null)
            return 0;
        return imageView.getLayoutX() + imageView.getTranslateX();
    }

    public double getGlobalY() {
        if (imageView == null)
            return 0;
        return imageView.getLayoutY() + imageView.getTranslateY();
    }

    public Bounds getHitboxAt(double deltaX, double deltaY) {
        if (boundBox == null || imageView == null) {
            return new BoundingBox(0, 0, 0, 0);
        }
        double hitboxX = getGlobalX() + boundBox.getX() + deltaX;
        double hitboxY = getGlobalY() + boundBox.getY() + deltaY;
        return new BoundingBox(hitboxX, hitboxY, boundBox.getWidth(), boundBox.getHeight());
    }

    public Bounds getHitbox() {
        return getHitboxAt(0, 0);
    }

    public void moveX(double deltaX) {
        if (imageView != null) {
            imageView.setTranslateX(imageView.getTranslateX() + deltaX);
        }
    }

    public void moveY(double deltaY) {
        if (imageView != null) {
            imageView.setTranslateY(imageView.getTranslateY() + deltaY);
        }
    }

    public void move(double deltaX, double deltaY) {
        moveX(deltaX);
        moveY(deltaY);
    }

    public EntityState getState() {
        return state;
    }

    public void setState(EntityState state) {
        this.state = state;
    }

    @Override
    public void takeDamage(int amount) {
        this.setHealth(this.getHealth() - amount);
    }

    @Override
    public boolean isDead() {
        return this.getHealth() <= 0;
    }

}
