/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ethercubes.world;

import ethercubes.data.GlobalBlockPosition;

/**
 *
 * @author Philipp
 */
public interface BlockWorld extends BlockWorldReadonly {
    void setBlock(GlobalBlockPosition pos, byte value);
}
