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
public class Coordinate extends ArrayList<Thing>{
    private Map map;
    int xPos;
    int yPos;
    private WaterSource source;
    private Water water;
    private Ground ground;
    private Plant plant;
    private Random r;
    boolean voided = false;
    boolean edge = false;
    String background = "▌";
    String backgroundColour = "[#6C9190]";
    int lightLevel;
    int depth;
    
    public Coordinate(Map map, int xPos, int yPos, int noiseDepth){
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
        set(4, water);
        
        source = null;
        
        depth = noiseDepth;//0;//r.nextInt(40)-20;
        lightLevel = depth/10;
        
        set(0, ground);
        if (r.nextInt(5) == 1){
            //depth = -1000;
            //set(5, new Wall(map, xPos, yPos));
        }
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
        
        if (get(5) != null){
            get(5).move();
        }
    }
    
    public void updateWater(){
        if (get(3) != null){
            source.flow();
        }
        
       
        water.flow();
            
            /* FERTILIZATION
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
            }*/
        
    }
    
    public void setWater(Water water){
        this.water = water;
        set(4, water);
    }
    
    public void dropWater(int waterDrop){
        if (!voided){
            water.addWater(waterDrop);
            //set(4, water);
        } /*else if (!voided){
            ground.addFertility(1);
            int nextDrop = waterDrop - 10;
            if (nextDrop > 10){
                dropWater(nextDrop);
                selectRandomAdjCoordinate().dropWater(0);
            }
        }*/
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
        this.background = " ";
        removeWater();
    }
    
    public void setEdge() {
        ground.channel();
        ground.addFertility(-100);
        set(5, null);
        this.ground.symbol = " ";
        this.voided = true;
        this.edge = true;
        this.background = " ";
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
    
    public void setWaterLevel(int level) {
        if (get(4) != null){
            set(4, new Water(map, xPos, yPos, level));
        }
    }
    
    public boolean isChannel(){
        return ground.isChannel();
    }

    public void digChannel(int amount) {
        if (!voided){
            //ground.channel();
            removeWater();
            this.depth = this.depth + amount;
            this.lightLevel = depth/10;
            //backgroundColour = "[#113b3a]";
            if (amount >= 0){
                ground.noSymbol();
            } else {
                ground.symbolGen();
            }
        }
    }
    
    public void removeWater(){
        set(3, null);
        set(4, null);
    }

    public void setGround() {
        this.voided = false;
        this.background = "▌";
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
        if (!voided && ground.getFertility() > 2 && !ground.isChannel() && !ground.isStone() && getWaterLevel() < 10){
            removeWater();
            plant = new Grass(map, xPos, yPos);
            set(2, plant);
        } else if (get(2) != null){
            plant.grow();
        }
    }

    public void setBackGroundColour(String colour) {
        backgroundColour = colour;
    }
    
    public String getBackGround(){
        String output = backgroundColour + background;
        return output;
    }
    
    public void setBackgroundToGround(){
        backgroundColour = ground.getBGColour();
    }

    public int getLightLevel() {
        return lightLevel;
    }

    public void setLightLevel(int lightLevel) {
        this.lightLevel = lightLevel;
    }

    public Char getCharacter(){
        return (Char) get(5);
    }
    
    public Ground getGround(){
        return ground;
    }

    public void setSpore(String colour) {
        if (!voided && !ground.isChannel() && water.getDepth() < 5 && get(2) == null){
            removeWater();
            plant = new Fungus(map, xPos, yPos, colour);
            set(2, plant);
        } else if (get(2) != null){
            plant.grow();
        }
    }
    
    public String getStats(){
        String plantStats = "";
        String faecStats = "";
        if (get(2) != null){
            plantStats = "\nplant map: " + get(2).map.getMapController().getWorldX() + ", " + get(2).map.getMapController().getWorldY();
        }
        if (get(5) != null){
            faecStats = "\nfaec level: " + get(5).faec;
        }
        return "depth: " + this.depth 
                + "\nwater: " + water.getDepth() 
                + "\nmap: " + map.getMapController().getWorldX() + ", " + map.getMapController().getWorldY()
                + plantStats
                + faecStats;
    }

    public int getDepth() {
        return depth;
    }
    
    public int getTotalDepth(){
        return depth-water.getDepth();
    }

    public Water getWater() {
        return water;
    }

    public Map getMap() {
        return this.map;
    }
    
    public int[] getIntsCoord(){
        int[] out = new int[2];
        out[0] = xPos;
        out[1] = yPos;
        return out;
    }

    public boolean isWalkable(Coordinate from) {
        //false if void/get(5) is full
        if (get(5) != null || Math.abs(this.depth - from.depth) >= 100){
            return false;
        }
        return true;
    }
}
