package it.unicam.cs.mpgc.rpg126598.model;

import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import it.unicam.cs.mpgc.rpg126598.service.AnimationService;

public abstract class Entity implements Targetable {
    private double speed;
    private double health;
    private double maxHealth;
    private double defense;
    private double maxDefense;
    private double damage;
    private double level;
    private double xp;
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

    public double getXp() {
        return xp;
    }

    public void setXp(double xp) {
        this.xp = xp;
    }

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        this.defense = defense;
    }

    public double getMaxDefense() {
        return maxDefense;
    }

    public void setMaxDefense(double maxDefense) {
        this.maxDefense = maxDefense;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
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

    private transient Effect originalEffect;
    private transient PauseTransition hitEffectTimer;

    @Override
    public void takeDamage(double amount) {
        if (this.defense > 0) {
            if (amount <= this.defense) {
                this.defense -= amount;
                amount = 0;
            } else {
                amount -= this.defense;
                this.defense = 0;
            }
        }
        if (amount > 0) {
            this.setHealth(this.getHealth() - amount);
        }
        playHitEffect();
    }

    public void playHitEffect() {
        if (imageView == null || imageView.getImage() == null) {
            return;
        }

        if (hitEffectTimer != null) {
            hitEffectTimer.stop();
        } else {
            originalEffect = imageView.getEffect();
        }

        double width = imageView.getBoundsInLocal().getWidth();
        double height = imageView.getBoundsInLocal().getHeight();
        if (width <= 0) width = imageView.getImage().getWidth();
        if (height <= 0) height = imageView.getImage().getHeight();

        ColorInput redOverlay = new ColorInput(0, 0, width, height, Color.color(1.0, 0.15, 0.15, 0.75));
        Blend redBlend = new Blend(BlendMode.SRC_ATOP, null, redOverlay);

        imageView.setEffect(redBlend);

        hitEffectTimer = new PauseTransition(Duration.millis(180));
        hitEffectTimer.setOnFinished(e -> {
            imageView.setEffect(originalEffect);
            hitEffectTimer = null;
            originalEffect = null;
        });
        hitEffectTimer.play();
    }

    @Override
    public boolean isDead() {
        return this.getHealth() <= 0;
    }
}
