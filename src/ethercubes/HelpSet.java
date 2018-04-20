/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import ethercubes.data.ChunkPosition;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class HelpSet implements HelpShape {
    private final Set<ChunkPosition> set;

    public HelpSet(Set<ChunkPosition> set, int grow) {
        this(grow(set, grow));
    }
    public HelpSet(Set<ChunkPosition> set) {
        this.set = set;
    }
    
    public Set<ChunkPosition> toCollection() {
        return set;
    }

    public boolean intersects(ChunkPosition pos) {
        return set.contains(pos);
    }
    
    private static Set<ChunkPosition> grow(Set<ChunkPosition> set, int size) {
        Set<ChunkPosition> result = new HashSet<ChunkPosition>();
        for (ChunkPosition pos : set) {
            for (int x = -size; x <= size; x++) {
                for (int y = -size; y <= size; y++) {
                    for (int z = -size; z <= size; z++) {
                        result.add(new ChunkPosition(x + pos.getX(), y + pos.getY(), z + pos.getZ()));
                    }
                }
            }
        }
        return result;
    }
}
