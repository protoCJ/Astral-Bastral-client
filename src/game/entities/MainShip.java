package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Definition of class which represents main game ship.
 */
public class MainShip extends Ship {

    // Speed and sprite key of the main ship.
    private static final String MAIN_SPRITE_KEY = "main_ship.png";
    private static final float MAIN_SHIP_SPEED = 0.0f;

    public MainShip() {
        super(GameEntitiesTypes.MAIN_SHIP, MAIN_SPRITE_KEY);
    }

    public MainShip(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.MAIN_SHIP, MAIN_SPRITE_KEY,
            x, y, rotation, MAIN_SHIP_SPEED
        );
    }

}
