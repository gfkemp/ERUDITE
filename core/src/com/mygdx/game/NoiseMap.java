/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

/**
 *
 * @author gregclemp
 */
public class NoiseMap {
    // 2D noise variables
    int nOutputWidth;
    int nOutputHeight;
    float[] fNoiseSeed2D;
    float[][] fPerlinNoise2D;

    int nOctaveCount = 4;
    float fScalingBias = 1f;

    public NoiseMap(int mapHeight, int mapWidth, int worldHeight, int worldWidth){
        nOutputWidth = mapWidth * worldWidth;
        nOutputHeight = mapHeight * worldHeight;

        fNoiseSeed2D = new float[nOutputWidth*nOutputHeight];
        fPerlinNoise2D = new float[nOutputWidth][nOutputHeight];

        for (int i = 0; i < nOutputWidth * nOutputHeight; i++) {
          fNoiseSeed2D[i] = (float) Math.random();
        }
    }

    void generateMap(){
        PerlinNoise2D(nOutputWidth, nOutputHeight, fNoiseSeed2D, nOctaveCount, fScalingBias, fPerlinNoise2D);
        //fill((int)(fPerlinNoise2D[y * nOutputWidth + x] * 255f));
    }

    void PerlinNoise2D(int nWidth, int nHeight, float[] fSeed, int nOctaves, float fBias, float[][] fOutput){
        for (int x = 0; x < nWidth; x++) {
            for (int y = 0; y < nHeight; y++){

                float fNoise = 0.0f;
                float fScaleAcc = 0.0f;
                float fScale = 1.0f;

                for (int o = 0; o < nOctaves; o++) {
                    int nPitch = nWidth >> o;
                    int nSampleX1 = (x / nPitch) * nPitch;
                    int nSampleY1 = (y / nPitch) * nPitch;

                    int nSampleX2 = (nSampleX1 + nPitch) % nWidth;          
                    int nSampleY2 = (nSampleY1 + nPitch) % nWidth;

                    float fBlendX = (float)(x - nSampleX1) / (float)nPitch;
                    float fBlendY = (float)(y - nSampleY1) / (float)nPitch;

                    float fSampleT = (1.0f - fBlendX) * fSeed[nSampleY1 * nWidth + nSampleX1] + fBlendX * fSeed[nSampleY1 * nWidth + nSampleX2];
                    float fSampleB = (1.0f - fBlendX) * fSeed[nSampleY2 * nWidth + nSampleX1] + fBlendX * fSeed[nSampleY2 * nWidth + nSampleX2];

                    fScaleAcc += fScale;
                    fNoise += (fBlendY * (fSampleB - fSampleT) + fSampleT) * fScale;
                    fScale = fScale / fBias;
                }

              // Scale to seed range
              fOutput[x][y] = fNoise / fScaleAcc;
            }
        }

    }

    public int getDepth(int x, int y, int worldX, int worldY) {
        int output = (int) (fPerlinNoise2D[x*worldX][y*worldY] * 200f) - 50;
        return output;
    }
}
