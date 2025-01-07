package src.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;



public class Ball extends GameObject {


    private int hitCount;
    private final Sound sound;

    /**
     * This is the ball object constructor. It uses it's super's constructor
     * and saves the sound file.
     * @param topLeftCorner: position of the top left corner of the ball in
     *                     the window.
     * @param dimensions: the dimensions of the ball.
     * @param renderable: the image object of the ball.
     * @param sound: the sound file object of the ball's collision.
     */
    public Ball(Vector2 topLeftCorner,
                 Vector2 dimensions,
                 Renderable renderable,
                 Sound sound){
        super(topLeftCorner, dimensions, renderable);
        this.sound = sound;
        this.hitCount = 0;
    }

    public int getHitCount(){
        return hitCount;
    }


    /**
     * This method overwrites the OnCollisionEnter of GameObject. When it
     * collides with another object, it flips its direction..
     * @param other the object that the ball collided with.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision
     *                  .getNormal())). the collision parameters.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision){
        super.onCollisionEnter(other, collision);
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        this.sound.play();
        hitCount ++;
    }
}
