package com.etherblood.ethercubes.pagination;

import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public final class ChunkUpdateTask {
    public final ChunkPosition pos;
    public final int level;

    public ChunkUpdateTask(ChunkPosition pos, int level) {
        this.pos = pos;
        this.level = level;
    }

}
