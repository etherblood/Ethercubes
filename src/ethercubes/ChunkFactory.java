/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

/**
 *
 * @author Philipp
 */
public interface ChunkFactory<C> {
    void populate(C chunk);
}
