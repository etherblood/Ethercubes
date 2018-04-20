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
public interface BlockChunk extends BlockChunkReadonly {
    void setBlock(LocalBlockPosition pos, byte value);
    void setBlock(int x, int y, int z, byte value);
}