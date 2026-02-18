package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.ExtraPaddle;

/**
 * create another paddle in the game after colliding with a brick
 */
public class PaddleStrategy implements CollisionStrategy {
    //privates and consts
    private static final String EXTRA_PADDLE_TAG = "extra_paddle";
    private final GameObjectCollection gameObjects;
    private final Renderable paddleImage;
    private final Vector2 windowDimensions;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;

    /**
     * basic constructor of the strategy
     * @param gameObjects the other game objects
     * @param paddleImage image
     * @param windowDimensions the window dimensions
     * @param inputListener the user input
     * @param paddleDimensions the dimensions of the original paddle
     */
    public PaddleStrategy(GameObjectCollection gameObjects,Renderable paddleImage,Vector2 windowDimensions,
                          UserInputListener inputListener,Vector2 paddleDimensions) {
        this.gameObjects = gameObjects;
        this.paddleImage = paddleImage;
        this.windowDimensions = windowDimensions;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
    }

    /**
     * Override the paddle collision strategy
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjects.removeGameObject(thisObj); // Remove the brick
        // Check if extra paddle already exists
        for (GameObject obj : gameObjects) {
            if (EXTRA_PADDLE_TAG.equals(obj.getTag())) {
                return; // Extra paddle already exists, do nothing
            }
        }
        // Create extra paddle
        ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO,paddleDimensions, paddleImage,
                inputListener,windowDimensions,gameObjects);
        //Set the paddle
        extraPaddle.setCenter(new Vector2(windowDimensions.x() / 2f,windowDimensions.y() / 2f ));
        extraPaddle.setTag(EXTRA_PADDLE_TAG);
        gameObjects.addGameObject(extraPaddle, Layer.DEFAULT);
    }
}
