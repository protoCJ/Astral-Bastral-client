package game.entities;

import java.awt.*;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents enemy ships.
 */
public class EnemyShip extends Ship {

    // Enemy ships constants.
    private static final String ENEMY_SPRITE_KEY = "enemy_ship.png";
    private static final float ENEMY_SHIP_SPEED = (float) 15.0;


    public EnemyShip() {
        super(GameEntitiesTypes.ENEMY_SHIP, ENEMY_SPRITE_KEY);
        this.speed = ENEMY_SHIP_SPEED;
    }

    public EnemyShip(float x, float y) {
        super(
            GameEntitiesTypes.ENEMY_SHIP, ENEMY_SPRITE_KEY,
            x, y, (float) Math.atan2(-y, -x),
            ENEMY_SHIP_SPEED
        );
    }

    @Override
    public void draw(Graphics graphics, int center_x, int center_y) {
        super.draw(graphics, center_x, center_y);
        //TODO life
        //LifeLabelDrawer.drawLabel(graphics, center_x + (int)x, center_y + (int)y, 0.8);
    }
}
