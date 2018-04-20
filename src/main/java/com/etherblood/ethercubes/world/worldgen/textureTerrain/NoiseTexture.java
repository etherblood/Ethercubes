/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.etherblood.ethercubes.world.worldgen.textureTerrain;

import com.etherblood.ethercubes.world.worldgen.SimplexNoise_octave;

/**
 *
 * @author Philipp
 */
public class NoiseTexture {

    private final int[] size;
    private final byte[] data;

    public NoiseTexture(int[] size, float[] scale, SimplexNoise_octave noise) {
        this.size = size;
        int length = 1;
        for (int i : size) {
            length *= i;
        }
        data = new byte[length];
        populateData(new int[size.length], 0, scale, noise);
    }

    private void populateData(int[] pos, int index, float[] scale, SimplexNoise_octave noise) {
        boolean leaf = index == scale.length;
        if (leaf) {
            for (int i = 0; i < scale[index]; i++) {
                pos[index] = i;
                data[dataIndex(pos)] = fromNoise(pos, scale, noise);
            }
        } else {
            for (int i = 0; i < scale[index]; i++) {
                pos[index] = i;
                populateData(pos, index + 1, scale, noise);
            }
        }
    }
    
    private byte fromNoise(int[] pos, float[] scale, SimplexNoise_octave noise) {
        switch(size.length) {
            case 2:
                return fromNoiseValue(noise.noise(pos[0] * scale[0], pos[1] * scale[1]));
            case 3:
                return fromNoiseValue(noise.noise(pos[0] * scale[0], pos[1] * scale[1], pos[2] * scale[2]));
            case 4:
                return fromNoiseValue(noise.noise(pos[0] * scale[0], pos[1] * scale[1], pos[2] * scale[2], pos[3] * scale[3]));
            default:
                throw new RuntimeException("invalid texture dimension: " + size.length);
        }
    }
    
    private byte fromNoiseValue(double value) {
        value *= 128;
        return (byte)value;
    }
    
    private int dataIndex(int... pos) {
        int index = 0;
        for (int i = 0; i < size.length; i++) {
            index *= size[i];
            index += pos[i];
        }
        return index;
    }
    
    public byte value(int... pos) {
        for (int i = 0; i < pos.length; i++) {
            pos[i] %= size[i];
        }
        return data[dataIndex(pos)];
    }
}
