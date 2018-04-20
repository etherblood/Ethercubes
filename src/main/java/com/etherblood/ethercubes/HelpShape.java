/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

import com.etherblood.ethercubes.data.ChunkPosition;
import java.util.Collection;

/**
 *
 * @author Philipp
 */
public interface HelpShape {
    
//    ChunkPosition getMin();
//    ChunkPosition getMax();
    Collection<ChunkPosition> toCollection();
    
    boolean intersects(ChunkPosition pos);
}
