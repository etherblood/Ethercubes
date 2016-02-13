/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import com.jme3.scene.Node;
import ethercubes.data.ChunkPosition;
import ethercubes.data.GlobalBlockPosition;

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
