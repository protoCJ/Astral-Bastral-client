package game.entities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;


/**
 * Created by micha on 22.04.2017.
 *
 * Base abstract class for all in-game entities.
 */
public abstract class GameEntity {

    protected GameEntitiesTypes type;

    // Position fields.
    protected float x, y;
    protected float rotation;

    // Movement vector coordinates.
    protected float dx, dy;
    protected float speed;


    public GameEntity(GameEntitiesTypes type) {
        this.type = type;
    }

    public GameEntity(
            GameEntitiesTypes type,
            float x, float y,
            float rotation, float speed
    ) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = speed;

        // Derive coordinates of movement direction vector from rotation.
        this.dx = (float) Math.cos(rotation);
        this.dy = (float) Math.sin(rotation);
    }

    public GameEntitiesTypes getType() {
        return type;
    }

    // Base method used for entity movement.
    public void move() {
        x += speed * dx;
        y += speed * dy;
    }

    // Method used to input entity data from input to data.
    public void readFrom(DataInputStream input) throws IOException {
        this.x = input.readFloat();
        this.y = input.readFloat();
        this.rotation = input.readFloat();
        this.speed = input.readFloat();

        // Derive coordinates of movement direction vector from rotation.
        this.dx = (float) Math.cos(rotation);
        this.dy = (float) Math.sin(rotation);
    }

}
