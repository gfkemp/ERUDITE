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
    private Plant plant;
    private Random r;
    private boolean voided = false;
    
    public Coordinate(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        for (int i = 0; i < 6; i++){
            add(null);
        }
        
        r = new Random();
        
        ground = new Ground(map, xPos, yPos);
        ground.setFertility(r.nextInt(5));
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
        if (get(4) == null && !voided){
            ground.checkFertility();
        }
        
        if (plant != null && r.nextInt(100) <= 1000){
            plant.grow();
        }
    }
    
    public void updateWater(){
        if (get(3) != null){
            source.flow();
        }
        
        if (getWaterLevel() > 1){
            water.flow();
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
        removeWater();
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getWaterLevel() {
        if (get(4) == null){
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
            removeWater();
        }
    }
    
    public void removeWater(){
        set(3, null);
        set(4, null);
    }

    public void setGround() {
        voided = false;
        set(0, new Ground(map, xPos, yPos));
        removeWater();
    }

    public void setSource() {
        voided = false;
        source = new WaterSource(map, xPos, yPos);
        set(3, source);
    }
    
    public void setGrass() {
        if (!voided && ground.getFertility() > 2 && !ground.isChannel() && getWaterLevel() < 10 && get(2) == null){
            removeWater();
            plant = new Grass(map, xPos, yPos);
            set(2, plant);
        } else if (get(2) != null){
            plant.grow();
        }
    }
}
