package ethercubes.world.worldgen.services;

import ethercubes.context.Autowire;
import ethercubes.events.CubesEventHandler;
import ethercubes.events.CubesEventbus;
import ethercubes.world.worldgen.ConcurrentTerrainChunkFactory;
import ethercubes.world.worldgen.events.ChunkPopulatedEvent;
import ethercubes.world.worldgen.events.ChunkPopulationRequest;
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
