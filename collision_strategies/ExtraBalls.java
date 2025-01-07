package src.collision_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.SoundReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;

import java.util.Random;

import static src.BrickerGameManager.*;

public class ExtraBalls extends CollisionStrategy{
    private static final int BALL_NUM = 3;
    private static final String PUCK_PATH = "assets/mockBall.png";
    private static final float PUCK_TO_BRICK_LENGTH_RATIO = 1 / (float)3;
    private final Ball[] ballArray = new Ball[BALL_NUM];

    /**
     * class's constructor
     * creates an array of three Pucks with wanted attributes ready for deployment
     */
    public ExtraBalls(GameObjectCollection gameObjectCollection, ImageReader imageReader,
                      SoundReader soundReader, WindowController windowController, Vector2 brickLocation) {
        super(gameObjectCollection);
        Vector2 puckSize = new Vector2(BRICK_VEC2_DIMS.x() * PUCK_TO_BRICK_LENGTH_RATIO, BRICK_VEC2_DIMS.y());
        Renderable puckImage = imageReader.readImage(PUCK_PATH,true);
        Sound collisionSound = soundReader.readSound(BLOP_SOUND_PATH);
        for (int i = 0; i < BALL_NUM; i++) {
            ballArray[i] = new Ball(new Vector2(brickLocation.x() - 1 + i, brickLocation.y()),
                    puckSize, puckImage,collisionSound);
            inisializePuckBehaviur(ballArray[i]);
        }
    }

    /**
     * creates random trajectory for a given Puck
     * @param puck the white balls
     */
    private void inisializePuckBehaviur(Ball puck){
        float ballVelY = BALL_SPEED;
        float ballVelX = BALL_SPEED;
        Random rand = new Random();
        if(rand.nextBoolean()){
            ballVelX *=-1;
        }
        if(rand.nextBoolean()){
            ballVelY *=-1;
        }
        puck.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    /**
     *deploys pucks to game on collision
     * @param thisObj Brick
     * @param otherObj Ball
     * @param brickCounter Counter of the number of bricks
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter brickCounter) {
        super.onCollision(thisObj, otherObj, brickCounter);
        for (int i = 0; i < BALL_NUM; i++) {
            gameObjects.addGameObject(ballArray[i]);
        }
    }
}
