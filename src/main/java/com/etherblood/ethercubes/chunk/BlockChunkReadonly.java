/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

import com.etherblood.ethercubes.data.LocalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface BlockChunkReadonly extends ChunkReadonly {
    byte getBlock(int x, int y, int z);
    byte getBlock(LocalBlockPosition pos);
}
