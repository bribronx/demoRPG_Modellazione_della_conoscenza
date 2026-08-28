package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.controller.PlayerController;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.ShieldItem;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemService {

    private static final int TILE_SIZE = 16;
    private final List<ShieldItem> shieldItems = new ArrayList<>();
    private Pane parentPane;

    public ItemService() {
    }

    public ItemService(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public void setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public ShieldItem spawnShieldItemAtTile(int tileX, int tileY, double amount) {
        double pixelX = tileX * TILE_SIZE;
        double pixelY = tileY * TILE_SIZE;

        ShieldItem item = new ShieldItem(pixelX, pixelY, amount);
        shieldItems.add(item);
        if (parentPane != null) {
            if (item.getShadow() != null && !parentPane.getChildren().contains(item.getShadow())) {
                parentPane.getChildren().add(item.getShadow());
            }
            if (item.getImageView() != null && !parentPane.getChildren().contains(item.getImageView())) {
                parentPane.getChildren().add(item.getImageView());
            }
        }
        return item;
    }

    public ShieldItem spawnShieldItemAtTile(int tileX, int tileY) {
        return spawnShieldItemAtTile(tileX, tileY, 20.0);
    }

    public void update(Player player, PlayerController playerController) {
        if (player == null || player.isDead()) return;

        Iterator<ShieldItem> iterator = shieldItems.iterator();
        while (iterator.hasNext()) {
            ShieldItem item = iterator.next();
            if (item != null && !item.isCollected()) {
                if (item.checkCollision(player)) {
                    item.collect(player);
                    iterator.remove();

                    if (playerController != null) {
                        playerController.updateDefenseBar();
                    }

                    playPickupAnimation(item);
                    System.out.println("Item Scudo raccolto! Difesa attuale: " + player.getDefense() + " / " + player.getMaxDefense());
                }
            }
        }
    }

    private void playPickupAnimation(ShieldItem item) {
        if (item.getShadow() != null && parentPane != null) {
            parentPane.getChildren().remove(item.getShadow());
        }

        if (item.getImageView() != null && parentPane != null) {
            FadeTransition fade = new FadeTransition(Duration.millis(250), item.getImageView());
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            TranslateTransition moveUp = new TranslateTransition(Duration.millis(250), item.getImageView());
            moveUp.setByY(-16.0);

            ParallelTransition pt = new ParallelTransition(fade, moveUp);
            pt.setOnFinished(e -> {
                if (parentPane != null) {
                    parentPane.getChildren().remove(item.getImageView());
                }
            });
            pt.play();
        }
    }

    public void clear() {
        if (parentPane != null) {
            for (ShieldItem item : shieldItems) {
                if (item.getShadow() != null) {
                    parentPane.getChildren().remove(item.getShadow());
                }
                if (item.getImageView() != null) {
                    parentPane.getChildren().remove(item.getImageView());
                }
            }
        }
        shieldItems.clear();
    }

    public List<ShieldItem> getShieldItems() {
        return shieldItems;
    }
}
