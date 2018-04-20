/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.display.connectivity;

import com.etherblood.ethercubes.data.ChunkPosition;

/**
 *
 * @author Philipp
 */
public class ConnectivityNodeSym {
    private final ChunkPosition pos;
    private ConnectivityNodeSym sym;
    private final int from;
    private final ConnectivityNodeSym[] neighbors = new ConnectivityNodeSym[6];

    public ConnectivityNodeSym(ChunkPosition pos, int from) {
        this.pos = pos;
        this.from = from;
    }

    public ChunkPosition getPos() {
        return pos;
    }

    public int getFrom() {
        return from;
    }
    
    public ConnectivityNodeSym getSym() {
        return sym;
    }

    public void setSym(ConnectivityNodeSym sym) {
        this.sym = sym;
    }
    
    public ConnectivityNodeSym getNeighbor(int i) {
        return neighbors[i];
    }

    public void setNeighbor(int i, ConnectivityNodeSym neighbor) {
        neighbors[i] = neighbor;
    }

}
