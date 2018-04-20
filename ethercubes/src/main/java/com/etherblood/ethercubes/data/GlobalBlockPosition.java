/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public final class GlobalBlockPosition {
    private final int x, y, z;
    
    public static final GlobalBlockPosition ZERO = new GlobalBlockPosition(0, 0, 0);

    public GlobalBlockPosition(int x, int y, int z) {
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
    
    public GlobalBlockPosition add(int x, int y, int z) {
        return new GlobalBlockPosition(x + this.x, y + this.y, z + this.z);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + this.x;
        hash = 29 * hash + this.y;
        hash = 29 * hash + this.z;
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
        return equals((GlobalBlockPosition) obj);
    }
    public boolean equals(GlobalBlockPosition other) {
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
        return GlobalBlockPosition.class.getSimpleName() + "{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
