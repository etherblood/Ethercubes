/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public interface WorldWrapper {
    void createChunk(ChunkPosition pos);
    void deleteChunk(ChunkPosition pos);
}
