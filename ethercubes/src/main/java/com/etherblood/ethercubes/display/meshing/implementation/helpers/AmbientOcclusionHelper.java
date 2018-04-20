package com.etherblood.ethercubes.display.meshing.implementation.helpers;

/**
 *
 * @author Philipp
 */
public class AmbientOcclusionHelper {

    public static final int upperLeft = 0;
    public static final int upperRight = 1;
    public static final int lowerLeft = 2;
    public static final int lowerRight = 3;
    private static final int[] bakedVertexOcclusions = new int[16];
    private static final boolean[] lowerLeftDiagonalDarker = new boolean[256];
    
    static {
        for (int i = 0; i < bakedVertexOcclusions.length; i++) {
            bakedVertexOcclusions[i] = bakeVertexOcclusionHelper(i);
        }
        for (int i = 0; i < lowerLeftDiagonalDarker.length; i++) {
            lowerLeftDiagonalDarker[i] = isLowerLeftDiagonalDarkerHelper(i);
        }
    }
    
    public static int bakeOcclusionMasks(int faceData) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int value = (faceData >>> (4 * i)) & 0xf;
            value = bakedVertexOcclusions[value];
            result |= value << (2 * i);
        }
        return result;
    }

    public static int singleOcclusionMask(int vertexIndex, int lightIndex) {
        return 1 << (lightIndex + 4 * vertexIndex);
    }
    
    public static boolean isLowerLeftDiagonalDarker(int bakedValue) {
        return lowerLeftDiagonalDarker[bakedValue];
    }
    
    private static int bakeVertexOcclusionHelper(int vertexData) {
        int a = (1 << lowerLeft) | (1 << upperRight);
        int b = (1 << lowerRight) | (1 << upperLeft);
        if((vertexData & a) == a || (vertexData & b) == b) {
            return 0;
        }
        return 3 ^ Integer.bitCount(vertexData);
    }

    private static boolean isLowerLeftDiagonalDarkerHelper(int bakedValue) {
        int upperLeftValue = (bakedValue >>> (AmbientOcclusionHelper.upperLeft * 2)) & 0x3;
        int upperRightValue = (bakedValue >>> (AmbientOcclusionHelper.upperRight * 2)) & 0x3;
        int lowerLeftValue = (bakedValue >>> (AmbientOcclusionHelper.lowerLeft * 2)) & 0x3;
        int lowerRightValue = (bakedValue >>> (AmbientOcclusionHelper.lowerRight * 2)) & 0x3;
        return upperLeftValue + lowerRightValue > lowerLeftValue + upperRightValue;
    }
}
