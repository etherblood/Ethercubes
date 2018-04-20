package ethercubes.pagination.services;

import com.jme3.math.Vector3f;
import ethercubes.context.Autowire;
import ethercubes.data.ChunkPosition;
import ethercubes.events.CubesEventHandler;
import ethercubes.events.CubesEventbus;
import ethercubes.pagination.ChunkPagination;
import ethercubes.pagination.events.FinishTasksRequest;
import ethercubes.pagination.events.GenerateMeshChunkTasksRequest;
import ethercubes.pagination.events.GenerateStructuresChunkTasksRequest;
import ethercubes.pagination.events.LoadChunkTasksRequest;
import ethercubes.pagination.events.PlayerPositionChangedEvent;
import ethercubes.pagination.events.UnloadChunkTasksRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class ChunkPaginationService {
    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private ChunkPagination pagination;
    private Vector3f lastRefreshPosition = null;
    private final float refreshDistanceSquared = 1;
    private final Set<ChunkPosition> invalidChunks = Collections.newSetFromMap(new ConcurrentHashMap<ChunkPosition, Boolean>());
    
    @PostConstruct
    public void init() {
        eventbus.register(PlayerPositionChangedEvent.class, new CubesEventHandler<PlayerPositionChangedEvent>() {
            @Override
            public void handle(PlayerPositionChangedEvent event) {
                Vector3f newPosition = event.getNewPosition();
                if(lastRefreshPosition == null || newPosition.distanceSquared(lastRefreshPosition) > refreshDistanceSquared) {
                    update(newPosition);
                    lastRefreshPosition = newPosition;
                }
            }
        });
    }
    
    private void update(Vector3f center) {
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
        
        eventbus.fireEvent(new UnloadChunkTasksRequest(unloadChunksTasks));
        eventbus.fireEvent(new LoadChunkTasksRequest(generateTerrainTasks));
        eventbus.fireEvent(new FinishTasksRequest());
        eventbus.fireEvent(new GenerateStructuresChunkTasksRequest(generateStructuresTasks));
        eventbus.fireEvent(new FinishTasksRequest());
        eventbus.fireEvent(new GenerateMeshChunkTasksRequest(generateMeshesTasks));
        eventbus.fireEvent(new FinishTasksRequest());
    }
    
//    public List<Runnable> convertTasks(List<ChunkUpdateTask> tasks) {
//        List<Runnable> list = new ArrayList<Runnable>();
//        for (final ChunkUpdateTask task : tasks) {
//            list.add(new Runnable() {
//                @Override
//                public void run() {
//                    if(task.level == -1) {
//                        graph.remove(task.pos);
//                        worldGen.delete(task.pos);
//                    } else if(task.level == 0) {
//                        worldGen.update0(task.pos);
//                    } else if(task.level == 1) {
//                        worldGen.update1(task.pos);
//                    } else {
//                        T chunk = worldData.getChunk(task.pos);
//                        if(chunk != null) {
//                            graph.generateMesh(chunk);
//                        }
//                    }
//                }
//            });
//        }
//        return list;
//    }
//    
//    
//    
//    public void setBlock(GlobalBlockPosition pos, byte value) {
//        ChunkPosition chunkPos = worldData.getConverter().getContainerPosition(pos);
////        if(!worldData.isLegal(pos)) {
////            return;
////        }
//        T chunk = worldData.getChunk(chunkPos);
//        LocalBlockPosition local = worldData.getConverter().getLocalPosition(pos);
//        chunk.setBlock(local, value);
//        
//        GlobalBlockPosition offsetPos = new GlobalBlockPosition(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//        LocalBlockPosition borderPos = worldData.getConverter().getLocalPosition(offsetPos);
//        
//        ArrayList<Direction> directions = new ArrayList<>();
//        
//        final int x = borderPos.getX();
//        if(x == 1) {
//            directions.add(Direction.WEST);
//        } else if(x == 0) {
//            directions.add(Direction.EAST);
//        }
//        
//        final int y = borderPos.getY();
//        if(y == 1) {
//            directions.add(Direction.DOWN);
//        } else if(y == 0) {
//            directions.add(Direction.UP);
//        }
//        
//        final int z = borderPos.getZ();
//        if(z == 1) {
//            directions.add(Direction.SOUTH);
//        } else if(z == 0) {
//            directions.add(Direction.NORTH);
//        }
//        updateDirections(chunkPos, directions);
//    }
//    
//    private void updateDirections(ChunkPosition pos, List<Direction> directions) {
//        int minX = directions.contains(Direction.WEST) ? -1 : 0;
//        int maxX = directions.contains(Direction.EAST) ? 1 : 0;
//
//        int minY = directions.contains(Direction.DOWN) ? -1 : 0;
//        int maxY = directions.contains(Direction.UP) ? 1 : 0;
//
//        int minZ = directions.contains(Direction.SOUTH) ? -1 : 0;
//        int maxZ = directions.contains(Direction.NORTH) ? 1 : 0;
//        int i = 0;
//        for (int x = minX; x <= maxX; x++) {
//            for (int y = minY; y <= maxY; y++) {
//                for (int z = minZ; z <= maxZ; z++) {
//                    updateVersion(new ChunkPosition(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
//                    i++;
//                }
//            }
//        }
//    }
//    
//    public void setChunk(GlobalBlockPosition pos, byte value) {
////        if(!worldData.isLegal(pos)) {
////            return;
////        }
//        ChunkPosition cp = worldData.getConverter().getContainerPosition(pos);
//        T chunk = worldData.getChunk(cp);
//        chunk.fill(value);
//        updateDirections(cp, Arrays.asList(Direction.values()));
//    }
//    
//    private void incVersion(T chunk) {
//        if(chunk == null || chunk.getVersion() < 2) {
//            System.err.println(getClass() + " - WARNING: tried to incVersion of uninitialized chunk.");
//            return;
//        }
//        chunk.incVersion();
//    }
//    
//    private void updateVersion(ChunkPosition pos) {
//        incVersion(worldData.getChunk(pos));//version is increased to make sure the chunk will be saved (no grass/tree generation on reload)
//        invalidChunks.add(pos);
//    }
//
//    public ChunkPagination getPagination() {
//        return pagination;
//    }

}
