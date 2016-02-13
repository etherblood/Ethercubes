/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.data;

/**
 *
 * @author Philipp
 */
public interface PositionLimit {
    boolean isLegal(ChunkPosition pos);
    boolean isLegal(GlobalBlockPosition pos);
}
