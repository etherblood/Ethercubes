package ethercubes.pagination;

import com.jme3.math.Vector3f;
import ethercubes.data.ChunkPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class RadiusCubicLoadZone {
    private static final int minY = -2;
    private static final int maxY = 2;
    private final Vector3f center;
    private final float radius;
    private final float halfSize;

    public RadiusCubicLoadZone(Vector3f center, float radius, float halfSize) {
        this.center = center;
        this.radius = radius;
        this.halfSize = halfSize;
    }

    public List<ChunkPosition> subtract(RadiusCubicLoadZone zone) {
        ArrayList<ChunkPosition> result = new ArrayList<ChunkPosition>();
        float r = radius + halfSize;
        int minX = (int)(center.getX() - r);
        int maxX = (int)(center.getX() + r);
        int minZ = (int)(center.getZ() - r);
        int maxZ = (int)(center.getZ() + r);
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                if (contains(x, z) && (zone == null || !zone.contains(x, z))) {
                    for (int y = minY; y <= maxY; y++) {
                        result.add(new ChunkPosition(x, y, z));
                    }
                }
            }
        }
        return result;
    }
    
    private boolean contains(float x, float z) {
        float pX, pZ;
        float right = center.getX() + halfSize;
        float left = center.getX() - halfSize;
        if(right < x) {
            pX = right;
        }
        else if(x < left) {
            pX = left;
        }
        else {
            pX = x;
        }
        
        float east = center.getZ() + halfSize;
        float west = center.getZ() - halfSize;
        if(east < z) {
            pZ = east;
        }
        else if(z < west) {
            pZ = west;
        }
        else {
            pZ = z;
        }
        
        pX -= x;
        pZ -= z;
        
        return radius * radius >= pX * pX + pZ * pZ;
    }
}
