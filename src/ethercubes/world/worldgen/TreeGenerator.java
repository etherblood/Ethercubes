package ethercubes.world.worldgen;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.settings.implementation.TileBlockSettings;
import ethercubes.statistics.TimeStatistics;
import ethercubes.world.worldgen.templates.TreeTemplate1;
import java.util.Random;

/**
 *
 * @author Philipp
 */
public class TreeGenerator<C extends HasNeighbors<C> & BlockChunk> {
    private final long seed;
    private StructureTemplate tree;
    private final SimplexNoise_octave treeDensity;

    public TreeGenerator(long seed) {
        tree = new TreeTemplate1(TileBlockSettings.WOOD, TileBlockSettings.LEAFES, TileBlockSettings.DIRT, TileBlockSettings.AIR);
        this.seed = seed;
        Random rng = new Random(seed);
        treeDensity = new SimplexNoise_octave(~rng.nextInt());
    }
    
    public void spawnTrees(C chunk) {
        ChunkSize chunkSize = chunk.getSize();
        ChunkPosition chunkPosition = chunk.getPosition();
        long start = TimeStatistics.TIME_STATISTICS.start();
        Random rng = new Random(seed ^ chunkPosition.hashCode());
        
        double treeNoise = noise(treeDensity, 0.01, chunkPosition.getX(), chunkPosition.getY(), chunkPosition.getZ());
        int numTrees = (int)(treeNoise * chunkSize.getX() * chunkSize.getZ() / 16);
        if(numTrees < 0) {
            numTrees /= -4;
        }
        for (int i = 0; i < numTrees; i++) {
            int x = rng.nextInt(chunkSize.getX());
            int z = rng.nextInt(chunkSize.getZ());
            int yBase = 0;
            for (int y = chunkSize.getY() - 1; y >= 0; y--) {
                if(tree.isSpawnValid(chunk, x, y + yBase, z)) {
                    tree.spawn(chunk, x, y + yBase, z);
                }
            }
        }
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
    }
    
    private double noise(SimplexNoise_octave gen, double density, int x, int y, int z) {
        return gen.noise(density * x, density * y, density * z);
    }
}
