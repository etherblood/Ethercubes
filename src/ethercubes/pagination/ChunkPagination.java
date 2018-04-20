package ethercubes.pagination;

import com.jme3.math.Vector3f;
import ethercubes.data.ChunkPosition;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class ChunkPagination {
    private int radius0 = 10;
    private RadiusCubicLoadZone level0, level1, level2;
    
    public List<ChunkPosition> generateDelTasks(Vector3f center) {
        if(level0 != null) {
            RadiusCubicLoadZone new0 = new RadiusCubicLoadZone(center, radius0, 2);
            return level0.subtract(new0);
        }
        return Collections.emptyList();
    }
    
    public List<ChunkPosition> generateTasks0(Vector3f center) {
        RadiusCubicLoadZone new0 = new RadiusCubicLoadZone(center, radius0, 2);
        return new0.subtract(level0);
    }
    
    public List<ChunkPosition> generateTasks1(Vector3f center) {
        RadiusCubicLoadZone new1 = new RadiusCubicLoadZone(center, radius0, 1);
        return new1.subtract(level1);
    }
    
    public List<ChunkPosition> generateTasks2(Vector3f center) {
        RadiusCubicLoadZone new2 = new RadiusCubicLoadZone(center, radius0, 0);
        return new2.subtract(level2);
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
