/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.display.meshing;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.texture.Texture;

/**
 *
 * @author Philipp
 */
public class CubesMaterial extends Material {
    public static final String COLOR_MAP = "ColorMap";
    public static final String LIGHT_DIRECTION = "LightDirection";
    public static final String AMBIENT_LIGHT = "AmbientLight";
    public static final String NUM_TILES = "NumTiles";
    public static final String ALPHA_DISCARD_THRESHOLD = "AlphaDiscardThreshold";
    public static final String TIME = "Time";
    private final Texture texture;
    
    public CubesMaterial(AssetManager assetManager, String blockTextureFilePath) {
        super(assetManager, "Shaders/Cubes/Cubes.j3md");
        texture = assetManager.loadTexture(new TextureKey(blockTextureFilePath, false));
        setTexture(COLOR_MAP, texture);
        setVector3(LIGHT_DIRECTION, new Vector3f(0.4f, 0.8f, 0.6f).normalize());
        setVector3(AMBIENT_LIGHT, new Vector3f(1, 1, 1));
        setVector2(NUM_TILES, new Vector2f(16, 16));
        setFloat(ALPHA_DISCARD_THRESHOLD, 0.1f);
//        setFloat(TIME, 0);
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
    }

    public Texture getTexture() {
        return texture;
    }
    
}
