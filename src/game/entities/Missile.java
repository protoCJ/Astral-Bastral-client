package game.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by puchake on 21.03.17.
 *
 * Class describing in-game missile.
 */
public abstract class Missile extends GameEntity {

    // Temporary constant for missile speed.
    private static final float MISSILE_SPEED = (float) 5.0;

    protected MissilesTypes missileType;


    public Missile(GameEntitiesTypes type) {
        super(type);
    }

    public Missile(
            GameEntitiesTypes type, MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(type, x, y, rotation, MISSILE_SPEED);
        this.missileType = missileType;
    }

    // Method used to input entity data from input to data.
    public void readFrom(DataInputStream input) throws IOException {

        // Read first missile type.
        this.missileType = MissilesTypes.getByValue(input.readShort());

        super.readFrom(input);
    }

}
