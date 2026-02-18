package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Create a new extra paddle with collision counter
 */
public class ExtraPaddle extends Paddle {
    private static final int MAX_COLLISIONS = 4;
    private int collisions;
    private final GameObjectCollection gameObjects;

    /**
     * constructor
     * @param topLeftCorner the top left corner of the game
     * @param dimensions the dimensions of the paddle
     * @param renderable image
     * @param inputListener user input
     * @param windowDimensions the dimensions of the game window
     * @param gameObjects the other game objects
     */
    public ExtraPaddle(Vector2 topLeftCorner,Vector2 dimensions,Renderable renderable,
                       UserInputListener inputListener,Vector2 windowDimensions,
                       GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions);
        this.collisions = 0;
        this.gameObjects = gameObjects;
        this.setTag("ExtraPaddle");
    }

    /**
     * avoid colliding with a heart
     * @param other The other GameObject.
     * @return
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        // Never collide with a Heart
        if (other instanceof Heart) return false;
        return super.shouldCollideWith(other);
    }

    /**
     * override the paddle on collision method
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (other instanceof Heart) {
            return;
        }
        super.onCollisionEnter(other, collision);
        // Only count collisions with balls and pucks
        if (other instanceof Ball) {
            collisions++;
            if (collisions >= MAX_COLLISIONS) {
                gameObjects.removeGameObject(this);
            }
        }
    }
}
