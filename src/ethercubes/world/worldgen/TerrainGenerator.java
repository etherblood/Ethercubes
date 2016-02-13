/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen;

import java.util.Random;
import ethercubes.ChunkFactory;
import ethercubes.statistics.TimeStatistics;
import ethercubes.world.implementation.AbstractBlockChunkWorld;
import ethercubes.data.ChunkSize;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.data.LocalBlockPosition;
import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.listutil.ByteArrayList;
import ethercubes.listutil.IntArrayList;
import ethercubes.settings.ChunkSettings;
import ethercubes.settings.implementation.TestBlockSettings;
import java.util.ArrayList;

/**
 *
 * @author Philipp
 */
public class TerrainGenerator<C extends BlockChunk & FastXZYChunk> implements ChunkFactory<C> {
    private final long seed;
    private final ChunkSize chunkSize;
//    private final AbstractBlockChunkWorld<C> world;
    private final ChunkSettings<C> settings;
    private final SimplexNoise_octave height_factor;
    private final SimplexNoise_octave density_detailed;
    private final SimplexNoise_octave density_rough;
    private final SimplexNoise_octave density_weight;
    private final SimplexNoise_octave cave1;
    private final SimplexNoise_octave cave2;
    private final float[] values = new float[5*5*5];
    private final float[] noiseCache;
    private final double small = 0.1;
    private final double medium = 0.01;
    private final double big = 0.001;
    private final double huge = 0.0001;
    
    private final double height = 100;
    private final int waterLevel = -15;

    public TerrainGenerator(long seed, ChunkSettings<C> settings) {
        this.seed = seed;
        this.settings = settings;
        chunkSize = settings.getSize();
        Random rng = new Random(seed);
        height_factor = new SimplexNoise_octave(rng.nextInt());
        density_detailed = new SimplexNoise_octave(rng.nextInt());
        density_rough = new SimplexNoise_octave(rng.nextInt());
        density_weight = new SimplexNoise_octave(rng.nextInt());
        cave1 = new SimplexNoise_octave(rng.nextInt());
        cave2 = new SimplexNoise_octave(rng.nextInt());
        if(((chunkSize.getX() | chunkSize.getY() | chunkSize.getZ()) & 3) != 0) {
            throw new RuntimeException("size must be a multiple of 4x4x4");
        }
        noiseCache = new float[(chunkSize.getX() / 4 + 1) * (chunkSize.getY() / 4 + 1) * (chunkSize.getZ() / 4 + 1)];
    }

    @Override
    public void populate(C chunk) {
        long start = TimeStatistics.TIME_STATISTICS.start();
        ChunkSize size = chunk.getSize();
        if(!size.equals(chunkSize)) {
            throw new RuntimeException("invalid chunksize");
        }
        fillNoiseCache(settings.getGlobalPosition(chunk.getPosition(), LocalBlockPosition.ZERO));
        if(true) {
            doPopulate(chunk);
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
            return;
        }
//        int sizeX = chunkSize.getX() / 4 + 1;
//        int sizeZ = chunkSize.getZ() / 4 + 1;
//        int sizeXZ = sizeX * sizeZ;
//        int globalY = chunk.getPosition().getY() * chunkSize.getY();
//        byte baseBlock = blockType(noiseCache[0], globalY);
//        boolean allEqual = true;
//        for (int i = 1; i < noiseCache.length; i++) {
//            allEqual = blockType(noiseCache[i], globalY + (i / sizeXZ) * 4) == baseBlock;
//            if(!allEqual) {
//                break;
//            }
//        }
//        if(allEqual) {
//            chunk.fill(baseBlock);
//        } else {
            for (int y = 0; y < size.getY(); y += 4) {
                for (int x = 0; x < size.getX(); x += 4) {
                    for (int z = 0; z < size.getZ(); z += 4) {
                        populate4x4x4(chunk, x, y, z);
                    }
                }
            }
//        }
        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
    }
    
