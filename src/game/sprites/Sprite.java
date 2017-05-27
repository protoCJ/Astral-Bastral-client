package game.sprites;


import java.awt.*;


public class Sprite {

    // Instance of image, which will be drawn by this sprite.
    private Image image;


    public Sprite(Image image) {
        this.image = image;
    }

    /**
     * Draw this sprite.
     *
     * @param graphics graphics context of draw operation
     * @param x x position of the top-left corner of the drawing
     * @param y y position of the same corner
     * @param rotation value in radians by which image should be rotated
     */
    public void draw(Graphics graphics, int x, int y, float rotation) {
        // Copy grapfics context.
        Graphics2D graphicsCopy = (Graphics2D) graphics.create();
        // Draw rotated and translated image represented by this Sprite.
        graphicsCopy.rotate(
            rotation,
            x + image.getWidth(null) / 2,
            y + image.getWidth(null) / 2
        );
        graphicsCopy.drawImage(image, x, y,null);
        graphicsCopy.dispose();
    }
}