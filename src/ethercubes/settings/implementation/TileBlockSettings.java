/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.settings.implementation;


/**
 *
 * @author Philipp
 */
public class TileBlockSettings extends BlockSettingsImpl {
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
    public static final byte ICE = NUM_BLOCKS++;
    public static final byte IRON_ORE = NUM_BLOCKS++;
    public static final byte COAL_ORE = NUM_BLOCKS++;
    public static final byte GOLD_ORE = NUM_BLOCKS++;
    
    public TileBlockSettings() {
        //super(application.getAssetManager().loadMaterial("Materials/CubesMaterial.j3m"));
//        texture.setMagFilter(Texture.MagFilter.Nearest);
//        texture.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
//        super(new BlockChunk_Material(application.getAssetManager(), "Textures/terrain.png"));

        
        registerTile(49, false);
        registerTile(52, false);
        registerTile(67, false);
        registerTile(205, false);
        registerTile(255, true);
        //TODO read tile transparency from texture
        
        //EAST, WEST, UP, DOWN, SOUTH, NORTH
        registerSimpleBlock(AIR, INVALID_TILE);
        registerBlock(GRASS, 3,3,166,2,3,3);
        registerSimpleBlock(GLASS, 49);
        registerSimpleBlock(ICE, 67);
        registerSimpleBlock(LEAFES, 52);
        registerSimpleBlock(SAND, 18);
        registerSimpleBlock(STONE, 1);
        registerSimpleBlock(GOLD_ORE, 32);
        registerSimpleBlock(IRON_ORE, 33);
        registerSimpleBlock(COAL_ORE, 34);
        registerBlock(SNOW, 68,68,66,2,68,68);
        registerSimpleBlock(WATER, 205);
        registerBlock(WOOD, 20,20,21,21,20,20);
        registerSimpleBlock(DIRT, 2);
        
        if(!isBlockOpaque(GRASS) || !isBlockOpaque(SAND) || !isBlockOpaque(STONE) || !isBlockOpaque(SNOW) || !isBlockOpaque(WOOD) || !isBlockOpaque(DIRT)) {
            throw new RuntimeException();
        }
    }
    
}
