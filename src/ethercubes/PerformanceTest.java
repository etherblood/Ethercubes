package ethercubes;

import ethercubes.chunk.implementation.ArrayChunk;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.data.Direction;
import ethercubes.display.meshing.implementation.GreedyMesher;
import ethercubes.settings.implementation.ChunkSettingsImpl;
import ethercubes.settings.implementation.TileBlockSettings;
import ethercubes.statistics.TimeStatistics;
import ethercubes.world.worldgen.OreGenerator;
import ethercubes.world.worldgen.TerrainGenerator;
import ethercubes.world.worldgen.TreeGenerator;
import ethercubes.world.worldgen.WorldGeneratorImpl;

/**
 *
 * @author Philipp
 */
public class PerformanceTest {

    public static void main(String[] args) {
        long seed = 0xDEADBEEF;
        TileBlockSettings blockSettings = new TileBlockSettings();
        ChunkSettingsImpl chunkSettings = new ChunkSettingsImpl(new ChunkSize(32, 32, 32));
        
        ArrayChunk chunk = new ArrayChunk(chunkSettings.getSize());
        for (Direction dir : Direction.values()) {
            chunk.setNeighbor(dir, chunk);
        }
        
        ChunkFactory<ArrayChunk> gen = new TerrainGenerator<ArrayChunk>(seed, chunkSettings);
        WorldGeneratorImpl worldGen = new WorldGeneratorImpl(gen, new TreeGenerator(seed), chunkSettings.getSize(), new OreGenerator(seed));
        
        
        GreedyMesher mesher = new GreedyMesher(blockSettings, chunkSettings.getSize());
        
        System.out.println("warmup...");
        for (int i = 0; i < 1000; i++) {
            chunk.reset(new ChunkPosition(i, i % 5 - 2, 0));
            worldGen.firstPass(chunk);
            worldGen.secondPass(chunk);
            mesher.generateMesh(chunk, 5);
        }
        System.out.println(TimeStatistics.TIME_STATISTICS.displayString());
        TimeStatistics.TIME_STATISTICS.clear();
        System.out.println("start");
        
        long millis = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            chunk.reset(new ChunkPosition(i, i % 5 - 2, 0));
            worldGen.firstPass(chunk);
            worldGen.secondPass(chunk);
            mesher.generateMesh(chunk, 5);
        }
        System.out.println("");
        System.out.println(TimeStatistics.TIME_STATISTICS.displayString());
        System.out.println(System.currentTimeMillis() - millis + "ms");
    }
}
