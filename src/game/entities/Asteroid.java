package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents in-game asteroid.
 */
public class Asteroid extends GameEntity {

    // Constant for asteroid.
    private static final float ASTEROID_SPEED = (float) 5.0;

    public Asteroid() {
        super(GameEntitiesTypes.ASTEROID);
    }

    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID,
            x, y, rotation,
            ASTEROID_SPEED
        );
    }

}
