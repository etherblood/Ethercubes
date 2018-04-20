/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.chunk.compression;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Philipp
 */
public interface FastChunkCompressor {
    void compress(DataOutputStream buffer, byte[] chunk);
    void decompress(DataInputStream buffer, byte[] chunk);
}
