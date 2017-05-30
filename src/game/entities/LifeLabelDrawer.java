package game.entities;

import java.awt.*;

/**
 * Created by vivace on 30.05.17.
 */
public class LifeLabelDrawer {

    private static final int LABEL_Y_OFFSET = -35;
    private static final int LABEL_HEIGHT = 4;
    private static final int LABEL_WIDTH = 80;
    private static final int LABEL_BORDER_THICKNESS = 1;

    private LifeLabelDrawer() {}

    public static void drawLabel(Graphics graphics, int x, int y, double life) {
        int labelX = x - LABEL_WIDTH / 2;
        int labelY = y - LABEL_HEIGHT / 2 + LABEL_Y_OFFSET;

        graphics.setColor(Color.black);
        graphics.fillRect(
                labelX - LABEL_BORDER_THICKNESS,
                labelY - LABEL_BORDER_THICKNESS,
                LABEL_WIDTH + 2 * LABEL_BORDER_THICKNESS,
                LABEL_HEIGHT + 2 * LABEL_BORDER_THICKNESS);
        graphics.setColor(Color.gray);
        graphics.fillRect(labelX, labelY, LABEL_WIDTH, LABEL_HEIGHT);
        graphics.setColor(Color.red);
        graphics.fillRect(labelX, labelY, (int)(LABEL_WIDTH * life), LABEL_HEIGHT);
    }
}
