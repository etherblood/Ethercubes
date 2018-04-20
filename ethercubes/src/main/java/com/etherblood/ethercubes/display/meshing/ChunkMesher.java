package com.etherblood.ethercubes.display.meshing;

/**
 *
 * @author Philipp
 */
public interface ChunkMesher<C> {
    ChunkMeshingResult generateMesh(C chunk, int version);
}