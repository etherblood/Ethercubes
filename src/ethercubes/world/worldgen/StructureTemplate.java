package ethercubes.world.worldgen;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.data.ChunkSize;
import ethercubes.data.Direction;
import java.util.ArrayList;
import ethercubes.data.LocalBlockPosition;

/**
 *
 * @author Philipp
 */
public class StructureTemplate<C extends HasNeighbors<C> & BlockChunk> {
    protected final ArrayList<BlockOffset> positions = new ArrayList<BlockOffset>();
    protected final ArrayList<Byte> blocks = new ArrayList<Byte>();
    
    public final void spawn(C chunk, int x, int y, int z) {
        for (int i = 0; i < positions.size(); i++) {
            BlockOffset offset = positions.get(i);
            trySetBlock(chunk, x + offset.getX(), y + offset.getY(), z + offset.getZ(), blocks.get(i));
        }
    }
    
    protected void trySetBlock(C chunk, int x, int y, int z, byte block) {
        ChunkSize size = chunk.getSize();
        while(x < 0) {
            chunk = chunk.getNeighbor(Direction.WEST);
            if(chunk == null) {
                return;
            }
            x += size.getX();
        }
        while(x >= size.getX()) {
            chunk = chunk.getNeighbor(Direction.EAST);
            if(chunk == null) {
                return;
            }
            x -= size.getX();
        }
        
        while(z < 0) {
            chunk = chunk.getNeighbor(Direction.SOUTH);
            if(chunk == null) {
                return;
            }
            z += size.getZ();
        }
        while(z >= size.getZ()) {
            chunk = chunk.getNeighbor(Direction.NORTH);
            if(chunk == null) {
                return;
            }
            z -= size.getZ();
        }
        
        while(y < 0) {
            chunk = chunk.getNeighbor(Direction.DOWN);
            if(chunk == null) {
                return;
            }
            y += size.getY();
        }
        while(y >= size.getY()) {
            chunk = chunk.getNeighbor(Direction.UP);
            if(chunk == null) {
                return;
            }
            y -= size.getY();
        }
        chunk.setBlock(x, y, z, block);
    }
    protected Byte tryGetBlock(C chunk, int x, int y, int z) {
        ChunkSize size = chunk.getSize();
        while(x < 0) {
            chunk = chunk.getNeighbor(Direction.WEST);
            if(chunk == null) {
                return null;
            }
            x += size.getX();
        }
        while(x >= size.getX()) {
            chunk = chunk.getNeighbor(Direction.EAST);
            if(chunk == null) {
                return null;
            }
            x -= size.getX();
        }
        
        while(z < 0) {
            chunk = chunk.getNeighbor(Direction.SOUTH);
            if(chunk == null) {
                return null;
            }
            z += size.getZ();
        }
        while(z >= size.getZ()) {
            chunk = chunk.getNeighbor(Direction.NORTH);
            if(chunk == null) {
                return null;
            }
            z -= size.getZ();
        }
        
        while(y < 0) {
            chunk = chunk.getNeighbor(Direction.DOWN);
            if(chunk == null) {
                return null;
            }
            y += size.getY();
        }
        while(y >= size.getY()) {
            chunk = chunk.getNeighbor(Direction.UP);
            if(chunk == null) {
                return null;
            }
            y -= size.getY();
        }
        return chunk.getBlock(x, y, z);
    }
    
    public void add(BlockOffset offset, byte block) {
        positions.add(offset);
        blocks.add(block);
    }
    
    public boolean isSpawnValid(C chunk, int x, int y, int z) {
        return true;
    }
}
