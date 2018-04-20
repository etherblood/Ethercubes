package ethercubes.pagination;

import com.jme3.math.Vector3f;
import ethercubes.chunk.BlockChunk;
import ethercubes.chunk.FastXZYChunk;
import ethercubes.chunk.PoolChunk;
import ethercubes.chunk.Versioned;
import ethercubes.data.ChunkPosition;
import ethercubes.data.Direction;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.data.LocalBlockPosition;
import ethercubes.world.implementation.AbstractBlockChunkWorld;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class PaginatedWorldManager<T extends BlockChunk & Versioned & FastXZYChunk & PoolChunk> {
    private final AbstractBlockChunkWorld<T> worldData;
    private final WorldGraph<T> graph;
    private final ChunkPagination pagination = new ChunkPagination();
    private final TaskExecutor executor;
    private final WorldGen<T> worldGen;
    private final Set<ChunkPosition> invalidChunks = Collections.synchronizedSet(new HashSet<ChunkPosition>());

    public PaginatedWorldManager(AbstractBlockChunkWorld<T> worldData, WorldGraph<T> graph, TaskExecutor executor, WorldGen<T> worldGen) {
        this.worldData = worldData;
        this.graph = graph;
        this.executor = executor;
        this.worldGen = worldGen;
    }
    
    public void update(Vector3f center) {
        List<ChunkPosition> unloadChunksTasks;
        List<ChunkPosition> generateTerrainTasks;
        List<ChunkPosition> generateStructuresTasks;
        List<ChunkPosition> generateMeshesTasks;
        synchronized(pagination) {
            unloadChunksTasks = pagination.generateDelTasks(center);
            generateTerrainTasks = pagination.generateTasks0(center);
            generateStructuresTasks = pagination.generateTasks1(center);
            generateMeshesTasks = pagination.generateTasks2(center);
            pagination.setNewCenter(center);
        }
        
        List<ChunkPosition> positions = new ArrayList<>(invalidChunks);
        generateMeshesTasks.addAll(positions);
        invalidChunks.removeAll(positions);
        
        executor.submitTasks(convertTasks(unloadChunksTasks, -1));
        executor.submitTasks(convertTasks(generateTerrainTasks, 0));
        executor.blockUntilFinished();
        executor.submitTasks(convertTasks(generateStructuresTasks, 1));
        executor.blockUntilFinished();
        executor.submitTasks(convertTasks(generateMeshesTasks, 2));
        executor.blockUntilFinished();
    }
    
    public List<Runnable> convertTasks(List<ChunkPosition> tasks, final int level) {
        List<Runnable> list = new ArrayList<Runnable>();
        for (final ChunkPosition pos : tasks) {
            list.add(new Runnable() {
                @Override
                public void run() {
                    if(level == -1) {
                        graph.remove(pos);
                        worldGen.delete(pos);
                    } else if(level == 0) {
                        worldGen.update0(pos);
                    } else if(level == 1) {
                        worldGen.update1(pos);
                    } else {
                        T chunk = worldData.getChunk(pos);
                        if(chunk != null) {
                            graph.generateMesh(chunk);
                        }
                    }
                }
            });
        }
        return list;
    }
    
    
    
    public void setBlock(GlobalBlockPosition pos, byte value) {
        ChunkPosition chunkPos = worldData.getConverter().getContainerPosition(pos);
//        if(!worldData.isLegal(pos)) {
//            return;
//        }
        T chunk = worldData.getChunk(chunkPos);
        LocalBlockPosition local = worldData.getConverter().getLocalPosition(pos);
        chunk.setBlock(local, value);
        
        GlobalBlockPosition offsetPos = new GlobalBlockPosition(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        LocalBlockPosition borderPos = worldData.getConverter().getLocalPosition(offsetPos);
        
        ArrayList<Direction> directions = new ArrayList<>();
        
        final int x = borderPos.getX();
        if(x == 1) {
            directions.add(Direction.WEST);
        } else if(x == 0) {
            directions.add(Direction.EAST);
        }
        
        final int y = borderPos.getY();
        if(y == 1) {
            directions.add(Direction.DOWN);
        } else if(y == 0) {
            directions.add(Direction.UP);
        }
        
        final int z = borderPos.getZ();
        if(z == 1) {
            directions.add(Direction.SOUTH);
        } else if(z == 0) {
            directions.add(Direction.NORTH);
        }
        updateDirections(chunkPos, directions);
    }
    
    private void updateDirections(ChunkPosition pos, List<Direction> directions) {
        int minX = directions.contains(Direction.WEST) ? -1 : 0;
        int maxX = directions.contains(Direction.EAST) ? 1 : 0;

        int minY = directions.contains(Direction.DOWN) ? -1 : 0;
        int maxY = directions.contains(Direction.UP) ? 1 : 0;

        int minZ = directions.contains(Direction.SOUTH) ? -1 : 0;
        int maxZ = directions.contains(Direction.NORTH) ? 1 : 0;
        int i = 0;
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    updateVersion(new ChunkPosition(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
                    i++;
                }
            }
        }
    }
    
    public void setChunk(GlobalBlockPosition pos, byte value) {
//        if(!worldData.isLegal(pos)) {
//            return;
//        }
        ChunkPosition cp = worldData.getConverter().getContainerPosition(pos);
        T chunk = worldData.getChunk(cp);
        chunk.fill(value);
        updateDirections(cp, Arrays.asList(Direction.values()));
    }
    
    private void incVersion(T chunk) {
        if(chunk == null || chunk.getVersion() < 2) {
            System.err.println(getClass() + " - WARNING: tried to incVersion of uninitialized chunk.");
            return;
        }
        chunk.incVersion();
    }
    
    private void updateVersion(ChunkPosition pos) {
        incVersion(worldData.getChunk(pos));//version is increased to make sure the chunk will be saved (no grass/tree generation on reload)
        invalidChunks.add(pos);
    }

    public ChunkPagination getPagination() {
        return pagination;
    }
}
