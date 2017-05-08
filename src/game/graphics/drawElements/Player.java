package game.graphics.drawElements;

import java.awt.*;

/**
 * Created by protoCJ on 24.04.2017.
 */
public class Player implements DrawElement {

    float x, y, rotation;
    Color color;

    public Player(float x, float y, float rotation, Color color) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.color = color;
    }

    @Override
    public void draw(Graphics2D g2d) {
        Graphics2D tmp = (Graphics2D) g2d.create();
        tmp.setColor(color);
        Rectangle turret = new Rectangle(-10, -10, 20, 20);
        Rectangle cannon = new Rectangle(-3, -50, 6, 50);
        tmp.translate((int)x, (int)y);
        tmp.rotate(rotation*2*3.14);
        tmp.draw(turret);
        tmp.draw(cannon);
        tmp.dispose();
    }
}
