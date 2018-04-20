/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public class NeighborVisibility {
    private final int visibility;

    public NeighborVisibility(int visibility) {
        this.visibility = visibility;
    }
    
    public boolean visibleToEachother(Direction neighborA, Direction neighborB) {
        boolean result = (visibility & flagFromDirections(neighborA, neighborB)) != 0;
        return result;
    }
    
    protected static int flagFromDirections(Direction neighborA, Direction neighborB) {
        return flagFromDirectionsSimple(neighborA.ordinal(), neighborB.ordinal());
    }
//    protected static int flagFromDirections(int a, int b) {
//        if(a == b) {
//            throw new RuntimeException("Green");
//        }
//        if(b < a) {
//            int tmp = a;
//            a = b;
//            b = tmp;
//        }
//        int offsetA = 0;
//        int remain = 5;
//        for (int i = 0; i < a; i++) {
//            offsetA += remain;
//            remain--;
//        }
//        int result = 1 << (offsetA + b - a - 1);
//        if((result & 0x7FFF) != result)
//            throw new RuntimeException("A");
//        if((result | 0x7FFF) != 0x7FFF)
//            throw new RuntimeException("B");
//        if(Integer.bitCount(result) != 1) {
//            throw new RuntimeException("C");
//        }
//        return result;
//    }
    protected static int flagFromDirectionsSimple(int a, int b) {
        if(a == b) {
            throw new RuntimeException("Green");
        }
        if(b < a) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        b--;
        return 1 << (a * 5 + b);
    }
}
