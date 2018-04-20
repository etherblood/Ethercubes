/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

/**
 *
 * @author Philipp
 */
public interface FastXZYChunk extends DataXZY {
    void setBlockFast(int indexXZY, byte block);
    byte getBlockFast(int indexXZY);
    void fill(byte block);
    void setLayersBlocks(int startY, int endY, byte value);
}