package ethercubes.pagination.events;

import com.jme3.math.Vector3f;

/**
 *
 * @author Philipp
 */
public class PlayerPositionChangedEvent {
    private final Vector3f newPosition;

    public PlayerPositionChangedEvent(Vector3f newPosition) {
        this.newPosition = newPosition;
    }

    public Vector3f getNewPosition() {
        return newPosition;
    }
}
