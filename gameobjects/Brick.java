package src.gameobjects;

import src.collision_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import static src.BrickerGameManager.MAX_STRATEGIES;
public class Brick extends GameObject {
    private final Counter counter;
    private CollisionStrategy[] strategies = new CollisionStrategy[MAX_STRATEGIES];

    /**
     * This constructor extends the super's GameObject constructor, and also
     * saves the strategy given.
     * @param topLeftCorner: the position in the window the top left corner
     *                     of the object will be placed.
     * @param dimensions: the 2d dimensions of the object on the screen.
     * @param renderable: the image object to display on the screen.
     * @param strategy: the strategy that will be used when the brick breaks.
     * @param counter: counter of the bricks.
     */
    public Brick(Vector2 topLeftCorner,
                  Vector2 dimensions,
                  Renderable renderable,
                 CollisionStrategy strategy,
                 Counter counter){
        super(topLeftCorner, dimensions, renderable);
        this.strategies[0] = strategy;
        this.counter =  counter;
    }

    public Brick(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 Counter counter, CollisionStrategy[] strategies){
        super(topLeftCorner, dimensions, renderable);
        this.strategies = strategies;
        this.counter = counter;

    }

    /**
     * This is an override method for GameObject's onCollisionEnter. When
     * the game detects a collision between the two objects, it activates
     * the strategy of the brick.
     * @param other the object this brick has collided with.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.
     *                  getNormal())); the attributes of the collision that
     *                  occurred.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other, collision);
        for (CollisionStrategy strategy : strategies) {
            if (strategy == null) {
                continue;
            }
            strategy.onCollision(this, other, this.counter);
        }
    }
}
