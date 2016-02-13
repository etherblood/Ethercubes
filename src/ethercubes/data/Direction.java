/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.data;

import java.util.EnumMap;

/**
 *
 * @author Philipp
 */
public enum Direction {
    EAST, WEST, UP, DOWN, NORTH, SOUTH;
    
    private final static EnumMap<Direction, Direction> inverseMap;
    
    static {
        inverseMap = new EnumMap<Direction, Direction>(Direction.class);
        inverseMap.put(EAST, WEST);
        inverseMap.put(WEST, EAST);
        inverseMap.put(UP, DOWN);
        inverseMap.put(DOWN, UP);
        inverseMap.put(SOUTH, NORTH);
        inverseMap.put(NORTH, SOUTH);
    }
    
    public Direction inverse() {
        return inverseMap.get(this);
    }
    
    public GlobalBlockPosition neighbor(GlobalBlockPosition p) {
        switch(this) {
            case EAST:
                return new GlobalBlockPosition(p.getX() + 1, p.getY(), p.getZ());
            case WEST:
                return new GlobalBlockPosition(p.getX() - 1, p.getY(), p.getZ());
            case UP:
                return new GlobalBlockPosition(p.getX(), p.getY() + 1, p.getZ());
            case DOWN:
                return new GlobalBlockPosition(p.getX(), p.getY() - 1, p.getZ());
            case NORTH:
                return new GlobalBlockPosition(p.getX(), p.getY(), p.getZ() + 1);
            case SOUTH:
                return new GlobalBlockPosition(p.getX(), p.getY(), p.getZ() - 1);
        }
        return null;
    }
    public LocalBlockPosition neighbor(LocalBlockPosition p) {
        switch(this) {
            case EAST:
                return new LocalBlockPosition(p.getX() + 1, p.getY(), p.getZ());
            case WEST:
                return new LocalBlockPosition(p.getX() - 1, p.getY(), p.getZ());
            case UP:
                return new LocalBlockPosition(p.getX(), p.getY() + 1, p.getZ());
            case DOWN:
                return new LocalBlockPosition(p.getX(), p.getY() - 1, p.getZ());
            case NORTH:
                return new LocalBlockPosition(p.getX(), p.getY(), p.getZ() + 1);
            case SOUTH:
                return new LocalBlockPosition(p.getX(), p.getY(), p.getZ() - 1);
        }
        return null;
    }
    public ChunkPosition neighbor(ChunkPosition p) {
        switch(this) {
            case EAST:
                return new ChunkPosition(p.getX() + 1, p.getY(), p.getZ());
            case WEST:
                return new ChunkPosition(p.getX() - 1, p.getY(), p.getZ());
            case UP:
                return new ChunkPosition(p.getX(), p.getY() + 1, p.getZ());
            case DOWN:
                return new ChunkPosition(p.getX(), p.getY() - 1, p.getZ());
            case NORTH:
                return new ChunkPosition(p.getX(), p.getY(), p.getZ() + 1);
            case SOUTH:
                return new ChunkPosition(p.getX(), p.getY(), p.getZ() - 1);
        }
        return null;
    }
}
