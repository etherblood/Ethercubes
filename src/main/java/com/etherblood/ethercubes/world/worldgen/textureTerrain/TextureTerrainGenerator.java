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
public class TextureTerrainGenerator {
    private final NoiseTexture height_factor;
    private final NoiseTexture density_detailed;
    private final NoiseTexture density_rough;
    private final NoiseTexture density_weight;
//       private final double small = 0.1;
//    private final double medium = 0.01;
//    private final double big = 0.001;
//double rough = noise(density_rough, medium, x, y, z);
//        double detail = noise(density_detailed, small / 3, x, y, z);
//        double weight = noise(density_weight, big, x, y, z);
//        weight *= weight;
//        
//        double heightFactor = noise(height_factor, big, x, y, z);
//        heightFactor *= heightFactor;
//        double block = interpolate(detail, rough, weight);
//        double heightW = (double)y / (height * (heightFactor + 0.1));
    public TextureTerrainGenerator() {
        int[] size = new int[]{512, 64, 512};
        height_factor = new NoiseTexture(size, new float[]{1000, 1000, 1000}, new SimplexNoise_octave(0));
        density_detailed = new NoiseTexture(size, new float[]{30, 30, 30}, new SimplexNoise_octave(0));
        density_rough = new NoiseTexture(size, new float[]{100, 100, 100}, new SimplexNoise_octave(0));
        density_weight = new NoiseTexture(size, new float[]{1000, 1000, 1000}, new SimplexNoise_octave(0));
    }
    
}
