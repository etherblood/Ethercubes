package ethercubes.display.meshing.implementation.helpers;

import ethercubes.listutil.ShortArrayList;
import ethercubes.listutil.FloatArrayList;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Philipp
 */
public class RectangleMeshBuilder {
    private final FloatArrayList fPositions = new FloatArrayList();
    private final FloatArrayList fNormals = new FloatArrayList();
    private final FloatArrayList fColors = new FloatArrayList();
    private final FloatArrayList fTextures = new FloatArrayList();
    private final ShortArrayList sIndices = new ShortArrayList();

    public RectangleMeshBuilder() {
    }

    public Mesh build() {
        Mesh mesh = new Mesh();
        mesh.setBuffer(VertexBuffer.Type.Position, 3, createBuffer(fPositions));
        mesh.setBuffer(VertexBuffer.Type.TexCoord, 3, createBuffer(fTextures));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, createBuffer(fNormals));
        mesh.setBuffer(VertexBuffer.Type.Color, 1, createBuffer(fColors));
        mesh.setBuffer(VertexBuffer.Type.Index, 1, createBuffer(sIndices));
        mesh.updateBound();
        return mesh;
    }

    private FloatBuffer createBuffer(FloatArrayList array) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.size());
        buffer.put(array.data(), 0, array.size());
        return buffer;
    }
    
    private ShortBuffer createBuffer(ShortArrayList array) {
        ShortBuffer buffer = BufferUtils.createShortBuffer(array.size());
        buffer.put(array.data(), 0, array.size());
        return buffer;
    }
    
    public void addCorner(float x, float y, float z, float brightness) {
        fPositions.add(x);
        fPositions.add(y);
        fPositions.add(z);
        
        fColors.add(brightness);
    }
    
    public void addTextureCoordinate(int tile, int width, int height) {
        fTextures.add(width);
        fTextures.add(height);
        fTextures.add(tile);
    }
    
    public void prepareFaceIndices(int[] order){
        addFaceIndices(fPositions.size() / 3, order);
    }
    
    private void addFaceIndices(int offset, int[] order){
        sIndices.add((short) (offset + order[2]));
        sIndices.add((short) (offset + order[0]));
        sIndices.add((short) (offset + order[1]));
        sIndices.add((short) (offset + order[1]));
        sIndices.add((short) (offset + order[3]));
        sIndices.add((short) (offset + order[2]));
    }
    
    public void prepareRectNormals(Vector3f normal){
        for(int i=0;i<4;i++){
            fNormals.add(normal.x);
            fNormals.add(normal.y);
            fNormals.add(normal.z);
        }
    }
    
    public void clear() {
        fPositions.clear();
        fNormals.clear();
        fColors.clear();
        fTextures.clear();
        sIndices.clear();
    }
}
