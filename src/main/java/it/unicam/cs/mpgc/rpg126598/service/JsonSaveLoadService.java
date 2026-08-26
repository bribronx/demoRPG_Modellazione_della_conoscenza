package it.unicam.cs.mpgc.rpg126598.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.Skeleton;
import it.unicam.cs.mpgc.rpg126598.model.Slime;
import it.unicam.cs.mpgc.rpg126598.model.Zombie;
import it.unicam.cs.mpgc.rpg126598.model.enums.EnemyType;
import it.unicam.cs.mpgc.rpg126598.model.save.EnemySaveData;
import it.unicam.cs.mpgc.rpg126598.model.save.GameSaveData;
import it.unicam.cs.mpgc.rpg126598.model.save.PlayerSaveData;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione di {@link SaveLoadService} che serializza e deserializza lo stato del gioco
 * in formato JSON utilizzando la libreria Gson.
 */
public class JsonSaveLoadService implements SaveLoadService {

    private final Gson gson;

    public JsonSaveLoadService() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public JsonSaveLoadService(Gson gson) {
        this.gson = gson != null ? gson : new GsonBuilder().setPrettyPrinting().create();
    }

    @Override
    public void saveGame(GameSaveData data, File file) throws IOException {
        if (data == null) {
            throw new IllegalArgumentException("I dati di gioco da salvare non possono essere null.");
        }
        if (file == null) {
            throw new IllegalArgumentException("Il file di destinazione non può essere null.");
        }
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }
        String json = toJson(data);
        Files.writeString(file.toPath(), json, StandardCharsets.UTF_8);
    }


    @Override
    public GameSaveData loadGame(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Il file da caricare non può essere null.");
        }
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File di salvataggio non trovato o non valido: " + file.getAbsolutePath());
        }
        String json = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        return fromJson(json);
    }

    @Override
    public String toJson(GameSaveData data) {
        if (data == null) {
            throw new IllegalArgumentException("I dati di gioco non possono essere null.");
        }
        return gson.toJson(data);
    }

    @Override
    public GameSaveData fromJson(String json) {
        if (json == null || json.trim().isEmpty()) {
            throw new IllegalArgumentException("La stringa JSON non può essere vuota o null.");
        }
        try {
            GameSaveData data = gson.fromJson(json, GameSaveData.class);
            if (data == null) {
                throw new IllegalArgumentException("Impossibile parsare i dati di salvataggio da JSON.");
            }
            return data;
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Sintassi JSON non valida per i dati di salvataggio: " + e.getMessage(), e);
        }
    }

    @Override
    public GameSaveData createSaveData(Player player, List<Enemy> enemies, String mapPath) {
        if (player == null) {
            throw new IllegalArgumentException("Il player non può essere null.");
        }

        PlayerSaveData playerSave = new PlayerSaveData(
                player.getGlobalX(),
                player.getGlobalY(),
                player.getHealth(),
                player.getMaxHealth(),
                player.getDefense(),
                player.getMaxDefense(),
                player.getDamage(),
                player.getSpeed(),
                player.getLevel(),
                player.getXp(),
                player.getDirection(),
                player.getState()
        );

        List<EnemySaveData> enemiesSave = new ArrayList<>();
        if (enemies != null) {
            for (Enemy enemy : enemies) {
                if (enemy != null && !enemy.isDead()) {
                    EnemyType type = getEnemyType(enemy);
                    enemiesSave.add(new EnemySaveData(
                            type,
                            enemy.getGlobalX(),
                            enemy.getGlobalY(),
                            enemy.getHealth(),
                            enemy.getMaxHealth(),
                            enemy.getDefense(),
                            enemy.getMaxDefense(),
                            enemy.getDamage(),
                            enemy.getSpeed(),
                            enemy.getDirection(),
                            enemy.getState()
                    ));
                }
            }
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return new GameSaveData(1, timestamp, mapPath, playerSave, enemiesSave);
    }


    public EnemyType getEnemyType(Enemy enemy) {
        if (enemy == null) return null;
        if (enemy.getEnemyType() != null) {
            return enemy.getEnemyType();
        }
        return switch (enemy) {
            case Slime slime -> EnemyType.SLIME;
            case Zombie zombie -> EnemyType.ZOMBIE;
            case Skeleton skeleton -> EnemyType.SKELETON;
            default -> null;
        };
    }

}
