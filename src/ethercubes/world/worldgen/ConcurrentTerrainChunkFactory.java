package ethercubes.world.worldgen;

import ethercubes.ChunkFactory;
import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.settings.ChunkSettings;

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
