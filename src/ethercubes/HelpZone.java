/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import java.util.HashSet;
import java.util.Set;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;

/**
 *
 * @author Philipp
 */
public class HelpZone implements HelpShape {
    private int minX, maxX, minY, maxY, minZ, maxZ;

    public HelpZone(ChunkPosition pos, int size) {
        set(pos.getX(), pos.getY(), pos.getZ(), size, size, size);
    }
    
    public void set(int centerX, int centerY, int centerZ, int sizeX, int sizeY, int sizeZ) {
        minX = centerX - sizeX;
        maxX = centerX + sizeX;
        minY = centerY - sizeY;
        maxY = centerY + sizeY;
        minZ = centerZ - sizeZ;
        maxZ = centerZ + sizeZ;
    }
    
    public boolean contains(int x, int y, int z) {
        return minX <= x && x <= maxX && minZ <= z && z <= maxZ && minY <= y && y <= maxY;
    }
    
    public boolean intersects(ChunkPosition pos) {
        return contains(pos.getX(), pos.getY(), pos.getZ());
    }

    public ChunkPosition getMin() {
        return new ChunkPosition(minX, minY, minZ);
    }

    public ChunkPosition getMax() {
        return new ChunkPosition(maxX, maxY, maxZ);
    }

    public Set<ChunkPosition> toCollection() {
        HashSet<ChunkPosition> set = new HashSet<ChunkPosition>();
        for (int x = getMin().getX(); x <= getMax().getX(); x++) {
                for (int z = getMin().getZ(); z <= getMax().getZ(); z++) {
                    for (int y = getMin().getY(); y <= getMax().getY(); y++) {
                    ChunkPosition pos = new ChunkPosition(x, y, z);
                    if(intersects(pos)) {
                        set.add(pos);
                    }
                }
            }
        }
        return set;
    }
    
}
