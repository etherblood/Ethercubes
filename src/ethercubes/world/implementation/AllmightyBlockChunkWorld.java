/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.implementation;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.data.PositionLimit;
import ethercubes.data.Direction;

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
    
    public boolean isLegal(GlobalBlockPosition pos) {
        return isLegal(getConverter().getContainerPosition(pos));
    }
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
