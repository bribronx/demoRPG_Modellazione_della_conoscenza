package it.unicam.cs.mpgc.rpg126598.service;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MapBuilderService {

    private Image grass;
    private Image sand;
    private Image tree;
    private Image wall;
    private Image water;
    private Image earth;
    private Image floor;
    private Image road;
    private int[][] collisionMap;
    private final int TILE_SIZE = 16;

    public int[][] getCollisionMap() {
        return collisionMap;
    }

    public MapBuilderService() {
        grass = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/grass.png")));
        earth = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/earth.png")));
        sand = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/sand.png")));
        tree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/tree.png")));
        wall = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/wall.png")));
        water = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/water.png")));
        floor = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/floor.png")));
        road = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/it/unicam/cs/mpgc/rpg126598/tiles/road.png")));
    }

    public void generateMap(Canvas canvas, String mapName){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        try {
            File file = new File(mapName);
            Scanner scanner = new Scanner(file);

            List<String[]> rows = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    rows.add(line.split(" "));
                }
            }
            scanner.close();

            if (rows.isEmpty()) return;

            int rowCount = rows.size();
            int colCount = rows.getFirst().length;
            collisionMap = new int[rowCount][colCount];

            //ridimensione del canvas per settarlo alla grandezza della mappa
            canvas.setWidth(colCount * TILE_SIZE);
            canvas.setHeight(rowCount * TILE_SIZE);

            //popola l'array per le collisioni e disegna la mappa
            for (int y = 0; y < rowCount; y++) {
                String[] numbers = rows.get(y);
                for (int x = 0; x < numbers.length; x++) {
                    int tileType = Integer.parseInt(numbers[x]);
                    collisionMap[y][x] = tileType;

                    Image tileImage = getTileImage(tileType);

                    gc.drawImage(tileImage, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }

        } catch (Exception e) {
            System.out.println("Errore nel caricamento della mappa: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Image getTileImage(int type){
        switch (type) {
            case 0: return grass;
            case 1: return earth;
            case 2: return sand;
            case 3: return floor;
            case 4: return road;
            case 5: return tree;
            case 6: return wall;
            case 7: return water;
            default: return grass;
        }
    }
}

