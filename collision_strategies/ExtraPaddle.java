package src.collision_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Paddle;
import src.gameobjects.ExPaddle;

import static src.BrickerGameManager.*;

public class ExtraPaddle extends CollisionStrategy{

    private static final String EXPADDLE_PATH = "assets/botGood.png";
    private final Paddle extraPaddle;

    public ExtraPaddle(GameObjectCollection gameObjectCollection, ImageReader imageReader,
                       UserInputListener inputListener, WindowController windowController) {
        super(gameObjectCollection);
        Vector2 topLeftCorner =new Vector2(windowController.getWindowDimensions().x()/2,
                windowController.getWindowDimensions().y()/2);
        Renderable paddleImage = imageReader.readImage(EXPADDLE_PATH, false);
        this.extraPaddle = new ExPaddle(topLeftCorner, V2_PADDLE_DIMS, paddleImage, inputListener,
                windowController.getWindowDimensions(), (int)BORDER_WIDTH + KEEP_DIST_FROM_BORDER,
                gameObjectCollection);

    }

    /**
     * adds this's extra paddle to game on collision if no extra paddle is currently in game.
     * @param thisObj Brick
     * @param otherObj Ball
     * @param brickCounter Counter of the number of bricks
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter brickCounter) {
        super.onCollision(thisObj, otherObj, brickCounter);
        if(BrickerGameManager.extraPaddleCounter.value() == 0){
            gameObjects.addGameObject(this.extraPaddle);
            extraPaddleCounter.increment();
        }
    }
}
