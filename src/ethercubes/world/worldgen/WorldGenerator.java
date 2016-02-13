/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world.worldgen;

/**
 *
 * @author Philipp
 */
public interface WorldGenerator<C> {
    void firstPass(C chunk);
    void secondPass(C chunk);
}
