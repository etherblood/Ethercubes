/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.threading.scheduling;

import ethercubes.chunk.BlockChunkReadonly;
import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public abstract class PriorityTask<C extends BlockChunkReadonly> implements MyTask {
    private int priority;
    private final C chunk;

    public PriorityTask(C chunk) {
        this.chunk = chunk;
    }

    public C getChunk() {
        return chunk;
    }

    public int getPriority() {
        return priority;
    }
    
    public void adjustPriority(ChunkPosition player) {
        int x = Math.abs(player.getX() - chunk.getPosition().getX());
        int y = Math.abs(player.getY() - chunk.getPosition().getY());
        int z = Math.abs(player.getZ() - chunk.getPosition().getZ());
        priority = -(x + y + z);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + chunk.getPosition().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof PriorityTask)) {
            return false;
        }
        final PriorityTask other = (PriorityTask) obj;
        return chunk.getPosition().equals(other.chunk.getPosition());
    }
}
