package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class for all in-game missiles fired by player's turrets.
 */
public class FriendlyMissile extends Missile {

    // Constant sprite key for friendly missile.
    private static final String FRIENDLY_MISSILE_SPRITE_KEY =
        "friendly_missile.png";

    public FriendlyMissile() {
        super(GameEntitiesTypes.FRIENDLY_MISSILE, FRIENDLY_MISSILE_SPRITE_KEY);
    }

    public FriendlyMissile(
        MissilesTypes missileType,
        float x, float y, float rotation
    ) {
        super(
            GameEntitiesTypes.FRIENDLY_MISSILE, FRIENDLY_MISSILE_SPRITE_KEY,
            missileType, x, y, rotation
        );
    }

}
