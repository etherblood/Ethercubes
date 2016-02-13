/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk;

import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface PoolChunk extends ChunkReadonly {
    void reset(ChunkPosition pos);
}
