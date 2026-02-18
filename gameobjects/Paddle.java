package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * create the paddle for the player
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 300;
    private final UserInputListener inputListener;
    private final Vector2 windowDimensions;

    /**
     * basic constructor
     * @param topLeftCorner the top left corner of the game
     * @param dimensions the dimensions of the paddle
     * @param renderable image
     * @param inputListener the input from the user
     * @param windowDimensions the window dimensions
     */
    public Paddle(Vector2 topLeftCorner,Vector2 dimensions,Renderable renderable,
                  UserInputListener inputListener,Vector2 windowDimensions) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.setTag("paddle");
    }

    /**
     * Override the update method and change constantly the paddle placement according to the user input
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
        // Prevent the paddle from moving outside the window
        Vector2 topLeftCorner = getTopLeftCorner();
        Vector2 dimensions = getDimensions();
        if (topLeftCorner.x() < 0) {
            setTopLeftCorner(new Vector2(0, topLeftCorner.y()));
        }
        if (topLeftCorner.x() + dimensions.x() > windowDimensions.x()) {
            setTopLeftCorner(new Vector2(windowDimensions.x() - dimensions.x(), topLeftCorner.y()));
        }
    }
}
