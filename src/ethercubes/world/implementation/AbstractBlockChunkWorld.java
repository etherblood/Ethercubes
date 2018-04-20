/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.implementation;

import ethercubes.chunk.BlockChunk;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.settings.implementation.ChunkSettingsImpl;
import ethercubes.world.BlockWorld;
import ethercubes.world.ChunkWorld;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Philipp
 */
public abstract class AbstractBlockChunkWorld<C extends BlockChunk> implements BlockWorld, ChunkWorld<C> {
    private final ConcurrentHashMap<ChunkPosition, C> chunks = new ConcurrentHashMap<ChunkPosition, C>();
    private final ChunkSize chunkSize;
    private final ChunkSettingsImpl converter;

    public AbstractBlockChunkWorld(ChunkSize chunkSize) {
        this.chunkSize = chunkSize;
        converter = new ChunkSettingsImpl(chunkSize);
    }
    
    @Override
    public final C getChunk(ChunkPosition chunkPosition) {
        return chunks.get(chunkPosition);
    }
    @Override
    public void addChunk(C chunk) {
        ChunkPosition pos = chunk.getPosition();
        C tmp = chunks.put(pos, chunk);
        if(tmp != null) {
            chunks.put(pos, tmp);//undo
            throw new RuntimeException("Could not create chunk because its position was already used.");
        }
    }
    @Override
    public C removeChunk(ChunkPosition pos) {
        return chunks.remove(pos);
    }
//    public void setChunk(ChunkPosition chunkPosition, C chunk) {
//        removeChunk(chunkPosition);
//        addChunk(chunkPosition, chunk);
//    }
//    public void removeChunk(ChunkPosition chunkPosition) {
//        chunks.remove(chunkPosition);
//    }
//    public void addChunk(ChunkPosition chunkPosition, C chunk) {
//        if(!chunkSize.equals(chunk.getSize())) {
//            throw new RuntimeException("tried adding a chunk with invalid chunksize");
//        }
//        chunks.put(chunkPosition, chunk);
//    }
    
    @Override
    public final byte getBlock(GlobalBlockPosition blockPosition) {
        C chunk = getChunk(converter.getContainerPosition(blockPosition));
        if(chunk != null) {
            return chunk.getBlock(converter.getLocalPosition(blockPosition));
        }
        return getDefaultBlock(blockPosition);
    }
    
    @Override
    public final void setBlock(GlobalBlockPosition blockPosition, byte value) {
        ChunkPosition chunkPos = converter.getContainerPosition(blockPosition);
        C chunk = chunks.get(chunkPos);
        if(chunk == null) {
            throw new RuntimeException("cannot set block in uninitialized chunk: " + chunkPos);
        }
        chunk.setBlock(converter.getLocalPosition(blockPosition), value);
    }
    
    public final ChunkSize getChunkSize() {
        return chunkSize;
    }

    public ChunkSettingsImpl getConverter() {
        return converter;
    }
    
    protected byte getDefaultBlock(GlobalBlockPosition blockPosition) {
        return 0;
    }

    public ConcurrentHashMap<ChunkPosition, C> getChunks() {
        return chunks;
    }
}
