package it.unicam.cs.mpgc.rpg126598.model;

public record Hitbox(double x, double y, double width, double height) {

    /**
     * Verifica se questa hitbox si interseca con un'altra hitbox.
     *
     * @param other l'altra hitbox con cui verificare l'intersezione
     * @return true se si intersecano, false altrimenti
     */
    public boolean intersects(Hitbox other) {
        if (other == null) return false;
        return this.x < other.x + other.width &&
               this.x + this.width > other.x &&
               this.y < other.y + other.height &&
               this.y + this.height > other.y;
    }

    public double getMinX() {
        return x;
    }

    public double getMinY() {
        return y;
    }

    public double getMaxX() {
        return x + width;
    }

    public double getMaxY() {
        return y + height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
