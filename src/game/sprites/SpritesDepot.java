package game.sprites;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

public class SpritesDepot {

    // Constant path to resources. MUST BE SET.
    private static final String RESOURCES_PATH = "";

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

            // Key is also sprite filename.
            String spritePath = RESOURCES_PATH + key;
            BufferedImage image = null;

            // Try to read image from hard drive.
            try {
                image = ImageIO.read(new URL(RESOURCES_PATH + key));
            }
            catch (Exception exception) {

                // TODO error
                System.out.println("");

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