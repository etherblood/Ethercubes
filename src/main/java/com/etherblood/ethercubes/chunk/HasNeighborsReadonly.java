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
public interface HasNeighborsReadonly<C> {
    C getNeighbor(Direction direction);
}
