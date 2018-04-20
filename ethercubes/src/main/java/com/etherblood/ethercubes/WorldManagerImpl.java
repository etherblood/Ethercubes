/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

import com.jme3.app.Application;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Node;
import com.etherblood.ethercubes.chunk.Factory;
import com.etherblood.ethercubes.chunk.compression.CompressedChunks;
import com.etherblood.ethercubes.chunk.implementation.ArrayChunk;
import com.etherblood.ethercubes.chunk.implementation.ChunkPoolImpl;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.data.GlobalBlockPosition;
import com.etherblood.ethercubes.data.LocalBlockPosition;
import com.etherblood.ethercubes.data.NeighborVisibilityCalculatorImpl;
import com.etherblood.ethercubes.display.connectivity.ConnectivityGraph;
import com.etherblood.ethercubes.display.meshing.ChunkMesher;
import com.etherblood.ethercubes.display.meshing.ChunkMeshingResult;
import com.etherblood.ethercubes.display.meshing.ChunkNode;
import com.etherblood.ethercubes.settings.implementation.TestBlockSettings;
import com.etherblood.ethercubes.statistics.TimeStatistics;
import com.etherblood.ethercubes.world.implementation.AllmightyBlockChunkWorld;
import com.etherblood.ethercubes.world.worldgen.WorldGenerator;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 *
 * @author Philipp
 */
public class WorldManagerImpl implements WorldManager, WorldWrapper {
    private final Application app;
    private final ChunkPoolImpl<ArrayChunk> chunkPool;
    private final WorldGenerator worldGenerator;
    private final AllmightyBlockChunkWorld<ArrayChunk> worldData;
    private final Node rootNode = new Node("worldRoot");
    private final HashMap<ChunkPosition, ChunkNode> chunkNodes = new HashMap<ChunkPosition, ChunkNode>();
    private final CompressedChunks<ArrayChunk> compressor;
    private final Material material;
    private final ChunkMesher<ArrayChunk> mesher;
    private final NeighborVisibilityCalculatorImpl<ArrayChunk> neighborVisibilityCalculator;
    public ConnectivityGraph<ArrayChunk> graph = new ConnectivityGraph<ArrayChunk>();
//    private final ExecutorService executor;
    
    private final HashMap<ChunkPosition, Integer> neighborCounterV1 = new HashMap<ChunkPosition, Integer>();
    private final HashMap<ChunkPosition, Integer> neighborCounterV2 = new HashMap<ChunkPosition, Integer>();

    public WorldManagerImpl(Application app, AssetManager assets, WorldGenerator worldGenerator, AllmightyBlockChunkWorld<ArrayChunk> worldData, CompressedChunks<ArrayChunk> compressor, Material material, ChunkMesher<ArrayChunk> mesher, TestBlockSettings blockSettings, Factory<ArrayChunk> chunkFactory, ExecutorService executor) {
        this.app = app;
        this.worldGenerator = worldGenerator;
        this.worldData = worldData;
        this.compressor = compressor;
        this.material = material;
        this.mesher = mesher;
//        this.executor = executor;
        neighborVisibilityCalculator = new NeighborVisibilityCalculatorImpl<ArrayChunk>(blockSettings, worldData.getChunkSize());
        chunkPool = new ChunkPoolImpl<ArrayChunk>(chunkFactory);
//        rootNode.attachChild(graph.node);
//        graph.mat = new Material(assets, "Common/MatDefs/Misc/Unshaded.j3md");
//        graph.mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
    }

