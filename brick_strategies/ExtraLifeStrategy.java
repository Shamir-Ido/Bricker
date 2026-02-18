package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import bricker.gameobjects.Heart;

/**
 * create an extra life if the brick breaks
 */
public class ExtraLifeStrategy implements CollisionStrategy {

    //privates
    private final GameObjectCollection gameObjects;
    private final Renderable heartImage;
    private final Vector2 windowDimensions;
    private final Vector2 heartDimensions;

    /**
     * constructor
     * @param gameObjects the game objects
     * @param heartImage the image of the heart
     * @param windowDimensions the dimensions of the window
     */
    public ExtraLifeStrategy(GameObjectCollection gameObjects,
                             Renderable heartImage,
                             Vector2 windowDimensions,
                             Vector2 heartDimensions) {
        this.gameObjects = gameObjects;
        this.heartImage = heartImage;
        this.windowDimensions = windowDimensions;
        this.heartDimensions = heartDimensions;
    }

    /**
     * create a heart on the collision
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        // Remove the brick
        gameObjects.removeGameObject(thisObj);

        // Spawn a heart at the brick’s position
        Vector2 heartSize = heartDimensions;
        Heart heart = new Heart(thisObj.getTopLeftCorner(),heartSize,heartImage,
                windowDimensions
        );

        gameObjects.addGameObject(heart, Layer.DEFAULT);
    }
}
