/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings.implementation;

/**
 *
 * @author Philipp
 */
public class Blocks {
    private static final int OPAQUE_LIMIT;
    
    public static final int AIR;
    public static final int WATER;
    public static final int LEAFES;
    public static final int GRASS;
    public static final int STONE;
    public static final int SAND;
    public static final int SNOW;
    public static final int WOOD;
    
    public static final int AMOUNT;
    
    static {
        int next = 0;
        AIR = next++;
        
        //transparent blocks here
        WATER = next++;
        LEAFES = next++;
        
        OPAQUE_LIMIT = next;
        
        //opaque blocks here
        GRASS = next++;
        STONE = next++;
        SAND = next++;
        SNOW = next++;
        WOOD = next++;
        
        AMOUNT = next;
    }
    
    public static boolean isOpaque(int block) {
        return block >= OPAQUE_LIMIT;
    }
    
}
