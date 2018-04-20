package com.etherblood.ethercubes.pagination;

import com.etherblood.ethercubes.data.ChunkPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class CubicLoadZone {
    private final ChunkPosition center;
    private final int radius;

    public CubicLoadZone(ChunkPosition center, int radius) {
        this.center = center;
        this.radius = radius;
    }
    
    public List<ChunkPosition> subtract(CubicLoadZone zone) {
        ArrayList<ChunkPosition> result = new ArrayList<ChunkPosition>();
        for (int x = center.getX() - radius; x <= center.getX() + radius; x++) {
//            for (int y = Math.max(center.getY() - radius, -3); y <= Math.min(center.getY() + radius, 3); y++) {
            for (int y = -2; y <= 2; y++) {
                for (int z = center.getZ() - radius; z <= center.getZ() + radius; z++) {
                    if(zone == null || !zone.contains(x, y, z)) {
                        result.add(new ChunkPosition(x, y, z));
                    }
                }
            }
        }
        return result;
    }
    
    public boolean contains(int x, int y, int z) {
        return Math.abs(x - center.getX()) <= radius
//                && Math.abs(y - center.getY()) <= radius
                && Math.abs(z - center.getZ()) <= radius;
    }
}
