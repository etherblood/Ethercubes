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
public interface HasNeighbors<C> extends HasNeighborsReadonly<C> {
    void setNeighbor(Direction direction, C neighbor);
}
