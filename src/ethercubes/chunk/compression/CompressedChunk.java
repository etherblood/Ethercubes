package ethercubes.chunk.compression;

import ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public class CompressedChunk {
    private final ChunkPosition pos;
    private byte[] data;
    private int version;

    public CompressedChunk(ChunkPosition pos) {
        this.pos = pos;
    }

    public ChunkPosition getPos() {
        return pos;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
