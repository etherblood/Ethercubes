package com.etherblood.ethercubes.display.meshing.implementation;

import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.FastXZYChunk;
import com.etherblood.ethercubes.chunk.HasNeighbors;
import com.etherblood.ethercubes.chunk.VersionedReadonly;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.display.meshing.ChunkMesher;
import com.etherblood.ethercubes.display.meshing.ChunkMeshingResult;
import com.etherblood.ethercubes.settings.BlockSettings;

/**
 *
 * @author Philipp
 */
public class ConcurrentGreedyMesher<C extends FastXZYChunk & HasNeighbors<C> & VersionedReadonly & ChunkReadonly> implements ChunkMesher<C> {
    private static final ThreadLocal<ChunkMesher> locals = new ThreadLocal<>();
    private final BlockSettings blockSettings;
    private final ChunkSize size;
    
    public ConcurrentGreedyMesher(BlockSettings blockSettings, ChunkSize size) {
        this.blockSettings = blockSettings;
        this.size = size;
    }

    @Override
    public ChunkMeshingResult generateMesh(C chunk, int version) {
        ChunkMesher<C> mesher = locals.get();
        if(mesher == null) {
            mesher = new GreedyMesher<C>(blockSettings, size);
            locals.set(mesher);
        }
        return mesher.generateMesh(chunk, version);
    }
}
