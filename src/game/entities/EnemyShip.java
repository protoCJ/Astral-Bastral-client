package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents enemy ships.
 */
public class EnemyShip extends Ship {

    // Enemy ships constants.
    private static final float ENEMY_SHIP_SPEED = (float) 0.0;


    public EnemyShip() {
        super(GameEntitiesTypes.ENEMY_SHIP);
    }

    public EnemyShip(float x, float y) {
        super(
            GameEntitiesTypes.ENEMY_SHIP,
            x, y, (float) Math.atan2(-y, -x),
            ENEMY_SHIP_SPEED
        );
    }

}
