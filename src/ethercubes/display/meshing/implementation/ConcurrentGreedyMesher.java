package ethercubes.display.meshing.implementation;

import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.chunk.VersionedReadonly;
import ethercubes.data.ChunkSize;
import ethercubes.display.meshing.ChunkMesher;
import ethercubes.display.meshing.ChunkMeshingResult;
import ethercubes.settings.BlockSettings;
import ethercubes.settings.implementation.ChunkSettingsImpl;

/**
 *
 * @author Philipp
 */
public class ConcurrentGreedyMesher<C extends FastXZYChunk & HasNeighbors<C> & VersionedReadonly & ChunkReadonly> implements ChunkMesher<C> {
    private final ThreadLocal<ChunkMesher<C>> locals = new ThreadLocal<ChunkMesher<C>>();
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
