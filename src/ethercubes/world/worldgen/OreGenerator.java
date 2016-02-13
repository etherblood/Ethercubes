package ethercubes.world.worldgen;

import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.statistics.TimeStatistics;
import java.util.Random;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.settings.implementation.TileBlockSettings;
import ethercubes.world.worldgen.templates.OreTemplate;

/**
 *
 * @author Philipp
 */
public class OreGenerator<C extends HasNeighbors<C> & BlockChunk> {
    private final long seed;
    private final StructureTemplate[] ores;
    private final SimplexNoise_octave oreDensity;

    public OreGenerator(long seed) {
        this.ores = new StructureTemplate[]{
            new OreTemplate(TileBlockSettings.GOLD_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.IRON_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.IRON_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.IRON_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE),
            new OreTemplate(TileBlockSettings.COAL_ORE, TileBlockSettings.STONE)
        };
        this.seed = seed;
        Random rng = new Random(seed);
        oreDensity = new SimplexNoise_octave(~rng.nextInt());
    }
    
    public void spawnOres(C chunk) {
        ChunkPosition chunkPosition = chunk.getPosition();
        ChunkSize chunkSize = chunk.getSize();
        if(chunkPosition.getY() < -1 || chunkPosition.getY() > 1) {
            TimeStatistics.TIME_STATISTICS.end(TimeStatistics.TIME_STATISTICS.start(), getClass().getSimpleName());
            return;
        }
        long start = TimeStatistics.TIME_STATISTICS.start();
        Random rng = new Random(seed ^ chunkPosition.hashCode());
        
        double oreNoise = noise(oreDensity, 0.01, chunkPosition.getX(), chunkPosition.getY(), chunkPosition.getZ());
        int numOres = (int)(oreNoise * chunkSize.getX() * chunkSize.getY() * chunkSize.getZ() / 64);
        if(numOres < 0) {
            numOres /= -4;
        }
        for (int i = 0; i < numOres; i++) {
            int x = rng.nextInt(chunkSize.getX());
            int z = rng.nextInt(chunkSize.getZ());
            int y = rng.nextInt(chunkSize.getY());
            StructureTemplate ore = ores[rng.nextInt(ores.length)];
            if(ore.isSpawnValid(chunk, x, y, z)) {
                ore.spawn(chunk, x, y, z);
            }
        }
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
    }
    
    private double noise(SimplexNoise_octave gen, double density, int x, int y, int z) {
        return gen.noise(density * x, density * y, density * z);
    }
}
