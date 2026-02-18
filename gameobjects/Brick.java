package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * The class for creating a brick game object
 */
public class Brick extends GameObject {

    //privates
    private final CollisionStrategy collisionStrategy;
    private final Counter brickCounter;
    private boolean isDestroyed = false;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner     Position of the object, in window coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionStrategy
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 CollisionStrategy collisionStrategy, Counter brickCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        this.brickCounter = brickCounter;

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
        if (isDestroyed) return;
        isDestroyed = true;

        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
        brickCounter.decrement();
    }
}
