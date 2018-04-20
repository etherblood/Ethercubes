/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes;

import ethercubes.data.ChunkPosition;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Philipp
 */
public class CamChunkLoadManager {
    private WorldWrapper wm;
    private HelpShape lastShape = null;
    private HashSet<ChunkPosition> lastSet = new HashSet<ChunkPosition>();

    public CamChunkLoadManager(WorldWrapper wm) {
        this.wm = wm;
    }

    public void updateCamPos(HelpShape nextShape) {
        Collection<ChunkPosition> col;
        if(lastShape != null) {
            col = lastShape.toCollection();
            for (ChunkPosition pos : col) {
                if(!nextShape.intersects(pos)) {
                    hideChunk(pos);
                    destroyChunk(pos);
                }
            }
        }
        
        col = nextShape.toCollection();
        for (ChunkPosition pos : col) {
            if(lastShape == null || !lastShape.intersects(pos)) {
                generateChunk(pos);
                showChunk(pos);
            }
        }
        
        lastShape = nextShape;
    }
//    public void updateCamPos(HelpShape nextShape) {
//        HashSet<ChunkPosition> nextSet = new HashSet<ChunkPosition>(nextShape.toCollection());
////        Collection<ChunkPosition> col;
////        if(lastShape != null) {
////            col = lastShape.toCollection();
//            for (ChunkPosition pos : lastSet) {
////                if(!nextShape.intersects(pos)) {
//                if(!nextSet.contains(pos)) {
//                    hideChunk(pos);
//                    destroyChunk(pos);
//                }
//            }
////        }
//        
////        col = nextShape.toCollection();
//        for (ChunkPosition pos : nextSet) {
////            if(lastShape == null || !lastShape.intersects(pos)) {
//            if(!lastSet.contains(pos)) {
//                generateChunk(pos);
//                showChunk(pos);
//            }
//        }
//        
////        lastShape = nextShape;
//        lastSet = nextSet;
//    }
    private void generateChunk(ChunkPosition pos) {
        wm.createChunk(pos);
    }
    private void destroyChunk(ChunkPosition pos) {
        wm.deleteChunk(pos);
    }
    private void showChunk(ChunkPosition pos) {
    }
    private void hideChunk(ChunkPosition pos) {
    }
    
}
