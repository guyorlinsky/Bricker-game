package src.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

public class NumericLifeCounter extends GameObject {


    private final Counter livesCounter;
    private final TextRenderable textRenderable;

    /**
     * The constructor of the textual representation of lives left.
     * @param livesCounter: The counter of how many lives are left right now.
     * @param topLeftCorner: the top left corner of the position of the text
     *                    object.
     * @param dimensions: the size of the text object.
     */
    public NumericLifeCounter(Counter livesCounter,
                              Vector2 topLeftCorner,
                              Vector2 dimensions){
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        this.textRenderable = new TextRenderable(Integer.toString(livesCounter.value()));
        this.renderer().setRenderable(textRenderable);
    }


    /**
     * This method is overwritten from GameObject. It sets the string value
     * of the text object to the number of current lives left.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity. unused.
     */
    @Override
    public void update(float deltaTime){
        super.update(deltaTime);
        Color color = generateColor();
        this.textRenderable.setColor(color);
        this.textRenderable.setString(String.valueOf(this.livesCounter.value()));
    }

    /**
     * @return color for the number to show
     */
    private Color generateColor() {
        int livesLeft = livesCounter.value();
        Color color = Color.white;
        if(livesLeft == 3 || livesLeft == 4){
            color = Color.green;
        }
        if(livesLeft == 2){
            color = Color.yellow;
        }
        if(livesLeft == 1){
            color = Color.red;
        }
        return color;
    }
}
