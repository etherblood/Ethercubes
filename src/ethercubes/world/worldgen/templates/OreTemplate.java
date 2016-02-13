/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen.templates;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.world.worldgen.BlockOffset;
import ethercubes.world.worldgen.StructureTemplate;

/**
 *
 * @author Philipp
 */
public class OreTemplate<C extends HasNeighbors<C> & BlockChunk> extends StructureTemplate<C> {

    private final byte stone;
    
    public OreTemplate(byte ore, byte stone) {
        this.stone = stone;
        
        add(new BlockOffset(0, -2, 0), ore);
        add(new BlockOffset(0, -1, 0), ore);
        add(new BlockOffset(0, 0, 0), ore);
        add(new BlockOffset(0, 1, 0), ore);
        add(new BlockOffset(0, 2, 0), ore);
        
        add(new BlockOffset(1, 0, 0), ore);
        add(new BlockOffset(2, 0, 0), ore);
        
        add(new BlockOffset(-1, 0, 0), ore);
        add(new BlockOffset(-2, 0, 0), ore);
        
        add(new BlockOffset(0, 0, 1), ore);
        add(new BlockOffset(0, 0, 2), ore);
        
        add(new BlockOffset(0, 0, -1), ore);
        add(new BlockOffset(0, 0, -2), ore);
        
        add(new BlockOffset(-1,0, 1), ore);
        add(new BlockOffset(1, 0, -1), ore);
        add(new BlockOffset(1, 0, 1), ore);
        add(new BlockOffset(-1, 0, -1), ore);
        
        add(new BlockOffset(-1, -1, 1), ore);
        add(new BlockOffset(1, -1, -1), ore);
        add(new BlockOffset(1, -1, 1), ore);
        add(new BlockOffset(-1, -1, -1), ore);
        
        add(new BlockOffset(-1, 1, 1), ore);
        add(new BlockOffset(1, 1, -1), ore);
        add(new BlockOffset(1, 1, 1), ore);
        add(new BlockOffset(-1, 1, -1), ore);
        
        
        add(new BlockOffset(0, -1, 1), ore);
        add(new BlockOffset(0, -1, -1), ore);
        add(new BlockOffset(1, -1, 0), ore);
        add(new BlockOffset(-1, -1, 0), ore);
        
        add(new BlockOffset(0, 1, 1), ore);
        add(new BlockOffset(0, 1, -1), ore);
        add(new BlockOffset(1, 1, 0), ore);
        add(new BlockOffset(-1, 1, 0), ore);
    }

    @Override
    public boolean isSpawnValid(C chunk, int x, int y, int z) {
        return chunk.getBlock(x, y, z) == stone;
    }
    
}
