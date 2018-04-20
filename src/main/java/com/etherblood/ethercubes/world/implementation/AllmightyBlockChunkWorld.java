/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world.implementation;

import com.etherblood.ethercubes.chunk.BlockChunk;
import com.etherblood.ethercubes.chunk.HasNeighbors;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.data.GlobalBlockPosition;
import com.etherblood.ethercubes.data.PositionLimit;

/**
 *
 * @author Philipp
 */
public class AllmightyBlockChunkWorld<C extends BlockChunk & HasNeighbors<C>> extends AbstractBlockChunkWorld<C> implements PositionLimit {
    private final PositionLimit[] limits;

    public AllmightyBlockChunkWorld(ChunkSize chunkSize, PositionLimit... limits) {
        super(chunkSize);
        this.limits = limits;
    }
    
    @Override
    public void addChunk(C chunk) {
        ChunkPosition pos = chunk.getPosition();
        if(!isLegal(pos)) {
            throw new RuntimeException("Chunk at " + pos + " cannot be initilaize because the position is illegal.");
        }
        super.addChunk(chunk);
        
        for (Direction direction : Direction.values()) {
            ChunkPosition neighborPos = direction.neighbor(pos);
            C neighbor = getChunk(neighborPos);
            if(neighbor != null) {
                chunk.setNeighbor(direction, neighbor);
                neighbor.setNeighbor(direction.inverse(), chunk);
            }
        }
    }
    
    @Override
    public boolean isLegal(GlobalBlockPosition pos) {
        return isLegal(getConverter().getContainerPosition(pos));
    }
    @Override
    public boolean isLegal(ChunkPosition pos) {
        for (PositionLimit limit : limits) {
            if(!limit.isLegal(pos)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public C removeChunk(ChunkPosition pos) {
        C chunk = getChunk(pos);
        
        if(chunk == null) {
            return null;
//            if(!isLegalChunkPosition(pos)) {
//                throw new RuntimeException("cannot delete chunks at invalid positions.");
//            }
//            throw new RuntimeException("cannot delete non existant chunk.");
        }
        
        for (Direction direction : Direction.values()) {
            C neighbor = chunk.getNeighbor(direction);
            if(neighbor != null) {
                chunk.setNeighbor(direction, null);
                neighbor.setNeighbor(direction.inverse(), null);
            }
        }
        
        return super.removeChunk(pos);
    }
    
}
