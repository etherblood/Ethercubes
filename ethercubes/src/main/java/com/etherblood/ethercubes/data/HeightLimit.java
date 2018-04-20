/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public class HeightLimit implements PositionLimit {
    private final int minY, maxY, sizeY;

    public HeightLimit(int minY, int maxY_excluded, int sizeY) {
        this.minY = minY;
        this.maxY = maxY_excluded;
        this.sizeY = sizeY;
    }
    
    public boolean isLegal(ChunkPosition pos) {
        return minY <= pos.getY() && pos.getY() < maxY;
    }

    public boolean isLegal(GlobalBlockPosition pos) {
        return minY * sizeY <= pos.getY() && pos.getY() < maxY * sizeY;
    }
    
}
