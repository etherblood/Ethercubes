package ethercubes.pagination;

import com.jme3.math.Vector3f;
import ethercubes.data.ChunkPosition;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class ChunkPagination {
    private int radius0 = 10;
    private RadiusCubicLoadZone level0, level1, level2;
    
    public List<ChunkUpdateTask> generateDelTasks(Vector3f center) {
        ArrayList<ChunkUpdateTask> queue = new ArrayList<ChunkUpdateTask>();
        if(level0 != null) {
            RadiusCubicLoadZone new0 = new RadiusCubicLoadZone(center, radius0, 2);

            for (ChunkPosition pos : level0.subtract(new0)) {
                queue.add(new ChunkUpdateTask(pos, -1));
            }
        }
        return queue;
    }
    
    public List<ChunkUpdateTask> generateTasks0(Vector3f center) {
        ArrayList<ChunkUpdateTask> queue = new ArrayList<ChunkUpdateTask>();
        
        RadiusCubicLoadZone new0 = new RadiusCubicLoadZone(center, radius0, 2);
        
        for (ChunkPosition pos : new0.subtract(level0)) {
            queue.add(new ChunkUpdateTask(pos, 0));
        }
        return queue;
    }
    
    public List<ChunkUpdateTask> generateTasks1(Vector3f center) {
        ArrayList<ChunkUpdateTask> queue = new ArrayList<ChunkUpdateTask>();
        
        RadiusCubicLoadZone new1 = new RadiusCubicLoadZone(center, radius0, 1);
        
        for (ChunkPosition pos : new1.subtract(level1)) {
            queue.add(new ChunkUpdateTask(pos, 1));
        }
        return queue;
    }
    
    public List<ChunkUpdateTask> generateTasks2(Vector3f center) {
        ArrayList<ChunkUpdateTask> queue = new ArrayList<ChunkUpdateTask>();
        
        RadiusCubicLoadZone new2 = new RadiusCubicLoadZone(center, radius0, 0);
        
        for (ChunkPosition pos : new2.subtract(level2)) {
            queue.add(new ChunkUpdateTask(pos, 2));
        }
        return queue;
    }
    
    public void setNewCenter(Vector3f center) {
        level0 = new RadiusCubicLoadZone(center, radius0, 2);
        level1 = new RadiusCubicLoadZone(center, radius0, 1);
        level2 = new RadiusCubicLoadZone(center, radius0, 0);
    }

    public int getRadius() {
        return radius0;
    }

    public void setRadius(int radius) {
        this.radius0 = radius;
    }
    
}
