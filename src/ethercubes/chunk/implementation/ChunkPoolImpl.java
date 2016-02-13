/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk.implementation;

import java.util.ArrayList;
import java.util.Collections;
import ethercubes.chunk.Factory;
import ethercubes.chunk.PoolChunk;
import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public class ChunkPoolImpl<C extends PoolChunk> {
    private final Factory<C> factory;
    private final ArrayList<C> chunks = new ArrayList<C>();
    private final ArrayList<Integer> free = new ArrayList<Integer>();

    public ChunkPoolImpl(Factory<C> factory) {
        this.factory = factory;
    }
    
    int numAllocs = 0;
    public synchronized C allocChunk(ChunkPosition pos) {
        C chunk;
        if(free.isEmpty()) {
            chunk = factory.create();
            chunks.add(chunk);
        } else {
            chunk = chunks.get(free.remove((int)free.size() - 1));
        }
        chunk.reset(pos);
        return chunk;
    }
    
    public synchronized void freeChunk(C chunk) {
        free.add(chunks.indexOf(chunk));
    }
    
    public synchronized void compact() {
        System.out.println("ChunkPool" + ", used: " + (chunks.size() - free.size())+ ", free: " + free.size());
        Collections.sort(free);
        System.out.println("dealloc unused chunks...");
        int last = chunks.size() - 1;
        for (int i = free.size() - 1; i >= 0; i--) {
            int f = free.get(i);
            if(f != last) {
                chunks.set(f, chunks.remove(last));
            } else {
                chunks.remove(last);
            }
            last--;
        }
        free.clear();
        System.out.println("ChunkPool" + ", used: " + (chunks.size() - free.size())+ ", free: " + free.size());
    }
}
