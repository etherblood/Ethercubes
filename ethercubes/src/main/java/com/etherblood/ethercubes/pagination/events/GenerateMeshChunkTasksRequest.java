package com.etherblood.ethercubes.pagination.events;

import com.etherblood.ethercubes.data.ChunkPosition;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class GenerateMeshChunkTasksRequest {
    private final List<ChunkPosition> chunkPositions;

    public GenerateMeshChunkTasksRequest(List<ChunkPosition> chunkPositions) {
        this.chunkPositions = Collections.unmodifiableList(chunkPositions);
    }

    public List<ChunkPosition> getChunkPositions() {
        return chunkPositions;
    }
}
