/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world.worldgen;

import com.etherblood.ethercubes.data.GlobalBlockPosition;

/**
 *
 * @author Philipp
 */
public class BlockOffset {
    private final int x, y, z;

    public BlockOffset(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public GlobalBlockPosition offsetGlobal(GlobalBlockPosition global) {
        return new GlobalBlockPosition(x + global.getX(), y + global.getY(), z + global.getZ());
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
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.y;
        hash = 59 * hash + this.z;
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
        final BlockOffset other = (BlockOffset) obj;
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
        return "BlockOffset{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}
