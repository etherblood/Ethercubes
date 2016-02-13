/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.chunk.compression;

import ethercubes.statistics.TimeStatistics;
import java.io.IOException;
import java.util.zip.DataFormatException;
import ethercubes.Util;
import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.DataXZY;
import ethercubes.data.ChunkPosition;
import ethercubes.chunk.Versioned;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Philipp
 */
public class CompressedChunks<Chunk extends Versioned & DataXZY & ChunkReadonly> {
    private final ConcurrentHashMap<ChunkPosition, CompressedChunk> chunks = new ConcurrentHashMap<ChunkPosition, CompressedChunk>();
//    private final ByteBuffer buffer = ByteBuffer.allocateDirect(10000);
//    private int maxBuffer = 0;
    
    
//    private DataInputStream dataIn = new DataInputStream(null);
    private AtomicInteger approxSize = new AtomicInteger(0);

    public void save(Chunk chunk) {
        ChunkPosition pos = chunk.getPosition();
        CompressedChunk c = chunks.get(pos);
        if(c == null) {
            c = new CompressedChunk(pos);
            chunks.put(pos, c);
            c.setVersion(~chunk.getVersion());
        }
        if(c.getVersion() != chunk.getVersion()) {
            if(c.getData() != null) {
                approxSize.addAndGet(-c.getData().length);
            }
            long start = TimeStatistics.TIME_STATISTICS.start();
//            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//            DataOutputStream dataOut = new DataOutputStream(byteOut);
//            compressor.compress(dataOut, chunk.getDataXZY());
            try {
                c.setData(DeflaterWrapper.compress(chunk.getDataXZY()));
//                c.setData(DeflaterWrapper.compress(byteOut.toByteArray()));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            c.setVersion(chunk.getVersion());
            TimeStatistics.TIME_STATISTICS.end(start, "save");
                approxSize.addAndGet(-c.getData().length);
            if(chunks.size() % 1000 == 0) {
                System.out.println(Util.humanReadableByteCount(approxSize.get()) + ", " + Util.humanReadableByteCount(approxSize.get() / chunks.size()) + "/chunk");
            }
        }
    }
    public boolean tryLoad(Chunk chunk) {
        CompressedChunk c = chunks.get(chunk.getPosition());
        if(c != null) {
            long start = TimeStatistics.TIME_STATISTICS.start();
            try {
//                byteIn = new ByteArrayInputStream(DeflaterWrapper.decompress(c.getData()));
                DeflaterWrapper.decompress(c.getData(), chunk.getDataXZY());
            } catch (IOException | DataFormatException ex) {
                throw new RuntimeException(ex);
            }
//            DataInputStream dataIn = new DataInputStream(byteIn);
//            compressor.decompress(dataIn, chunk.getDataXZY());
            chunk.setVersion(c.getVersion());
            TimeStatistics.TIME_STATISTICS.end(start, "load");
            return true;
        }
        return false;
    }
}
