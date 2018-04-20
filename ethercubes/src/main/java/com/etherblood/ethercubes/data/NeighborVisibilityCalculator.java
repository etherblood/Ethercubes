/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.chunk.NeighborVisibilityChunk;

/**
 *
 * @author Philipp
 */
public interface NeighborVisibilityCalculator<C extends FastXZYChunk & NeighborVisibilityChunk> {
    void computeNeighborVisibility(C chunk);
}
