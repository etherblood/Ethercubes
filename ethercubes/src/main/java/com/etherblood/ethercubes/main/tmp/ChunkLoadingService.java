package com.etherblood.ethercubes.main.tmp;

import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.chunk.implementation.ChunkPoolImpl;
import com.etherblood.ethercubes.context.Autowire;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.events.CubesEventHandler;
import com.etherblood.ethercubes.events.CubesEventbus;
import com.etherblood.ethercubes.pagination.TaskExecutor;
import com.etherblood.ethercubes.pagination.events.LoadChunkTasksRequest;
import com.etherblood.ethercubes.world.ChunkWorld;
import com.etherblood.ethercubes.world.worldgen.events.ChunkPopulationRequest;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class ChunkLoadingService {
    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private ChunkPoolImpl<ArrayChunk> pool;
//    private CompressedChunks<ArrayChunk> compressed = new CompressedChunks<>();
    @Autowire
    private TaskExecutor exe;
    
    @Autowire
    private ChunkWorld chunkWorld;
    
    @PostConstruct
    public void init() {
        eventbus.register(LoadChunkTasksRequest.class, new CubesEventHandler<LoadChunkTasksRequest>() {
            @Override
            public void handle(LoadChunkTasksRequest event) {
                List<Runnable> tasks = new ArrayList<>(event.getChunkPositions().size());
                for (final ChunkPosition chunkPosition : event.getChunkPositions()) {
                    tasks.add(new Runnable() {
                        @Override
                        public void run() {
                            ArrayChunk chunk = pool.allocChunk(chunkPosition);
                            chunkWorld.addChunk(chunk);
                            System.out.println("allocated " + chunkPosition);
                            //TODO: do actual loading...
                            eventbus.fireEvent(new ChunkPopulationRequest(chunk));
                        }
                    });
                }
                exe.submitTasks(tasks);
            }
        });
//        eventbus.register(UnloadChunkTasksRequest.class, new CubesEventHandler<UnloadChunkTasksRequest>() {
//            @Override
//            public void handle(UnloadChunkTasksRequest event) {
//                for (ChunkPosition chunkPosition : event.getChunkPositions()) {
//                    pool.freeChunk(chunkPosition);
//                }
//            }
//        });
    }
}
