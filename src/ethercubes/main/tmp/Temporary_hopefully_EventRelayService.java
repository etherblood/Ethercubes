package ethercubes.main.tmp;

import ethercubes.context.Autowire;
import ethercubes.data.ChunkPosition;
import ethercubes.data.ChunkSize;
import ethercubes.display.meshing.ChunkMeshingResult;
import ethercubes.display.meshing.ChunkNode;
import ethercubes.display.meshing.CubesMaterial;
import ethercubes.display.meshing.events.ChunkMeshedEvent;
import ethercubes.display.meshing.events.ChunkMeshingRequest;
import ethercubes.events.CubesEventHandler;
import ethercubes.events.CubesEventbus;
import ethercubes.main.GuiService;
import ethercubes.pagination.TaskExecutor;
import ethercubes.pagination.events.GenerateMeshChunkTasksRequest;
import ethercubes.world.ChunkWorld;
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
