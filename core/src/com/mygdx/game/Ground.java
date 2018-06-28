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
public class Ground extends Thing {
    
    private boolean channel;
    private int fertility;
    
    public Ground(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        fertility = 0;
        
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
    
    public void checkFertility(){
        int waterSupply = map.getCoordinate(xPos, yPos+1).getWaterLevel() +
                          map.getCoordinate(xPos+1, yPos).getWaterLevel() +
                          map.getCoordinate(xPos, yPos-1).getWaterLevel() +
                          map.getCoordinate(xPos-1, yPos).getWaterLevel();
        if (waterSupply > 20){
            fertility = 2;
        } else {
            setColour("[WHITE]");
        }
        
        String r = Integer.toHexString((int) (255 - (waterSupply*0.5)));
        String g = Integer.toHexString((int) (255 - (waterSupply*0.7)));
        String b = Integer.toHexString((int) (255 - (waterSupply*0.9)));
        
        
        setColour("[#" + r + g + b + "]");
    }
}
