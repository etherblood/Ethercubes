package ethercubes.units;

import com.jme3.math.Vector3f;
import ethercubes.data.GlobalBlockPosition;
import ethercubes.settings.implementation.TestBlockSettings;
import ethercubes.world.BlockWorld;

/**
 *
 * @author Philipp
 */
public class Hitbox {
    private final byte air = TestBlockSettings.AIR;
    private final byte water = TestBlockSettings.WATER;

    private final float radius, height;
    private Vector3f bottomCenter;
    private boolean jesus = false;

    public Hitbox(float radius, float height) {
        this.radius = radius;
        this.height = height;
    }

    public Vector3f min() {
        return bottomCenter.add(-radius, 0, -radius);
    }

    public Vector3f max() {
        return bottomCenter.add(radius, height, radius);
    }
    
    boolean isSolid(BlockWorld world, int x, int y, int z) {
        byte block = world.getBlock(new GlobalBlockPosition(x, y, z));
        return isBlockSolid(block);
    }

    private boolean isBlockSolid(byte block) {
        return block != air && (jesus || block != water);
    }

    public Vector3f getBottomCenter() {
        return bottomCenter;
    }
    public Vector3f getTopCenter() {
        return bottomCenter.add(0, height, 0);
    }

    public void setBottomCenter(Vector3f bottomCenter) {
        this.bottomCenter = bottomCenter;
    }
    
    public void move(BlockWorld world, Vector3f mov) {
        moveX(mov, world);
        moveZ(mov, world);
        moveY(mov, world);
    }

    private void moveX(Vector3f mov, BlockWorld world) {
        float deltaX = mov.x;
        int direction = (int) Math.signum(deltaX);
        float x = bottomCenter.x + direction * radius;
        float destX = x + deltaX;
        int gridX = (int) Math.floor(x);
        int destGridX = (int) Math.floor(destX);
        gridX = moveAlongX(gridX, destGridX, world, direction);
        if(gridX == destGridX) {
            bottomCenter.x += deltaX;
        } else {
            bottomCenter.x = gridX - direction * radius + Math.max(direction * 0.999f, 0.001f);
        }
    }
    
    private int moveAlongX(int gridX, int destGridX, BlockWorld world, int direction) {
        Vector3f min = min();
        Vector3f max = max();
        int minY = (int) Math.floor(min.y);
        int maxY = (int) Math.ceil(max.y);
        int minZ = (int) Math.floor(min.z);
        int maxZ = (int) Math.ceil(max.z);
        while (direction * gridX < direction * destGridX) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (isSolid(world, gridX + direction, y, z)) {
                        return gridX;
                    }
                }
            }
            gridX += direction;
        }
        return gridX;
    }

    private void moveZ(Vector3f mov, BlockWorld world) {
        float deltaZ = mov.z;
        int direction = (int) Math.signum(deltaZ);
        float z = bottomCenter.z + direction * radius;
        float destZ = z + deltaZ;
        int gridZ = (int) Math.floor(z);
        int destGridZ = (int) Math.floor(destZ);
        gridZ = moveAlongZ(gridZ, destGridZ, world, direction);
        if(gridZ == destGridZ) {
            bottomCenter.z += deltaZ;
        } else {
            bottomCenter.z = gridZ - direction * radius + Math.max(direction * 0.999f, 0.001f);
        }
    }
    
    private int moveAlongZ(int gridZ, int destGridZ, BlockWorld world, int direction) {
        Vector3f min = min();
        Vector3f max = max();
        int minY = (int) Math.floor(min.y);
        int maxY = (int) Math.ceil(max.y);
        int minX = (int) Math.floor(min.x);
        int maxX = (int) Math.ceil(max.x);
        while (direction * gridZ < direction * destGridZ) {
            for (int y = minY; y < maxY; y++) {
                for (int x = minX; x < maxX; x++) {
                    if (isSolid(world, x, y, gridZ + direction)) {
                        return gridZ;
                    }
                }
            }
            gridZ += direction;
        }
        return gridZ;
    }

    private void moveY(Vector3f mov, BlockWorld world) {
        float deltaY = mov.y;
        int direction = (int) Math.signum(deltaY);
        float y = bottomCenter.y + Math.max(direction * height, 0);
        float destY = y + deltaY;
        int gridY = (int) Math.floor(y);
        int destGridY = (int) Math.floor(destY);
        gridY = moveAlongY(gridY, destGridY, world, direction);
        if(gridY == destGridY) {
            bottomCenter.y += deltaY;
        } else {
            bottomCenter.y = gridY + Math.max(direction * 0.999f, 0.001f) - Math.max(direction * height, 0);
        }
    }
    
    private int moveAlongY(int gridY, int destGridY, BlockWorld world, int direction) {
        Vector3f min = min();
        Vector3f max = max();
        int minX = (int) Math.floor(min.x);
        int maxX = (int) Math.ceil(max.x);
        int minZ = (int) Math.floor(min.z);
        int maxZ = (int) Math.ceil(max.z);
        while (direction * gridY < direction * destGridY) {
            for (int x = minX; x < maxX; x++) {
                for (int z = minZ; z < maxZ; z++) {
                    if (isSolid(world, x, gridY + direction, z)) {
                        return gridY;
                    }
                }
            }
            gridY += direction;
        }
        return gridY;
    }
}
