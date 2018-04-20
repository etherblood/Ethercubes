package ethercubes.world.worldgen;

import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.data.Direction;
import ethercubes.statistics.TimeStatistics;

/**
 *
 * @author Philipp
 */
public class GrassGenerator<C extends FastXZYChunk & HasNeighbors<C> & ChunkReadonly> {

    private final byte grass, dirt, air;

    public GrassGenerator(byte grass, byte dirt, byte air) {
        this.grass = grass;
        this.dirt = dirt;
        this.air = air;
    }
    
    public void generateGrass(C chunk) {
        long start = TimeStatistics.TIME_STATISTICS.start();
        int limit = chunk.getSize().getY() - 1;
        int sizeXZ = chunk.getSize().getX() * chunk.getSize().getZ();
        limit *= sizeXZ;
        int index;
        for (index = 0; index < limit; index++) {
            if (chunk.getBlockFast(index) == dirt && chunk.getBlockFast(index + sizeXZ) == air) {
                chunk.setBlockFast(index, grass);
            }
        }

        C neighbor = chunk.getNeighbor(Direction.UP);
        if (neighbor != null) {
            for (int xz = 0; xz < sizeXZ; xz++) {
                if (chunk.getBlockFast(index) == dirt && neighbor.getBlockFast(xz) == air) {
                    chunk.setBlockFast(index, grass);
                }
                index++;
            }
        }
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
    }
}
