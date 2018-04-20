/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.settings;

import com.etherblood.ethercubes.data.Direction;

/**
 *
 * @author Philipp
 */
public interface BlockSettings {
    int tileFromBlock(byte block, Direction face);
    boolean isBlockVisible(byte block);
    boolean isBlockOpaque(byte block);
    boolean isBlockTransparent(byte block);
    boolean isTileOpaque(int tile);
    int invalidTile();
//    Material getMaterial();
}
