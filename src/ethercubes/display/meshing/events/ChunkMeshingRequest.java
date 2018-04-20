package ethercubes.display.meshing.events;

import ethercubes.chunk.ChunkReadonly;

/**
 *
 * @author Philipp
 */
public class ChunkMeshingRequest<C extends ChunkReadonly> {
    private final C chunk;

    public ChunkMeshingRequest(C chunk) {
        this.chunk = chunk;
    }

    public C getChunk() {
        return chunk;
    }
}
