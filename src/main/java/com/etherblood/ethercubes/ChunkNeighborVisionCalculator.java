/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes;

import com.etherblood.ethercubes.chunk.ChunkReadonly;
import com.etherblood.ethercubes.chunk.HasNeighbors;
import com.etherblood.ethercubes.chunk.NeighborVisibilityChunk;
import com.etherblood.ethercubes.data.ChunkPosition;
import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.data.NeighborVisibility;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class ChunkNeighborVisionCalculator<C extends NeighborVisibilityChunk& HasNeighbors<C>& ChunkReadonly> {
//    private ArrayList<SearchNode> open = new ArrayList<SearchNode>();
//    private HashSet<Face> closed = new HashSet<Face>();
    
//    public <C extends NeighborVisibilityChunk& HasNeighbors<C>& ChunkReadonly> Set<ChunkPosition> search(C startChunk, int range, Direction... directions) {
//        long nanos = -System.nanoTime();
//        HashSet<ChunkPosition> result = new HashSet<ChunkPosition>();
//        int dirs = 0;
//        for (Direction direction : directions) {
//            dirs |= 1 << direction.ordinal();
//            open.add(new SearchNode(dirs, startChunk, range, new Face(startChunk.getPosition(), direction)));
//        }
//        result.add(startChunk.getPosition());
//        
//        while(!open.isEmpty()) {
//            SearchNode<C> current = open.get(open.size() - 1);
//            open.remove(open.size() - 1);
//            if(current.steps == 0) continue;
//            Face face = current.face;
//            Direction from = face.direction.inverse();
////            if(closed.contains(face)) continue;
////            closed.add(face);
//            int currentMask = current.directions;
//            for (int i = 0; i < 6; i++) {
//                if(((1 << i) & currentMask) != 0) {
//                    Direction dir = Direction.values()[i];
//                    
//                    C neighbor = current.chunk.getNeighbor(current.face.direction);
//                    result.add(current.face.direction.neighbor(current.chunk.getPosition()));
//                    if(neighbor != null && neighbor.getNeighborVisibility() != null && neighbor.getNeighborVisibility().visibleToEachother(from, dir)) {
//
//                        Direction inverse = dir.inverse();
//                        int nextMask = currentMask & ~(1 << inverse.ordinal());
//                        SearchNode<C> nextNode = new SearchNode<C>(nextMask, neighbor, current.steps - 1, new Face(neighbor.getPosition(), dir));
//                        open.add(nextNode);
//                    }
//                }
//            }
//        }
//        nanos += System.nanoTime();
//        System.out.println(Util.humanReadableNanos(nanos) + "/visionField");
//        System.out.println(result.size() + " visionChunks");
//        return result;
//    }
    
    public Set<ChunkPosition> searchSimple(C startChunk, int range) {
        ArrayList<SimpleNode<C>> open = new ArrayList<SimpleNode<C>>();
        long nanos = -System.nanoTime();
        HashSet<ChunkPosition> result = new HashSet<ChunkPosition>();
        
        ChunkPosition pos = startChunk.getPosition();
        result.add(pos);
        for (Direction direction : Direction.values()) {
            C neighbor = startChunk.getNeighbor(direction);
            ChunkPosition nextPos = direction.neighbor(pos);
            result.add(nextPos);
            if(neighbor != null) {
                SimpleNode<C> nextNode = new SimpleNode<C>(neighbor, range, direction.inverse());
                open.add(nextNode);
            }
        }
        
        while(!open.isEmpty()) {
            SimpleNode<C> currentNode = removeBest(open);
            pos = currentNode.chunk.getPosition();
            NeighborVisibility visibility = currentNode.chunk.getNeighborVisibility();
            for (Direction direction : Direction.values()) {
                if(direction == currentNode.from) {
                    continue;
                }
                ChunkPosition nextPos = direction.neighbor(pos);
                if(result.contains(nextPos)) {
                    continue;
                }
                result.add(nextPos);
                if(visibility != null && visibility.visibleToEachother(currentNode.from, direction)) {
                    C neighbor = currentNode.chunk.getNeighbor(direction);
                    int remain = currentNode.steps - 1;
                    if(neighbor != null && remain > 0) {
                        SimpleNode<C> nextNode = new SimpleNode<C>(neighbor, remain, direction.inverse());
                        open.add(nextNode);
                    }
                }
            }
        }
        
        nanos += System.nanoTime();
        System.out.println(Util.humanReadableNanos(nanos) + "/visionField");
        System.out.println(result.size() + " visionChunks");
        return result;
    }
    private SimpleNode<C> removeBest(ArrayList<SimpleNode<C>> open) {
        int bestIndex = 0;
        int bestValue = open.get(0).steps;
        for (int i = 1; i < open.size(); i++) {
            int tmpValue = open.get(i).steps;
            if(tmpValue > bestValue) {
                bestValue = tmpValue;
                bestIndex = i;
            }
        }
        int lastIndex = open.size() - 1;
        if(lastIndex == bestIndex) {
            return  open.remove(lastIndex);
        }
        return open.set(bestIndex, open.remove(lastIndex));
    }
    
    private class SimpleNode<C extends NeighborVisibilityChunk& HasNeighbors<C>& ChunkReadonly> {
        public C chunk;
        public int steps;
        public Direction from;

        public SimpleNode(C chunk, int steps, Direction from) {
            this.chunk = chunk;
            this.steps = steps;
            this.from = from;
        }

        @Override
        public int hashCode() {
            return chunk.getPosition().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return ((SearchNode<C>) obj).chunk.getPosition().equals(chunk.getPosition());
        }
    }
    
    private class Face {
        public ChunkPosition pos;
        public Direction direction;

        public Face(ChunkPosition pos, Direction face) {
            this.pos = pos;
            this.direction = face;
        }

        @Override
        public int hashCode() {
            return 7 * pos.hashCode() + direction.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Face f = (Face) obj;
            return f.direction == direction && f.pos.equals(pos);
        }
        
    }
    
    private class SearchNode<C extends NeighborVisibilityChunk& HasNeighbors<C>& ChunkReadonly> {
        public int directions;
        public C chunk;
        public int steps;
        public Face face;

        public SearchNode(int directions, C chunk, int steps, Face face) {
            this.directions = directions;
            this.chunk = chunk;
            this.steps = steps;
            this.face = face;
        }

        @Override
        public int hashCode() {
            return face.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return ((SearchNode<C>) obj).face.equals(face);
        }
        
        
    }
}
