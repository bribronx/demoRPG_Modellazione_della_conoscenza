package it.unicam.cs.mpgc.rpg126598.service;

import javafx.scene.image.Image;

import java.util.Objects;

public class LoadFramesService {

    public Image[] loadFrames(String folder1, String folder2 ,String prefix, int count) {
        Image[] frames = new Image[count];
        for (int i = 0; i < count; i++) {
            String path = String.format("/it/unicam/cs/mpgc/rpg126598/%s/%s/%s_%02d.png", folder1, folder2, prefix, i + 1);
            frames[i] = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        }
        return frames;
    }

}
