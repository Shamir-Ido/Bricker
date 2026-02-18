package bricker.main;

import danogl.GameManager;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.util.Counter;
import danogl.util.Vector2;
import danogl.collisions.Layer;
import danogl.gui.rendering.*;
import danogl.GameObject;
import bricker.gameobjects.*;
import bricker.brick_strategies.*;
import java.awt.Rectangle;
import java.util.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * BrickerGameManager class initializes and runs the Bricker game.
 * It sets up the game window, ball, paddle, bricks, walls, and UI elements
 * and handles game logic like life tracking, winning conditions, and power-ups.
 */
public class BrickerGameManager extends GameManager {
    // Constants
    private static final int MAX_LIVES = 4;
    private static final float BALL_SPEED = 250;
    private static final Vector2 BALL_SIZE = new Vector2(20, 20);
    private static final Vector2 PADDLE_SIZE = new Vector2(100, 15);
    private static final Vector2 HEART_SIZE = new Vector2(20, 20);
    private static final String END_GAME_STATMENT = "You lose! Play again?";
    private static final String WIN_STATMNET = "You win! Play again?";
    private static final int INITIAL_LIFE = 3;
    private static final float BRICK_HEIGHT = 15f;
    private static final float SPACING = 8f;
    private static final float MARGIN = 10f;
    private static final int ROWS = 7;
    private static final int BRICKS_PER_ROW = 8;
    private static final Vector2 BOARD_SIZE = new Vector2(700, 500);

    // Fields
    private final int bricksPerRow;
    private final int brickRows;
    private WindowController windowController;
    private Vector2 windowDimensions;
    private Ball ball;
    private Paddle mainPaddle;
    private Counter brickCounter;
    private int remainingLives;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener inputListener;
    private Renderable normalBallImage;
    private Renderable turboBallImage;
    private Sound collosionSound;

