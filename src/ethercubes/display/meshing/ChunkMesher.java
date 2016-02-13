/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.display.meshing;

/**
 *
 * @author Philipp
 */
public interface ChunkMesher<C> {
    ChunkMeshingResult generateMesh(C chunk, int version);
}