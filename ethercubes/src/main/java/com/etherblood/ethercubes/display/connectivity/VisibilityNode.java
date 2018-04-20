/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.display.connectivity;

/**
 *
 * @author Philipp
 */
public class VisibilityNode<C> {
    public boolean[] legalDirections;
    public int remain;
    public C chunk;
    public int from;

    public VisibilityNode(int remain, C chunk, int from) {
        this.remain = remain;
        this.chunk = chunk;
        this.from = from;
    }
}
