/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

import com.jme3.scene.Node;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.GlobalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface WorldManager {
    void setBlock(GlobalBlockPosition pos, byte value);
    void loadChunk(ChunkPosition pos);
    void unloadChunk(ChunkPosition pos);
    void update();
    Node getRootNode();
}
