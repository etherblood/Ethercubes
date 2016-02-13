/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen;

import ethercubes.ChunkFactory;
import ethercubes.data.ChunkPosition;
import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.HasNeighbors;
import ethercubes.data.ChunkSize;
import ethercubes.settings.implementation.TileBlockSettings;


public class WorldGeneratorImpl<C extends BlockChunk& FastXZYChunk & HasNeighbors<C>> implements WorldGenerator<C> {
    private final ChunkFactory<BlockChunk> factory;
    private final TreeGenerator treeGen;
    private final OreGenerator oreGen;
    private final GrassGenerator grassGen = new GrassGenerator(TileBlockSettings.GRASS, TileBlockSettings.DIRT, TileBlockSettings.AIR);
    private final SandGenerator sandGen;

    public WorldGeneratorImpl(ChunkFactory<BlockChunk> factory, TreeGenerator treeGen, ChunkSize chunkSize, OreGenerator oreGen) {
        this.factory = factory;
        this.treeGen = treeGen;
        sandGen = new SandGenerator(chunkSize);
        this.oreGen = oreGen;
    }

    @Override
    public void firstPass(C chunk) {
        factory.populate(chunk);
    }

    @Override
    public void secondPass(C chunk) {
        oreGen.spawnOres(chunk);
        treeGen.spawnTrees(chunk);
//        if(chunk.getPosition().getY() < 0) {
//            sandGen.generateSand(chunk);
//        }
        grassGen.generateGrass(chunk);
    }
    
}
