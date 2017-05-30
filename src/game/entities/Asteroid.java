package game.entities;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents in-game asteroid.
 */
public class Asteroid extends GameEntity {

    // Constant for asteroid.
    private static final String ASTEROID_SPRITE_KEY = "asteroid.png";
    private static final float ASTEROID_SPEED = (float) 25.0;
    private static final float ASTEROID_ROTATION_SPEED = (float) 0.5;
    // Size of float.
    private static final int FLOAT_SIZE = 4;


    private float innerRotation;


    public Asteroid() {
        super(GameEntitiesTypes.ASTEROID, ASTEROID_SPRITE_KEY);
    }

    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID, ASTEROID_SPRITE_KEY,
            x, y, rotation,
            ASTEROID_SPEED
        );
    }

    @Override
    // Override for game entity move method. Asteroids also rotate on their own.
    public void move(float deltaTime) {
        innerRotation += ASTEROID_ROTATION_SPEED * deltaTime;
        if (innerRotation < 0.0f) {
            innerRotation += Math.PI * 2;
        }
        if (innerRotation > Math.PI * 2) {
            innerRotation -= Math.PI * 2;
        }
        super.move(deltaTime);
    }

    @ Override
    // Drawing method for asteroid uses inner rotation.
    public void draw(Graphics graphics, int center_x, int center_y) {
        sprite.draw(
            graphics, (int) x + center_x, (int) y + center_y,
            innerRotation + (float) Math.PI / 2
        );
        //TODO life
        //LifeLabelDrawer.drawLabel(graphics, center_x + (int)x, center_y + (int)y, 0.8);
    }

    @Override
    public int readFrom(DataInputStream input) throws IOException {
        // Read asteroids inner rotation.
        this.innerRotation = input.readFloat();
        this.speed = ASTEROID_SPEED;
        int bytesRead = super.readFrom(input);
        return bytesRead + FLOAT_SIZE;
    }

}
