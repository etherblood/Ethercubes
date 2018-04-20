/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.display.connectivity;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import ethercubes.chunk.ChunkReadonly;
import ethercubes.chunk.HasNeighborsReadonly;
import ethercubes.chunk.NeighborVisibilityChunk;
import ethercubes.data.ChunkPosition;
import ethercubes.data.Direction;
import ethercubes.data.NeighborVisibility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Philipp
 */
public class ConnectivityGraph<C extends ChunkReadonly & NeighborVisibilityChunk & HasNeighborsReadonly<C>> {
    public Material mat;
    public Node node = new Node();
    private HashMap<ChunkPosition, ConnectivityNodeSym[]> nodes = new HashMap<ChunkPosition, ConnectivityNodeSym[]>();
    private HashMap<ChunkPosition, Geometry> geos = new HashMap<ChunkPosition, Geometry>();

    public void add(C chunk) {
        ChunkPosition pos = chunk.getPosition();
        ConnectivityNodeSym[] currentNodes = new ConnectivityNodeSym[6];
        for (int i = 0; i < 6; i++) {
            ConnectivityNodeSym currentNode = new ConnectivityNodeSym(pos, i);
            Direction from = Direction.values()[i];
            ChunkPosition prevPos = from.neighbor(pos);
            ConnectivityNodeSym[] prevNodes = nodes.get(prevPos);
            if (prevNodes != null) {
                Direction to = from.inverse();
                ConnectivityNodeSym prevNode = prevNodes[to.ordinal()];
                prevNode.setSym(currentNode);
                currentNode.setSym(prevNode);
            }
            currentNodes[i] = currentNode;
        }
        nodes.put(pos, currentNodes);
        
        Mesh mesh = new Mesh();
        mesh.setMode(Mesh.Mode.Lines);
        Vector3f[] vertices = new Vector3f[6];
        for (int i = 0; i < 6; i++) {
            vertices[i] = fromDir(i).mult(new Vector3f(chunk.getSize().getX(), chunk.getSize().getY(), chunk.getSize().getZ()));
        }
        ArrayList<Short> indices = new ArrayList<Short>();
        
        NeighborVisibility visibility = chunk.getNeighborVisibility();
        for (int i = 0; i < 5; i++) {
            for (int j = i + 1; j < 6; j++) {
                if(visibility.visibleToEachother(Direction.values()[i], Direction.values()[j])) {
                    currentNodes[i].setNeighbor(j, currentNodes[j]);
                    currentNodes[j].setNeighbor(i, currentNodes[i]);
                    
                    indices.add((short)i);
                    indices.add((short)j);
                }
            }
        }
        short[] indicesArr = new short[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArr[i] = indices.get(i);
        }
        
        mesh.setBuffer(VertexBuffer.Type.Index, 1, BufferUtils.createShortBuffer(indicesArr));
        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
        
        Geometry geo = new Geometry();
        geo.setMesh(mesh);
        geo.setMaterial(mat);
        geo.setCullHint(Spatial.CullHint.Never);
        geo.setLocalTranslation(chunk.getPosition().getX() * chunk.getSize().getX(), chunk.getPosition().getY() * chunk.getSize().getY(), chunk.getPosition().getZ() * chunk.getSize().getZ());
        node.attachChild(geo);
        geos.put(pos, geo);
    }
    
//    EAST, WEST, UP, DOWN, NORTH, SOUTH;
    private Vector3f fromDir(int dir) {
        switch(dir) {
            case 0:
                return new Vector3f(1, 0.5f, 0.5f);
            case 1:
                return new Vector3f(0, 0.5f, 0.5f);
            case 2:
                return new Vector3f(0.5f, 1, 0.5f);
            case 3:
                return new Vector3f(0.5f, 0, 0.5f);
            case 4:
                return new Vector3f(0.5f, 0.5f, 1);
            default:
                return new Vector3f(0.5f, 0.5f, 0);
        }
    }
    
    public void remove(ChunkPosition pos) {
        ConnectivityNodeSym[] currentNodes = nodes.remove(pos);
        for (int i = 0; i < 6; i++) {
            ConnectivityNodeSym sym = currentNodes[i].getSym();
            sym.setSym(null);
        }
    }
    
    public Set<ChunkPosition> flood(C from, int range) {
        HashSet<ChunkPosition> result = new HashSet<ChunkPosition>();
        result.add(from.getPosition());
        VisibilityNode<C> startNode = new VisibilityNode<C>(range, from, -1);
        startNode.legalDirections = new boolean[]{true, true, true, true, true, true};
        Arrays.fill(startNode.legalDirections, true);
        LinkedList<VisibilityNode<C>> open = new LinkedList<VisibilityNode<C>>();
        open.add(startNode);
        while (!open.isEmpty()) {
            VisibilityNode<C> currentNode = open.poll();
            NeighborVisibility visibility = currentNode.chunk.getNeighborVisibility();
            if(visibility == null) {
                continue;
            }
            for (int i = 0; i < 6; i++) {
                if(currentNode.legalDirections[i]) {
                    Direction direction = Direction.values()[i];
                    if(currentNode.from == -1 || visibility.visibleToEachother(direction, Direction.values()[currentNode.from])) {
                        ChunkPosition nextPos = direction.neighbor(currentNode.chunk.getPosition());
    //                    if(result.contains(nextPos)) {
    //                        continue;
    //                    }
                        if(!result.contains(nextPos)) result.add(nextPos);
                        if(currentNode.remain <= 1) {
                            continue;
                        }
                        C nextChunk = currentNode.chunk.getNeighbor(direction);
                        if(nextChunk == null) {
                            continue;
                        }
                        int inverseDirection = direction.inverse().ordinal();
                        VisibilityNode<C> nextNode = new VisibilityNode<C>(currentNode.remain - 1, nextChunk, inverseDirection);
                        nextNode.legalDirections = Arrays.copyOf(currentNode.legalDirections, 6);
                        nextNode.legalDirections[inverseDirection] = false;
                        open.add(nextNode);
                    }
                }
            }
        }
        
        return result;
    }
}
