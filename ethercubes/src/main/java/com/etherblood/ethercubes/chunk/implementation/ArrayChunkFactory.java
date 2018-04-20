package com.etherblood.ethercubes.chunk.implementation;

import com.etherblood.ethercubes.chunk.Factory;
import com.etherblood.ethercubes.data.ChunkSize;

/**
 *
 * @author Philipp
 */
public class ArrayChunkFactory implements Factory<ArrayChunk>{
    private final ChunkSize size;

    public ArrayChunkFactory(ChunkSize size) {
        this.size = size;
    }

    @Override
    public ArrayChunk create() {
        return new ArrayChunk(size);
    }
}
