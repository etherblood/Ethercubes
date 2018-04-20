package com.etherblood.ethercubes.main.tmp;

import com.etherblood.ethercubes.context.Autowire;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.ChunkSize;
import com.etherblood.ethercubes.display.meshing.ChunkMeshingResult;
import com.etherblood.ethercubes.display.meshing.ChunkNode;
import com.etherblood.ethercubes.display.meshing.CubesMaterial;
import com.etherblood.ethercubes.display.meshing.events.ChunkMeshedEvent;
import com.etherblood.ethercubes.display.meshing.events.ChunkMeshingRequest;
import com.etherblood.ethercubes.events.CubesEventHandler;
import com.etherblood.ethercubes.events.CubesEventbus;
import com.etherblood.ethercubes.main.GuiService;
import com.etherblood.ethercubes.pagination.TaskExecutor;
import com.etherblood.ethercubes.pagination.events.GenerateMeshChunkTasksRequest;
import com.etherblood.ethercubes.world.ChunkWorld;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author Philipp
 */
public class Temporary_hopefully_EventRelayService {

    @Autowire
    private CubesEventbus eventbus;
    @Autowire
    private TaskExecutor exe;
    @Autowire
    private ChunkWorld chunkWorld;
    @Autowire
    private GuiService guiService;
    @Autowire
    private CubesMaterial cubesMat;
    @Autowire
    private ChunkSize size;

    @PostConstruct
    public void init() {
        eventbus.register(GenerateMeshChunkTasksRequest.class, new CubesEventHandler<GenerateMeshChunkTasksRequest>() {

            @Override
            public void handle(GenerateMeshChunkTasksRequest event) {
                List<Runnable> tasks = new ArrayList<>(event.getChunkPositions().size());
                for (final ChunkPosition chunkPosition : event.getChunkPositions()) {
                    tasks.add(new Runnable() {

                        @Override
                        public void run() {
                            eventbus.fireEvent(new ChunkMeshingRequest<>(chunkWorld.getChunk(chunkPosition)));
                        }
                    });
                }
                exe.submitTasks(tasks);
            }
        });
        
        eventbus.register(ChunkMeshedEvent.class, new CubesEventHandler<ChunkMeshedEvent>() {

            @Override
            public void handle(ChunkMeshedEvent event) {
                ChunkMeshingResult mesh = event.getMesh();
                ChunkNode node = new ChunkNode(cubesMat, mesh.getOpaque(), mesh.getTransparent());
                node.getNode().setLocalTranslation(mesh.getPos().getX() * size.getX(), mesh.getPos().getY() * size.getY(), mesh.getPos().getZ() * size.getZ());
                guiService.attach(node.getNode());
            }
        });
    }
}
