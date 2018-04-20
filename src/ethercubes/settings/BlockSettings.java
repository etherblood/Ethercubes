/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.settings;

import ethercubes.data.Direction;

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
