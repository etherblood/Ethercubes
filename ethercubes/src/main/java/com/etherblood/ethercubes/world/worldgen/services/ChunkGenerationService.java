package com.etherblood.ethercubes.world.worldgen.services;

import com.etherblood.ethercubes.context.Autowire;
import com.etherblood.ethercubes.events.CubesEventHandler;
import com.etherblood.ethercubes.events.CubesEventbus;
import com.etherblood.ethercubes.world.worldgen.ConcurrentTerrainChunkFactory;
import com.etherblood.ethercubes.world.worldgen.events.ChunkPopulatedEvent;
import com.etherblood.ethercubes.world.worldgen.events.ChunkPopulationRequest;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class ChunkGenerationService {
    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private ConcurrentTerrainChunkFactory chunkFactory;
    
    @PostConstruct
    public void init() {
        eventbus.register(ChunkPopulationRequest.class, new CubesEventHandler<ChunkPopulationRequest>() {
            @Override
            public void handle(ChunkPopulationRequest event) {
                System.out.println("populating " + event.getChunk().getPosition());
                chunkFactory.populate(event.getChunk());
                eventbus.fireEvent(new ChunkPopulatedEvent(event.getChunk()));
            }
        });
    }
}
