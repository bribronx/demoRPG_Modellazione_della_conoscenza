package it.unicam.cs.mpgc.rpg126598.service;

import it.unicam.cs.mpgc.rpg126598.controller.PlayerController;
import it.unicam.cs.mpgc.rpg126598.model.Item;
import it.unicam.cs.mpgc.rpg126598.model.Player;
import it.unicam.cs.mpgc.rpg126598.model.ShieldItem;
import it.unicam.cs.mpgc.rpg126598.model.SwordItem;
import it.unicam.cs.mpgc.rpg126598.view.EntityViewFactory;
import it.unicam.cs.mpgc.rpg126598.view.ItemView;
import it.unicam.cs.mpgc.rpg126598.view.ShieldItemView;
import it.unicam.cs.mpgc.rpg126598.view.SwordItemView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ItemService {

    private static final int TILE_SIZE = 16;
    private final List<Item> Items = new ArrayList<>();
    private final Map<Item, ItemView> itemViews = new HashMap<>();
    private Pane parentPane;

    public ItemService() {
    }


    public void setParentPane(Pane parentPane) {
        this.parentPane = parentPane;
    }

    public void spawnShieldItemAtTile(int tileX, int tileY, double amount) {
        double pixelX = tileX * TILE_SIZE;
        double pixelY = tileY * TILE_SIZE;

        ShieldItem item = new ShieldItem(pixelX, pixelY, amount);
        ShieldItemView view = EntityViewFactory.createShieldItemView(item);

        Items.add(item);
        itemViews.put(item, view);

        if (parentPane != null) {
            if (view.getShadow() != null && !parentPane.getChildren().contains(view.getShadow())) {
                parentPane.getChildren().add(view.getShadow());
            }
            if (view.getImageView() != null && !parentPane.getChildren().contains(view.getImageView())) {
                parentPane.getChildren().add(view.getImageView());
            }
        }
    }

    public void spawnSwordItemAtTile(int tileX, int tileY) {
        double pixelX = tileX * TILE_SIZE;
        double pixelY = tileY * TILE_SIZE;

        SwordItem item = new SwordItem(pixelX, pixelY);
        SwordItemView view = EntityViewFactory.createSwordItemView(item);

        Items.add(item);
        itemViews.put(item, view);
        if (parentPane != null) {
            if (view.getShadow() != null && !parentPane.getChildren().contains(view.getShadow())) {
                parentPane.getChildren().add(view.getShadow());
            }
            if (view.getImageView() != null && !parentPane.getChildren().contains(view.getImageView())) {
                parentPane.getChildren().add(view.getImageView());
            }
        }
    }

    public void update(Player player, PlayerController playerController) {
        if (player == null || player.isDead()) return;

        Iterator<Item> iterator = Items.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();
            if (item != null && !item.isCollected()) {
                if (item.checkCollision(player)) {
                    item.collect(player);
                    iterator.remove();

                    if (playerController != null) {
                        playerController.updateDefenseBar();
                    }

                    ItemView view = itemViews.remove(item);
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
                }
            }
        }
    }

    public void clear() {
        if (parentPane != null) {
            for (ItemView view : itemViews.values()) {
                if (view.getShadow() != null) {
                    parentPane.getChildren().remove(view.getShadow());
                }
                if (view.getImageView() != null) {
                    parentPane.getChildren().remove(view.getImageView());
                }
            }
        }
        itemViews.clear();
        Items.clear();
    }

    public List<Item> getItems() {
        return Items;
    }
}
