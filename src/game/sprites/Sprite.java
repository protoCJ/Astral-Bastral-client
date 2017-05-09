package game.sprites;

import java.awt.*;

public class Sprite {

    private Image image;

    public Sprite(Image image) {
        this.image = image;
    }

    public int getWidth() {
        return image.getWidth(null);
    }

    public int getHeight() {
        return image.getHeight(null);
    }

    public void draw(Graphics g, int x, int y, float rotation) {
        Graphics2D tmp = (Graphics2D) g.create();
        tmp.rotate(rotation);
        tmp.drawImage(image, x, y,null);
        tmp.dispose();
    }
}