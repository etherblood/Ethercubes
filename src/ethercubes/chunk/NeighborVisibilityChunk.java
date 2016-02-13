/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk;

import ethercubes.data.NeighborVisibility;

/**
 *
 * @author Philipp
 */
public interface NeighborVisibilityChunk {
    void setNeighborVisibility(NeighborVisibility value);
    NeighborVisibility getNeighborVisibility();
}