    private void doPopulate(C chunk) {
        int globalY = chunk.getPosition().getY() * chunkSize.getY();
        int sizeX = chunkSize.getX() / 4 + 1;
        int sizeY = chunkSize.getY() / 4 + 1;
        int sizeZ = chunkSize.getZ() / 4 + 1;
        int sizeXZ = sizeX * sizeZ;
        
        IntArrayList layerBlocks = new IntArrayList();
        
        for (int y = 0; y < sizeY; y++) {
            int block = blockType(noiseCache[y * sizeXZ], y * 4 + globalY);
            boolean equal = true;
            for (int i = 0; equal && i < sizeXZ; i++) {
                if(blockType(noiseCache[i + y * sizeXZ], globalY + (i / sizeXZ) * 4) != block) {
                    equal = false;
                }
            }
            layerBlocks.add(equal? block: Integer.MIN_VALUE);
            
//            if(equal) {
//                if(start == -1) {
//                    start = y;
//                    startBlock = block;
//                }
//            } else {
//                if(start != -1) {
//                    chunk.setLayersBlocks(start, y - 1, startBlock);
//                    start = -1;
//                }
//                populateLayer(chunk, y - 1);
//            }
        }
        
        IntArrayList next = new IntArrayList();
        for (int i = 1; i < layerBlocks.size(); i++) {
            int block = layerBlocks.get(i - 1);
            int block2 = layerBlocks.get(i);
            
            if(block == Integer.MIN_VALUE || block2 == Integer.MIN_VALUE || block != block2) {
                next.add(Integer.MIN_VALUE);
            } else {
                next.add(block);
            }
        }
        
        for (int i = 0; i < next.size();) {
            int block = next.get(i);
            if(block == Integer.MIN_VALUE) {
                populateLayer(chunk, i);
                i++;
            } else {
                int j;
                for (j = i + 1; j < next.size() && next.get(j) != Integer.MIN_VALUE && block == next.get(j); j++) {
                }
                chunk.setLayersBlocks(i * 4, j * 4, (byte)block);
                i = j;
            }
        }
        
//        int start = -1;
//        for (int i = 0; i < next.size(); i++) {
//            if(next.get(i) == null) {
//                if(start != -1) {
//                    int end = i;
//                    if(start < end) {
//                        chunk.setLayersBlocks(start * 4, end * 4, next.get(start));
//                    }
//                populateLayer(chunk, i - 1);
//                    start = -1;
//                }
//                populateLayer(chunk, i);
//            } else {
//                if(start == -1) {
//                    start = i;
//                } else {
//                    if(next.get(i).byteValue() != next.get(start).byteValue()) {
//                        int end = i;
//                        if(start < end) {
//                            chunk.setLayersBlocks(start * 4, end * 4, next.get(start));
//                        }
//                populateLayer(chunk, i - 1);
//                        start = i;
//                    }
//                }
//            }
//        }
//        if(start != -1) {
//            chunk.setLayersBlocks(start * 4, next.size() * 4, next.get(start));
//        }
    }
    
    private void populateLayer(C chunk, int y) {
        ChunkSize size = chunk.getSize();
        y *= 4;
        for (int x = 0; x < size.getX(); x += 4) {
            for (int z = 0; z < size.getZ(); z += 4) {
                populate4x4x4(chunk, x, y, z);
            }
        }
    }
    
    private void fillNoiseCache(GlobalBlockPosition global) {
        int sizeX = chunkSize.getX() / 4 + 1;
        int sizeY = chunkSize.getY() / 4 + 1;
        int sizeZ = chunkSize.getZ() / 4 + 1;
        
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    noiseCache[x + sizeX * (z + sizeZ * y)] = (float)density(global.getX() + 4 * x, global.getY() + 4 * y, global.getZ() + 4 * z);
                }
            }
        }
    }
    private float fromNoiseCache(LocalBlockPosition pos) {
        return fromNoiseCache(pos.getX(), pos.getY(), pos.getZ());
    }
    private float fromNoiseCache(int x, int y, int z) {
        int sizeX = chunkSize.getX() / 4 + 1;
        int sizeZ = chunkSize.getZ() / 4 + 1;
        x/=4;
        y/=4;
        z/=4;
        return noiseCache[x + sizeX * (z + sizeZ * y)];
    }
    
    private void populate4x4x4(C chunk, int x, int y, int z) {
//        values[0] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x, y, z)));
//        values[4] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x + 4, y, z)));
//        values[20] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x, y + 4, z)));
//        values[24] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x + 4, y + 4, z)));
//        
//        values[100] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x, y, z + 4)));
//        values[104] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x + 4, y, z + 4)));
//        values[120] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x, y + 4, z + 4)));
//        values[124] = (float)density(world.getConverter().getGlobalPosition(chunk.getPosition(), new LocalBlockPosition(x + 4, y + 4, z + 4)));
        
        values[0] = fromNoiseCache(new LocalBlockPosition(x, y, z));
        values[4] = fromNoiseCache(new LocalBlockPosition(x + 4, y, z));
        values[20] = fromNoiseCache(new LocalBlockPosition(x, y + 4, z));
        values[24] = fromNoiseCache(new LocalBlockPosition(x + 4, y + 4, z));
        
        values[100] = fromNoiseCache(new LocalBlockPosition(x, y, z + 4));
        values[104] = fromNoiseCache(new LocalBlockPosition(x + 4, y, z + 4));
        values[120] = fromNoiseCache(new LocalBlockPosition(x, y + 4, z + 4));
        values[124] = fromNoiseCache(new LocalBlockPosition(x + 4, y + 4, z + 4));
        
        interpolateFromCorners5x5x5(values);
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    int arrIndex = i + 5*j + 25*k;
                    LocalBlockPosition pos = new LocalBlockPosition(x + i, y + j, z + k);
                    GlobalBlockPosition global = settings.getGlobalPosition(chunk.getPosition(), pos);
                    byte block = blockType(values[arrIndex], global.getY());
                    chunk.setBlock(pos, block);
                }
            }
        }
    }
    public byte blockType(float density, int y) {
        if(density <= 0) {
            if(y <= waterLevel) {
                return TestBlockSettings.WATER;
            }
            return TestBlockSettings.AIR;
        }
        if(density > 0.5) {
            return TestBlockSettings.STONE;
        }
        if(y <= waterLevel + 1 && density <= 0.11) {
            return TestBlockSettings.SAND;
//        } else if (y > 50) {
//            return provider.getSnow();
        }
        return TestBlockSettings.DIRT;
    }
    
