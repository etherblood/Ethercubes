package ethercubes.chunk.implementation;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.DataXZY;
import java.util.Arrays;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.LocalBlockPosition;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.chunk.NeighborVisibilityChunk;
import ethercubes.chunk.PoolChunk;
import ethercubes.chunk.Versioned;
import ethercubes.data.Direction;
import ethercubes.data.NeighborVisibility;
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
        return getBlockFast(index(x, y, z));
    }
    
    @Override
    public final void setBlock(LocalBlockPosition localPosition, byte value) {
        setBlock(localPosition.getX(), localPosition.getY(), localPosition.getZ(), value);
    }
    @Override
    public final void setBlock(int x, int y, int z, byte value) {
        checkInBounds(x, y, z);
        setBlockFast(index(x, y, z), value);
    }
    
    public final void setLayerBlocks(int y, byte value) {
        setLayersBlocks(y, y + 1, value);
    }
    @Override
    public final void setLayersBlocks(int startY, int endY, byte value) {
        int startIndex = indexY(startY);
        int endIndex = indexY(endY);
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
    
    private int index(int x, int y, int z) {
        int index = y;
        index *= size.getZ();
        index += z;
        index *= size.getX();
        index += x;
        return index;
    }
    private int indexY(int y) {
        return size.getX() * size.getY() * y;
    }
    
}