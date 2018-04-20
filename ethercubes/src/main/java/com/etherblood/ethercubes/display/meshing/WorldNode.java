/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.display.meshing;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import java.util.HashMap;

/**
 *
 * @author Philipp
 */
public class WorldNode<K, N extends Node> extends AbstractControl {
    private HashMap<K, N> chunks = new HashMap<K, N>();
    private Node node = new Node();

    public WorldNode() {
        node.setShadowMode(ShadowMode.CastAndReceive);
    }
    
    public N getChunk(K pos) {
        return chunks.get(pos);
    }
    
    public void addChunk(K pos, N chunk) {
        chunks.put(pos, chunk);
        node.attachChild(chunk);
    }
    public void removeChunk(K pos) {
        node.detachChild(chunks.remove(pos));
    }
    
    @Override
    public void setSpatial(Spatial spatial){
        Spatial oldSpatial = this.spatial;
        super.setSpatial(spatial);
        if(spatial instanceof Node){
            Node parentNode = (Node) spatial;
            parentNode.attachChild(node);
        }
        else if(oldSpatial instanceof Node){
            Node oldNode = (Node) oldSpatial;
            oldNode.detachChild(node);
        }
    }
    
//    public void showChunk(K pos) {
//        N chunk = chunks.get(pos);
//        if(chunk == null) {
////            throw new RuntimeException("Chunk at " + pos + " was not found and thus cannot be shown.");
//            return;
//        }
//        while(spatial.removeControl(chunk)) {}
//        spatial.addControl(chunk);
//    }
//    public void hideChunk(K pos) {
//        N chunk = chunks.get(pos);
//        if(chunk == null) {
////            throw new RuntimeException("Chunk at " + pos + " was not found and thus cannot be hidden.");
//            return;
//        }
//        while(spatial.removeControl(chunk)) {}
//    }
    
    @Override
    protected void controlUpdate(float tpf) {
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
}
