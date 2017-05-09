package game.entities;

import game.sprites.Sprite;
import game.sprites.SpritesDepot;

import java.awt.*;

// ---------- --- -------------

/**
 * An entity represents any element that appears in the game. The
 * entity is responsible for resolving collisions and movement
 * based on a set of properties defined either by subclass or externally.
 *
 * Note that doubles are used for positions. This may seem strange
 * given that pixels locations are integers. However, using double means
 * that an entity can move a partial pixel. It doesn't of course mean that
 * they will be display half way through a pixel but allows us not lose
 * accuracy as we move.
 */
public abstract class Entity {
    /** The current x location of this entity */
    protected float x;
    /** The current y location of this entity */
    protected float y;
    /** The sprite that represents this entity */
    protected Sprite sprite;
    /** The current speed of this entity (pixels/sec) */
    protected float speed;
    /** The current rotation of this entity in radians */
    protected float rotation;

    /**
     * Construct a entity based on a sprite image and a location.
     *
     * @param ref The reference to the image to be displayed for this entity
     * @param x The initial x location of this entity
     * @param y The initial y location of this entity
     */
    public Entity(String ref, int x, int y, double speed, double rotation) {
        this.sprite = SpritesDepot.getInstance().getSpriteByKey(ref);
        this.x = x;
        this.y = y;
    }

    /**
     * Request that this entity move itself based on a certain ammount
     * of time passing.
     *
     * @param delta The ammount of time that has passed in milliseconds
     */
    public void move(long delta) {
        // update the location of the entity based on move speeds

        x += (delta * speed * Math.cos(rotation)) / 1000;
        y += (delta * speed * Math.sin(rotation)) / 1000;
    }

    /**
     * Set the speed of this entity
     */
    public void setSpeed(float speed) {
        this.speed = speed;
    }


    /**
     * Get the speed of this entity
     */
    public double getSpeed() {
        return speed;
    }


    /**
     * Draw this entity to the graphics context provided
     *
     * @param g The graphics context on which to draw
     */
    public void draw(Graphics g) {
        sprite.draw(g,(int) x,(int) y, rotation);
    }

    /**
     * Do the logic associated with this entity. This method
     * will be called periodically based on game events
     */
    public void doLogic() {
    }

    /**
     * Get the x location of this entity
     *
     * @return The x location of this entity
     */
    public int getX() {
        return (int) x;
    }

    /**
     * Get the y location of this entity
     *
     * @return The y location of this entity
     */
    public int getY() {
        return (int) y;
    }
}