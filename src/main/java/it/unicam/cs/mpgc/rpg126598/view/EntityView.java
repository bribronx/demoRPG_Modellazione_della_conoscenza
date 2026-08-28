package it.unicam.cs.mpgc.rpg126598.view;

import javafx.animation.PauseTransition;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.effect.Effect;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;


public class EntityView {

    private final ImageView imageView;
    private final Ellipse shadow;

    private Effect originalEffect;
    private PauseTransition hitEffectTimer;

    public EntityView(ImageView imageView, Ellipse shadow) {
        this.imageView = imageView;
        this.shadow = shadow;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Ellipse getShadow() {
        return shadow;
    }


    public void updatePosition(double x, double y) {
        if (imageView != null) {
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        }
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
}
