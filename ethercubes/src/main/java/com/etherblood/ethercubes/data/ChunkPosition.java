/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public final class ChunkPosition {
    private final int x, y, z;

    public ChunkPosition(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public ChunkPosition add(int x, int y, int z) {
        return new ChunkPosition(x + this.x, y + this.y, z + this.z);
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

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7 + this.x;
        hash = 31 * hash + this.z;
        hash = 31 * hash + this.y;
        return hash;
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")//performance critical code
    public boolean equals(Object obj) {
        return this == obj || equals((ChunkPosition) obj);
    }
    public boolean equals(ChunkPosition other) {
        return x == other.x && z == other.z && y == other.y;
    }
}
