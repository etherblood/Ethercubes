/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world;

import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface ChunkWorld<C extends BlockChunk> extends ChunkWorldReadonly<C> {
    void addChunk(C chunk);
    C removeChunk(ChunkPosition pos);
}
