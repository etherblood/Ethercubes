package ethercubes.pagination;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.DataXZY;
import ethercubes.chunk.PoolChunk;
import ethercubes.chunk.Versioned;
import ethercubes.chunk.compression.CompressedChunks;
import ethercubes.chunk.implementation.ChunkPoolImpl;
import ethercubes.data.ChunkPosition;
import ethercubes.world.implementation.AbstractBlockChunkWorld;
import ethercubes.world.worldgen.WorldGenerator;

/**
 *
 * @author Philipp
 */
public class WorldGen<T extends BlockChunk & Versioned & DataXZY & PoolChunk> {

    private final WorldGenerator worldGenerator;
    private final AbstractBlockChunkWorld<T> worldData;
    private final CompressedChunks<T> compressor;
    private final ChunkPoolImpl<T> chunkPool;

    public WorldGen(WorldGenerator worldGenerator, AbstractBlockChunkWorld<T> worldData, CompressedChunks<T> compressor, ChunkPoolImpl<T> chunkPool) {
        this.worldGenerator = worldGenerator;
        this.worldData = worldData;
        this.compressor = compressor;
        this.chunkPool = chunkPool;
    }
    
    public void delete(ChunkPosition pos) {
        T chunk = worldData.getChunk(pos);
        if(chunk.getVersion() > 2) {
            compressor.save(chunk);
        }
        worldData.removeChunk(pos);
        chunkPool.freeChunk(chunk);
    }

    public void update0(ChunkPosition pos) {
        T chunk = chunkPool.allocChunk(pos);
        worldData.addChunk(chunk);
        if (!compressor.tryLoad(chunk)) {
            worldGenerator.firstPass(chunk);
            chunk.setVersion(1);
        }
    }

    public void update1(ChunkPosition pos) {
        T chunk = worldData.getChunk(pos);
        if (chunk.getVersion() == 1) {
            worldGenerator.secondPass(chunk);
            chunk.setVersion(2);
        }
    }
}
