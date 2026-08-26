package it.unicam.cs.mpgc.rpg126598.model.save;

import java.util.ArrayList;
import java.util.List;

public class GameSaveData {
    private int saveVersion = 1;
    private String timestamp;
    private String mapPath;
    private PlayerSaveData player;
    private List<EnemySaveData> enemies = new ArrayList<>();

    public GameSaveData() {
    }

    public GameSaveData(int saveVersion, String timestamp, String mapPath, PlayerSaveData player, List<EnemySaveData> enemies) {
        this.saveVersion = saveVersion;
        this.timestamp = timestamp;
        this.mapPath = mapPath;
        this.player = player;
        this.enemies = enemies != null ? enemies : new ArrayList<>();
    }

    public int getSaveVersion() {
        return saveVersion;
    }

    public void setSaveVersion(int saveVersion) {
        this.saveVersion = saveVersion;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public PlayerSaveData getPlayer() {
        return player;
    }

    public void setPlayer(PlayerSaveData player) {
        this.player = player;
    }

    public List<EnemySaveData> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<EnemySaveData> enemies) {
        this.enemies = enemies;
    }
}
