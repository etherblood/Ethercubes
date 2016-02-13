package ethercubes.world.worldgen.templates;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.world.worldgen.BlockOffset;
import ethercubes.world.worldgen.StructureTemplate;

/**
 *
 * @author Philipp
 */
public class TreeTemplate1<C extends HasNeighbors<C> & BlockChunk> extends StructureTemplate<C> {

    private final byte dirt, air;
    
    public TreeTemplate1(byte wood, byte leafes, byte dirt, byte air) {
        this.dirt = dirt;
        this.air = air;
        
        add(new BlockOffset(0, 0, 0), wood);
        add(new BlockOffset(0, 1, 0), wood);
        add(new BlockOffset(0, 2, 0), wood);
        add(new BlockOffset(0, 3, 0), wood);
        
        add(new BlockOffset(0, 4, 0), leafes);
        add(new BlockOffset(0, 5, 0), leafes);
        
        add(new BlockOffset(1, 3, 0), leafes);
        add(new BlockOffset(2, 3, 0), leafes);
        
        add(new BlockOffset(-1, 3, 0), leafes);
        add(new BlockOffset(-2, 3, 0), leafes);
        
        add(new BlockOffset(0, 3, 1), leafes);
        add(new BlockOffset(0, 3, 2), leafes);
        
        add(new BlockOffset(0, 3, -1), leafes);
        add(new BlockOffset(0, 3, -2), leafes);
        
        add(new BlockOffset(-1, 3, 1), leafes);
        add(new BlockOffset(1, 3, -1), leafes);
        add(new BlockOffset(1, 3, 1), leafes);
        add(new BlockOffset(-1, 3, -1), leafes);
        
        add(new BlockOffset(-1, 2, 1), leafes);
        add(new BlockOffset(1, 2, -1), leafes);
        add(new BlockOffset(1, 2, 1), leafes);
        add(new BlockOffset(-1, 2, -1), leafes);
        
        add(new BlockOffset(-1, 4, 1), leafes);
        add(new BlockOffset(1, 4, -1), leafes);
        add(new BlockOffset(1, 4, 1), leafes);
        add(new BlockOffset(-1, 4, -1), leafes);
        
        
        add(new BlockOffset(0, 2, 1), leafes);
        add(new BlockOffset(0, 2, -1), leafes);
        add(new BlockOffset(1, 2, 0), leafes);
        add(new BlockOffset(-1, 2, 0), leafes);
        
        add(new BlockOffset(0, 4, 1), leafes);
        add(new BlockOffset(0, 4, -1), leafes);
        add(new BlockOffset(1, 4, 0), leafes);
        add(new BlockOffset(-1, 4, 0), leafes);
    }

    @Override
    public boolean isSpawnValid(C chunk, int x, int y, int z) {
        Byte down = tryGetBlock(chunk, x, y - 1, z);
        if(down != null && down.byteValue() == dirt) {
            for (BlockOffset offset : positions) {
                Byte block = tryGetBlock(chunk, offset.getX() + x, offset.getY() + y, offset.getZ() +  z);
                if(block == null || block.byteValue() != air) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
}
