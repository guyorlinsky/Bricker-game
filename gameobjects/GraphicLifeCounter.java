package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class GraphicLifeCounter extends GameObject {
    private static final float SPACE = 10;
    private final Counter livesCounter;
    private final GameObjectCollection gameObjectsCollection;
    private final GameObject[] hearts;
    private int preGameLives;

    /**
     * This is the constructor for the graphic lives counter. It creates a
     * 0x0 sized object (to be able to call its update method in game),
     * Creates numOfLives hearts, and adds them to the game.
     * @param widgetTopLeftCorner: the top left corner of the left most heart.
     * @param widgetDimensions: the dimension of each heart.
     * @param livesCounter: the counter which holds current lives count.
     * @param widgetRenderable: the image renderable of the hearts.
     * @param gameObjectsCollection: the collection of all game objects
     *                             currently in the game.
     * @param numOfLives: number of current live.
     */
    public GraphicLifeCounter(Vector2 widgetTopLeftCorner,
                              Vector2 widgetDimensions, Counter livesCounter,
                              Renderable widgetRenderable,
                              GameObjectCollection gameObjectsCollection,
                              int numOfLives){
        super(widgetTopLeftCorner,widgetDimensions,widgetRenderable);
        this.livesCounter = livesCounter;
        this.preGameLives = livesCounter.value();
        this.gameObjectsCollection = gameObjectsCollection;
        this.hearts = new GameObject[numOfLives + 1];
        for (int i = 0; i < numOfLives + 1; i++) {
            this.hearts[i] = new GameObject(
                    new Vector2(widgetTopLeftCorner.x() + i * (widgetDimensions.x() + SPACE),
                            widgetTopLeftCorner.y()),
                    widgetDimensions, widgetRenderable);
            if(i<numOfLives){
                gameObjectsCollection.addGameObject(hearts[i], Layer.BACKGROUND);
            }
        }
    }


    /**
     * This method is overwritten from GameObject It removes hearts from the
     * screen if there are more hearts than there are lives left.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity. unused.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(this.livesCounter.value() < preGameLives){
            preGameLives--;
            gameObjectsCollection.removeGameObject(hearts[preGameLives], Layer.BACKGROUND);
        }
        if(this.livesCounter.value() > preGameLives){
            gameObjectsCollection.addGameObject(hearts[preGameLives], Layer.BACKGROUND);
            preGameLives++;
        }
    }
}
