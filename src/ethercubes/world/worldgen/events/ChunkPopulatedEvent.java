package ethercubes.world.worldgen.events;

import ethercubes.chunk.BlockChunk;

/**
 *
 * @author Philipp
 */
public class ChunkPopulatedEvent {
    private final BlockChunk chunk;

    public ChunkPopulatedEvent(BlockChunk chunk) {
        this.chunk = chunk;
    }

    public BlockChunk getChunk() {
        return chunk;
    }

}
