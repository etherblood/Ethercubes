package ethercubes.events;

/**
 *
 * @author Philipp
 */
public interface CubesEventHandler<E> {
    void handle(E event);
}
