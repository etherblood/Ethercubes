/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface PoolChunk extends ChunkReadonly {
    void reset(ChunkPosition pos);
}
