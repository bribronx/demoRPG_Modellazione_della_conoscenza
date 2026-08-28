package it.unicam.cs.mpgc.rpg126598.view;

import it.unicam.cs.mpgc.rpg126598.model.BoneProjectile;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class BoneProjectileView {

    private final ImageView imageView;
    private final Image[] frames;
    private int currentFrame = 0;
    private long lastFrameTime = 0;

    public BoneProjectileView(BoneProjectile projectile, Image[] frames) {
        this.frames = frames;
        this.imageView = new ImageView();
        if (frames != null && frames.length > 0) {
            this.imageView.setImage(frames[0]);
        }
        this.imageView.setFitWidth(10.0);
        this.imageView.setFitHeight(10.0);
        this.imageView.setPreserveRatio(true);
        this.imageView.setLayoutX(projectile.getX());
        this.imageView.setLayoutY(projectile.getY());
    }

    public ImageView getImageView() {
        return imageView;
    }


    public void update(BoneProjectile projectile) {
        if (imageView != null && projectile != null) {
            imageView.setLayoutX(projectile.getX());
            imageView.setLayoutY(projectile.getY());

            if (frames != null && frames.length > 0) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastFrameTime >= 100 || lastFrameTime == 0) {
                    currentFrame = (currentFrame + 1) % frames.length;
                    imageView.setImage(frames[currentFrame]);
                    lastFrameTime = currentTime;
                }
            }
        }
    }
}
