/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

/**
 *
 * @author Philipp
 */
public interface ChunkFactory<C> {
    void populate(C chunk);
}
