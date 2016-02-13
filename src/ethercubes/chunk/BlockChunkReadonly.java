/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk;

import ethercubes.data.LocalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface BlockChunkReadonly extends ChunkReadonly {
    byte getBlock(int x, int y, int z);
    byte getBlock(LocalBlockPosition pos);
}
