package com.etherblood.ethercubes.world.worldgen;

import com.etherblood.ethercubes.ChunkFactory;
import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.settings.ChunkSettings;

/**
 *
 * @author Philipp
 */
public class ConcurrentTerrainChunkFactory<C extends BlockChunk & FastXZYChunk> implements ChunkFactory<C> {
    private final ThreadLocal<ChunkFactory<C>> locals = new ThreadLocal<ChunkFactory<C>>();
    private final long seed;
    private final ChunkSettings<C> settings;

    public ConcurrentTerrainChunkFactory(long seed, ChunkSettings<C> settings) {
        this.seed = seed;
        this.settings = settings;
    }

    @Override
    public void populate(C chunk) {
        ChunkFactory<C> gen = locals.get();
        if(gen == null) {
//            gen = new RoomsGenerator<C>();
//            gen = new Superflat<C>();
            gen = new TerrainGenerator<C>(seed, settings);
            locals.set(gen);
        }
        gen.populate(chunk);
    }
}
