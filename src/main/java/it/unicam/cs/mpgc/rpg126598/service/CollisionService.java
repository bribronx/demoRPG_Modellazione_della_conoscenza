package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.model.Entity;
import javafx.geometry.Bounds;
import javafx.geometry.BoundingBox;

import java.util.List;

public class CollisionService {

    private static final int TILE_SIZE = 16;

    /**
     * Verifica la collisione con i tile della mappa per un'entità nella sua
     * posizione futura (deltaX, deltaY).
     */
    public boolean checkTileCollision(Entity entity, double deltaX, double deltaY, int[][] collisionMap) {
        if (entity == null || collisionMap == null)
            return false;

        Bounds futureHitbox = entity.getHitboxAt(deltaX, deltaY);
        double hitboxX = futureHitbox.getMinX();
        double hitboxY = futureHitbox.getMinY();
        double hitboxW = futureHitbox.getWidth();
        double hitboxH = futureHitbox.getHeight();

        int minCol = (int) (hitboxX / TILE_SIZE);
        int maxCol = (int) ((hitboxX + hitboxW) / TILE_SIZE);
        int minRow = (int) (hitboxY / TILE_SIZE);
        int maxRow = (int) ((hitboxY + hitboxH) / TILE_SIZE);

        // Controllo fuori mappa
        if (minCol < 0 || minRow < 0 || maxRow >= collisionMap.length || maxCol >= collisionMap[0].length) {
            return true;
        }

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                if (collisionMap[row][col] >= 3) {
                    Bounds tileBounds = new BoundingBox(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                    if (futureHitbox.intersects(tileBounds)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkEntityCollision(Entity entity, double deltaX, double deltaY,
            Entity otherEntity) {
        if (entity == null || otherEntity == null)
            return false;

        Bounds futureHitbox = entity.getHitboxAt(deltaX, deltaY);

        if (otherEntity != entity) {
            if (futureHitbox.intersects(otherEntity.getHitbox())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkEntityCollision(Entity entity, double deltaX, double deltaY,
            List<? extends Entity> otherEntities) {
        if (entity == null || otherEntities == null)
            return false;

        Bounds futureHitbox = entity.getHitboxAt(deltaX, deltaY);

        for (Entity other : otherEntities) {
            if (other != null && other != entity) {
                if (futureHitbox.intersects(other.getHitbox())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkCollision(Entity entity, double deltaX, double deltaY, int[][] collisionMap,
            Entity otherEntity) {
        boolean tileCol = checkTileCollision(entity, deltaX, deltaY, collisionMap);
        boolean entityCol = checkEntityCollision(entity, deltaX, deltaY, otherEntity);
        return tileCol || entityCol;
    }

    public boolean checkCollision(Entity entity, double deltaX, double deltaY, int[][] collisionMap,
            List<? extends Entity> otherEntities) {
        boolean tileCol = checkTileCollision(entity, deltaX, deltaY, collisionMap);
        boolean entityCol = checkEntityCollision(entity, deltaX, deltaY, otherEntities);
        return tileCol || entityCol;
    }

}
