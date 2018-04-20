/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings.implementation;


/**
 *
 * @author Philipp
 */
public class TestBlockSettings extends BlockSettingsImpl {
    public static byte NUM_BLOCKS = 0;
    public static final byte AIR = NUM_BLOCKS++;
    public static final byte WATER = NUM_BLOCKS++;
    public static final byte LEAFES = NUM_BLOCKS++;
    public static final byte GLASS = NUM_BLOCKS++;
    public static final byte DIRT = NUM_BLOCKS++;
    public static final byte GRASS = NUM_BLOCKS++;
    public static final byte STONE = NUM_BLOCKS++;
    public static final byte SAND = NUM_BLOCKS++;
    public static final byte WOOD = NUM_BLOCKS++;
    public static final byte SNOW = NUM_BLOCKS++;
    
    public TestBlockSettings() {
        //super(application.getAssetManager().loadMaterial("Materials/CubesMaterial.j3m"));
//        texture.setMagFilter(Texture.MagFilter.Nearest);
//        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//        super(new BlockChunk_Material(application.getAssetManager(), "Textures/terrain.png"));
        registerTile(8, false);
        registerTile(24, false);
        registerTile(39, false);
        registerTile(40, false);
        //TODO read tile transparency from texture
        
        //EAST, WEST, UP, DOWN, SOUTH, NORTH
        registerSimpleBlock(AIR, INVALID_TILE);
        registerBlock(GRASS, 1,1,0,2,1,1);
        registerSimpleBlock(GLASS, 8);
        registerSimpleBlock(LEAFES, 39);
        registerSimpleBlock(SAND, 18);
        registerSimpleBlock(STONE, 23);//9
        registerBlock(SNOW, 17,17,16,2,17,17);
        registerSimpleBlock(WATER, 40);
        registerBlock(WOOD, 3,3,4,4,3,3);
        registerSimpleBlock(DIRT, 2);
        
        if(!isBlockOpaque(GRASS) || !isBlockOpaque(SAND) || !isBlockOpaque(STONE) || !isBlockOpaque(SNOW) || !isBlockOpaque(WOOD) || !isBlockOpaque(DIRT)) {
            throw new RuntimeException();
        }
    }
    
}
