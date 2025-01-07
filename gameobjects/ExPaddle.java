package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import static src.BrickerGameManager.extraPaddleCounter;

public class ExPaddle extends Paddle{
    private static final int EXTRA_PADDLE_LIVES = 3;
    private final GameObjectCollection gameObjects;
    private int livesCounter;

    /**
     * This constructor creates the paddle object
     *
     * @param topLeftCorner     : the top left corner of the position of the text
     *                          object.
     * @param dimensions        : the size of the text object.
     * @param renderable        : the image file of the paddle.
     * @param inputListener     : The input listener which waits for user inputs
     *                          and acts on them.
     * @param windowDimensions  : The dimensions of the screen, to know the
     *                          limits for paddle movements.
     * @param minDistFromEdge   : Minimum distance allowed for the paddle from
     *                          the edge of the walls.
     */
    public ExPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, UserInputListener inputListener,
                    Vector2 windowDimensions, int minDistFromEdge, GameObjectCollection gameObjects) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions,
                minDistFromEdge );
        this.livesCounter = EXTRA_PADDLE_LIVES;
        this.gameObjects = gameObjects;
    }

    /**
     * when collides with gameObject decrements this lives, if reaches zero, then removes itself from game.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other, collision);
        this.livesCounter--;
        if(this.livesCounter == 0 ){

            gameObjects.removeGameObject(this);
            extraPaddleCounter.decrement();
        }
    }

}
