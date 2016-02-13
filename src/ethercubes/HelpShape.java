/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import java.util.Collection;
import java.util.Set;
import ethercubes.data.ChunkPosition;

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
