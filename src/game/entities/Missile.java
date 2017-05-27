package game.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by puchake on 21.03.17.
 *
 * Class describing in-game missile.
 */
public abstract class Missile extends GameEntity {

    // Temporary constant for missile speed.
    private static final float MISSILE_SPEED = (float) 100.0;
    private static final int SHORT_SIZE = 2;

    protected MissilesTypes missileType;


    public Missile(GameEntitiesTypes type, String spriteKey) {
        super(type, spriteKey);
    }

    public Missile(
            GameEntitiesTypes type, String spriteKey,
            MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(type, spriteKey, x, y, rotation, MISSILE_SPEED);
        this.missileType = missileType;
    }

    // Method used to input entity data from input to data.
    public int readFrom(DataInputStream input) throws IOException {

        // Read first missile type.
        this.missileType = MissilesTypes.getByValue(input.readShort());
        this.speed = MISSILE_SPEED;

        int bytesRead = super.readFrom(input);
        return bytesRead + SHORT_SIZE;
    }

}
