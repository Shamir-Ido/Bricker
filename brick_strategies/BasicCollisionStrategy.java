package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;

/**
 * the basic method for a collision with a brick
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final GameObjectCollection gameObjectCollection;

    /**
     * basic constructor
     * @param gameObjectCollection - the game objects
     */
    public BasicCollisionStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjectCollection = gameObjectCollection;
    }

    /**
     * method called when there is a collision of a ball with the brick
     * @param thisObj - the brick
     * @param otherObj - the ball/ puck
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        gameObjectCollection.removeGameObject(thisObj);
    }
}
