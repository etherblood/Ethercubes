/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world;

import com.etherblood.ethercubes.chunk.BlockChunkReadonly;
import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface ChunkWorldReadonly<C extends BlockChunkReadonly> {
    C getChunk(ChunkPosition pos);
}
