/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.Random;

/**
 *
 * @author gregclemp
 */
public class Grass extends Plant{
    
    private Random r;
    
    public Grass(Map map, int xPos, int yPos){
        super(map, xPos, yPos);
        growthStages = new String[3];
        growthStages[0] = ".";
        growthStages[1] = ",";
        growthStages[2] = "w";//"„";
        setGrowthSymbol();
        r = new Random();
        
        colour = "[#00" + 
                Integer.toHexString(r.nextInt(50)+100)
                + "00]";
    }
    
    @Override
    public void grow(){
        if (stage < 2){
            stage++;
            setGrowthSymbol();
        } else {
            spread();
        }
    }
    
    @Override
    public void spread(){
        int pX = r.nextInt(3) - 1;
        int pY = r.nextInt(3) - 1;
        
        int newX = pX + xPos;
        int newY = pY + yPos;
        
        if (pX != 0 || pY != 0){
            if (newX < map.getWidth() && newX >= 0 && newY < map.getHeight() && newY >= 0){
                map.getCoordinate(newX, newY).setGrass();
            }
        }
    }
    
    public void setGrowthSymbol(){
        symbol = growthStages[stage];
    }
}
