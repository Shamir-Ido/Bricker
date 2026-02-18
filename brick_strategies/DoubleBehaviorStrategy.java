package bricker.brick_strategies;

import danogl.GameObject;

/**
 * class for multiply behaviors
 */
public class DoubleBehaviorStrategy implements CollisionStrategy {
    //privates
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * constructor
     * @param strategy1 strategy
     * @param strategy2 strategy
     */
    public DoubleBehaviorStrategy(CollisionStrategy strategy1, CollisionStrategy strategy2) {
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * create the collision behavior
     * @param thisObj the brick
     * @param otherObj the thing colliding with the brick
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
    }
}
