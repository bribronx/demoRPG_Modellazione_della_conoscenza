package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.controller.PlayerController;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.ShieldItem;
import it.unicam.cs.mpgc.rpg126598.view.EntityViewFactory;
import it.unicam.cs.mpgc.rpg126598.view.ShieldItemView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemService {

    private static final int TILE_SIZE = 16;
    private final List<ShieldItem> shieldItems = new ArrayList<>();
    private final Map<ShieldItem, ShieldItemView> itemViews = new HashMap<>();
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
        ShieldItemView view = EntityViewFactory.createShieldItemView(item);

        shieldItems.add(item);
        itemViews.put(item, view);

        if (parentPane != null) {
            if (view.getShadow() != null && !parentPane.getChildren().contains(view.getShadow())) {
                parentPane.getChildren().add(view.getShadow());
            }
            if (view.getImageView() != null && !parentPane.getChildren().contains(view.getImageView())) {
                parentPane.getChildren().add(view.getImageView());
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

                    ShieldItemView view = itemViews.remove(item);
                    if (view != null) {
                        if (view.getShadow() != null && parentPane != null) {
                            parentPane.getChildren().remove(view.getShadow());
                        }
                        view.playPickupAnimation(() -> {
                            if (parentPane != null && view.getImageView() != null) {
                                parentPane.getChildren().remove(view.getImageView());
                            }
                        });
                    }
                    System.out.println("Item Scudo raccolto! Difesa attuale: " + player.getDefense() + " / " + player.getMaxDefense());
                }
            }
        }
    }

    public void clear() {
        if (parentPane != null) {
            for (ShieldItemView view : itemViews.values()) {
                if (view.getShadow() != null) {
                    parentPane.getChildren().remove(view.getShadow());
                }
                if (view.getImageView() != null) {
                    parentPane.getChildren().remove(view.getImageView());
                }
            }
        }
        itemViews.clear();
        shieldItems.clear();
    }

    public List<ShieldItem> getShieldItems() {
        return shieldItems;
    }
}
