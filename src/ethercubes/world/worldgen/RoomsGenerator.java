/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen;

import ethercubes.ChunkFactory;
import ethercubes.chunk.BlockChunk;
import ethercubes.settings.implementation.TestBlockSettings;

/**
 *
 * @author Philipp
 */
public class RoomsGenerator<C extends BlockChunk> implements ChunkFactory<C> {
    private int roomSize = 50;
    
    public void populate(C chunk) {
        int offX = chunk.getPosition().getX() * chunk.getSize().getX();
        int offY = chunk.getPosition().getY() * chunk.getSize().getY();
        int offZ = chunk.getPosition().getZ() * chunk.getSize().getZ();
        for (int x = 0; x < chunk.getSize().getX(); x++) {
            for (int y = 0; y < chunk.getSize().getY(); y++) {
                for (int z = 0; z < chunk.getSize().getZ(); z++) {
                    if((x + offX) % roomSize == 0 || (y + offY) % roomSize == 0 || (z + offZ) % roomSize == 0) {
                        chunk.setBlock(x, y, z, TestBlockSettings.DIRT);
                    } else {
                        chunk.setBlock(x, y, z, TestBlockSettings.AIR);
                    }
                }
            }
        }
    }
    
}
