package ethercubes.chunk.implementation;

import ethercubes.chunk.BlockChunk;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.LocalBlockPosition;
import ethercubes.listutil.ByteArrayList;
import ethercubes.listutil.IntArrayList;
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
        setBlockFast(index(x, y, z), value);
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
            blockIndices.insertAt(-i, index);
            blockValues.insertAt(-i, value);
        }
    }

    @Override
    public byte getBlock(LocalBlockPosition pos) {
        return getBlock(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public byte getBlock(int x, int y, int z) {
        return getBlockFast(index(x, y, z));
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

    private int index(int x, int y, int z) {
        int index = y;
        index *= size.getZ();
        index += z;
        index *= size.getX();
        index += x;
        return index;
    }
}
