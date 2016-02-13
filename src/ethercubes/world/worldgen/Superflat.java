/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen;

import ethercubes.ChunkFactory;
import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.data.ChunkPosition;
import ethercubes.settings.implementation.TestBlockSettings;

/**
 *
 * @author Philipp
 */
public class Superflat<C extends FastXZYChunk & ChunkReadonly> implements ChunkFactory<C> {

    @Override
    public void populate(C chunk) {
        ChunkPosition pos = chunk.getPosition();
        if (pos.getY() < 1) {
            if (pos.getY() < 0) {
                chunk.fill(TestBlockSettings.STONE);
            } else {
                chunk.fill(TestBlockSettings.DIRT);
                chunk.setLayersBlocks(0, 29, TestBlockSettings.STONE);
            }
        } else {
            chunk.fill(TestBlockSettings.AIR);
        }
    }
}