//    private void adjustSurfaceDirt(C chunk) {
//        for (int i = 0; i < chunk.getSize().getX(); i++) {
//            for (int j = 0; j + 1 < chunk.getSize().getY(); j++) {
//                for (int k = 0; k < chunk.getSize().getZ(); k++) {
//                    int a = i + chunk.getSize().getX() * (k + chunk.getSize().getZ() * j);
//                    if(a == provider.getDirt()) {
//                        int b = a + chunk.getSize().getX() * chunk.getSize().getZ();
//                        if(b == provider.getAir()) {
//                            chunk.setBlock(i, j, k, a);
//                        }
//                    }
//                }
//            }
//        }
//    }
    
    private double density(GlobalBlockPosition global) {
        return density(global.getX(), global.getY(), global.getZ());
    }
    private double density(int x, int y, int z) {
        double rough = noise(density_rough, medium, x, y, z);
        double detail = noise(density_detailed, small / 3, x, y, z);
        double weight = noise(density_weight, big, x, y, z);
        weight *= weight;
        
        double heightFactor = noise(height_factor, big, x, y, z);
        heightFactor *= heightFactor;
        double block = interpolate(detail, rough, weight);
        double heightW = (double)y / (height * (heightFactor + 0.1));
        
        return block - heightW;
//        double c1 = to_0_1(noise(cave1, small / 3, x, y, z));// - heightW;
//        double c2 = to_0_1(noise(cave2, small / 4, x, y, z));// - heightW;
//        c1 *= c1;
//        c2 *= c2;
//        c1 = from_0_1(c1 * c2);
//        return interpolate(block - heightW, c1, clamp0_1(heightW + 1.3));
    }
    
    private double clamp0_1(double value) {
        return value < 0? 0: value > 1? 1: value;
    }
    
    private void interpolateFromCorners5x5x5(float[] values) {
        interpolate(values, 0, 1);
        interpolate(values, 20, 1);
        interpolate(values, 100, 1);
        interpolate(values, 120, 1);
        for (int i = 0; i < 5; i++) {
            interpolate(values, i, 5);
            interpolate(values, 100 + i, 5);
        }
        for (int i = 0; i < 25; i++) {
            interpolate(values, i, 25);
        }
    }
    private void interpolate(float[] values, int startIndex, int stepSize) {
        int endIndex = startIndex + 4 * stepSize;
        float value = values[startIndex];
        float delta = (values[endIndex] - value) * 0.25f;
        for (int i = startIndex + stepSize; i < endIndex; i += stepSize) {
            value += delta;
            values[i] = value;
        }
    }
    
    private double interpolate(double a, double b, double weightA) {
        return a * weightA + b * (1 - weightA);
    }
    
    private double noise(SimplexNoise_octave gen, double density, int x, int y, int z) {
        return gen.noise(density * x, density * y, density * z);
    }
    private double noise(SimplexNoise_octave gen, double density, int x, int z) {
        return gen.noise(density * x, density * z);
    }
    
    private double to_0_1(double value) {
        return value * 0.5d + 1;
    }
    private double from_0_1(double value) {
        return value * 2 - 1;
    }

    public ChunkSize getChunkSize() {
        return chunkSize;
    }
}
