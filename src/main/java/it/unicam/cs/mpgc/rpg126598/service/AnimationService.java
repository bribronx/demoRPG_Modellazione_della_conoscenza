package it.unicam.cs.mpgc.rpg126598.service;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class AnimationService {

    private Image[] activeFrames;
    private int currentFrame = 0;
    private Timeline walkingTimeline;

    public void walkingAnimation(ImageView entity, Image[] targetFrames, double frameDuration) {
        if (activeFrames == targetFrames && walkingTimeline != null
                && walkingTimeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }

        activeFrames = targetFrames;
        currentFrame = 0;

        walkingTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration), e -> {
            entity.setImage(activeFrames[currentFrame]);
            currentFrame = (currentFrame + 1) % activeFrames.length;
        }));

        walkingTimeline.setCycleCount(Animation.INDEFINITE);
        walkingTimeline.play();
    }

    public void attackAnimation(ImageView entity, Image[] targetFrames, double frameDuration) {
        if (targetFrames == null || targetFrames.length == 0) {
            return;
        }

        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }

        activeFrames = targetFrames;
        currentFrame = 0;

        Image previousImage = entity.getImage();

        walkingTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration), e -> {
            if (currentFrame < activeFrames.length) {
                entity.setImage(activeFrames[currentFrame]);
                currentFrame++;
            }
        }));

        walkingTimeline.setCycleCount(targetFrames.length);
        walkingTimeline.setOnFinished(e -> {
            entity.setImage(previousImage);
        });
        walkingTimeline.play();
    }
}
