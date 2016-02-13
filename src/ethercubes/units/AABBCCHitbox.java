package ethercubes.units;

import com.jme3.math.Vector3f;

/**
 *
 * @author Philipp
 */
public class AABBCCHitbox {

    private final float radius, height;
    private Vector3f bottomCenter;

    public AABBCCHitbox(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }
    
    public void solveIntersectionWith(int x, int y, int z) {
        Vector3f min = min();
        Vector3f max = max();
        float solveX = solveFor(x, min.x, max.x);
        float solveY = solveFor(y, min.y, max.y);
        float solveZ = solveFor(z, min.z, max.z);
        float absX = Math.abs(solveX);
        float absY = Math.abs(solveY);
        float absZ = Math.abs(solveZ);
        if(absX < absY) {
            if(absX < absZ) {
                bottomCenter.addLocal(solveX, 0, 0);
            } else {
                bottomCenter.addLocal(0, 0, solveZ);
            }
        } else {
            if(absY < absZ) {
                bottomCenter.addLocal(0, solveY, 0);
            } else {
                bottomCenter.addLocal(0, 0, solveZ);
            }
        }
    }
    public void solveX(int x, int y, int z) {
        Vector3f min = min();
        Vector3f max = max();
        float solveX = solveFor(x, min.x, max.x);
        bottomCenter.addLocal(solveX, 0, 0);
    }
    public void solveY(int x, int y, int z) {
        Vector3f min = min();
        Vector3f max = max();
        float solveY = solveFor(y, min.y, max.y);
        bottomCenter.addLocal(0, solveY, 0);
    }
    public void solveZ(int x, int y, int z) {
        Vector3f min = min();
        Vector3f max = max();
        float solveZ = solveFor(z, min.z, max.z);
        bottomCenter.addLocal(0, 0, solveZ);
    }
    
    private float solveFor(int cube, float min, float max) {
        float a = cube + 1 - min;
        float b = max - cube;
        if(a < b) {
            return a;
        }
        return -b;
    }

    public Vector3f min() {
        return bottomCenter.add(-radius, 0, -radius);
    }
    public Vector3f max() {
        return bottomCenter.add(radius, height, radius);
    }
    
    public boolean intersects(int x, int y, int z) {
        Vector3f min = min();
        Vector3f max = max();
        return intersects(x, min.x, max.x) && intersects(y, min.y, max.y) && intersects(z, min.z, max.z);
    }
    
    private boolean intersects(int cube, float min, float max) {
        return min < cube + 1 && cube < max;
    }
    
    public Vector3f getBottomCenter() {
        return bottomCenter;
    }

    public void setBottomCenter(Vector3f bottomCenter) {
        this.bottomCenter = bottomCenter;
    }
}
