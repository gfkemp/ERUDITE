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
    private String sporeColour;
    
    public Fungus(Map map, int xPos, int yPos, String startColour){
        super(map, xPos, yPos);
        r = new Random();
        sporeColour = startColour;
        
        colour = "[#" + 
                Integer.toHexString(r.nextInt(55)+200) + Integer.toHexString(r.nextInt(55)+200)
                + "F0]";
        
        growthStages = new String[4];
        growthStages[0] = sporeColour + "▓[]";
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
        if (stage >= 1 && !map.getCoordinate(this).getGround().isStone()){
            die();
        } else if (stage < 2 && r.nextInt(100) <= 1){
            stage++;
            setGrowthSymbol();
        } else if (stage < 3 && r.nextInt(200) <= 1){
            stage++;
            setGrowthSymbol();
        } else if (stage == 3 && r.nextInt(500) <= 1){
            trample();
            map.getCoordinate(this).getGround().turnToDirt();
        }
    }

    
    @Override
    public void trample(){
        if (stage == 3) {
            die();
            map.getCoordinate(this).getGround().turnToDirt();
            //map.getChar().addSpores(r.nextInt(4));
            
            //coordinate.setSpore()
            
            ArrayList<Coordinate> targets = new ArrayList<Coordinate>();
            for (int i = -1; i <= 1; i++){
                targets.add(map.getSafeCoordinate(2 + xPos, i + yPos));
                targets.add(map.getSafeCoordinate(-2 + xPos, i + yPos));
                targets.add(map.getSafeCoordinate(i + xPos, 2 + yPos));
                targets.add(map.getSafeCoordinate(i + xPos, -2 + yPos));
            }
            
            for (Coordinate coordinate : targets){
                if (coordinate != null){
                    coordinate.setSpore(colour);
                }
            }
        }
    }
    
    @Override
    public void spread(){
        
    }
    
    public void setGrowthSymbol(){
        symbol = growthStages[stage];
    }
}
