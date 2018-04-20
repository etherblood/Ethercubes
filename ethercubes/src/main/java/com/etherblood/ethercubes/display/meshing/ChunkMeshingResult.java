package com.etherblood.ethercubes.display.meshing;

import com.etherblood.ethercubes.data.ChunkPosition;
import com.jme3.scene.Mesh;

/**
 *
 * @author Philipp
 */
public class ChunkMeshingResult {
    private final Mesh opaque, transparent;
    private final ChunkPosition pos;
    private final int version;

    public ChunkMeshingResult(Mesh opaque, Mesh transparent, ChunkPosition pos, int version) {
        this.opaque = opaque;
        this.transparent = transparent;
        this.pos = pos;
        this.version = version;
    }

    public Mesh getOpaque() {
        return opaque;
    }

    public Mesh getTransparent() {
        return transparent;
    }

    public ChunkPosition getPos() {
        return pos;
    }

    public int getVersion() {
        return version;
    }
}
