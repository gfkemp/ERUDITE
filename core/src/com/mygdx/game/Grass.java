/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gregclemp
 */
public class Grass extends Plant{
    
    private Random r;
    
    public Grass(Map map, int xPos, int yPos){
        super(map, xPos, yPos);
        r = new Random();
        
        growthStages = new String[3];
        growthStages[0] = "[#113b3a]▓[]";
        growthStages[1] = "▓";
        growthStages[2] = "[#00" + 
                    Integer.toHexString(r.nextInt(25)+125)
                     + "00AA]▓[]";//"„";
        setGrowthSymbol();
        
        colour = "[#00" + 
                Integer.toHexString(r.nextInt(25)+125)
                + "00AA]";
    }
    
    @Override
    public void grow(){
        if (stage < 2 && r.nextInt(100) <= 1){
            stage++;
            setGrowthSymbol();
            if (stage == 2){
                spread();
                this.map.getCoordinate(xPos, yPos).backgroundColour = colour;
            }
        } /*else {
            if (stage == 2 && map.getCoordinate(this).getCharacter() != null) {
                stage = 0;
                setGrowthSymbol();
            }
        } */
    }
    
    @Override
    public void trample(){
        if (stage == 2) {
            stage = 0;
            setGrowthSymbol();
        }
    }
    
    @Override
    public void spread(){
        int planted = 0;
        int count = 0;
        
        while (planted < 9 && count < 30){
            int pX = r.nextInt(3) - 1;
            int pY = r.nextInt(3) - 1;

            int newX = pX + xPos;
            int newY = pY + yPos;

            if (pX != 0 || pY != 0){
                if (newX < map.getWidth()-1 && newX >= 1 && newY < map.getHeight()-1 && newY >= 1){
                    if (map.getCoordinate(newX, newY).get(2) == null){
                        map.getCoordinate(newX, newY).setGrass();
                        planted++;
                    }
                } else {
                    //world map logic
                    ArrayList cache = map.getNextMapCache(newX, newY);
                    Map nextMap = (Map) cache.get(0);
                    newX = (Integer) cache.get(1);
                    newY = (Integer) cache.get(2);
                    if (nextMap != null && nextMap.getCoordinate(newX, newY).get(2) == null){
                        nextMap.getCoordinate(newX, newY).setGrass();
                        planted++;
                    }
                }
            }
            
            count++;
        }
    }
    
    public void setGrowthSymbol(){
        symbol = growthStages[stage];
    }
    
    
}
