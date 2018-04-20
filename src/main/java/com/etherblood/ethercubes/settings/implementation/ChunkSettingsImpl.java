/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings.implementation;

import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.GlobalBlockPosition;
import com.etherblood.ethercubes.data.LocalBlockPosition;
import com.etherblood.ethercubes.settings.ChunkSettings;

/**
 *
 * @author Philipp
 */
public class ChunkSettingsImpl implements ChunkSettings<ArrayChunk>{
    private final ChunkSize size;

    public ChunkSettingsImpl(ChunkSize size) {
        this.size = size;
    }
    
//    public AllmightyChunkImpl createInstance(ChunkPosition position) {
//        return new AllmightyChunkImpl(position, size);
//    }

    @Override
    public ChunkPosition getContainerPosition(GlobalBlockPosition blockPosition) {
        int x = getContainerX(blockPosition.getX());
        int y = getContainerY(blockPosition.getY());
        int z = getContainerZ(blockPosition.getZ());
        return new ChunkPosition(x, y, z);
    }
    
    @Override
    public LocalBlockPosition getLocalPosition(GlobalBlockPosition blockPosition) {
        int x = getLocalX(blockPosition.getX());
        int y = getLocalY(blockPosition.getY());
        int z = getLocalZ(blockPosition.getZ());
        assert size.contains(x, y, z);
        return new LocalBlockPosition(x, y, z);
    }
    
    @Override
    public GlobalBlockPosition getGlobalPosition(ChunkPosition chunkPos, LocalBlockPosition localPos) {
        int x = getGlobalX(chunkPos.getX(), localPos.getX());
        int y = getGlobalY(chunkPos.getY(), localPos.getY());
        int z = getGlobalZ(chunkPos.getZ(), localPos.getZ());
        return new GlobalBlockPosition(x, y, z);
    }
    
    private int getContainerX(int x) {
        return floorDiv(x, size.getX());
    }
    private int getContainerY(int y) {
        return floorDiv(y, size.getY());
    }
    private int getContainerZ(int z) {
        return floorDiv(z, size.getZ());
    }
    
    private int getLocalX(int x) {
        return floorMod(x, size.getX());
    }
    private int getLocalY(int y) {
        return floorMod(y, size.getY());
    }
    private int getLocalZ(int z) {
        return floorMod(z, size.getZ());
    }
    
    private int getGlobalX(int chunkX, int localX) {
        return chunkX * size.getX() + localX;
    }
    private int getGlobalY(int chunkY, int localY) {
        return chunkY * size.getY() + localY;
    }
    private int getGlobalZ(int chunkZ, int localZ) {
        return chunkZ * size.getZ() + localZ;
    }
    
    private int floorDiv(int n, int d) {
        assert(d > 0);
        if (n >= 0) {
            return n / d;
        } else {
            return ~(~n / d);
        }
    }
    private int floorMod(int n, int d) {
        assert(d > 0);
        if (n >= 0) {
            return n % d;
        } else {
            return d + ~(~n % d);
        }
    }

    @Override
    public ChunkSize getSize() {
        return size;
    }
}
