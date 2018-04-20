package com.etherblood.ethercubes.chunk.implementation;

import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.LocalBlockPosition;
import com.etherblood.ethercubes.listutil.ByteArrayList;
import com.etherblood.ethercubes.listutil.IntArrayList;
import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class SparseChunk implements BlockChunk {
    private final ChunkSize size;
    private final ChunkPosition position;
    private final ByteArrayList blockValues = new ByteArrayList();
    private final IntArrayList blockIndices = new IntArrayList();
    private byte baseBlock = 0;

    public SparseChunk(ChunkSize size, ChunkPosition position) {
        this.size = size;
        this.position = position;
    }

    @Override
    public void setBlock(LocalBlockPosition pos, byte value) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), value);
    }

    @Override
    public void setBlock(int x, int y, int z, byte value) {
        setBlockFast(size.index(x, y, z), value);
    }

    public void setBlockFast(int index, byte value) {
        int i = Arrays.binarySearch(blockIndices.data(), 0, blockIndices.size(), index);
        if(i >= 0) {
            if(value == baseBlock) {
                blockIndices.removeAt(i);
                blockValues.removeAt(i);
            } else {
                blockValues.set(i, value);
            }
        } else if(value != baseBlock) {
            i = -i - 1;
            blockIndices.insertAt(i, index);
            blockValues.insertAt(i, value);
        }
    }

    @Override
    public byte getBlock(LocalBlockPosition pos) {
        return getBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public byte getBlock(int x, int y, int z) {
        return getBlockFast(size.index(x, y, z));
    }
    
    public byte getBlockFast(int index) {
        int i = Arrays.binarySearch(blockIndices.data(), 0, blockIndices.size(), index);
        if(i >= 0) {
            return blockValues.get(i);
        } else {
            return baseBlock;
        }
    }
    
    public void fill(byte block) {
        baseBlock = block;
        blockValues.clear();
        blockIndices.clear();
    }

    @Override
    public ChunkSize getSize() {
        return size;
    }

    @Override
    public ChunkPosition getPosition() {
        return position;
    }
}
