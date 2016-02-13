/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.settings;

import ethercubes.chunk.ChunkReadonly;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.data.LocalBlockPosition;

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
