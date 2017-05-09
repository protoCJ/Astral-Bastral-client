package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents enemy ships.
 */
public class EnemyShip extends Ship {

    // Enemy ships constants.
    private static final String ENEMY_SPRITE_KEY = "enemy_ship.jpg";
    private static final float ENEMY_SHIP_SPEED = (float) 0.0;


    public EnemyShip() {
        super(GameEntitiesTypes.ENEMY_SHIP, ENEMY_SPRITE_KEY);
    }

    public EnemyShip(float x, float y) {
        super(
            GameEntitiesTypes.ENEMY_SHIP, ENEMY_SPRITE_KEY,
            x, y, (float) Math.atan2(-y, -x),
            ENEMY_SHIP_SPEED
        );
    }

}
