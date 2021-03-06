package com.etherblood.ethercubes.main;

import com.jme3.math.Vector3f;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunkFactory;
import com.etherblood.ethercubes.chunk.implementation.ChunkPoolImpl;
import com.etherblood.ethercubes.context.CubesContextBuilder;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.display.meshing.implementation.ConcurrentGreedyMesher;
import com.etherblood.ethercubes.display.meshing.services.ChunkMeshingService;
import com.etherblood.ethercubes.events.CubesEventbus;
import com.etherblood.ethercubes.main.tmp.ChunkLoadingService;
import com.etherblood.ethercubes.main.tmp.Temporary_hopefully_EventRelayService;
import com.etherblood.ethercubes.pagination.ChunkPagination;
import com.etherblood.ethercubes.pagination.TaskExecutor;
import com.etherblood.ethercubes.pagination.events.PlayerPositionChangedEvent;
import com.etherblood.ethercubes.pagination.services.ChunkPaginationService;
import com.etherblood.ethercubes.pagination.services.TaskExecutorService;
import com.etherblood.ethercubes.settings.BlockSettings;
import com.etherblood.ethercubes.settings.ChunkSettings;
import com.etherblood.ethercubes.settings.implementation.ChunkSettingsImpl;
import com.etherblood.ethercubes.settings.implementation.TileBlockSettings;
import com.etherblood.ethercubes.world.implementation.AllmightyBlockChunkWorld;
import com.etherblood.ethercubes.world.worldgen.ConcurrentTerrainChunkFactory;
import com.etherblood.ethercubes.world.worldgen.services.ChunkGenerationService;
import java.util.concurrent.Executors;

/**
 *
 * @author Philipp
 */
public class CubesStartup {
    public void run(Object... beans) {
        long seed = 0xdeadbeefL;
        ChunkSize chunkSize = new ChunkSize(32, 32, 32);
        BlockSettings blockSettings = new TileBlockSettings();
        ChunkSettings chunkSettings = new ChunkSettingsImpl(chunkSize);
        AllmightyBlockChunkWorld world = new AllmightyBlockChunkWorld(chunkSize);
        CubesEventbus eventbus = new CubesEventbus();
        
//        WorldGenerator<ArrayChunk> worldGen = new WorldGeneratorImpl(environment, new TreeGenerator(seed), chunkSize, new OreGenerator(seed));
//        WorldGraph<ArrayChunk> worldGraph = new WorldGraph<ArrayChunk>(renderTasks, mesher, material);
//        ChunkPoolImpl<ArrayChunk> chunkPool = new ChunkPoolImpl<ArrayChunk>(new ArrayChunkFactory(chunkSize));
//        WorldGen<ArrayChunk> worldGen1 = new WorldGen<ArrayChunk>(worldGen, world, compressedChunks, chunkPool);
//        PaginatedWorldManager<ArrayChunk> worldManager = new PaginatedWorldManager<ArrayChunk>(world, worldGraph, new TaskExecutor(executor), worldGen1);
        
        CubesContextBuilder builder = new CubesContextBuilder();
        for (Object bean : beans) {
            builder.addBean(bean);
        }
        builder.addBean(chunkSize);
        builder.addBean(blockSettings);
        builder.addBean(world);
        builder.addBean(eventbus);
        
        builder.addBean(new ChunkPoolImpl<ArrayChunk>(new ArrayChunkFactory(chunkSize)));
        
        builder.addBean(new ChunkMeshingService());
        builder.addBean(new ConcurrentGreedyMesher<ArrayChunk>(blockSettings, chunkSize));
        
        builder.addBean(new ChunkGenerationService());
        builder.addBean(new ConcurrentTerrainChunkFactory(seed, chunkSettings));
        
        ChunkPagination pagination = new ChunkPagination();
        pagination.setRadius(5);
        builder.addBean(pagination);
        builder.addBean(new ChunkPaginationService());
        builder.addBean(new TaskExecutor(Executors.newFixedThreadPool(3)));
        builder.addBean(new TaskExecutorService());
//        builder.addBean(worldManager);
        builder.addBean(new ChunkLoadingService());
        builder.addBean(new Temporary_hopefully_EventRelayService());
        builder.build();
        
        eventbus.fireEvent(new PlayerPositionChangedEvent(Vector3f.ZERO));
    }
}
