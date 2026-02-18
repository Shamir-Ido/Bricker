package bricker.brick_strategies;

import danogl.GameObject;

/**
 * The interface of a collision strategies in the game
 * if a puck/ball collides with a brick, then...
 */
public interface CollisionStrategy {
    /**
     * the basic method - what happens when there is a collision
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     */
    public void onCollision(GameObject thisObj, GameObject otherObj);
}
