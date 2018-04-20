/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.display.models;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector4f;
import com.jme3.texture.Texture;
import java.awt.Color;

/**
 *
 * @author Carl
 */
public class MaterialFactory{
    
    private final AssetManager assetManager;

    public MaterialFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }
    
    public Material generateUnshadedMaterial(ColorRGBA color){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color",  color);
        return material;
    }

    public Material generateLightingMaterial(ColorRGBA color){
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Diffuse",  color);
        if(true){
            material.setColor("Ambient",  color);
            //material.setColor("Specular", ColorRGBA.White);
            //material.setFloat("Shininess", 15);
        }
        return material;
    }
    
    public void generateAmbientColor(Material material, float ambient){
        if((material.getParam("Diffuse") != null) && (material.getParam("Ambient") != null)){
            ColorRGBA diffuseColor = (ColorRGBA) (material.getParam("Diffuse").getValue());
            Vector4f newAmbient = diffuseColor.toVector4f().multLocal(ambient, ambient, ambient, 1);
            material.setVector4("Ambient", newAmbient);
        }
    }

    public Material generateLightingMaterial(String textureFilePath){
        return generateLightingMaterial(textureFilePath, null);
    }
    
    public Material generateLightingMaterial(String textureFilePath, String normalMapFilePath){
        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        Texture textureDiffuse = loadTexture(textureFilePath);
        textureDiffuse.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("DiffuseMap", textureDiffuse);
        if(normalMapFilePath != null){
            Texture textureNormalMap = loadTexture(normalMapFilePath);
            material.setTexture("NormalMap", textureNormalMap);
        }
        material.setFloat("Shininess", 5);
        return material;
    }
    
    public Material generateUnshadedMaterial(String textureFilePath){
        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture texture = loadTexture(textureFilePath);
        texture.setWrap(Texture.WrapMode.Repeat);
        material.setTexture("ColorMap", texture);
        return material;
    }
    
    public Texture loadTexture(String filePath){
        return assetManager.loadTexture(new TextureKey(filePath, false));
    }

    public void setFilter_Nearest(Material material){
        String textureParameterName = ((material.getParam("DiffuseMap") != null)?"DiffuseMap":"ColorMap");
        Texture texture = material.getTextureParam(textureParameterName).getTextureValue();
        texture.setMagFilter(Texture.MagFilter.Nearest);
        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
    }
    
    public ColorRGBA getColor(Color color){
        return new ColorRGBA(getColorComponent(color.getRed()),
                             getColorComponent(color.getGreen()),
                             getColorComponent(color.getBlue()),
                             getColorComponent(color.getAlpha()));
    }
    
    private float getColorComponent(int colorComponent_255){
        return (colorComponent_255 / 255f);
    }

    public AssetManager getAssetManager(){
        return assetManager;
    }
}
