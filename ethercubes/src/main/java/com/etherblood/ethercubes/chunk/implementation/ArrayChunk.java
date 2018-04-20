package com.etherblood.ethercubes.chunk.implementation;

import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.chunk.DataXZY;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.chunk.HasNeighbors;
import com.etherblood.ethercubes.chunk.NeighborVisibilityChunk;
import com.etherblood.ethercubes.chunk.PoolChunk;
import com.etherblood.ethercubes.chunk.Versioned;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.data.LocalBlockPosition;
import com.etherblood.ethercubes.data.NeighborVisibility;
import java.util.Arrays;
import java.util.EnumMap;

/**
 *
 * @author Philipp
 */
public class ArrayChunk implements BlockChunk, FastXZYChunk, PoolChunk, DataXZY, Versioned, HasNeighbors<ArrayChunk>, NeighborVisibilityChunk {
    private final byte[] blocks;
    private final ChunkSize size;
    private ChunkPosition position;
    private final EnumMap<Direction, ArrayChunk> neighbors = new EnumMap<Direction, ArrayChunk>(Direction.class);
    private NeighborVisibility neighborVisibility;
    private int version = 0;

    public ArrayChunk(ChunkSize size) {
        if(size == null) {
            throw new NullPointerException("size of " + ArrayChunk.class.getSimpleName() + " must not be NULL.");
        }
        this.size = size;
        blocks = new byte[size.getX() * size.getY() * size.getZ()];
    }
    @Override
    public final byte getBlock(LocalBlockPosition localPosition) {
        return getBlock(localPosition.getX(), localPosition.getY(), localPosition.getZ());
    }
    @Override
    public final byte getBlock(int x, int y, int z) {
        checkInBounds(x, y, z);
        return getBlockFast(size.index(x, y, z));
    }
    
    @Override
    public final void setBlock(LocalBlockPosition localPosition, byte value) {
        setBlock(localPosition.getX(), localPosition.getY(), localPosition.getZ(), value);
    }
    @Override
    public final void setBlock(int x, int y, int z, byte value) {
        checkInBounds(x, y, z);
        setBlockFast(size.index(x, y, z), value);
    }
    
    public final void setLayerBlocks(int y, byte value) {
        setLayersBlocks(y, y + 1, value);
    }
    @Override
    public final void setLayersBlocks(int startY, int endY, byte value) {
        int startIndex = size.index(0, startY, 0);
        int endIndex = size.index(0, endY, 0);
        setBlocksFast(startIndex, endIndex, value);
    }
    @Override
    public int getVersion() {
        return version;
    }

    @Override
    public void incVersion() {
        this.version++;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public void setNeighbor(Direction direction, ArrayChunk neighbor) {
        neighbors.put(direction, neighbor);
    }

    @Override
    public ArrayChunk getNeighbor(Direction direction) {
        return neighbors.get(direction);
    }

    @Override
    public NeighborVisibility getNeighborVisibility() {
        return neighborVisibility;
    }

    @Override
    public void setNeighborVisibility(NeighborVisibility neighborVisibility) {
        this.neighborVisibility = neighborVisibility;
    }
    
    @Override
    public final ChunkSize getSize() {
        return size;
    }
    
    @Override
    public final ChunkPosition getPosition() {
        return position;
    }

    /**
     *
     * @param position
     * @deprecated For ChunkPool only
     */
    @Deprecated
    @Override
    public void reset(ChunkPosition position) {
        this.position = position;
    }
    
    public final void clear() {
        fill((byte)0);
    }
    @Override
    public final void fill(byte block) {
        setBlocksFast(0, blocks.length, block);
    }

    @Override
    public String toString() {
        return ArrayChunk.class.getSimpleName() + "{" + "position=" + position + '}';
    }

    @Override
    public final byte[] getDataXZY() {
        return blocks;
    }
    private void checkInBounds(int x, int y, int z) {
        if(!size.contains(x, y, z)) {
            throw new RuntimeException("local position not contained in chunk.");
        }
    }

    @Override
    public byte getBlockFast(int index) {
        return blocks[index];
    }

    @Override
    public void setBlockFast(int index, byte value) {
        blocks[index] = value;
    }

    private void setBlocksFast(int startIndex, int endIndex, byte value) {
        Arrays.fill(blocks, startIndex, endIndex, value);
    }
    
}