package bricker.brick_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import bricker.gameobjects.Brick;
import java.util.Random;

/**
 * Factory class for creating Brick objects with various collision strategies.
 */
public class BrickFactory {
    //privates
    private final GameObjectCollection gameObjects;
    private final Renderable brickImage;
    private final Renderable turboBallImage;
    private final Sound collisionSound;
    private final Renderable normalBallImage;
    private final Renderable heartImage;
    private final Renderable puckImage;
    private final Sound puckSound;
    private final Renderable paddleImage;
    private final UserInputListener inputListener;
    private final Vector2 paddleDimensions;
    private final Vector2 windowDimensions;
    private final Vector2 ballSize;
    private final float ballSpeed;
    private final Random random = new Random();
    private final Vector2 heartSize;

    //consts for magic numbers
    private final static float CHANCE_FOR_REGULAR_BRICK = 0.5f;
    private final static float CHANCE_FOR_PUCK_BRICK = 0.6f;
    private final static float CHANCE_FOR_PADDLE_BRICK = 0.7f;
    private final static float CHANCE_FOR_LIFE_BRICK = 0.8f;
    private final static float CHANCE_FOR_TURBO_BRICK = 0.9f;
    private final static int CHANCE_FOR_MULT_BRICK = 1;

    /**
     * Constructor to initialize BrickFactory with required game assets and parameters.
     */
    public BrickFactory(GameObjectCollection gameObjects,
                        Renderable brickImage,
                        Renderable turboBallImage,
                        Sound collisionSound,
                        Renderable normalBallImage,
                        Renderable heartImage,
                        Renderable puckImage,
                        Sound puckSound,
                        Renderable paddleImage,
                        UserInputListener inputListener,
                        Vector2 paddleDimensions,
                        Vector2 windowDimensions,
                        Vector2 ballSize,
                        Vector2 heartSize,
                        float ballSpeed) {
        this.gameObjects = gameObjects;
        this.brickImage = brickImage;
        this.turboBallImage = turboBallImage;
        this.collisionSound = collisionSound;
        this.normalBallImage = normalBallImage;
        this.heartImage = heartImage;
        this.puckImage = puckImage;
        this.puckSound = puckSound;
        this.paddleImage = paddleImage;
        this.inputListener = inputListener;
        this.paddleDimensions = paddleDimensions;
        this.windowDimensions = windowDimensions;
        this.ballSize = ballSize;
        this.ballSpeed = ballSpeed;
        this.heartSize = heartSize;
    }

    /**
     * Creates a new Brick with a randomly selected collision strategy.
     *
     * @param position      The position of the brick.
     * @param size          The size of the brick.
     * @param brickCounter  Counter to track the number of active bricks.
     * @return A new Brick instance.
     */
    public Brick createBrick(Vector2 position, Vector2 size, Counter brickCounter) {
        float choice = random.nextFloat(); // 0 to 1
        CollisionStrategy finalStrategy;

        if (choice < CHANCE_FOR_REGULAR_BRICK) {
            finalStrategy = new BasicCollisionStrategy(gameObjects);
        }
        else if (choice < CHANCE_FOR_PUCK_BRICK) {
            finalStrategy = createPuckStrategy();
        }
        else if (choice < CHANCE_FOR_PADDLE_BRICK) {
            finalStrategy = createPaddleStrategy();
        }
        else if (choice < CHANCE_FOR_LIFE_BRICK) {
            finalStrategy = createExtraLifeStrategy();
        }
        else if (choice < CHANCE_FOR_TURBO_BRICK) {
            finalStrategy = createTurboStrategy();
        }
        else {
            finalStrategy = createDoubleStrategy(CHANCE_FOR_MULT_BRICK);
        }

        return new Brick(position, size, brickImage, finalStrategy, brickCounter);
    }

    /**
     * Creates a strategy that spawns pucks on collision.
     */
    private CollisionStrategy createPuckStrategy() {
        return new PuckStrategy(gameObjects, collisionSound, puckImage,windowDimensions, ballSpeed, ballSize);
    }

    /**
     * Creates a strategy that spawns an additional paddle on collision.
     */
    private CollisionStrategy createPaddleStrategy() {
        return new PaddleStrategy(
                gameObjects,
                paddleImage,
                windowDimensions,
                inputListener,
                paddleDimensions
        );
    }

    /**
     * Creates a strategy that grants an extra life on collision.
     */
    private CollisionStrategy createExtraLifeStrategy() {
        return new ExtraLifeStrategy(gameObjects, heartImage, windowDimensions, heartSize);
    }

    /**
     * Creates a strategy that spawns a turbo (fast) ball on collision.
     */
    private CollisionStrategy createTurboStrategy() {
        return new TurboStrategy(gameObjects, turboBallImage, collisionSound, normalBallImage, ballSpeed);
    }

    /**
     * Creates a strategy combining two random strategies, limiting recursion to a certain nesting level.
     *
     * @param nestingLevel The current recursion level of nested strategies.
     * @return A combined DoubleBehaviorStrategy.
     */
    private CollisionStrategy createDoubleStrategy(int nestingLevel) {
        if (nestingLevel > 2) {
            return pickRandomSingleStrategy();
        }

        CollisionStrategy first = pickRandomStrategy(nestingLevel + 1);
        CollisionStrategy second = pickRandomStrategy(nestingLevel + 1);

        return new DoubleBehaviorStrategy(first, second);
    }

    /**
     * Picks a random collision strategy, allowing for nesting if nesting level permits.
     *
     * @param nestingLevel Current nesting depth.
     * @return A CollisionStrategy instance.
     */
    private CollisionStrategy pickRandomStrategy(int nestingLevel) {
        int pick = random.nextInt(5);

        switch (pick) {
            case 0:
                return createPuckStrategy();
            case 1:
                return createPaddleStrategy();
            case 2:
                return createExtraLifeStrategy();
            case 3:
                return createTurboStrategy();
            case 4:
                if (nestingLevel < 2) {
                    return createDoubleStrategy(nestingLevel);
                } else {
                    return pickRandomSingleStrategy();
                }
            default:
                return createTurboStrategy();
        }
    }

    /**
     * Picks a single (non-nested) collision strategy at random.
     *
     * @return A simple CollisionStrategy instance.
     */
    private CollisionStrategy pickRandomSingleStrategy() {
        int pick = random.nextInt(4);

        switch (pick) {
            case 0:
                return createPuckStrategy();
            case 1:
                return createPaddleStrategy();
            case 2:
                return createExtraLifeStrategy();
            case 3:
                return createTurboStrategy();
            default:
                return createTurboStrategy();
        }
    }
}
