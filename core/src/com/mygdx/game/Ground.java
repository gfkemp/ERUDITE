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
    private String[] fertileColours;
    
    public Ground(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        fertility = 0;
        
        fertileColours = new String[5];
        fertileColours[0] = "[WHITE]";
        fertileColours[1] = "[#E1D7CD]";
        fertileColours[2] = "[#DBB7A2]";
        fertileColours[3] = "[#C19270]";
        fertileColours[4] = "[#997652]";
        
        
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
        if (waterSupply > 100){
            fertility = 4;
        } else if (waterSupply > 60){
            fertility = 3;
        } else if (waterSupply > 30){
            fertility = 2;
        } else if (waterSupply > 15){
            fertility = 2;
        } else {
            setColour("[WHITE]");
        }
        
        waterSupply = Math.min(waterSupply, 100);
        String r = Integer.toHexString((int) (255 - (waterSupply*1.5)));
        String g = Integer.toHexString((int) (255 - (waterSupply*2)));
        String b = Integer.toHexString((int) (255 - (waterSupply*2.5)));
        
        setColour(fertileColours[fertility]);
    }

    public int getFertility() {
        return fertility;
    }

    public void setFertility(int fertility) {
        this.fertility = fertility;
    }
}
