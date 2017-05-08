package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class for all in-game missiles fired by player's turrets.
 */
public class FriendlyMissile extends Missile {

    public FriendlyMissile(
        MissilesTypes missileType,
        float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.FRIENDLY_MISSILE, missileType, x, y, rotation);
    }

}
