/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public final class LocalBlockPosition {
    private final int x, y, z;

    public static final LocalBlockPosition ZERO = new LocalBlockPosition(0, 0, 0);
    
    public LocalBlockPosition(int x, int y, int z) {
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
    
    public LocalBlockPosition add(int x, int y, int z) {
        return new LocalBlockPosition(this.x + x, this.y + y, this.z + z);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 197 * hash + this.x;
        hash = 197 * hash + this.y;
        hash = 197 * hash + this.z;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return equals((LocalBlockPosition) obj);
    }
    public boolean equals(LocalBlockPosition other) {
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return LocalBlockPosition.class.getSimpleName() + "{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
