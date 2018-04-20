/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.threading.scheduling;

import com.etherblood.ethercubes.chunk.BlockChunkReadonly;
import com.etherblood.ethercubes.chunk.Versioned;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.display.meshing.ChunkMesher;
import com.etherblood.ethercubes.display.meshing.ChunkMeshingResult;
import com.etherblood.ethercubes.display.meshing.ChunkNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 *
 * @author Philipp
 */
public class MeshTask<C extends Versioned & BlockChunkReadonly> extends PriorityTask<C> {
    private final Map<ChunkPosition, ReadWriteLock> locks;
    private final ChunkNode node;
    private final ChunkMesher mesher;
    private ChunkMeshingResult result;

    public MeshTask(Map<ChunkPosition, ReadWriteLock> locks, ChunkNode node, ChunkMesher mesher, C chunk) {
        super(chunk);
        this.locks = locks;
        this.node = node;
        this.mesher = mesher;
    }

    public boolean tryFinish() {
        if(result == null) {
            return false;
        }
        if(result.getVersion() > node.getVersion()) {
            node.setOpaque(result.getOpaque());
            node.setTransparent(result.getTransparent());
            node.setVersion(result.getVersion());
        }
        return true;
    }

    public void run() {
        List<ChunkPosition> positions = new ArrayList<ChunkPosition>();
        positions.add(getChunk().getPosition());
        for (Direction direction: Direction.values()) {
            positions.add(direction.neighbor(getChunk().getPosition()));
        }
        for (ChunkPosition pos : positions) {
            ReadWriteLock l = locks.get(pos);
            if(l != null) {
                l.readLock().lock();
            }
        }
        try {
            result = mesher.generateMesh(getChunk(), getChunk().getVersion());
        } finally {
            for (ChunkPosition pos : positions) {
                ReadWriteLock l = locks.get(pos);
                if(l != null) {
                    l.readLock().unlock();
                }
            }
        }
    }
    
}
