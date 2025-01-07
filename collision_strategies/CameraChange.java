package src.collision_strategies;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.gameobjects.Ball;
import src.gameobjects.CameraHelper;

public class CameraChange extends CollisionStrategy {

    private static final int CAMERA_LIVES = 4;
    private final WindowController windowController;
    private final Ball ballToFollow;
    private final GameManager gameManager;
    private int maxHitsCount;

    public CameraChange(GameObjectCollection gameObjectCollection, WindowController windowController, Ball ballToFollow,
                        GameManager gameManager) {
        super(gameObjectCollection);
        this.windowController = windowController;
        this.ballToFollow = ballToFollow;
        this.gameManager = gameManager;
        gameObjectCollection.addGameObject(new CameraHelper(this, ballToFollow));
    }

    /**
     * @return the number of ball hits where our camera strategy would quiet.
     */
    public int getMaxHitsCount(){
        return maxHitsCount;
    }

    /**
     * @return game manager that the camera is owned by.
     */
    public GameManager getGameManager(){
        return gameManager;
    }

    /**
     * setting the game camera to follow the game ball if no camera strategy is currently active.
     * @param thisObj Brick
     * @param otherObj Ball
     * @param brickCounter Counter of the number of bricks
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter brickCounter) {
        super.onCollision(thisObj, otherObj, brickCounter);
        if(gameManager.getCamera() == null){
            this.maxHitsCount = ballToFollow.getHitCount() + CAMERA_LIVES;
            gameManager.setCamera(
                new Camera(
                        this.ballToFollow, //object to follow
                        Vector2.ZERO, //follow the center of the object
                        windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                        windowController.getWindowDimensions() //share the window dimensions
                ));
        }
    }
}
