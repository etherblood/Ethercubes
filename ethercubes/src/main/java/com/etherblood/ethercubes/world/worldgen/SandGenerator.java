/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world.worldgen;

import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.chunk.HasNeighbors;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.settings.implementation.TestBlockSettings;
import com.etherblood.ethercubes.statistics.TimeStatistics;

/**
 *
 * @author Philipp
 */
public class SandGenerator<C extends FastXZYChunk& HasNeighbors<C>& ChunkReadonly> {
    
    private final Direction[] directions = new Direction[]{Direction.EAST, Direction.UP, Direction.NORTH};
    private final int[] size;
    private final int[] indices;

    public SandGenerator(ChunkSize chunkSize) {
        size = new int[]{chunkSize.getX(), chunkSize.getY(), chunkSize.getZ()};
        indices = new int[]{1, size[0] * size[1], size[0]};
    }

    public void generateSand(C chunk) {
        long start = TimeStatistics.TIME_STATISTICS.start();
        int limit = chunk.getSize().getY() - 1;
        int sizeXZ = chunk.getSize().getX() * chunk.getSize().getZ();
        limit *= sizeXZ;
        int index;
        for (index = 0; index < limit; index++) {
            if(chunk.getBlockFast(index) == TestBlockSettings.DIRT && chunk.getBlockFast(index + sizeXZ) == TestBlockSettings.WATER) {
                chunk.setBlockFast(index, TestBlockSettings.SAND);
            }
        }
        
        C neighbor = chunk.getNeighbor(Direction.UP);
        if(neighbor != null) {
            for (int xz = 0; xz < sizeXZ; xz++) {
                if(chunk.getBlockFast(index) == TestBlockSettings.DIRT && neighbor.getBlockFast(xz) == TestBlockSettings.WATER) {
                    chunk.setBlockFast(index, TestBlockSettings.SAND);
                }
                index++;
            }
        }
        
        generate(chunk, 0);
        generate(chunk, 2);
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
    }
    
    private void generate(C chunk, int axis0) {
        int axis1 = (axis0 + 1) % 3, axis2 = (axis0 + 2) % 3;
        Direction direction = directions[axis0];
        int size0 = size[axis0], size1 = size[axis1], size2 = size[axis2];
        int index0 = indices[axis0], index1 = indices[axis1], index2 = indices[axis2];
        int neighborOffset = index0;
        C neighborChunk = chunk;
        
        for (int pos0 = 0; pos0 < size0; pos0++) {
            if(pos0 + 1 == size0) {
                neighborChunk = chunk.getNeighbor(direction);
                if (neighborChunk == null) {
                    break;
                }
                neighborOffset = -pos0 * index0;
            }
            for (int pos1 = 0; pos1 < size1; pos1++) {
                for (int pos2 = 0; pos2 < size2; pos2++) {
                    int index = pos0 * index0 + pos1 * index1 + pos2 * index2;
                    int neighborIndex = index + neighborOffset;
                    if(chunk.getBlockFast(index) == TestBlockSettings.DIRT && neighborChunk.getBlockFast(neighborIndex) == TestBlockSettings.WATER) {
                        chunk.setBlockFast(index, TestBlockSettings.SAND);
                    } else if(chunk.getBlockFast(index) == TestBlockSettings.WATER && neighborChunk.getBlockFast(neighborIndex) == TestBlockSettings.DIRT) {
                        neighborChunk.setBlockFast(neighborIndex, TestBlockSettings.SAND);
                    }
                }
            }
        }
    }
}
