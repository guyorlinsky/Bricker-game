package src;

import danogl.GameManager;
import danogl.GameObject;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.collision_strategies.CollisionStrategy;
import src.collision_strategies.StrategyEnum;
import src.collision_strategies.StrategyFactory;
import src.gameobjects.*;
import danogl.collisions.Layer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class BrickerGameManager extends  GameManager{
    public static final int MAX_LIVES = 4;
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(222, 158, 62));
    public static final float BORDER_WIDTH = 20.0f;
    public static final float BALL_SPEED = 250;
    private static final float HEART_X_TOP_LEFT = 30;
    private static final int ROW_NUM = 8;
    private static final int COL_NUM = 7;
    private static final int START_LIVES = 3;
    public static final int MAX_STRATEGIES = 3;
    public static final int KEEP_DIST_FROM_BORDER = 10;
    private static final String BALL_PATH = "assets/ball.png";
    public static final String PADDLE_PATH = "assets/paddle.png";
    public static final String BLOP_SOUND_PATH = "assets/blop_cut_silenced.wav";
    private static final String BRICK_PATH = "assets/brick.png";
    public static final String HEART_PATH = "assets/heart.png";
    public static final Vector2 HEARTS_DIMS = new Vector2(20,20);
    public static final Vector2 BRICK_VEC2_DIMS = new Vector2(60, 20 );
    private static final Vector2 VEC2_BALL_DIMS = new Vector2(20,20);
    public static final Vector2 V2_PADDLE_DIMS = new Vector2(150, 20);
    private static final Vector2 NUMERIC_LEFT_UPPER_CORNER = new Vector2(40, 400);
    private static final Vector2 NUMERIC_DIMS = new Vector2(40, 40);
    public final Vector2 windowDimensions;
    private static final String BACKGROUND_IMAGE_PATH = "assets/DARK_BG2_small.jpeg";
    private static final String WON_PROMT_DECLARATION = "you won!!!, play again??";
    private static final String LOOSE_PROMT_DECLARATION = "you loose ):, play again?";
    public static final Counter extraPaddleCounter = new Counter(0);
    private Counter brickCounter ;
    private Counter livesCounter ;
    private Ball ball;
    private WindowController windowController;
    private UserInputListener inputListener;

    /**
     * constructor
     * @param windowTitle window title
     * @param windowDimensions dimensions
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions){
        super (windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
    }

    /**
     * initialize one game
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        this.inputListener = inputListener;
        this.windowController = windowController;
        //create background
        createBackground(imageReader, windowController);
        //create borders
        createBorders(windowDimensions);
        //create ball
        this.ball = createBall(imageReader, soundReader, windowController);
        //create user paddle
        createPaddle(imageReader, inputListener);
        //create live counter
        this.livesCounter =  new Counter(START_LIVES);
        //create Bricks
        this.brickCounter =  new Counter(ROW_NUM*COL_NUM);
        createBricks(imageReader, this.brickCounter, soundReader, inputListener, windowController);

        //create numerical live counter
        NumericLifeCounter numericLifeCounter = new NumericLifeCounter(this.livesCounter, NUMERIC_LEFT_UPPER_CORNER,
                NUMERIC_DIMS);
        gameObjects().addGameObject(numericLifeCounter, Layer.BACKGROUND);

        //create graphical live counter
        Vector2 heartsTopLeftCorner = new Vector2(HEART_X_TOP_LEFT, windowDimensions.y() - 50);
        Renderable widgetRenderable = imageReader.readImage(HEART_PATH, true);
        GraphicLifeCounter graphicLifeCounter = new GraphicLifeCounter(heartsTopLeftCorner,
                HEARTS_DIMS, this.livesCounter, widgetRenderable, gameObjects(),livesCounter.value());
        gameObjects().addGameObject(graphicLifeCounter, Layer.BACKGROUND);
    }

    /**
     * adds borders to game
     * @param windowDimensions windowDimensions
     */
    private void createBorders(Vector2 windowDimensions) {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x()-BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        BORDER_RENDERABLE)
        );
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        BORDER_RENDERABLE)
        );
    }

    /**
     * creates a paddle and adds it to game
     * @param imageReader image of the paddle
     * @param inputListener reads user inputs
     */
    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(PADDLE_PATH, false);
        Paddle userPaddle = new Paddle(Vector2.ZERO, V2_PADDLE_DIMS, paddleImage,
                inputListener, windowDimensions, (int)BORDER_WIDTH + KEEP_DIST_FROM_BORDER);
        userPaddle.setCenter(new Vector2(windowDimensions.x()/2, windowDimensions.y() - 30));
        gameObjects().addGameObject(userPaddle);
    }

    /**
     * creates the game's visual background and adds it game
     * @param imageReader image for background display
     * @param windowController window controller
     */
    private void createBackground(ImageReader imageReader, WindowController windowController) {
        GameObject backGround = new GameObject(
                Vector2.ZERO,
                windowController.getWindowDimensions(),
                imageReader.readImage(BACKGROUND_IMAGE_PATH, false));
        gameObjects().addGameObject(backGround, Layer.BACKGROUND);
    }

    /**
     * creates bricks with up to three pseudo random generated strategies
     * @param imageReader      image of a brick
     * @param counter          counter of current unbroken game bricks
     * @param soundReader   sound reader for ball sounds
     * @param inputListener for extra balls
     * @param windowController for extra paddle
     */
    private void createBricks(ImageReader imageReader, Counter counter,
                              SoundReader soundReader, UserInputListener inputListener,
                              WindowController windowController){
        Renderable brickImage = imageReader.readImage(BRICK_PATH, false);
        StrategyFactory strategyFactory = new StrategyFactory();
        Vector2 brickCenter;
        for (int i = 0; i < ROW_NUM; i++) {
            for (int j = 0; j < COL_NUM; j++) {
//                creates new brick with the generated strategies and adds it to game.
                brickCenter = new Vector2(70+ i*80, 40+ 25*j);
                Brick brick = new Brick(Vector2.ZERO, BRICK_VEC2_DIMS, brickImage, counter,
                       generateStrategies(generateStrategyNums() , imageReader,
                        soundReader, inputListener, windowController, brickCenter,
                               strategyFactory));
                brick.setCenter(brickCenter);
                gameObjects().addGameObject(brick);
            }
        }
    }

    /**
     *
     * @param strategyEnums Enum of all the existing strategies names
     * @param imageReader imageReader
     * @param soundReader soundReader
     * @param inputListener inputListener
     * @param windowController windowController
     * @param brickLocation location of the brick that would own the generated strategies
     * @param strategyFactory factory design for generating strategies with enum input
     * @return an array of fixed MAX_STRATEGIES size,
     *         containing up to  MAX_STRATEGIES random generated CollisionStrategy, empty cells will hold null pointers.
     */
    private CollisionStrategy[] generateStrategies(StrategyEnum[] strategyEnums, ImageReader imageReader,
                                                   SoundReader soundReader, UserInputListener inputListener,
                                                   WindowController windowController, Vector2 brickLocation,
                                                   StrategyFactory strategyFactory){
        CollisionStrategy[] arr = new CollisionStrategy[MAX_STRATEGIES];
        for (int i = 0; i < MAX_STRATEGIES; i++) {
//            for each brick in game creates an array of strategies using the strategyFactory.
            arr[i] = strategyFactory.buildStrategy(strategyEnums[i], this.gameObjects(), imageReader,
                    soundReader, inputListener, windowController, brickLocation, this.ball, this,
                    this.livesCounter);
        }
        return arr;
    }

    /**
     * @return an array of Enums type of StrategyNum ready to be build. ie not containing DOUBLE CollisionStrategies.
     */
    private StrategyEnum[] generateStrategyNums() {
        StrategyEnum[] strategies = new StrategyEnum[MAX_STRATEGIES];
        StrategyEnum strategyEnum;
//        if DOUBLE strategyNum is generated, generate a strategyNum not including DOUBLE,
//        else add strategyNum to array and returns it.
        for (int i = 0; i < MAX_STRATEGIES; i++) {
            strategyEnum = StrategyEnum.generateRandomStrategy();
            if(strategyEnum != StrategyEnum.DOUBLE){
                strategies[i] = strategyEnum;
                break;
            }else{
               strategies[i] = StrategyEnum.generateRandomStrategyWithOutDouble();
            }
        }
        return strategies;
    }

    /**
     * creates ball and adds it game.
     * @param imageReader image of the ball.
     * @param soundReader sound ball makes when collides with other obj.
     * @param windowController the instructor of ball and other game objs.
     */
    private Ball createBall(ImageReader imageReader, SoundReader soundReader, WindowController windowController) {
        Renderable ballImage = imageReader.readImage(BALL_PATH,true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);
        ball = new Ball(Vector2.ZERO, VEC2_BALL_DIMS, ballImage, collisionSound);
        inisializeBallBehaviur(ball, windowController);
        gameObjects().addGameObject(ball);
        return ball;
    }

    /**
     * sets given ball's trajectory to be deployed from the screen's center.
     * @param ball main game's ball
     * @param windowController windowController
     */
    private void inisializeBallBehaviur(Ball ball, WindowController windowController){
        float ballVelY = BALL_SPEED;
        float ballVelX = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *=-1;
        }
        if(rand.nextBoolean()){
            ballVelY *=-1;
        }
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowController.getWindowDimensions().mult(0.5F));
    }

    /**
     * updates game participants
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHeight  = ball.getCenter().y();
        boolean gameEnded = false;
        String prompt  = "";
        if(ballHeight > windowDimensions.y()){
           inisializeBallBehaviur(this.ball, windowController);
           this.livesCounter.decrement();
        }
        if(this.livesCounter.value() == 0){
            //we loose
            gameEnded = true;
            prompt += LOOSE_PROMT_DECLARATION;
        }else if(inputListener.isKeyPressed(KeyEvent.VK_W) || brickCounter.value() == 0)
        {
            //we won
            gameEnded = true;
            prompt += WON_PROMT_DECLARATION;
        }
        if(gameEnded) {
            if (windowController.openYesNoDialog(prompt)) {
                windowController.resetGame();
            } else {
                windowController.closeWindow();
            }
        }
    }


    public static void main(String[] args){
        new BrickerGameManager("Bricker", new Vector2(700, 500)).run();
    }

}
