/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings.implementation;

import com.etherblood.ethercubes.data.Direction;
import com.etherblood.ethercubes.listutil.BoolArrayList;
import com.etherblood.ethercubes.settings.BlockSettings;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 *
 * @author Philipp
 */
public class BlockSettingsImpl implements BlockSettings {
//    private final CubesMaterial material;
    public final static int INVALID_TILE = -1;
    private final ArrayList<EnumMap<Direction, Integer>> blockTiles = new ArrayList<EnumMap<Direction, Integer>>();
    private final BoolArrayList tileOpaque = new BoolArrayList();
    private final BoolArrayList blockOpaque = new BoolArrayList();

//    public BlockSettingsImpl(CubesMaterial material) {
//        this.material = material;
//    }

    public void registerSimpleBlock(byte block, int tile) {
        registerBlock(block, tile,tile,tile,tile,tile,tile);
    }
    public void registerBlock(byte block, int... tiles) {
        int blockIndex = block & 0xFF;
        if(tiles.length != Direction.values().length) {
            throw new RuntimeException("must define a tile for each direction");
        }
        while(numBlocks() <= blockIndex) {
            blockTiles.add(null);
            blockOpaque.add(false);
        }
        EnumMap<Direction, Integer> map = new EnumMap<Direction, Integer>(Direction.class);
        int i = 0;
        for (Direction direction: Direction.values()) {
            map.put(direction, tiles[i++]);
        }
        blockTiles.set(blockIndex, map);
        
        boolean opaque = true;
        for (int tile : tiles) {
            if(tile == INVALID_TILE || isTileTransparent(tile)) {
                opaque = false;
                break;
            }
        }
        blockOpaque.set(blockIndex, opaque);
    }
    
    public void registerTile(int tile, boolean opaque) {
        while (this.tileOpaque.size() <= tile) {            
            this.tileOpaque.add(true);
        }
        this.tileOpaque.set(tile, opaque);
    }
    
    @Override
    public int tileFromBlock(byte block, Direction face) {
        int blockIndex = block & 0xFF;
        return blockTiles.get(blockIndex).get(face);
    }

    @Override
    public boolean isTileOpaque(int tile) {
//        if(tile == INVALID_TILE) {
//            return false;
//        }
        return tileOpaque.get(tile);
    }

    public boolean isTileTransparent(int tile) {
        return !isTileOpaque(tile);
    }
    
    private int numBlocks() {
        return blockTiles.size();
    }

    @Override
    public boolean isBlockOpaque(byte block) {
        int blockIndex = block & 0xFF;
        return blockOpaque.get(blockIndex);
    }

    @Override
    public int invalidTile() {
        return INVALID_TILE;
    }

//    @Override
//    public CubesMaterial getMaterial() {
//        return material;
//    }

    @Override
    public boolean isBlockTransparent(byte block) {
        return !isBlockOpaque(block);
    }

    @Override
    public boolean isBlockVisible(byte block) {
        return block != TestBlockSettings.AIR;
    }
}
