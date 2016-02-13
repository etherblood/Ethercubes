/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk;

import ethercubes.data.Direction;

/**
 *
 * @author Philipp
 */
public interface HasNeighborsReadonly<C> {
    C getNeighbor(Direction direction);
}
