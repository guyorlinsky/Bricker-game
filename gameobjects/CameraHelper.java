package src.gameobjects;

import danogl.GameObject;
import danogl.util.Vector2;
import src.collision_strategies.CameraChange;

public class CameraHelper extends GameObject {
    private final Ball ballToFollow;
    private final CameraChange cameraChange;


    /**
     *
     * @param cameraChange strategy
     * @param ballToFollow the ball
     */
    public CameraHelper(CameraChange cameraChange, Ball ballToFollow) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.ballToFollow = ballToFollow;
        this.cameraChange = cameraChange;
    }

    /**
     * quits camera operation after constant number of ball hits.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(cameraChange.getMaxHitsCount() == ballToFollow.getHitCount()){
            cameraChange.getGameManager().setCamera(null);
        }
    }
}
