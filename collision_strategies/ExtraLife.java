package src.collision_strategies;


import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;
import src.gameobjects.Heart;

import static src.BrickerGameManager.HEARTS_DIMS;
import static src.BrickerGameManager.HEART_PATH;

public class ExtraLife extends CollisionStrategy{
    private final Heart heart;
    public ExtraLife(GameObjectCollection gameObjectCollection, ImageReader imageReader, Vector2 brickLocation,
                     BrickerGameManager brickerGameManager, Counter livesCounter) {
        super(gameObjectCollection);
        Renderable heartImage = imageReader.readImage(HEART_PATH,true);
//        creates a heart ready to be deployed to game on collision.
        this.heart = new Heart(brickLocation, HEARTS_DIMS, heartImage, brickerGameManager,
                gameObjectCollection, livesCounter);
    }

    /**
     * add this's heart to the game on collision.
     * @param thisObj Brick
     * @param otherObj Ball
     * @param brickCounter Counter of the number of bricks
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter brickCounter) {
        super.onCollision(thisObj, otherObj, brickCounter);
        gameObjects.addGameObject(heart);
    }
}
