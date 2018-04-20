package com.etherblood.ethercubes.data;

/**
 *
 * @author Philipp
 */
public class LocalBlockIndexXZYPositionUtil {
    private final int xShift, yShift, zShift;
    private final int xMask, yMask, zMask;

    public LocalBlockIndexXZYPositionUtil(int sizeX, int sizeY, int sizeZ) {
        if(Integer.bitCount(sizeX) != 1 || Integer.bitCount(sizeY) != 1 || Integer.bitCount(sizeZ) != 1) {
            throw new IllegalArgumentException("size dimensions must be multiples of 2.");
        }
        xShift = 0;
        zShift = xShift + Integer.numberOfTrailingZeros(sizeX);
        yShift = zShift + Integer.numberOfTrailingZeros(sizeZ);
        xMask = sizeX - 1;
        yMask = sizeY - 1;
        zMask = sizeZ - 1;
    }
    
    public boolean isAddXInBounds(int index, int x) {
        index >>>= xShift;
        return (index & ~xMask) == ((index + x) & ~xMask);
    }
    
    public int addX(int index, int x) {
        x <<= xShift;
        if((index & ~(xMask << xShift)) != ((index + x) & ~(xMask << xShift))) {
            throw new IndexOutOfBoundsException("invalid x value: " + (((index >>> xShift) & xMask) + (x >>> xShift)));
        }
        return index + x;
    }
    
    public int cyclicAddX(int index, int x) {
        x <<= xShift;
        x += index;
        return (x & (xMask << xShift)) | (index & ~(xMask << xShift));
    }
    
    public int setX(int index, int x) {
        x <<= xShift;
        return (x & (xMask << xShift)) | (index & ~(xMask << xShift));
    }
    
    public int getX(int index) {
        return (index >>> xShift) & xMask;
    }
    
    public int fastAddX(int index, int x) {
        return index + (x << xShift);
    }
    public int fastAddY(int index, int y) {
        return index + (y << yShift);
    }
    public int fastAddZ(int index, int z) {
        return index + (z << zShift);
    }
    
    public int fastIndex(int x, int y, int z) {
        return (x << xShift) | (y << yShift) | (z << zShift);
    }
    
    public static void main(String... args) {
        LocalBlockIndexXZYPositionUtil util = new LocalBlockIndexXZYPositionUtil(16, 32, 64);
        int index = util.fastIndex(3, 17, 53);
        System.out.println(index);
        index = util.cyclicAddX(index, -4);
        System.out.println(index);
        System.out.println(util.getX(index));
        index = util.addX(index, 10);
        System.out.println(index);
        System.out.println(util.getX(index));
    }
}
