package it.unicam.cs.mpgc.rpg126598.view;

import it.unicam.cs.mpgc.rpg126598.model.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

import java.util.Objects;


public class EntityViewFactory {

    private static final String SKELETON_IDLE_SPRITE = "/it/unicam/cs/mpgc/rpg126598/skeleton/idle/skeleton_idle_01.png";
    private static final String SLIME_IDLE_SPRITE = "/it/unicam/cs/mpgc/rpg126598/slime/idle/slime_idle_01.png";
    private static final String ZOMBIE_IDLE_SPRITE = "/it/unicam/cs/mpgc/rpg126598/zombie/idle/zombie_idle_01.png";
    private static final String SHIELD_SPRITE = "/it/unicam/cs/mpgc/rpg126598/player/shield.png";
    private static final String SWORD_SPRITE = "/it/unicam/cs/mpgc/rpg126598/player/sword.png";

    public static EntityView createPlayerView(ImageView playerImageView) {
        Ellipse shadow = createEntityShadow(playerImageView);
        return new EntityView(playerImageView, shadow);
    }

    public static EntityView createEnemyView(Enemy enemy) {
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);

        if (enemy instanceof Skeleton) {
            Image image = new Image(Objects.requireNonNull(EntityViewFactory.class.getResourceAsStream(SKELETON_IDLE_SPRITE)));
            imageView.setImage(image);
            imageView.setFitWidth(10.0);
            imageView.setFitHeight(10.0);
        } else if (enemy instanceof Slime) {
            Image image = new Image(Objects.requireNonNull(EntityViewFactory.class.getResourceAsStream(SLIME_IDLE_SPRITE)));
            imageView.setImage(image);
            imageView.setFitWidth(12.0);
            imageView.setFitHeight(12.0);
        } else if (enemy instanceof Zombie) {
            Image image = new Image(Objects.requireNonNull(EntityViewFactory.class.getResourceAsStream(ZOMBIE_IDLE_SPRITE)));
            imageView.setImage(image);
            imageView.setFitWidth(14.0);
            imageView.setFitHeight(14.0);
            imageView.setViewport(new Rectangle2D(4.0, 0.0, 24.0, 24.0));
        }

        imageView.setLayoutX(enemy.getX());
        imageView.setLayoutY(enemy.getY());

        return createPlayerView(imageView);
    }

    public static ShieldItemView createShieldItemView(ShieldItem item) {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(
                EntityViewFactory.class.getResourceAsStream(SHIELD_SPRITE))));
        imageView.setFitWidth(item.getWidth());
        imageView.setFitHeight(item.getHeight());
        imageView.setLayoutX(item.getX());
        imageView.setLayoutY(item.getY());

        Ellipse shadow = createEntityShadow(imageView);

        return new ShieldItemView(imageView, shadow);
    }

    public static SwordItemView createSwordItemView(SwordItem item) {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(
                EntityViewFactory.class.getResourceAsStream(SWORD_SPRITE))));
        imageView.setFitWidth(item.getWidth());
        imageView.setFitHeight(item.getHeight());
        imageView.setLayoutX(item.getX());
        imageView.setLayoutY(item.getY());

        Ellipse shadow = createEntityShadow(imageView);

        return new SwordItemView(imageView, shadow);
    }

    public static BoneProjectileView createBoneProjectileView(BoneProjectile projectile, Image[] frames) {
        return new BoneProjectileView(projectile, frames);
    }

    private static Ellipse createEntityShadow(ImageView imageView) {
        if (imageView == null) return null;

        Ellipse shadow = new Ellipse();
        shadow.radiusXProperty().bind(imageView.fitWidthProperty().divide(2.5));
        shadow.radiusYProperty().bind(imageView.fitWidthProperty().divide(6.0));
        shadow.setFill(Color.color(0, 0, 0, 0.1));

        shadow.layoutXProperty().bind(imageView.layoutXProperty().add(imageView.fitWidthProperty().divide(2)));
        shadow.layoutYProperty().bind(imageView.layoutYProperty().add(imageView.fitHeightProperty().subtract(1)));

        shadow.translateXProperty().bind(imageView.translateXProperty());
        shadow.translateYProperty().bind(imageView.translateYProperty());

        return shadow;
    }
}
