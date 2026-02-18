package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * create a puck which is an extension of the Ball class
 */
public class Puck extends Ball {
    //privates and consts
    private static final int MAX_COLLISIONS = 4;
    private int collisions;
    private final GameObjectCollection gameObjects;

    /**
     * constructor
     * @param topLeftCorner the top left corner of the game
     * @param dimensions the dimensions of the paddle
     * @param renderable image
     * @param sound sound of collision
     * @param gameObjects the other game objects
     */
    public Puck(Vector2 topLeftCorner,Vector2 dimensions,Renderable renderable,
                Sound sound,GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, sound);
        this.collisions = 0;
        this.gameObjects = gameObjects;
    }

    /**
     * override the collision method of the ball to count the erase the ball when there are 4 collisions
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // Only count if the collision has a significant vertical component:
        if (Math.abs(collision.getNormal().y()) < Math.abs(collision.getNormal().x()))
            return;
        collisions++;
        if (collisions >= MAX_COLLISIONS)
            gameObjects.removeGameObject(this);
    }

}
