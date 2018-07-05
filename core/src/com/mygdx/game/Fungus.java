/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gregclemp
 */
public class Fungus extends Plant{
    private Random r;
    
    public Fungus(Map map, int xPos, int yPos){
        super(map, xPos, yPos);
        r = new Random();
        
        colour = "[#" + 
                Integer.toHexString(r.nextInt(55)+200) + Integer.toHexString(r.nextInt(55)+200)
                + "FF]";
        
        growthStages = new String[4];
        growthStages[0] = "[#FFFFFF]▓[]";
        growthStages[1] = colour + "▓[]";
        String p = r.nextInt(2) > 0 ? "p" : "q";
        growthStages[2] = colour + p + "[]";
        
        int Q = r.nextInt(4);
        switch (Q) {
            case 0:
                growthStages[3] = colour + "P[]";
                break;
            case 1:
                growthStages[3] = colour + "¶[]";
                break;
            case 2:
                growthStages[3] = colour + "ß[]";
                break;
            case 3:
                growthStages[3] = colour + "9[]";
                break;
            default:
                break;
        }
        setGrowthSymbol();
    }
    
    @Override
    public void grow(){
        if (stage < 3 && r.nextInt(100) <= 1){
            stage++;
            setGrowthSymbol();
        }
    }

    
    @Override
    public void trample(){
        if (stage == 3) {
            setGrowthSymbol();
            map.getChar().addSpores(r.nextInt(4));
        }
    }
    
    @Override
    public void spread(){
        
    }
    
    public void setGrowthSymbol(){
        symbol = growthStages[stage];
    }
}
