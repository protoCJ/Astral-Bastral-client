package game.entities;

import game.sprites.Sprite;
import game.sprites.SpritesDepot;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;


/**
 * Created by micha on 22.04.2017.
 *
 * Base abstract class for all in-game entities.
 */
public abstract class GameEntity {

    private static final int BYTES_PER_GAME_ENTITY = 16;


    protected GameEntitiesTypes type;

    // Game entity's sprite.
    Sprite sprite;

    // Position fields.
    protected float x, y;
    protected float rotation;

    // Movement vector coordinates.
    protected float dx, dy;
    protected float speed;


    public GameEntity(GameEntitiesTypes type, String spriteKey) {
        this.type = type;
        this.sprite = SpritesDepot.getInstance().getSpriteByKey(spriteKey);
    }

    public GameEntity(
            GameEntitiesTypes type, String spriteKey,
            float x, float y,
            float rotation, float speed
    ) {
        this.type = type;
        this.sprite = SpritesDepot.getInstance().getSpriteByKey(spriteKey);
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.speed = speed;

        // Derive coordinates of movement direction vector from rotation.
        this.dx = (float) Math.cos(rotation - Math.PI / 2);
        this.dy = (float) Math.sin(rotation - Math.PI / 2);
    }

    public GameEntitiesTypes getType() {
        return type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    // Base method used for entity movement.
    public void move(float deltaTime) {
        x += speed * dx * deltaTime;
        y += speed * dy * deltaTime;
    }

    // Drawing method.
    public void draw(Graphics graphics, int center_x, int center_y) {
        sprite.draw(
            graphics, (int) x + center_x, (int) y + center_y,
            rotation + (float) Math.PI / 2
        );
    }

    // Method used to input entity data from input to data.
    public int readFrom(DataInputStream input) throws IOException {
        this.x = input.readFloat();
        this.y = input.readFloat();
        this.rotation = input.readFloat();

        // Derive coordinates of movement direction vector from rotation.
        this.dx = (float) Math.cos(rotation);
        this.dy = (float) Math.sin(rotation);

        return BYTES_PER_GAME_ENTITY;
    }

}
