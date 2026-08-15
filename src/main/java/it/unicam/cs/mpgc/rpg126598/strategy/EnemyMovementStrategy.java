package it.unicam.cs.mpgc.rpg126598.strategy;

import it.unicam.cs.mpgc.rpg126598.model.Enemy;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.service.CollisionService;
import it.unicam.cs.mpgc.rpg126598.service.MapBuilderService;

import java.util.List;

public interface EnemyMovementStrategy {

    /**
     * Logica del movimento dei nemici
     *
     * @param enemy             entità nemico
     * @param target            entità player
     * @param enemies           lista di tutti i nemici
     * @param mapBuilderService mappa del gioco per conoscere le tile di collisione
     * @param collisionService  servizio di collisione
     * @param deltaTime         timestamp del frame corrente in nanosecondi
     */
    void move(Enemy enemy, Player target, List<Enemy> enemies, MapBuilderService mapBuilderService, CollisionService collisionService, double deltaTime);
}
