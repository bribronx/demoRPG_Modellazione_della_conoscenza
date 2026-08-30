package it.unicam.cs.mpgc.rpg126598.view;


import javafx.scene.image.ImageView;
import javafx.scene.shape.Ellipse;

public class SwordItemView extends ItemView{

    public SwordItemView(ImageView imageView, Ellipse shadow) {
        this.setImageView(imageView);
        this.setShadow(shadow);
    }
}
