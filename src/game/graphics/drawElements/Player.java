package game.graphics.drawElements;

import java.awt.*;

/**
 * Created by protoCJ on 24.04.2017.
 */
public class Player implements DrawElement {

    float x, y, rotation;

    public Player(float x, float y, float rotation) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
    }

    @Override
    public void draw(Graphics2D g2d) {

    }
}
