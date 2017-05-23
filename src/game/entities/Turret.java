package game.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by micha on 22.04.2017.
 *
 * Class for player controlled turrets.
 */
public class Turret extends GameEntity {

    // Turrets constants.
    private static final String TURRET_SPRITE_KEY = "turret.png";
    private static final float TURRET_SPEED = 0.0f;
    private static final int INT_SIZE = 4;

    // Id needed to connect turret to player.
    private int playerId;

    public Turret() {
        super(GameEntitiesTypes.TURRET, TURRET_SPRITE_KEY);
    }

    public Turret(int playerId, float x, float y, float rotation) {
        super(
            GameEntitiesTypes.TURRET, TURRET_SPRITE_KEY,
            x, y, rotation, TURRET_SPEED
        );
        this.playerId = playerId;
    }

    // Method used to rotate turret.
    public void rotate(float rotation) {
        this.rotation = rotation;
    }

    // Might be not needed.
    public int getPlayerId() {
        return playerId;
    }

    // Method used to input entity data from input to data.
    public int readFrom(DataInputStream input) throws IOException {

        // Read first missile type.
        this.playerId = input.readInt();

        int bytesRead = super.readFrom(input);
        return bytesRead + INT_SIZE;
    }

}
