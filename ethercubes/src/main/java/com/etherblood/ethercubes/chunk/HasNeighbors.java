/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

import com.etherblood.ethercubes.data.Direction;

/**
 *
 * @author Philipp
 */
public interface HasNeighbors<C> extends HasNeighborsReadonly<C> {
    void setNeighbor(Direction direction, C neighbor);
}
