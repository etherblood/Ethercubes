package ethercubes.display.meshing.implementation;

import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.chunk.VersionedReadonly;
import ethercubes.data.ChunkSize;
import ethercubes.display.meshing.ChunkMesher;
import ethercubes.display.meshing.ChunkMeshingResult;
import ethercubes.settings.BlockSettings;

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
        System.out.println("meshing " + chunk.getPosition());
        return mesher.generateMesh(chunk, version);
    }
}
