/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk.implementation;

import ethercubes.chunk.Factory;
import ethercubes.chunk.PoolChunk;
import ethercubes.data.ChunkPosition;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Philipp
 */
public class ChunkPoolImpl<C extends PoolChunk> {
    private final Factory<C> factory;
    private final ConcurrentLinkedQueue<C> pool = new ConcurrentLinkedQueue();

    public ChunkPoolImpl(Factory<C> factory) {
        this.factory = factory;
    }
    
    public C allocChunk(ChunkPosition pos) {
        C chunk = pool.poll();
        if(chunk == null) {
            chunk = factory.create();
        }
        chunk.reset(pos);
        return chunk;
    }
    
    public void freeChunk(C chunk) {
        pool.offer(chunk);
    }

    public void sout() {
        System.out.println("pool chunks: " + pool.size());
    }
}
