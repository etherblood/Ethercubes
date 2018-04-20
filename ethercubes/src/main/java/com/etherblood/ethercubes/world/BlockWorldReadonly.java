/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world;

import com.etherblood.ethercubes.data.GlobalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface BlockWorldReadonly {
    byte getBlock(GlobalBlockPosition pos);
}
