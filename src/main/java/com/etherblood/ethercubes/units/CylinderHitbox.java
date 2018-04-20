package com.etherblood.ethercubes.units;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 *
 * @author Philipp
 */
public class CylinderHitbox {

    private final float radius, height;
    private Vector3f bottomCenter;

    public CylinderHitbox(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }
    
    public Vector3f min() {
        return bottomCenter.add(-radius, 0, -radius);
    }
    public Vector3f max() {
        return bottomCenter.add(radius, height, radius);
    }
    
    public boolean intersects(int x, int y, int z) {
        if(bottomCenter.y + height <= y || y <= bottomCenter.y) {
            return false;
        }
        
        float centerX = bottomCenter.x;
        float centerZ = bottomCenter.z;
        
        float pX, pZ;

        if (centerX < x) {
            pX = x;
        } else if (x + 1 < centerX) {
            pX = x + 1;
        } else {
            pX = centerX;
        }

        if (centerZ < z) {
            pZ = z;
        } else if (z + 1 < centerZ) {
            pZ = z + 1;
        } else {
            pZ = centerZ;
        }
        
        pX -= centerX;
        pZ -= centerZ;
//          if (circleX < rect.Left) x = rect.Left //circle is to the left, the closest point must lie on the left rectangle border
//          else if(circleX > rect.Right) x = rect.Right
//          else x = circleX
//        intersection if (circle.radius² >= (x - circle.x)² + (y - circle.y)²) //pythagoras
        
        return pX * pX + pZ * pZ <= radius * radius;
    }

    public void solveIntersectionWith(int x, int y, int z) {
        Vector2f a = solve(x, z);
        float b = solve(y);
        if (a.lengthSquared() < b * b) {
            bottomCenter.addLocal(a.x, 0, a.y);
        } else {
            bottomCenter.addLocal(0, b, 0);
        }
    }

    private Vector2f solve(int x, int z) {
        float centerX = bottomCenter.x;
        float centerZ = bottomCenter.z;
        
        float pX, pZ;

        if (centerX < x) {
            pX = x;
        } else if (x + 1 < centerX) {
            pX = x + 1;
        } else {
            pX = centerX;
        }

        if (centerZ < z) {
            pZ = z;
        } else if (z + 1 < centerZ) {
            pZ = z + 1;
        } else {
            pZ = centerZ;
        }
        
        pX -= centerX;
        pZ -= centerZ;
        
        if(pX == 0 || pZ == 0) {
            return new Vector2f(100, 0);
        }

        Vector2f result = new Vector2f(pX, pZ).negateLocal();
        Vector2f unit = result.normalize();
        return result.subtract(unit.multLocal(radius));
    }

    private float solve(int y) {
        float a = (y + 1) - bottomCenter.y;
        float b = y - (bottomCenter.y + height);

        if (a > -b) {
            return a;
        }
        return b;
    }

    public Vector3f getBottomCenter() {
        return bottomCenter;
    }

    public void setBottomCenter(Vector3f bottomCenter) {
        this.bottomCenter = bottomCenter;
    }
}
