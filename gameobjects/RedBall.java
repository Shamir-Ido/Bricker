package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * create the turbo mode ball
 */
public class RedBall extends Ball {
    //privates
    private static final int MAX_COLLISIONS = 6;
    private static final float TURBO_SPEED_MULTIPLIER = 1.4f;
    private int collisions;
    private final Renderable normalImage;
    private final GameObjectCollection gameObjects;
    private final Sound sound;
    private final float normalSpeed;
    private boolean alreadyReverted = false;

    /**
     * constructor
     * @param topLeftCorner the 0,0 coordinate
     * @param dimensions the dimentions of the object
     * @param turboImage the image of the tyrbo ball
     * @param normalImage the normal image of a ball
     * @param sound sound of collision
     * @param gameObjects the list of game objects
     * @param velocity velocity of the ball
     * @param normalSpeed the regualr speed of the ball in the game
     */
    public RedBall(Vector2 topLeftCorner, Vector2 dimensions,
                   Renderable turboImage, Renderable normalImage,
                   Sound sound, GameObjectCollection gameObjects,
                   Vector2 velocity, float normalSpeed) {

        super(topLeftCorner, dimensions, turboImage, sound);
        this.normalImage = normalImage;
        this.gameObjects = gameObjects;
        this.sound = sound;
        this.normalSpeed = normalSpeed;
        this.collisions = 0;

        setVelocity(velocity.normalized().mult(velocity.magnitude() * TURBO_SPEED_MULTIPLIER));
    }

    /**
     * collision handling
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        collisions++;
        if (collisions >= MAX_COLLISIONS) {
            revertToNormalBall();
        }
    }
//private helper function
    private void revertToNormalBall() {
        if (alreadyReverted)
            return;

        alreadyReverted = true;

        Ball normalBall = new Ball(
                getTopLeftCorner(),
                getDimensions(),
                normalImage,
                sound
        );

        normalBall.setVelocity(getVelocity().normalized().mult(normalSpeed));
        normalBall.setCenter(getCenter());

        gameObjects.addGameObject(normalBall);
        gameObjects.removeGameObject(this);
    }
}
