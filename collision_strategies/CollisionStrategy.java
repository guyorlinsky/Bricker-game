package src.collision_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

public class CollisionStrategy {
    protected final GameObjectCollection gameObjects;

    /**
     *  collision Strategy for each brick with the ball
     * @param gameObjectCollection GameObjectCollection
     */

    public CollisionStrategy(GameObjectCollection gameObjectCollection) {
        this.gameObjects = gameObjectCollection;

    }

    /**
     * Remove the brick on collision with ball
     * @param thisObj Brick
     * @param otherObj Ball
     * @param brickCounter Counter of the number of bricks
     */
    public void onCollision(GameObject thisObj, GameObject otherObj, Counter brickCounter) {
        if (gameObjects.removeGameObject(thisObj)) {
            brickCounter.decrement();
        }
    }
}