    @Override
    public void setBlock(GlobalBlockPosition pos, byte value) {
        ChunkPosition chunkPos = worldData.getConverter().getContainerPosition(pos);
        if(!worldData.isLegal(pos)) {
            return;
        }
        ArrayChunk chunk = worldData.getChunk(chunkPos);
        LocalBlockPosition local = worldData.getConverter().getLocalPosition(pos);
        incVersion(chunk);
        chunk.setBlock(local, value);
        
        GlobalBlockPosition offsetPos = new GlobalBlockPosition(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        LocalBlockPosition borderPos = worldData.getConverter().getLocalPosition(offsetPos);
        final int x = borderPos.getX();
        if(x == 1) {
            invalidateMesh(Direction.WEST.neighbor(chunkPos));
        } else if(x == 0) {
            invalidateMesh(Direction.EAST.neighbor(chunkPos));
        }
        
        final int y = borderPos.getY();
        if(y == 1) {
            invalidateMesh(Direction.DOWN.neighbor(chunkPos));
        } else if(y == 0) {
            invalidateMesh(Direction.UP.neighbor(chunkPos));
        }
        
        final int z = borderPos.getZ();
        if(z == 1) {
            invalidateMesh(Direction.SOUTH.neighbor(chunkPos));
        } else if(z == 0) {
            invalidateMesh(Direction.NORTH.neighbor(chunkPos));
        }
    }
    
    public void setChunk(GlobalBlockPosition pos, byte value) {
        if(!worldData.isLegal(pos)) {
            return;
        }
        ChunkPosition cp = worldData.getConverter().getContainerPosition(pos);
        ArrayChunk chunk = worldData.getChunk(cp);
        chunk.fill(value);
        incVersion(chunk);
        for (Direction direction: Direction.values()) {
            invalidateMesh(direction.neighbor(cp));
        }
    }
    
    private void incVersion(ArrayChunk chunk) {
        if(chunk == null || chunk.getVersion() < 2) {
            System.err.println(WorldManagerImpl.class.getSimpleName() + " - WARNING: tried to incVersion of uninitialized chunk.");
            return;
        }
        chunk.setVersion(chunk.getVersion() + 1);
    }
    
    private void invalidateMesh(ChunkPosition pos) {
        incVersion(worldData.getChunk(pos));//version is increased to make sure the chunk will be saved (no grass/tree generation on reload)
//        ChunkNode node = chunkNodes.get(pos);
//        if(node == null) {
//            return;
//        }
//        node.setVersion(node.getVersion() - 1);
    }

    @Override
    public void loadChunk(ChunkPosition pos) {
        if(!worldData.isLegal(pos)) {
            return;
        }
        ArrayChunk chunk = worldData.getChunk(pos);
        if(chunk == null) {
            chunk = chunkPool.allocChunk(pos);//new AllmightyChunkImpl(pos, worldData.getChunkSize());
            worldData.addChunk(chunk);
            chunk = worldData.getChunk(pos);
        }
        chunk.setVersion(0);
    }

    @Override
    public void unloadChunk(ChunkPosition pos) {
        if(!worldData.isLegal(pos)) {
            return;
        }
        ArrayChunk chunk = worldData.getChunk(pos);
        if(chunk.getVersion() > 2) {
            compressor.save(chunk);
        }
        if(chunk.getVersion() != 0) {
            if(chunk.getVersion() > 1) {
                clearNeighborhood(neighborCounterV2, pos);
            }
            clearNeighborhood(neighborCounterV1, pos);
        }
        worldData.removeChunk(pos);
        ChunkNode chunkNode = chunkNodes.remove(pos);
        if(chunkNode != null) {
            rootNode.detachChild(chunkNode.getNode());
        }
        chunkPool.freeChunk(chunk);
    }

    @Override
    public void update() {
        for (ChunkPosition pos : worldData.getChunks().keySet()) {
            update(pos);
        }
        
//        Set<ChunkPosition> positions = new HashSet<ChunkPosition>(worldData.getChunks().keySet());
//        for (final ChunkPosition pos : positions) {
//            executor.submit(new Runnable() {
//                public void run() {
//                    synchronized(pos) {
//                        update(pos);
//                    }
//                }
//            });
//        }
        
        
//        List<ChunkPosition> removeQuery = new ArrayList<ChunkPosition>();
//        while(!positions.isEmpty()) {
//            for (final ChunkPosition pos : positions) {
//                if(locks.acquireLock(pos)) {
//                    removeQuery.add(pos);
//                    executor.submit(new Runnable() {
//                        public void run() {
//                            update(pos);
//                            locks.freeLock(pos);
//                        }
//                    });
//                }
//            }
//            positions.removeAll(removeQuery);
//            removeQuery.clear();
//        }
//        for (int i = 0; i < 4; i++) {
//            executor.submit(new Runnable() {
//                public void run() {
//                    while (!positions.isEmpty()) {                        
//                        for (ChunkPosition pos : positions) {
//                            
//                        }
//                    }
//                }
//            });
//        }
//        
//        for (final ChunkPosition pos : worldData.getChunks().keySet()) {
//                executor.submit(new Runnable() {
//                    public void run() {
//                    update(pos);
//                }
//            });
//        }
    }
    private void update(ChunkPosition pos) {
        ArrayChunk chunk = worldData.getChunk(pos);
        if(chunk.getVersion() == 0) {
            if(!compressor.tryLoad(chunk)) {
                worldGenerator.firstPass(chunk);
                chunk.setVersion(1);
                initNeighborhood(neighborCounterV1, pos);
            } else {
                if(chunk.getVersion() >= 1) {
                    initNeighborhood(neighborCounterV1, pos);
                    if(chunk.getVersion() >= 2) {
                        initNeighborhood(neighborCounterV2, pos);
                    }
                }
            }
        }
        
//        int minNeighborVersion = 2;
//        TimeStatistics.TIME_STATISTICS.start("Neighbors");
//            for (int x = -1; x < 2; x++) {
//                for (int y = -1; y < 2; y++) {
//                    for (int z = -1; z < 2; z++) {
//                        ChunkPosition neighborPos = new ChunkPosition(x + pos.getX(), y + pos.getY(), z + pos.getZ());
//                        if(!worldData.isLegal(neighborPos)) {
//                            continue;
//                        }
//                        AllmightyChunkImpl neighbor = worldData.getChunk(neighborPos);
//                        if(neighbor == null || neighbor.getVersion() == 0) {
//                            TimeStatistics.TIME_STATISTICS.end("Neighbors");
//                            return;
//                        }
//                        minNeighborVersion = Math.min(neighbor.getVersion(), minNeighborVersion);
//                    }
//                }
//            }
//        TimeStatistics.TIME_STATISTICS.end("Neighbors");
            if(neighborCounterV1.get(pos) == null || neighborCounterV1.get(pos) != 26) {
                return;
            }
        if(chunk.getVersion() == 1) {
            
            
            worldGenerator.secondPass(chunk);
//            neighborVisibility.computeNeighborVisibility(chunk);
//            graph.add(chunk);
            chunk.setVersion(2);
            initNeighborhood(neighborCounterV2, pos);
        }
        
        
//        TimeStatistics.TIME_STATISTICS.start("Neighbors");
//            for (int x = -1; x < 2; x++) {
//                for (int y = -1; y < 2; y++) {
//                    for (int z = -1; z < 2; z++) {
//                        ChunkPosition neighborPos = new ChunkPosition(x + pos.getX(), y + pos.getY(), z + pos.getZ());
//                        if(!worldData.isLegal(neighborPos)) {
//                            continue;
//                        }
//                        AllmightyChunkImpl neighbor = worldData.getChunk(neighborPos);
//                        if(neighbor == null || neighbor.getVersion() < 2) {
//                            TimeStatistics.TIME_STATISTICS.end("Neighbors");
//                            return;
//                        }
//                    }
//                }
//            }
//        TimeStatistics.TIME_STATISTICS.end("Neighbors");
            if(neighborCounterV2.get(pos) == null || neighborCounterV2.get(pos) != 26) {
                return;
            }
//        if(minNeighborVersion < 2) {
//            return;
//        }
        
        if(chunk.getVersion() >= 2) {
            final ChunkNode chunkNode = chunkNodes.get(pos);
            if(chunkNode == null) {
            long start = TimeStatistics.TIME_STATISTICS.start();
                ChunkMeshingResult result = mesher.generateMesh(chunk, chunk.getVersion());
                final ChunkNode newChunkNode = new ChunkNode(material, result.getOpaque(), result.getTransparent());
                newChunkNode.getNode().setLocalTranslation(pos.getX() * chunk.getSize().getX(), pos.getY() * chunk.getSize().getY(), pos.getZ() * chunk.getSize().getZ());
                chunkNodes.put(pos, newChunkNode);
                newChunkNode.setVersion(result.getVersion());
                app.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        rootNode.attachChild(newChunkNode.getNode());
                        return null;
                    } 
                });
            TimeStatistics.TIME_STATISTICS.end(start, "Meshing");
            } else if(chunk.getVersion() != chunkNode.getVersion()) {
            long start = TimeStatistics.TIME_STATISTICS.start();
                final ChunkMeshingResult result = mesher.generateMesh(chunk, chunk.getVersion());
//                result.getOpaque().updateCounts();
//                result.getTransparent().updateCounts();
                app.enqueue(new Callable() {
                    @Override
                    public Object call() throws Exception {
                        chunkNode.setOpaque(result.getOpaque());
                        chunkNode.setTransparent(result.getTransparent());
                        chunkNode.setVersion(result.getVersion());
                        return null;
                    } 
                });
            TimeStatistics.TIME_STATISTICS.end(start, "Meshing");
            }
        }
    }

    @Override
    public Node getRootNode() {
        return rootNode;
    }

    @Override
    public void createChunk(ChunkPosition pos) {
        loadChunk(pos);
    }

    @Override
    public void deleteChunk(ChunkPosition pos) {
        unloadChunk(pos);
    }

    public ChunkPoolImpl<ArrayChunk> getChunkPool() {
        return chunkPool;
    }
    
    private void initNeighborhood(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
        long start = TimeStatistics.TIME_STATISTICS.start();
        for (ChunkPosition chunkPosition : populateNeighbors(pos)) {
            if(worldData.isLegal(chunkPosition)) {
                inc(map, chunkPosition);
            } else {
                inc(map, pos);
            }
        }
        TimeStatistics.TIME_STATISTICS.end(start, "Neighbors");
    }
    private void clearNeighborhood(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
        long start = TimeStatistics.TIME_STATISTICS.start();
        for (ChunkPosition chunkPosition : populateNeighbors(pos)) {
            if(worldData.isLegal(chunkPosition)) {
                dec(map, chunkPosition);
            } else {
                dec(map, pos);
            }
        }
        TimeStatistics.TIME_STATISTICS.end(start, "Neighbors");
    }
    
    private ChunkPosition[] populateNeighbors(ChunkPosition pos) {
        ChunkPosition[] neighbors = new ChunkPosition[26];
        int px = pos.getX();
        int py = pos.getY();
        int pz = pos.getZ();
        int i = 0;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if((x | y | z) == 0) {
                        continue;
                    }
                    
                    neighbors[i++] = new ChunkPosition(px + x, py + y, pz + z);
                }
            }
        }
        return neighbors;
    }
//    
//    private void incNeighbors(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
//        for (ChunkPosition chunkPosition : populateNeighbors(pos)) {
//            if(worldData.isLegal(chunkPosition)) {
//                inc(map, chunkPosition);
//            }
//        }
//    }
//    
    private void inc(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
        synchronized(pos) {
            Integer value = map.get(pos);
            if(value == null) {
                value = 0;
            }
            value++;
            map.put(pos, value);
        }
    }
//    
//    private void decNeighbors(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
//        for (ChunkPosition chunkPosition : populateNeighbors(pos)) {
//            if(worldData.isLegal(chunkPosition)) {
//                dec(map, chunkPosition);
//            }
//        }
//    }
//    
    private void dec(HashMap<ChunkPosition, Integer> map, ChunkPosition pos) {
        synchronized(pos) {
            Integer value = map.get(pos);
            if(value == null) {
                return;
            }
            value--;
            if(value == 0) {
                map.remove(pos);
            } else {
                map.put(pos, value);
            }
        }
    }
}
