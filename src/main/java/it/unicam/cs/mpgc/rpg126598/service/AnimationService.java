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
    private Timeline attackTimeline;
    private Timeline deathTimeline;

    public void walkingAnimation(ImageView entity, Image[] targetFrames, double frameDuration) {
        if (activeFrames == targetFrames && walkingTimeline != null
                && walkingTimeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }
        if (attackTimeline != null) {
            attackTimeline.stop();
        }
        if (deathTimeline != null) {
            deathTimeline.stop();
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
        if (attackTimeline != null) {
            attackTimeline.stop();
        }
        if (deathTimeline != null) {
            deathTimeline.stop();
        }

        activeFrames = targetFrames;
        currentFrame = 0;

        Image previousImage = entity.getImage();

        attackTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration), e -> {
            if (currentFrame < activeFrames.length) {
                entity.setImage(activeFrames[currentFrame]);
                currentFrame++;
            }
        }));

        attackTimeline.setCycleCount(targetFrames.length);
        attackTimeline.setOnFinished(e -> {
            entity.setImage(previousImage);
        });
        attackTimeline.play();
    }

    public void deathAnimation(ImageView entity, Image[] targetFrames, double frameDuration) {
        deathAnimation(entity, targetFrames, frameDuration, null);
    }

    public void deathAnimation(ImageView entity, Image[] targetFrames, double frameDuration, Runnable onFinished) {
        if (targetFrames == null || targetFrames.length == 0) {
            if (onFinished != null) {
                onFinished.run();
            }
            return;
        }

        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }
        if (attackTimeline != null) {
            attackTimeline.stop();
        }
        if (deathTimeline != null) {
            deathTimeline.stop();
        }

        activeFrames = targetFrames;
        currentFrame = 0;

        deathTimeline = new Timeline(new KeyFrame(Duration.millis(frameDuration), e -> {
            if (currentFrame < activeFrames.length) {
                entity.setImage(activeFrames[currentFrame]);
                currentFrame++;
            }
        }));

        deathTimeline.setCycleCount(targetFrames.length);
        deathTimeline.setOnFinished(e -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        deathTimeline.play();
    }

    public boolean isAttacking() {
        return attackTimeline != null && attackTimeline.getStatus() == Animation.Status.RUNNING;
    }

    public void stopWalking() {
        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }
    }

    public void stopAll() {
        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }
        if (attackTimeline != null) {
            attackTimeline.stop();
        }
        if (deathTimeline != null) {
            deathTimeline.stop();
        }
    }
}
