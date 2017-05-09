package game.entities;

/**
 * Created by micha on 22.04.2017.
 *
 * Base class for ships used in the game.
 */
public abstract class Ship extends GameEntity {

    public Ship(GameEntitiesTypes type, String spriteKey) {
        super(type, spriteKey);
    }

    public Ship(
            GameEntitiesTypes type, String spriteKey,
            float x, float y, float rotation,
            float speed
    ) {
        super(type, spriteKey, x, y, rotation, speed);
    }

}
