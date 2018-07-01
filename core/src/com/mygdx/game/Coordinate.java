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
    boolean voided = false;
    
    public Coordinate(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        
        for (int i = 0; i < 6; i++){
            add(null);
        } 
        /* 
        0 = ground
        1 = null
        2 = plant
        3 = water source
        4 = water
        5 = char
        */
        
        r = new Random();
        
        ground = new Ground(map, xPos, yPos);
        //ground.addFertility(4);
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
        
        if (get(2) != null){
            plant.grow();
        }
    }
    
    public void updateWater(){
        if (get(3) != null){
            source.flow();
        }
        
        int waterlevel = getWaterLevel();
        
        if (waterlevel > 1){
            water.flow();
            
            if (waterlevel > 20){
                int x = r.nextInt(3)-1;
                int y = r.nextInt(3)-1;

                if (x != 0 || y != 0){
                    if (x + xPos < map.getWidth() && x + xPos >= 0 && y + yPos < map.getHeight() && y + yPos >= 0){
                        Coordinate coord = map.getCoordinate(x + xPos, y + yPos);
                        if (coord.getWaterLevel() == 0 && coord.ground.getFertility() < 4 && !coord.ground.isChannel() && !coord.voided){
                            coord.ground.checkFertility(waterlevel);
                        }
                    }
                }
            }
        }
    }
    
    public void setWater(Water water){
        this.water = water;
        set(4, water);
    }
    
    public void dropWater(int waterDrop){
        if (ground.isChannel() && !voided){
            water.add(waterDrop);
            set(4, water);
        } else if (!voided){
            ground.addFertility(1);
            int nextDrop = waterDrop - 10;
            if (nextDrop > 10){
                dropWater(nextDrop);
                selectRandomAdjCoordinate().dropWater(0);
            }
        }
    }
    
    public Coordinate selectRandomAdjCoordinate(){
        int newX = xPos + r.nextInt(3)-1;
        int newY = yPos + r.nextInt(3)-1;
        while (newX == xPos && newY == yPos 
                || newX < 0 || newX >= map.getWidth() 
                || newY < 0 || newY >= map.getHeight()){
            newX = xPos + r.nextInt(3)-1;
            newY = yPos + r.nextInt(3)-1;
        }
        return map.getCoordinate(newX, newY);
    }
    
    public void groundVoid(){
        ground.channel();
        ground.addFertility(-100);
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
        this.voided = false;
        ground = new Ground(map, xPos, yPos);
        set(0, ground);
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
