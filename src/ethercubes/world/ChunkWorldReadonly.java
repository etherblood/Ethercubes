/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world;

import ethercubes.chunk.BlockChunkReadonly;
import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface ChunkWorldReadonly<C extends BlockChunkReadonly> {
    C getChunk(ChunkPosition pos);
}
