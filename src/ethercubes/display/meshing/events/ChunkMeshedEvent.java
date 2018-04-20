package ethercubes.display.meshing.events;

import ethercubes.display.meshing.ChunkMeshingResult;

/**
 *
 * @author Philipp
 */
public class ChunkMeshedEvent {
    private final ChunkMeshingResult mesh;

    public ChunkMeshedEvent(ChunkMeshingResult mesh) {
        this.mesh = mesh;
    }

    public ChunkMeshingResult getMesh() {
        return mesh;
    }
}
