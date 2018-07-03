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
public class Ground extends Thing {
    
    private boolean channel;
    private int fertility;
    private String[] fertileColours;
    private Random r;
    
    public Ground(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        r = new Random();
        fertility = 0;
        
        fertileColours = new String[5];
        switch (r.nextInt(3)){
            default:
            case 0:
                fertileColours[0] = "[#113b3a]";
            case 1:
                fertileColours[0] = "[#195351]";
                break;
            case 2:
                fertileColours[0] = "[#b2c9c3]";
                break;
        }
        
        fertileColours[1] = "[#E1D7CD]";
        fertileColours[2] = "[#DBB7A2]";
        fertileColours[3] = "[#C19270]";
        fertileColours[4] = "[#997652]";
        
        this.colour = fertileColours[fertility];
        symbolGen();
    }
    
    public void symbolGen(){
        double r = Math.random();
        r = r*100;
        if (r < 40){
            symbol = "·";
        } else if (r < 50){
            symbol = ".";
        } else if (r < 60){
            symbol = "°";
        } else if (r < 90){
            symbol = "+";
        } else if (r < 96){
            symbol = "=";
        } else if (r < 97){
            symbol = "-";
        } else if (r < 98){
            symbol = "#";
        } else {
            symbol = "~";
        }
    }
    
    public void channel(){
        if (!channel){
            symbol = "u";
            channel = true;
        } else {
            unChannel();
        }
    }
    
    public void unChannel(){
        channel = false;
        symbolGen();
    }
    
    public boolean isChannel(){
        return channel;
    }
    
    public void checkFertility(int waterSupply){
        if (waterSupply >= 60){
            fertility = 4;
        } else if (waterSupply > 45){
            fertility = 3;
        } else if (waterSupply > 30){
            fertility = 2;
        } else if (waterSupply > 15){
            fertility = 1;
        } else {
            setColour("[WHITE]");
        }
        
        setColour(fertileColours[fertility]);
        this.map.getCoordinate(xPos, yPos).backgroundColour = "[#195351]";
    }

    public int getFertility() {
        return fertility;
    }

    public void addFertility(int add){
        fertility += add;
        
        if (fertility > 4){
            fertility = 4;
        } else if (fertility < 0){
            fertility = 0;
        }
        
        setColour(fertileColours[fertility]);
        this.map.getCoordinate(xPos, yPos).backgroundColour = "[#195351]";
    }
}
