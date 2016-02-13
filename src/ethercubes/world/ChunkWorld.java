/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world;

import ethercubes.chunk.BlockChunk;
import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface ChunkWorld<C extends BlockChunk> extends ChunkWorldReadonly<C> {
    void addChunk(C chunk);
    C removeChunk(ChunkPosition pos);
}
