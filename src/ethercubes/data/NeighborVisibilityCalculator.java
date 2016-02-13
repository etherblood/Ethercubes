/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.data;

import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.NeighborVisibilityChunk;

/**
 *
 * @author Philipp
 */
public interface NeighborVisibilityCalculator<C extends FastXZYChunk & NeighborVisibilityChunk> {
    void computeNeighborVisibility(C chunk);
}
