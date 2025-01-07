package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import src.BrickerGameManager;

public class Heart extends GameObject {
    private static final int HEAR_SPEED = 100;
    private final BrickerGameManager brickerGameManager;
    private final GameObjectCollection gameObjectCollection;
    private final Counter livesCounter;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner        Position of the object, in window coordinates (pixels).
     *                             Note that (0,0) is the top-left corner of the window.
     * @param dimensions           Width and height in window coordinates.
     * @param renderable           The renderable representing the object. Can be null, in which case
     *                             the GameObject will not be rendered.
     * @param gameObjectCollection objects in the game.
     * @param livesCounter player's lives counter
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                 BrickerGameManager brickerGameManager, GameObjectCollection gameObjectCollection,
                 Counter livesCounter) {
        super(topLeftCorner, dimensions, renderable);
        this.setVelocity(Vector2.DOWN.mult( HEAR_SPEED));
        this.brickerGameManager = brickerGameManager;
        this.gameObjectCollection = gameObjectCollection;
        this.livesCounter = livesCounter;
    }

    /**
     * over riding so that heart for extra life could be picked exclusively with the main paddle.
     * @param other The other GameObject.
     * @return true for collision with main paddle.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other instanceof Paddle && !(other instanceof ExPaddle);
    }

    /**
     * adds an extra life when heart is colliding with main paddle if MAX_LIFE is not reached.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(livesCounter.value() < BrickerGameManager.MAX_LIVES){
            livesCounter.increment();
            gameObjectCollection.removeGameObject(this);
        }
        if(this.getCenter().y() < 0){
            gameObjectCollection.removeGameObject(this);
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if( this.getCenter().y() > brickerGameManager.windowDimensions.y()){
            gameObjectCollection.removeGameObject(this);
        }
    }
}
