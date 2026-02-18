package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Ball;
import bricker.gameobjects.Puck;

import java.util.Random;

/**
 * Executes creating pucks strategy in the game
 */
public class PuckStrategy implements CollisionStrategy {
    // privates
    private final GameObjectCollection gameObjects;
    private final Sound collisionSound;
    private final Renderable puckImage;
    private final Vector2 windowDimensions;
    private final float ballSpeed;
    private final Vector2 ballSize;
    private static final int NUM_OF_BALLS = 2;
    private static final float BALL_MULTIPLIER = 0.75f;

    /**
     * Constructor
     * @param gameObjects the existing game objects
     * @param collisionSound sound of collision
     * @param puckImage the puck image
     * @param windowDimensions the dimensions of the window
     * @param ballSpeed the speed of the puck
     * @param ballSize the size of the ball
     */
    public PuckStrategy(GameObjectCollection gameObjects,Sound collisionSound,Renderable puckImage,
                        Vector2 windowDimensions,float ballSpeed,Vector2 ballSize) {
        this.gameObjects = gameObjects;
        this.collisionSound = collisionSound;
        this.puckImage = puckImage;
        this.windowDimensions = windowDimensions;
        this.ballSpeed = ballSpeed;
        this.ballSize = ballSize;
    }

    /**
     * override the onCollision method
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     * create two pucks
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjects.removeGameObject(thisObj);
        for (int i = 0; i < NUM_OF_BALLS; i++) {
            Ball puck = createPuck(thisObj.getCenter());
            gameObjects.addGameObject(puck, Layer.DEFAULT);
        }
    }

    //private method for crating the pucks
    private Ball createPuck(Vector2 center) {
        Vector2 puckSize = ballSize.mult(BALL_MULTIPLIER); // Dynamically compute ¾ size
        Puck puck = new Puck(center, puckSize, puckImage, collisionSound, gameObjects);
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        float velocityX = (float) Math.cos(angle) * ballSpeed;
        float velocityY = (float) Math.sin(angle) * ballSpeed;
        puck.setVelocity(new Vector2(velocityX, velocityY));

        return puck;
    }
}
