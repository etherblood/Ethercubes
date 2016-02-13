///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package ethercubes.display.meshing.implementation;
//
//import ethercubes.display.meshing.implementation.helpers.RectangleMeshBuilder;
//import com.jme3.math.Vector3f;
//import ethercubes.statistics.TimeStatistics;
//import java.util.Arrays;
//import ethercubes.chunk.ChunkReadonly;
//import ethercubes.chunk.FastXZYChunk;
//import ethercubes.chunk.HasNeighbors;
//import ethercubes.data.ChunkSize;
//import ethercubes.data.Direction;
//import ethercubes.display.meshing.ChunkMesher;
//import ethercubes.display.meshing.ChunkMeshingResult;
//import ethercubes.display.meshing.implementation.helpers.AmbientOcclusionHelper;
//import ethercubes.settings.BlockSettings;
//import ethercubes.settings.implementation.TestBlockSettings;
//
///**
// *
// * @author Philipp
// */
//public final class GreedyMesher_old<C extends FastXZYChunk & HasNeighbors<C> & ChunkReadonly> implements ChunkMesher<C> {
//
//    private final BlockSettings blockSettings;
//    private final int invalidTile;
//    private final int chunkWidth;
//    private final int chunkHeight;
//    private final int chunkDepth;
//    private final RectangleMeshBuilder opaque = new RectangleMeshBuilder(), transparent = new RectangleMeshBuilder();
//    private final int[] position = new int[3];
//    private final int[] indexHelper = new int[3];
//    private final int[] chunkSize = new int[3];
//    private final int[] direction = new int[3];
//    private final int[] du = new int[3];
//    private final int[] dv = new int[3];
//    private final float[] corners = new float[12];
//    private final int[] mask;
//    private final int[] neighborMask;
//    private final float[] ambientLightLevels = new float[]{0.1f, 0.4f, 0.7f, 1};
//    private final int[] ambientShadowMasks = new int[9];
//    
//    private final Direction[] axisDirection = new Direction[3];
//    private final int[][] cornerOrders = new int[3][4];
//    private final int[][] neighborCornerOrders = new int[3][4];
//    private int[] cornerOrder;
//    private int[] neighborCornerOrder;
//    private RectangleMeshBuilder builder;
//    private C chunk;
//    private int neighborIndexOffset;
//    private int k, l, tmp, rectangleWidth, rectangleHeight, axis0, axis1, axis2, maskIndex, size0, size1, size2, pos0, pos1, pos2, index0, index1, index2;
//    private byte blockA, blockB;
//    private int texWidth, texHeight;
//    private int positionIndex;
//    private boolean transposeTex, neighborTransposeTex;
//    private boolean equalBlocks;
//    private boolean skipA, skipB;
//    private int baseBlock;
//    private boolean allEqual;
//    private Vector3f normal, inverseNormal;
//    private Direction neighborDirection, inverseNeighborDirection;
//    private C neighborChunk;
//    private final static int[] defaultOrder = new int[]{0, 1 ,2 ,3};
//    private final static int[] flippedOrder = new int[]{1, 3 ,0 ,2};
//
//    public GreedyMesher_old(BlockSettings blockSettings, ChunkSize size) {
//        this.blockSettings = blockSettings;
//        invalidTile = blockSettings.invalidTile();
//
//        chunkWidth = size.getX();
//        chunkHeight = size.getY();
//        chunkDepth = size.getZ();
//
//        indexHelper[0] = 1;
//        indexHelper[1] = chunkWidth * chunkDepth;
//        indexHelper[2] = chunkWidth;
//
//        chunkSize[0] = chunkWidth;
//        chunkSize[1] = chunkHeight;
//        chunkSize[2] = chunkDepth;
//        mask = new int[chunkWidth * chunkHeight * chunkDepth / Math.min(chunkWidth, Math.min(chunkHeight, chunkDepth))];
//        neighborMask = new int[mask.length];
//
//        du[0] = 0;
//        du[1] = 0;
//        du[2] = 0;
//        dv[0] = 0;
//        dv[1] = 0;
//        dv[2] = 0;
//        
//        axisDirection[0] = Direction.EAST;
//        axisDirection[1] = Direction.UP;
//        axisDirection[2] = Direction.NORTH;
//
//        Arrays.fill(mask, invalidTile);
//        Arrays.fill(neighborMask, invalidTile);
//        
//        cornerOrders[0][0] = 2;
//        cornerOrders[0][1] = 0;
//        cornerOrders[0][2] = 3;
//        cornerOrders[0][3] = 1;
//        
//        cornerOrders[1][0] = 3;
//        cornerOrders[1][1] = 2;
//        cornerOrders[1][2] = 1;
//        cornerOrders[1][3] = 0;
//        
//        cornerOrders[2][0] = 3;
//        cornerOrders[2][1] = 2;
//        cornerOrders[2][2] = 1;
//        cornerOrders[2][3] = 0;
//        
//        neighborCornerOrders[0][0] = 3;
//        neighborCornerOrders[0][1] = 1;
//        neighborCornerOrders[0][2] = 2;
//        neighborCornerOrders[0][3] = 0;
//        
//        neighborCornerOrders[1][0] = 0;
//        neighborCornerOrders[1][1] = 2;
//        neighborCornerOrders[1][2] = 1;
//        neighborCornerOrders[1][3] = 3;
//
//        neighborCornerOrders[2][0] = 2;
//        neighborCornerOrders[2][1] = 3;
//        neighborCornerOrders[2][2] = 0;
//        neighborCornerOrders[2][3] = 1;
//        
//        ambientShadowMasks[0] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperLeft, AmbientOcclusionHelper.upperLeft);
//        ambientShadowMasks[1] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperLeft, AmbientOcclusionHelper.upperRight)
//                                | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperRight, AmbientOcclusionHelper.upperLeft);
//        ambientShadowMasks[2] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperRight, AmbientOcclusionHelper.upperRight);
//        ambientShadowMasks[3] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperLeft, AmbientOcclusionHelper.lowerLeft)
//                                | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerLeft, AmbientOcclusionHelper.upperLeft);
//        ambientShadowMasks[4] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerRight, AmbientOcclusionHelper.upperLeft)
//                                    | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerLeft, AmbientOcclusionHelper.upperRight)
//                                    | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperLeft, AmbientOcclusionHelper.lowerRight)
//                                    | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperRight, AmbientOcclusionHelper.lowerLeft);
//        ambientShadowMasks[5] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.upperRight, AmbientOcclusionHelper.lowerRight)
//                                | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerRight, AmbientOcclusionHelper.upperRight);
//        ambientShadowMasks[6] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerLeft, AmbientOcclusionHelper.lowerLeft);
//        ambientShadowMasks[7] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerLeft, AmbientOcclusionHelper.lowerRight)
//                                | AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerRight, AmbientOcclusionHelper.lowerLeft);
//        ambientShadowMasks[8] = AmbientOcclusionHelper.singleOcclusionMask(AmbientOcclusionHelper.lowerRight, AmbientOcclusionHelper.lowerRight);
//    }
//
//    @Override
//    public ChunkMeshingResult generateMesh(C chunk, int version) {
//        long start = TimeStatistics.TIME_STATISTICS.start();
//        this.chunk = chunk;
//        greedy();
//        TimeStatistics.TIME_STATISTICS.end(start, getClass().getSimpleName());
//        start = TimeStatistics.TIME_STATISTICS.start();
//        ChunkMeshingResult result = new ChunkMeshingResult(opaque.build(), transparent.build(), chunk.getPosition(), version);
//        opaque.clear();
//        transparent.clear();
//        TimeStatistics.TIME_STATISTICS.end(start, RectangleMeshBuilder.class.getSimpleName());
//        return result;
//    }
//    
//    private void initAxis() {
//        axis1 = (axis0 + 1) % 3;
//        axis2 = (axis0 + 2) % 3;
//
//        size0 = chunkSize[axis0];
//        size1 = chunkSize[axis1];
//        size2 = chunkSize[axis2];
//
//        index0 = indexHelper[axis0];
//        index1 = indexHelper[axis1];
//        index2 = indexHelper[axis2];
//
//        direction[0] = 0;
//        direction[1] = 0;
//        direction[2] = 0;
//        direction[axis0] = 1;
//
//        neighborDirection = axisDirection[axis0];
//        inverseNeighborDirection = neighborDirection.inverse();
//
//        normal = new Vector3f(direction[0], direction[1], direction[2]);
//        inverseNormal = normal.negate();
//
//        neighborIndexOffset = index0;
//        neighborChunk = chunk;
//
//        cornerOrder = cornerOrders[axis0];
//        neighborCornerOrder = neighborCornerOrders[axis0];
//
//        transposeTex = axis0 == 0;
//        neighborTransposeTex = axis0 != 2;
//        
//        skipA = true;
//        skipB = true;
//        if (axis0 == 0) {
//            allEqual = true;
//            pos0 = 0;
//        } else if (allEqual) {
//            pos0 = size0 - 1;
//        } else {
//            pos0 = 0;
//        }
//    }
//
//    private void greedy() {
//        baseBlock = chunk.getBlockFast(0);
//        for (axis0 = 0; axis0 < 3; axis0++) {
//            initAxis();
//            while (pos0 < size0) {
//                if (pos0 + 1 == size0) {
//                    neighborChunk = chunk.getNeighbor(neighborDirection);
//                    if (neighborChunk == null) {
//                        break;
//                    }
//                    neighborIndexOffset = -pos0 * index0;
//                }
//                
//                maskIndex = 0;
//                for (pos2 = 0; pos2 < size2; pos2++) {
//                    for (pos1 = 0; pos1 < size1; pos1++) {
//                        positionIndex = pos0 * index0 + pos1 * index1 + pos2 * index2;//TODO: split between loop levels
//                        blockA = chunk.getBlockFast(positionIndex);
//                        blockB = neighborChunk.getBlockFast(positionIndex + neighborIndexOffset);
//
//                        if (allEqual && blockA != baseBlock) {
//                            allEqual = false;
//                        }
//                        equalBlocks = blockA == blockB;
//                        if (!equalBlocks) {
//                            if (blockA != TestBlockSettings.AIR && blockSettings.isBlockTransparent(blockB)) {
//                                skipA = false;
//                                mask[maskIndex] = blockSettings.tileFromBlock(blockA, neighborDirection) | (getAmbientOcclusionValues(neighborChunk, pos0 == size0 - 1? 0: pos0 + 1) << 24);
//                            }
//
//                            if (blockB != TestBlockSettings.AIR && blockSettings.isBlockTransparent(blockA)) {
//                                skipB = false;
//                                neighborMask[maskIndex] = blockSettings.tileFromBlock(blockB, inverseNeighborDirection) | (getAmbientOcclusionValues(chunk, pos0) << 24);
//                            }
//                        }
//                        maskIndex++;
//                    }
//                }
//
//                pos0++;
//
//                if (!skipA) {
//                    createRectanglesFromMask(mask, cornerOrder, transposeTex, normal);
//                }
//                if (!skipB) {
//                    createRectanglesFromMask(neighborMask, neighborCornerOrder, neighborTransposeTex, inverseNormal);
//                }
//            }
//        }
//    }
//    
//    private int getAmbientOcclusionValues(C c, int pos_0) {
//        int result = 0;
//        
//        for (int i = -1; i <= 1; i++) {
//            int pos_1 = pos1 + i;
//            C n = c;
//            if(pos_1 == -1) {
//                n = c.getNeighbor(axisDirection[axis1].inverse());
//                pos_1 = size1 - 1;
//                if(n == null) {
//                    continue;
//                }
//            } else if(pos_1 == size1) {
//                n = c.getNeighbor(axisDirection[axis1]);
//                pos_1 = 0;
//                if(n == null) {
//                    continue;
//                }
//            }
//            for (int j = -1; j <= 1; j++) {
//                int pos_2 = pos2 + j;
//                C n2 = n;
//                if (pos_2 == -1) {
//                    n2 = n.getNeighbor(axisDirection[axis2].inverse());
//                    pos_2 = size2 - 1;
//                    if (n2 == null) {
//                        continue;
//                    }
//                } else if (pos_2 == size2) {
//                    n2 = n.getNeighbor(axisDirection[axis2]);
//                    pos_2 = 0;
//                    if (n2 == null) {
//                        continue;
//                    }
//                }
//                
//                if (n2.getBlockFast(pos_0 * index0 + pos_1 * index1 + pos_2 * index2) != 0) {
//                    result |= ambientShadowMasks[3 * j + i + 4];
//                }
//            }
//        }
//        result = AmbientOcclusionHelper.bakeOcclusionMasks(result);
//        return result;
//    }
//    
//    private void createRectanglesFromMask(final int[] mask, final int[] cornerOrder, final boolean transposeTex, final Vector3f normal) {
//        position[axis0] = pos0;
//        maskIndex = 0;
//        for (pos2 = 0; pos2 < size2; pos2++) {
//            position[axis2] = pos2;
//            for (pos1 = 0; pos1 < size1;) {
//                position[axis1] = pos1;
//                if (mask[maskIndex] != invalidTile) {
//                    calcRectangleWidthAndHeight(mask);
//
//                    position[axis1] = pos1;
//                    du[axis1] = rectangleWidth;
//                    dv[axis2] = rectangleHeight;
//                    int tile = mask[maskIndex] & 0xffffff;
//                    
//                    int lightInfo = mask[maskIndex] >>> 24;
//                    boolean flipped = AmbientOcclusionHelper.isLowerLeftDiagonalDarker(lightInfo);
//                    int[] light = new int[4];
//                    light[cornerOrder[0]] = lightInfo & 0x3;
//                    lightInfo >>>= 2;
//                    light[cornerOrder[1]] = lightInfo & 0x3;
//                    lightInfo >>>= 2;
//                    light[cornerOrder[2]] = lightInfo & 0x3;
//                    lightInfo >>>= 2;
//                    light[cornerOrder[3]] = lightInfo & 0x3;
//                    
//                    builder = blockSettings.isTileOpaque(tile) ? opaque : transparent;
//
//
//                    if (transposeTex) {
//                        texWidth = rectangleHeight;
//                        texHeight = rectangleWidth;
//                    } else {
//                        texWidth = rectangleWidth;
//                        texHeight = rectangleHeight;
//                    }
//
//                    tmp = cornerOrder[0] * 3;
//                    corners[tmp++] = position[0];
//                    corners[tmp++] = position[1];
//                    corners[tmp] = position[2];
//                    
//                    tmp = cornerOrder[1] * 3;
//                    corners[tmp++] = position[0] + du[0];
//                    corners[tmp++] = position[1] + du[1];
//                    corners[tmp] = position[2] + du[2];
//                    
//                    tmp = cornerOrder[2] * 3;
//                    corners[tmp++] = position[0] + dv[0];
//                    corners[tmp++] = position[1] + dv[1];
//                    corners[tmp] = position[2] + dv[2];
//                    
//                    tmp = cornerOrder[3] * 3;
//                    corners[tmp++] = position[0] + du[0] + dv[0];
//                    corners[tmp++] = position[1] + du[1] + dv[1];
//                    corners[tmp] = position[2] + du[2] + dv[2];
//
//                    builder.prepareFaceIndices(flipped? flippedOrder: defaultOrder);
////                    builder.prepareFaceIndices(flipped? flippedOrder: defaultOrder);
//                    builder.prepareRectNormals(normal);
//                    builder.prepareTextureCoordinates(tile, texWidth, texHeight);
//                    builder.addCorner(corners[0], corners[1], corners[2], ambientLightLevels[light[0]]);
//                    builder.addCorner(corners[3], corners[4], corners[5], ambientLightLevels[light[1]]);
//                    builder.addCorner(corners[6], corners[7], corners[8], ambientLightLevels[light[2]]);
//                    builder.addCorner(corners[9], corners[10], corners[11], ambientLightLevels[light[3]]);
//                    
//                    
////                    lightInfo = mask[maskIndex] >>> 24;
////                    builder.addCorner(position[0], position[1], position[2], lightInfo & 0x3);
////                    lightInfo >>>= 2;
////                    builder.addCorner(position[0] + du[0], position[1] + du[1], position[2] + du[2], lightInfo & 0x3);
////                    lightInfo >>>= 2;
////                    builder.addCorner(position[0] + dv[0], position[1] + dv[1], position[2] + dv[2], lightInfo & 0x3);
////                    lightInfo >>>= 2;
////                    builder.addCorner(position[0] + du[0] + dv[0], position[1] + du[1] + dv[1], position[2] + du[2] + dv[2], lightInfo & 0x3);
//
//                    du[axis1] = 0;
//                    dv[axis2] = 0;
//
//                    for (l = 0; l < rectangleHeight; ++l) {
//                        tmp = maskIndex + l * size1;
//                        Arrays.fill(mask, tmp, tmp + rectangleWidth, invalidTile);
//                    }
//                    pos1 += rectangleWidth;
//                    maskIndex += rectangleWidth;
//                } else {
//                    pos1++;
//                    maskIndex++;
//                }
//            }
//        }
//    }
//
//    private void calcRectangleWidthAndHeight(final int[] mask) {
//        for (rectangleWidth = 1; pos1 + rectangleWidth < size1 && mask[maskIndex + rectangleWidth] == mask[maskIndex]; rectangleWidth++) {
//        }
//
//        for (rectangleHeight = 1; pos2 + rectangleHeight < size2; rectangleHeight++) {
//            for (k = 0; k < rectangleWidth; k++) {
//                if (mask[maskIndex + k + rectangleHeight * size1] != mask[maskIndex]) {
//                    return;
//                }
//            }
//        }
//    }
//
//}