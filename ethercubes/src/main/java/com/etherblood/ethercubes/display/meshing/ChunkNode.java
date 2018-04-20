/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.display.meshing;

import com.etherblood.ethercubes.chunk.Versioned;
import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;

/**
 *
 * @author Philipp
 */
public class ChunkNode implements Versioned {
//    private BlockChunkReadonly chunk;
//    private int version;
//    private Node node = new Node();
    private final Node node = new Node();
    private Geometry opaque, transparent;
    private int version = 0;

    public ChunkNode(Material material) {
        opaque = new Geometry("opaque");
        opaque.setQueueBucket(RenderQueue.Bucket.Opaque);
        opaque.setMaterial(material);
        node.attachChild(opaque);
        
        transparent = new Geometry("transparent");
        transparent.setQueueBucket(RenderQueue.Bucket.Transparent);
        transparent.setMaterial(material);
        node.attachChild(transparent);
    }
    
    public ChunkNode(Material material, Mesh opaqueMesh, Mesh transparentMesh) {
        this(material);
        setOpaque(opaqueMesh);
        setTransparent(transparentMesh);
    }
    
//    @Override
//    public void setMaterial(Material material) {
//        opaque.setMaterial(material);
//        transparent.setMaterial(material);
//    }

    @Override
    public int getVersion() {
        return version;
    }
    
    @Override
    public void incVersion() {
        version++;
    }

    @Override
    public void setVersion(int version) {
        this.version = version;
    }

//    public int getVersion() {
//        return version;
//    }
//
//    public void setVersion(int version) {
//        this.version = version;
//    }
    
//    public boolean isUpdated() {
//        return version == chunk.getVersion();
//    }
//    public void setUpdated() {
//        version = chunk.getVersion();
//    }
//    public void invalidate() {
//        version = ~chunk.getVersion();
//    }
    
    public final void setOpaque(Mesh mesh) {
        opaque.setMesh(mesh);
    }
    public final void setTransparent(Mesh mesh) {
        transparent.setMesh(mesh);
    }
    
//    }

    //    public BlockChunkReadonly getChunk() {
    //        return chunk;
    //    }
    //    @Override
    //    public void setSpatial(Spatial spatial){
    //        Spatial oldSpatial = this.spatial;
    //        super.setSpatial(spatial);
    //        if(spatial instanceof Node){
    //            Node parentNode = (Node) spatial;
    //            parentNode.attachChild(node);
    //        }
    //        else if(oldSpatial instanceof Node){
    //            Node oldNode = (Node) oldSpatial;
    //            oldNode.detachChild(node);
    //        }
    //    }
    //
    //    @Override
    //    protected void controlUpdate(float tpf) {
    //    }
    //    }
    //    @Override
    //    protected void controlRender(RenderManager rm, ViewPort vp) {
    public Node getNode() {
        return node;
    }
}
