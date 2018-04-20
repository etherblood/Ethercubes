package com.etherblood.ethercubes.display.meshing.services;

import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.context.Autowire;
import com.etherblood.ethercubes.display.meshing.ChunkMeshingResult;
import com.etherblood.ethercubes.display.meshing.events.ChunkMeshedEvent;
import com.etherblood.ethercubes.display.meshing.events.ChunkMeshingRequest;
import com.etherblood.ethercubes.display.meshing.implementation.ConcurrentGreedyMesher;
import com.etherblood.ethercubes.events.CubesEventHandler;
import com.etherblood.ethercubes.events.CubesEventbus;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class ChunkMeshingService {
    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private ConcurrentGreedyMesher<ArrayChunk> arrayChunkMesher;
    
    @PostConstruct
    public void init() {
        eventbus.register(ChunkMeshingRequest.class, new CubesEventHandler<ChunkMeshingRequest>() {
            @Override
            public void handle(ChunkMeshingRequest event) {
                ChunkReadonly chunk = event.getChunk();
                ChunkMeshingResult mesh = generateChunkMesh(chunk);
                System.out.println("meshed: " + chunk.getPosition());
                eventbus.fireEvent(new ChunkMeshedEvent(mesh));
            }
        });
    }
    
    private ChunkMeshingResult generateChunkMesh(ChunkReadonly chunk) {
        if (chunk instanceof ArrayChunk) {
            ArrayChunk arrayChunk = (ArrayChunk) chunk;
            return arrayChunkMesher.generateMesh(arrayChunk, arrayChunk.getVersion());
        }
        throw new UnsupportedOperationException("unable to create mesh for chunk of type " + chunk.getClass().getName());
    }
}
