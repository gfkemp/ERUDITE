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
public class Coordinate extends ArrayList{
    private Map map;
    private int xPos;
    private int yPos;
    private WaterSource source;
    private Water water;
    private Ground ground;
    private boolean voided = false;
    
    public Coordinate(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        for (int i = 0; i < 6; i++){
            add(null);
        }
        
        ground = new Ground(map, xPos, yPos);
        water = new Water(map, xPos, yPos, 0);
        
        source = null;
        
        set(0, ground);
    }
    
    public void flow(int amount){
        if (!voided){
            if (amount > 0){
                water.add(amount);
            }
            if (water.getDepth() > 0){
                set(4, water);
            }
        } else {
            Random r = new Random();
            double count = r.nextInt(5);
            if (count < 2){
                this.ground.symbol = " ";
            } else if (count < 3){
                this.ground.symbol = "·";
            } else if (count < 4){
                this.ground.symbol = ".";
            } else {
                this.ground.symbol = "*";
            }
            water.setDepth(0);
        }
    }
    
    public void update(){
        updateWater();
        checkFertility();
    }
    
    public void updateWater(){
        if (get(3) != null){
            source.flow();
        }
        
        if (getWaterLevel() > 1){
            water.flow();
        }
    }
    
    public void checkFertility(){
        if (get(3) == null){
            if ((map.getCoordinate(xPos, yPos+1).getWaterLevel() == 0) || 
                    (map.getCoordinate(xPos+1, yPos).getWaterLevel() == 0) || 
                    (map.getCoordinate(xPos, yPos-1).getWaterLevel() == 0) || 
                    (map.getCoordinate(xPos-1, yPos).getWaterLevel() == 0)) {
                ground.setColour("[BROWN]");
            }
        }
    }
    
    public void setWater(Water water){
        this.water = water;
        set(4, water);
    }
    
    public void groundVoid(){
        ground.channel();
        this.ground.symbol = " ";
        this.voided = true;
        set(3, null);
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getWaterLevel() {
        if (get(3) == null){
            return 0;
        }
        return water.getDepth();
    }
    
    public boolean isChannel(){
        return ground.isChannel();
    }

    public void digChannel() {
        if (!voided){
            ground.channel();
        }
    }

    public void setGround() {
        voided = false;
        set(0, new Ground(map, xPos, yPos));
        set(3, null);
    }

    public void setSource() {
        voided = false;
        source = new WaterSource(map, xPos, yPos);
        set(3, source);
    }
}
