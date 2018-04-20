/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world.worldgen;

import com.etherblood.ethercubes.ChunkFactory;
import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.settings.implementation.TestBlockSettings;

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
