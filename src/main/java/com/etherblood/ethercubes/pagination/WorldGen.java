package com.etherblood.ethercubes.pagination;

import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.chunk.DataXZY;
import com.etherblood.ethercubes.chunk.PoolChunk;
import com.etherblood.ethercubes.chunk.Versioned;
import com.etherblood.ethercubes.chunk.compression.CompressedChunks;
import com.etherblood.ethercubes.chunk.implementation.ChunkPoolImpl;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.world.implementation.AbstractBlockChunkWorld;
import com.etherblood.ethercubes.world.worldgen.WorldGenerator;

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
