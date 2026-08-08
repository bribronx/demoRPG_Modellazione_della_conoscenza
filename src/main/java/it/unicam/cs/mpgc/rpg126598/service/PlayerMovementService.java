package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.Objects;

public class PlayerMovementService {

    private final Player player;
    Image[] rightFrames = new Image[] {
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/06_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/07_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/08_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/09_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/10_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/11_player.png")))
    };

    Image [] leftFrames = new Image[] {
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left1_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left2_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left3_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left4_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left5_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/left6_player.png")))
    };

    Image [] upFrames = new Image[] {
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/12_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/13_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/14_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/15_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/16_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/17_player.png")))
    };

    Image [] downFrames = new Image[] {
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/00_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/01_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/02_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/03_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/04_player.png"))),
            new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/player/05_player.png")))
    };


    private Image [] activeFrames;
    private int currentFrame = 0;
    private Timeline walkingTimeline;

    public PlayerMovementService(Player player) {
        this.player = player;
    }

    public void makeMove(KeyEvent e){
        ImageView imageP= player.getPlayer();
        switch (e.getCode()) {
            case W :
                walkingAnimation(imageP, upFrames);
                imageP.setTranslateY(imageP.getTranslateY()-player.getSpeed());
                break;
            case S :
                walkingAnimation(imageP, downFrames);
                imageP.setTranslateY(imageP.getTranslateY()+player.getSpeed());
                break;
            case A :
                walkingAnimation(imageP, leftFrames);
                imageP.setTranslateX(imageP.getTranslateX()-player.getSpeed());
                break;
            case D :
                walkingAnimation(imageP, rightFrames);
                imageP.setTranslateX(imageP.getTranslateX()+player.getSpeed());
                break;
        }
    }

    public void walkingAnimation(ImageView player, Image[] targetFrames) {
        if (activeFrames == targetFrames && walkingTimeline != null && walkingTimeline.getStatus() == Animation.Status.RUNNING) {
            return;
        }

        if (walkingTimeline != null) {
            walkingTimeline.stop();
        }

        activeFrames = targetFrames;
        currentFrame = 0;

        walkingTimeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            player.setImage(activeFrames[currentFrame]);
            currentFrame = (currentFrame + 1) % activeFrames.length;
        }));

        walkingTimeline.setCycleCount(Animation.INDEFINITE);
        walkingTimeline.play();
    }
}
