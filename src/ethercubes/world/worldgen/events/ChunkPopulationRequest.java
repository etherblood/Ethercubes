package ethercubes.world.worldgen.events;

import ethercubes.chunk.BlockChunk;

/**
 *
 * @author Philipp
 */
public class ChunkPopulationRequest {
    private final BlockChunk chunk;

    public ChunkPopulationRequest(BlockChunk chunk) {
        this.chunk = chunk;
    }

    public BlockChunk getChunk() {
        return chunk;
    }

}
