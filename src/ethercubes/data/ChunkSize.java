package ethercubes.data;

/**
 *
 * @author Philipp
 */
public final class ChunkSize {
    private final int x, y, z;

    public ChunkSize(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
    
    public boolean contains(LocalBlockPosition position) {
        return contains(position.getX(), position.getY(), position.getZ());
    }
    public boolean contains(int x, int y, int z) {
        return 0 <= x && x < this.x && 0 <= y && y < this.y && 0 <= z && z < this.z;
    }
    
    public int index(int x, int y, int z) {
        return (y * this.z + z) * this.x + x;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.z;
        hash = 97 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChunkSize && equals((ChunkSize) obj);
    }
    public boolean equals(ChunkSize size) {
        return x == size.x && y == size.y && z == size.z;
    }
}
