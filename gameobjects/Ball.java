package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;


/**
 * The class for a creation of a ball in the game
 */
public class Ball extends GameObject {
    private static Counter collisionCounter;
    private static Sound collisionSound;

    /**
     * Construct a new GameObject instance.
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Sound sound) {
        super(topLeftCorner, dimensions, renderable);
        collisionCounter = new Counter(0);
        collisionSound = sound;
    }

    /**
     * Override the onCollisionEnter method for this particular gameObject
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        // ——— Ignore hearts ———
        if (other instanceof Heart) {
            return;
        }
        // ——— Otherwise, handle as before ———
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCounter.increment();
        collisionSound.play();
    }

    /**
     *
     * @return the number of collisions
     */
    public int getCollisionCounter() {
        return collisionCounter.value();
    }

}