    /**
     * Constructs the Bricker game manager with specific brick layout.
     * @param windowTitle Title of the game window.
     * @param vector Dimensions of the window.
     * @param bricksPerRow Number of bricks in each row.
     * @param brickRows Number of rows of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 vector, int bricksPerRow, int brickRows) {
        super(windowTitle, vector);
        this.bricksPerRow = bricksPerRow;
        this.brickRows = brickRows;
    }

    /**
     * Initializes the game and all its objects.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
        this.collosionSound = soundReader.readSound("assets/blop.wav");
        this.remainingLives = INITIAL_LIFE;
        brickCounter = new Counter(0);

        // Background setup
        Renderable bg = imageReader.readImage("assets/DARK_BG2_small.jpeg", false);
        GameObject background = new GameObject(Vector2.ZERO, windowDimensions, bg);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);

        // Load assets
        normalBallImage = imageReader.readImage("assets/ball.png", true);
        turboBallImage = imageReader.readImage("assets/redball.png", true);

        initBall(collosionSound);

        // Paddle setup
        Renderable paddleImage = imageReader.readImage("assets/paddle.png", true);
        mainPaddle = new Paddle(Vector2.ZERO, PADDLE_SIZE, paddleImage, inputListener, windowDimensions);
        mainPaddle.setCenter(new Vector2(windowDimensions.x() / 2f, windowDimensions.y() - 30));
        gameObjects().addGameObject(mainPaddle);

        // Bricks setup
        Renderable brickImage = imageReader.readImage("assets/brick.png", false);
        Renderable puckImage = imageReader.readImage("assets/mockBall.png", true);
        Sound puckSound = soundReader.readSound("assets/blop.wav");
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        BrickFactory brickFactory = new BrickFactory(
                gameObjects(), brickImage, turboBallImage, collosionSound,
                normalBallImage, heartImage, puckImage, puckSound,
                paddleImage, inputListener, PADDLE_SIZE, windowDimensions,
                HEART_SIZE, BALL_SIZE, BALL_SPEED
        );
        createBricks(brickFactory, brickImage);

        // Walls and UI
        createSides(windowDimensions);
        createHeartsDisplay();
        createNumericLifeDisplay();
    }

    /**
     * Initializes a new ball in the game with random velocity direction.
     */
    private void initBall(Sound collisionSound) {
        ball = new Ball(Vector2.ZERO, BALL_SIZE, normalBallImage, collisionSound);
        ball.setCenter(new Vector2(windowDimensions.x() / 2f, windowDimensions.y() * 0.60f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean()) ballVelX *= -1;
        if (rand.nextBoolean()) ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        gameObjects().addGameObject(ball);
    }

    /**
     * Called each frame to update game state.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        checkWinCondition();
        int ballCount = countMainBalls();
        handleBallReset(ballCount);
        handleExtraBalls(ballCount);
        handleBallOutOfBounds();
        handleHeartHandling();
    }

    // === Game Logic ===

    private int countMainBalls() {
        int count = 0;
        for (GameObject obj : gameObjects()) {
            if (obj instanceof Ball && !(obj instanceof Puck)) count++;
        }
        return count;
    }

    private void handleBallReset(int ballCount) {
        if (ballCount == 0) {
            remainingLives--;
            if (remainingLives > 0) resetBall();
            else endGame(END_GAME_STATMENT);
            refreshLifeDisplays();
        }
    }

    private void removeAllBalls() {
        List<GameObject> toRemove = new ArrayList<>();
        for (GameObject obj : gameObjects()) {
            if (obj instanceof Ball && !(obj instanceof Puck)) toRemove.add(obj);
        }
        for (GameObject obj : toRemove) {
            gameObjects().removeGameObject(obj);
        }
    }

    private void handleExtraBalls(int ballCount) {
        if (ballCount > 1) {
            boolean keepOne = false;
            for (GameObject obj : gameObjects()) {
                if (obj instanceof Ball && !(obj instanceof Puck)) {
                    if (!keepOne) keepOne = true;
                    else gameObjects().removeGameObject(obj);
                }
            }
        }
    }

    private void handleBallOutOfBounds() {
        for (GameObject obj : gameObjects()) {
            if (obj instanceof Ball && !(obj instanceof Puck)) {
                Ball b = (Ball) obj;
                if (b.getTopLeftCorner().y() > windowDimensions.y()) {
                    gameObjects().removeGameObject(b);
                    remainingLives--;
                    if (remainingLives > 0) resetBall();
                    else endGame(END_GAME_STATMENT);
                    refreshLifeDisplays();
                    break;
                }
            }
        }
    }

    private void handleHeartHandling() {
        boolean lifeGained = false;
        Set<Heart> toRemove = new HashSet<>(); // Avoid duplicates

        // Precompute main paddle rectangle
        Rectangle paddleRect = new Rectangle(
                (int) mainPaddle.getTopLeftCorner().x(),
                (int) mainPaddle.getTopLeftCorner().y(),
                (int) mainPaddle.getDimensions().x(),
                (int) mainPaddle.getDimensions().y());

        for (GameObject obj : gameObjects()) {
            if (!(obj instanceof Heart)) continue;
            Heart heart = (Heart) obj;

            // Remove heart if it fell off the screen
            if (heart.getTopLeftCorner().y() > windowDimensions.y()) {
                toRemove.add(heart);
                continue;
            }

            Rectangle heartRect = new Rectangle(
                    (int) heart.getTopLeftCorner().x(),
                    (int) heart.getTopLeftCorner().y(),
                    (int) heart.getDimensions().x(),
                    (int) heart.getDimensions().y());

            if (heartRect.intersects(paddleRect)) {
                toRemove.add(heart);
                if (remainingLives < MAX_LIVES) {
                    remainingLives++;
                    lifeGained = true;
                }
            }
        }

        toRemove.forEach(gameObjects()::removeGameObject);
        if (lifeGained) refreshLifeDisplays();
    }

    private void checkWinCondition() {
        if (brickCounter.value() <= 0 || inputListener.isKeyPressed(KeyEvent.VK_W)) {
            boolean again = windowController.openYesNoDialog(WIN_STATMNET);
            if (again) windowController.resetGame();
            else windowController.closeWindow();
        }
    }

    private void endGame(String message) {
        boolean again = windowController.openYesNoDialog(message);
        if (again) windowController.resetGame();
        else windowController.closeWindow();
    }

    private void refreshLifeDisplays() {
        removeHeartsDisplay();
        createHeartsDisplay();
        removeNumericLifeDisplay();
        createNumericLifeDisplay();
    }

    private void resetBall() {
        removeAllBalls();
        initBall(collosionSound);
    }

    // === Game Object Creation ===

    private void createSides(Vector2 windowDimensions) {
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(SPACING, windowDimensions.y()), null));
        gameObjects().addGameObject(new GameObject(new Vector2(windowDimensions.x() - SPACING, 0),
                new Vector2(SPACING, windowDimensions.y()), null));
        gameObjects().addGameObject(new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), SPACING), null));
    }

    private void createBricks(BrickFactory brickFactory, Renderable brickImage) {
        float totalSpacing = (bricksPerRow + 1) * SPACING;
        float brickWidth = (windowDimensions.x() - totalSpacing - 2 * MARGIN) / bricksPerRow;
        for (int row = 0; row < brickRows; row++) {
            for (int col = 0; col < bricksPerRow; col++) {
                float x = MARGIN + SPACING + col * (brickWidth + SPACING);
                float y = MARGIN + SPACING + row * (BRICK_HEIGHT + SPACING);
                Brick brick = brickFactory.createBrick(new Vector2(x, y),
                        new Vector2(brickWidth, BRICK_HEIGHT), brickCounter);
                gameObjects().addGameObject(brick);
                brickCounter.increment();
            }
        }
    }

    private void createHeartsDisplay() {
        Renderable heartImage = imageReader.readImage("assets/heart.png", true);
        Vector2 startPos = new Vector2(10f, windowDimensions.y() - HEART_SIZE.y() - 10f);
        for (int i = 0; i < remainingLives; i++) {
            Vector2 pos = startPos.add(new Vector2(i * (HEART_SIZE.x() + 5), 0));
            GameObject heart = new GameObject(pos, HEART_SIZE, heartImage);
            heart.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
            heart.setTag("heart");
            gameObjects().addGameObject(heart, Layer.UI);
        }
    }

    private void removeHeartsDisplay() {
        List<GameObject> toRemove = new ArrayList<>();
        for (GameObject obj : gameObjects()) {
            if ("heart".equals(obj.getTag())) toRemove.add(obj);
        }
        for (GameObject heart : toRemove) {
            gameObjects().removeGameObject(heart, Layer.UI);
        }
    }

    private void createNumericLifeDisplay() {
        TextRenderable lifeText = new TextRenderable(String.valueOf(remainingLives));
        lifeText.setColor(getColorForLives(remainingLives));
        Vector2 textSize = HEART_SIZE;
        Vector2 pos = new Vector2(windowDimensions.x() - textSize.x() - MARGIN,
                windowDimensions.y() - textSize.y() - MARGIN);
        GameObject textObj = new GameObject(pos, textSize, lifeText);
        textObj.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        textObj.setTag("lifeText");
        gameObjects().addGameObject(textObj, Layer.UI);
    }

    private void removeNumericLifeDisplay() {
        List<GameObject> toRemove = new ArrayList<>();
        for (GameObject obj : gameObjects()) {
            if ("lifeText".equals(obj.getTag())) toRemove.add(obj);
        }
        for (GameObject textObj : toRemove) {
            gameObjects().removeGameObject(textObj, Layer.UI);
        }
    }

    private Color getColorForLives(int lives) {
        if (lives >= 3) return Color.GREEN;
        if (lives == 2) return Color.YELLOW;
        return Color.RED;
    }

    /**
     * Entry point of the game. Accepts optional command line args
     * for bricks per row and number of rows.
     */
    public static void main(String[] args) {
        int bricksPerRow;
        int brickRows;
        if (args.length == 2) {
            bricksPerRow = Integer.parseInt(args[0]);
            brickRows = Integer.parseInt(args[1]);
        } else {
            bricksPerRow = BRICKS_PER_ROW;
            brickRows = ROWS;
        }
        new BrickerGameManager("Bricker", BOARD_SIZE, bricksPerRow, brickRows).run();
    }
}
