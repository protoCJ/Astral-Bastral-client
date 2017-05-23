package game.sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

public class SpritesDepot {

    // Singleton.
    private static SpritesDepot depot = new SpritesDepot();
    private HashMap<String, Sprite> sprites = new HashMap<>();


    private SpritesDepot() {

    }

    public static SpritesDepot getInstance() {
        return depot;
    }

    public Sprite getSpriteByKey(String key) {
        if (sprites.get(key) != null) {
            return sprites.get(key);
        }
        else {

            BufferedImage image = null;

            // Try to read image from hard drive.
            try {
                URL imageUrl = getClass().getResource(key);
                image = ImageIO.read(imageUrl);
            }
            catch (Exception exception) {
                System.out.println(exception.getMessage());
                // TODO error
            }

            // Draw loaded image on accelerated image.
            GraphicsConfiguration graphicsConfiguration =
                GraphicsEnvironment.getLocalGraphicsEnvironment().
                getDefaultScreenDevice().getDefaultConfiguration();
            Image compatibleImage =
                graphicsConfiguration.createCompatibleImage(
                    image.getWidth(), image.getHeight(), Transparency.BITMASK
                );
            compatibleImage.getGraphics().drawImage(image, 0, 0, null);

            // Create sprite from image, put it into hash map, and return it.
            Sprite sprite = new Sprite(compatibleImage);
            sprites.put(key, sprite);
            return sprite;

        }
    }

}