/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.threading.scheduling;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import ethercubes.ChunkFactory;
import ethercubes.data.ChunkPosition;
import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.Versioned;

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
