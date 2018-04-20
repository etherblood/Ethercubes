/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

import com.etherblood.ethercubes.data.NeighborVisibility;

/**
 *
 * @author Philipp
 */
public interface NeighborVisibilityChunk {
    void setNeighborVisibility(NeighborVisibility value);
    NeighborVisibility getNeighborVisibility();
}
