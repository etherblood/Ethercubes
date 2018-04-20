/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

import com.etherblood.ethercubes.Util;
import com.etherblood.ethercubes.chunk.BlockChunkReadonly;
import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.chunk.NeighborVisibilityChunk;
import com.etherblood.ethercubes.settings.BlockSettings;
import java.util.ArrayList;
import java.util.Arrays;


public class NeighborVisibilityCalculatorImpl<C extends FastXZYChunk & NeighborVisibilityChunk & ChunkReadonly> implements NeighborVisibilityCalculator<C> {
    private static final int west = Direction.WEST.ordinal();
    private static final int east = Direction.EAST.ordinal();
    private static final int down = Direction.DOWN.ordinal();
    private static final int up = Direction.UP.ordinal();
    private static final int south = Direction.SOUTH.ordinal();
    private static final int north = Direction.NORTH.ordinal();

    private final BlockSettings tileProvider;
    public NeighborVisibilityCalculatorImpl(BlockSettings tileProvider, ChunkSize chunkSize) {
        this.tileProvider = tileProvider;
        sizeX = chunkSize.getX();
        sizeY = chunkSize.getY();
        sizeZ = chunkSize.getZ();
        indexZ = indexX * sizeX;
        indexY = indexZ * sizeZ;
        visited = new boolean[sizeX * sizeY * sizeZ];
    }
    
    long nanos, count;
    public void computeNeighborVisibility(C chunk) {
        nanos -= System.nanoTime();
        updateNeighborVisibility(chunk);
        nanos += System.nanoTime();
        count++;
        if(count % 1000 == 0) {
            System.out.println(Util.humanReadableNanos(nanos / count) + " visibilityFill");
        }
    }
    
    private final ArrayList<Integer> groups = new ArrayList<Integer>();
    private final ArrayList<LocalBlockPosition> open = new ArrayList<LocalBlockPosition>();
    private final boolean[] visited;
    private final int sizeX, sizeY, sizeZ;
    private final int indexX = 1, indexY, indexZ;
    private void updateNeighborVisibility(C chunk) {
        for (int y = 0; y < sizeY; y++) {
            for (int z = 0; z < sizeZ; z++) {
                for (int x = 0; x < sizeX; x++) {
                    int index = x * indexX + y * indexY + z * indexZ;
                    if(!visited[index]) {
                        int groupMask = 0;
                        open.add(new LocalBlockPosition(x, y, z));
                        while(!open.isEmpty()) {
                            LocalBlockPosition pos = open.get(open.size() - 1);
                            open.remove(open.size() - 1);
                            int openIndex = pos.getX() * indexX + pos.getY() * indexY + pos.getZ() * indexZ;
                            if(visited[openIndex]) {
                                continue;//this happens because open is not a set
                            }
                            visited[openIndex] = true;
                            if(tileProvider.isBlockOpaque(chunk.getBlockFast(openIndex))) {
                                continue;
                            }
                            
                            if(pos.getX() == 0) {
                                groupMask |= 1 << west;
                            } else {
                                if(!visited[openIndex - indexX]) open.add(Direction.WEST.neighbor(pos));
                            }
                            if (pos.getX() + 1 == sizeX) {
                                groupMask |= 1 << east;
                            } else {
                                if(!visited[openIndex + indexX]) open.add(Direction.EAST.neighbor(pos));
                            }
                            
                            if(pos.getY() == 0) {
                                groupMask |= 1 << down;
                            } else {
                                if(!visited[openIndex - indexY]) open.add(Direction.DOWN.neighbor(pos));
                            }
                            if (pos.getY() + 1 == sizeY) {
                                groupMask |= 1 << up;
                            } else {
                                if(!visited[openIndex + indexY]) open.add(Direction.UP.neighbor(pos));
                            }
                            
                            if(pos.getZ() == 0) {
                                groupMask |= 1 << south;
                            } else {
                                if(!visited[openIndex - indexZ]) open.add(Direction.SOUTH.neighbor(pos));
                            }
                            if (pos.getZ() + 1 == sizeZ) {
                                groupMask |= 1 << north;
                            } else {
                                if(!visited[openIndex + indexZ]) open.add(Direction.NORTH.neighbor(pos));
                            }
                        }
                        groups.add(groupMask);
                    }
                }
            }
        }
        
        int neighborVisibility = 0;
        ArrayList<Integer> directions = new ArrayList<Integer>();
        for (int groupMask : groups) {
            for (int i = 0; i < 6; i++) {
                if(((1 << i) & groupMask) != 0) {
                    for (int direction : directions) {
                        neighborVisibility |= NeighborVisibility.flagFromDirectionsSimple(i, direction);
                    }
                    directions.add(i);
                }
            }
            directions.clear();
        }
        chunk.setNeighborVisibility(new NeighborVisibility(neighborVisibility));
        groups.clear();
        Arrays.fill(visited, false);
        check(chunk, neighborVisibility);
    }
    
    public static void test() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 6; j++) {
                int flags = NeighborVisibility.flagFromDirectionsSimple(i, j);
                int flags2 = NeighborVisibility.flagFromDirectionsSimple(j, i);
                if(flags != flags2) {
                    throw new RuntimeException("different flags");
                }
                if(list.contains(flags)) {
                    throw new RuntimeException("flags not unique");
                }
                list.add(flags);
            }
        }
    }
    
    private void check(C chunk, int visibility) {
        boolean allOpaque = true;
        for (int x = 0; x < chunk.getSize().getX() && allOpaque; x++) {
            for (int y = 0; y < chunk.getSize().getZ() && allOpaque; y++) {
                allOpaque = tileProvider.isBlockOpaque(((BlockChunkReadonly)chunk).getBlock(x, y, 0));
            }
        }
        if(allOpaque) {
            for (Direction direction : Direction.values()) {
                if(direction == Direction.SOUTH) continue;
                if((visibility & (NeighborVisibility.flagFromDirectionsSimple(Direction.SOUTH.ordinal(), direction.ordinal()) | NeighborVisibility.flagFromDirectionsSimple(direction.ordinal(), Direction.SOUTH.ordinal()))) != 0) {
                    throw new RuntimeException("moep");
                }
            }
        }
    }
}
