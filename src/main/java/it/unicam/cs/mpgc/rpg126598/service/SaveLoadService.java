package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.save.GameSaveData;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Interfaccia del servizio per il salvataggio e caricamento dello stato di gioco.
 */
public interface SaveLoadService {

    /**
     * Salva i dati di gioco nel file specificato.
     *
     * @param data Dati di gioco da salvare
     * @param file File di destinazione
     * @throws IOException in caso di errore I/O
     */
    void saveGame(GameSaveData data, File file) throws IOException;


    /**
     * Carica i dati di gioco da un file.
     *
     * @param file File sorgente del salvataggio
     * @return GameSaveData contenente lo stato deserializzato
     * @throws IOException in caso di errore I/O o formato non valido
     */
    GameSaveData loadGame(File file) throws IOException;


    /**
     * Converte un oggetto GameSaveData in formato JSON.
     *
     * @param data Dati di gioco
     * @return Stringa JSON formattata
     */
    String toJson(GameSaveData data);

    /**
     * Deserializza JSON in un oggetto GameSaveData.
     *
     * @param json Stringa JSON
     * @return Oggetto GameSaveData
     */
    GameSaveData fromJson(String json);

    /**
     * Costruisce un'istanza di GameSaveData a partire dallo stato corrente del giocatore, nemici e mappa.
     *
     * @param player Giocatore corrente
     * @param enemies Lista dei nemici attualmente vivi
     * @param mapPath Percorso della mappa di gioco
     * @return GameSaveData pronto per essere serializzato
     */
    GameSaveData createSaveData(Player player, List<Enemy> enemies, String mapPath);
}
