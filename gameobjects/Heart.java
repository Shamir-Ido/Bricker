package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * heart game object for the extra life
 */
public class Heart extends GameObject {
    //privates
    private static final Vector2 HEART_VELOCITY = new Vector2(0, 100); // Falling speed
    private final Vector2 windowDimensions;

    /**
     * checks if the other object is a paddle or not
     * @param other The other GameObject.
     * @return
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        // only collide with the original Paddle class, never ExtraPaddle
        return other.getClass() == Paddle.class;
    }

    /**
     * constructor
     * @param topLeftCorner 0,0 coordinate
     * @param dimensions the dimensions of the heart
     * @param renderable picture
     * @param windowDimensions the dimensions of the board
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.windowDimensions = windowDimensions;

        setVelocity(HEART_VELOCITY);
    }
}