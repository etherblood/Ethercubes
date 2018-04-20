package ethercubes.pagination.events;

import ethercubes.data.ChunkPosition;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class GenerateStructuresChunkTasksRequest {
    private final List<ChunkPosition> chunkPositions;

    public GenerateStructuresChunkTasksRequest(List<ChunkPosition> chunkPositions) {
        this.chunkPositions = Collections.unmodifiableList(chunkPositions);
    }

    public List<ChunkPosition> getChunkPositions() {
        return chunkPositions;
    }
}
