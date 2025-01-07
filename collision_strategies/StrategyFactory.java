package src.collision_strategies;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Ball;

public class StrategyFactory {
    /**
     * @param strategyEnum       Enum of wanted CollisionStrategy's name.
     * @param gameObjects        gameObjects
     * @param imageReader        imageReader
     * @param soundReader        soundReader
     * @param inputListener      inputListener
     * @param windowController   windowController
     * @param brickLocation      brickLocation
     * @param ballToFollow       ballToFollow
     * @param brickerGameManager brickerGameManager
     * @param livesCounter
     * @return a newly allocated CollisionStrategy, if null pointer is given, will return null.
     */
    public CollisionStrategy buildStrategy(StrategyEnum strategyEnum, GameObjectCollection gameObjects,
                                           ImageReader imageReader, SoundReader soundReader,
                                           UserInputListener inputListener, WindowController windowController,
                                           Vector2 brickLocation, Ball ballToFollow,
                                           BrickerGameManager brickerGameManager, Counter livesCounter) {
        if(strategyEnum == null){
            return null;
        }
        switch (strategyEnum){
            case REGULAR:
                return new CollisionStrategy(gameObjects);
            case EXTRA_BALLS:
                return new ExtraBalls(gameObjects, imageReader, soundReader, windowController, brickLocation);
            case EXTRA_PADDLE:
                return new ExtraPaddle(gameObjects, imageReader, inputListener, windowController);
            case CAMERA_CHANGE:
                return new CameraChange(gameObjects, windowController, ballToFollow, brickerGameManager);
            case EXTRA_LIFE:
                 return new ExtraLife(gameObjects, imageReader, brickLocation, brickerGameManager, livesCounter);
        }
        return new CollisionStrategy(gameObjects);
    }
}
