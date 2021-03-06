/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.threading.scheduling;

import com.etherblood.ethercubes.ChunkFactory;
import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.chunk.Versioned;
import com.etherblood.ethercubes.data.ChunkPosition;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 * @author Philipp
 */
public class PopulateTask<C extends Versioned & BlockChunk> extends PriorityTask<C> {
    private final Map<ChunkPosition, ReadWriteLock> locks;
    private ChunkFactory<C> factory;
    private boolean finished = false;

    public PopulateTask(Map<ChunkPosition, ReadWriteLock> locks, ChunkFactory<C> factory, C chunk) {
        super(chunk);
        this.factory = factory;
        this.locks = locks;
    }

    @Override
    public int getPriority() {
        return super.getPriority() + 1;
    }

    public boolean tryFinish() {
        return finished;
    }

    public void run() {
        locks.get(getChunk().getPosition()).writeLock().lock();
        try {
            factory.populate(getChunk());
        } finally {
            locks.get(getChunk().getPosition()).writeLock().unlock();
            getChunk().setVersion(getChunk().getVersion() + 1);
            finished = true;
        }
    }
    
}
