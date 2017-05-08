package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class representing enemy missiles.
 */
public class EnemyMissile extends Missile {

    public EnemyMissile() {
        super(GameEntitiesTypes.ENEMY_MISSILE);
    }

    public EnemyMissile(
            MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.ENEMY_MISSILE, missileType, x, y, rotation);
    }

}
