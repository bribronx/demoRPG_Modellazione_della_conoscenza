package it.unicam.cs.mpgc.rpg126598.view;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;
import javafx.util.Duration;

public abstract class ItemView {

    private ImageView imageView;
    private Ellipse shadow;


    public void playPickupAnimation(Runnable onFinished) {
        if (imageView != null) {
            FadeTransition fade = new FadeTransition(Duration.millis(250), imageView);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            TranslateTransition moveUp = new TranslateTransition(Duration.millis(250), imageView);
            moveUp.setByY(-16.0);

            ParallelTransition pt = new ParallelTransition(fade, moveUp);
            pt.setOnFinished(e -> {
                if (onFinished != null) {
                    onFinished.run();
                }
            });
            pt.play();
        } else if (onFinished != null) {
            onFinished.run();
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public Ellipse getShadow() {
        return shadow;
    }

    public void setShadow(Ellipse shadow) {
        this.shadow = shadow;
    }
}
