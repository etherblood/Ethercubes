/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk;

import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;

/**
 *
 * @author Philipp
 */
public interface ChunkReadonly {
    ChunkSize getSize();
    ChunkPosition getPosition();
}
