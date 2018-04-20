/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings;

import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.GlobalBlockPosition;
import com.etherblood.ethercubes.data.LocalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface ChunkSettings<C extends ChunkReadonly> {
    ChunkSize getSize();
//    C createInstance(ChunkPosition position);
    ChunkPosition getContainerPosition(GlobalBlockPosition blockPosition);
    LocalBlockPosition getLocalPosition(GlobalBlockPosition blockPosition);
    GlobalBlockPosition getGlobalPosition(ChunkPosition chunkPos, LocalBlockPosition localPos);
}
