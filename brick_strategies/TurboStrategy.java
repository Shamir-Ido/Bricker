package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import bricker.gameobjects.Ball;
import bricker.gameobjects.RedBall;

/**
 * create an extra paddle after collision
 */
public class TurboStrategy implements CollisionStrategy {
    //privates
    private final GameObjectCollection gameObjects;
    private final Renderable turboBallImage;
    private final Sound turboSound;
    private final Renderable normalBallImage;
    private final float normalSpeed;

    /**
     * constructor
     * @param gameObjects gameobjects
     * @param turboBallImage ball image
     * @param turboSound collision sound
     * @param normalBallImage the regular ball imgae
     * @param normalSpeed the original speed
     */
    public TurboStrategy(GameObjectCollection gameObjects,
                         Renderable turboBallImage,
                         Sound turboSound,
                         Renderable normalBallImage,
                         float normalSpeed) {
        this.gameObjects = gameObjects;
        this.turboBallImage = turboBallImage;
        this.turboSound = turboSound;
        this.normalBallImage = normalBallImage;
        this.normalSpeed = normalSpeed;
    }

    /**
     * override the collision strategy
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjects.removeGameObject(thisObj);

        if (!(otherObj instanceof Ball))
            return;

        Ball collidingBall = (Ball) otherObj;

        if (collidingBall instanceof RedBall)
            return;

        RedBall redBall = new RedBall(
                collidingBall.getTopLeftCorner(),
                collidingBall.getDimensions(),
                turboBallImage,
                normalBallImage,
                turboSound,
                gameObjects,
                collidingBall.getVelocity(),
                normalSpeed
        );

        redBall.setCenter(collidingBall.getCenter());

        gameObjects.removeGameObject(collidingBall);
        gameObjects.addGameObject(redBall);
    }
}
