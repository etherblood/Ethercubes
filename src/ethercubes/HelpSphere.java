/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.settings.implementation.ChunkSettingsImpl;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Philipp
 */
public class HelpSphere implements HelpShape {
    private GlobalBlockPosition center;
    private ChunkSettingsImpl converter;
    private ChunkSize chunkSize;
    private int radius;

    public HelpSphere(GlobalBlockPosition center, ChunkSettingsImpl converter, int radius) {
        this.center = center;
        this.converter = converter;
        this.radius = radius;
        chunkSize = converter.getSize();
    }

    public int getMinX() {
        return center.getX() - radius;
    }
    public int getMaxX() {
        return center.getX() + radius;
    }
    public int getMinY() {
        return center.getY() - radius;
    }
    public int getMaxY() {
        return center.getY() + radius;
    }
    public int getMinZ() {
        return center.getZ() - radius;
    }
    public int getMaxZ() {
        return center.getZ() + radius;
    }
    
    public boolean intersects(ChunkPosition pos) {
        int minX = pos.getX() * chunkSize.getX();
        int maxX = minX + chunkSize.getX() - 1;
        int minY = pos.getY() * chunkSize.getY();
        int maxY = minY + chunkSize.getY() - 1;
        int minZ = pos.getZ() * chunkSize.getZ();
        int maxZ = minZ + chunkSize.getZ() - 1;
        
        int x;
        if (center.getX() < minX) x = minX;
        else if(center.getX() > maxX) x = maxX;
        else x = center.getX();
        int y;
        if (center.getY() < minY) y = minY;
        else if(center.getY() > maxY) y = maxY;
        else y = center.getY();
        int z;
        if (center.getZ() < minZ) z = minZ;
        else if(center.getZ() > maxZ) z = maxZ;
        else z = center.getZ();
        
        x -= center.getX();
        y -= center.getY();
        z -= center.getZ();
        
        int rSq = radius * radius;
        
        return rSq > x * x + y * y + z * z;
    }

    public ChunkPosition getMin() {
        return converter.getContainerPosition(new GlobalBlockPosition(getMinX(), getMinY(), getMinZ()));
    }

    public ChunkPosition getMax() {
        return converter.getContainerPosition(new GlobalBlockPosition(getMaxX(), getMaxY(), getMaxZ()));
    }

    public Collection<ChunkPosition> toCollection() {
        ArrayList<ChunkPosition> set = new ArrayList<ChunkPosition>();
        ChunkPosition min = converter.getContainerPosition(new GlobalBlockPosition(getMinX(), getMinY(), getMinZ()));
        ChunkPosition max = converter.getContainerPosition(new GlobalBlockPosition(getMaxX(), getMaxY(), getMaxZ()));
        int minX = min.getX();
        int maxX = max.getX();
        int minZ = min.getZ();
        int maxZ = max.getZ();
        int minY = min.getY();
        int maxY = max.getY();
        
        minY = Math.max(minY, -3);
        maxY = Math.min(maxY, 3);
        
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = minY; y <= maxY; y++) {
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
