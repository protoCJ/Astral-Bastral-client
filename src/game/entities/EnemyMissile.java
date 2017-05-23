package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class representing enemy missiles.
 */
public class EnemyMissile extends Missile {

    // Constant sprite key for enemy missile.
    private static final String ENEMY_MISSILE_SPRITE_KEY =
        "enemy_missile.png";

    public EnemyMissile() {
        super(GameEntitiesTypes.ENEMY_MISSILE, ENEMY_MISSILE_SPRITE_KEY);
    }

    public EnemyMissile(
            MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(
            GameEntitiesTypes.ENEMY_MISSILE, ENEMY_MISSILE_SPRITE_KEY,
            missileType, x, y, rotation
        );
    }

}
