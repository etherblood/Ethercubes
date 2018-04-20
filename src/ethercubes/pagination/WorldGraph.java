package ethercubes.pagination;

import com.jme3.material.Material;
import com.jme3.scene.Node;
import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.VersionedReadonly;
import ethercubes.data.ChunkPosition;
import ethercubes.display.meshing.ChunkMesher;
import ethercubes.display.meshing.ChunkMeshingResult;
import ethercubes.display.meshing.ChunkNode;
import ethercubes.statistics.TimeStatistics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 *
 * @author Philipp
 */
public class WorldGraph<T extends VersionedReadonly & ChunkReadonly> {

    private final ConcurrentLinkedDeque<Runnable> renderTasks;
    private final Node rootNode = new Node("worldRoot");
    private final ConcurrentHashMap<ChunkPosition, ChunkNode> chunkNodes = new ConcurrentHashMap<ChunkPosition, ChunkNode>();
    private final ChunkMesher<T> mesher;
    private final Material material;

    public WorldGraph(ConcurrentLinkedDeque<Runnable> renderTasks, ChunkMesher<T> mesher, Material material) {
        this.renderTasks = renderTasks;
        this.mesher = mesher;
        this.material = material;
    }

    public void generateMesh(T chunk) {
        ChunkPosition pos = chunk.getPosition();
        final boolean attachNode = lazyInitNode(pos, chunk);
        final ChunkNode chunkNode = chunkNodes.get(pos);
        if (chunk.getVersion() != chunkNode.getVersion()) {
            long start = TimeStatistics.TIME_STATISTICS.start();
            final ChunkMeshingResult result = mesher.generateMesh(chunk, chunk.getVersion());
            chunkNode.setVersion(result.getVersion());
            renderTasks.push(new Runnable() {
                @Override
                public void run() {
                    chunkNode.setOpaque(result.getOpaque());
                    chunkNode.setTransparent(result.getTransparent());
                    if(attachNode) {
                        rootNode.attachChild(chunkNode.getNode());
                    }
                }
            });
            TimeStatistics.TIME_STATISTICS.end(start, "Meshing");
        }
    }
    
    public void remove(ChunkPosition pos) {
        final ChunkNode chunkNode = chunkNodes.remove(pos);
        if(chunkNode != null) {
            renderTasks.push(new Runnable() {
                @Override
                public void run() {
                    rootNode.detachChild(chunkNode.getNode());
                }
            });
        }
    }

    private boolean lazyInitNode(ChunkPosition pos, T chunk) {
        if(!chunkNodes.containsKey(pos)) {
            final ChunkNode newChunkNode = new ChunkNode(material);
            chunkNodes.put(pos, newChunkNode);
            newChunkNode.getNode().setLocalTranslation(pos.getX() * chunk.getSize().getX(), pos.getY() * chunk.getSize().getY(), pos.getZ() * chunk.getSize().getZ());
            return true;
        }
        return false;
    }

    public Node getRootNode() {
        return rootNode;
    }
}
