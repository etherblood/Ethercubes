/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface WorldWrapper {
    void createChunk(ChunkPosition pos);
    void deleteChunk(ChunkPosition pos);
}
