/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk;

import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;

/**
 *
 * @author Philipp
 */
public interface ChunkReadonly {
    ChunkSize getSize();
    ChunkPosition getPosition();
}
