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
public class Water extends Thing{
    
    private int depth;
    private Random r;
    
    public Water(Map map, int xPos, int yPos, int depth){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        this.depth = depth;
        symbol = " ";//depthToSymbol();
        r = new Random();
    }
    
    public void add(int amount){
        this.depth += amount;
        
        if (depth > 100){
            depth = 100;
        }
        setSymbol();
    }
    
    public String depthToSymbol(){
        return Integer.toString(depth/10);
    }
    /*
        int pX = r.nextInt(3) - 1;
        int pY = r.nextInt(3) - 1;
        
        int newX = pX + xPos;
        int newY = pY + yPos;
        
        if (pX != 0 || pY != 0){
            if (newX < map.getWidth() && newX >= 0 && newY < map.getHeight() && newY >= 0){
                map.getCoordinate(newX, newY).setGrass();
            }
        }
    */
    public void flow(){
        if (depth <= 5){
            if (depth == 0){
                updateBG();
            } else if (r.nextInt(20) == 1){
                depth--;
                map.getCoordinate(this).getGround().fertilize();
                map.getCoordinate(this).setBackgroundToGround();
            }
        } else {
            int groundDepth = map.getCoordinate(xPos, yPos).getDepth();
            int totalDepth = groundDepth - depth;
            
            ArrayList<Coordinate> nearTiles = map.getSurroundingCoordinates(xPos, yPos);
            
            int count = 0;
            while (thisTotalDepthLess(nearTiles) && count < 20){ //local totaldepths unequal
                for (Coordinate coordinate : nearTiles){
                    if (coordinate.getTotalDepth() > totalDepth && depth > 1){
                        int waterFlow = 1;
                        coordinate.getWater().addWater(waterFlow);
                        addWater(-waterFlow);
                        totalDepth = groundDepth - depth;
                    }
                }
                count ++;
            }
        }
        
        
        /*
        ArrayList<Coordinate> nearTiles = new ArrayList();
        for (int y = -1; y <= 1; y++){
            for (int x = -1; x <= 1; x++){
                Coordinate coord = map.getCoordinate(x + xPos, y + yPos);
                if (!coord.edge){
                    nearTiles.add(coord);
                } else {
                    ArrayList cache = map.getNextMapCache(x + xPos, y + yPos);
                    Map nextMap = (Map) cache.get(0);
                    //int newX = (Integer) cache.get(1);
                    //int newY = (Integer) cache.get(2);
                    if (nextMap != null){
                        nearTiles.add(nextMap.getCoordinate((Integer) cache.get(1), (Integer) cache.get(2)));
                    }
                }
            }
        }
        
        ArrayList<Coordinate> removals = new ArrayList();
        for (Coordinate coordinate : nearTiles){
            if (coordinate.getWaterLevel() > depth){
                removals.add(coordinate);
            } else if (!coordinate.isChannel()){
                removals.add(coordinate);
            }
        }
            
        nearTiles.removeAll(removals);
        nearTiles.trimToSize();
        
        if (!nearTiles.isEmpty()){
            int avg = 0;
            for (Coordinate coordinate : nearTiles){
                avg = avg + coordinate.getWaterLevel();
            }
            avg = avg/nearTiles.size();

            while (0 < (depth - avg)){
                Random r = new Random();
                int index = r.nextInt(nearTiles.size());
                
                nearTiles.get(index).flow(1);
                depth--;
                
                updateBG();
            }
        }*/
        
        
        /*
        for (Ground ground : nearTiles){
            if (ground.getChannel()){
                ArrayList coordinate = map.getCoordinate(ground);
                if (coordinate.get(4) == null){
                    coordinate.set(4, new Water(map, ground.xPos, ground.yPos, 1));
                } else {
                    Water water = (Water) coordinate.get(4);
                    water.add(1);
                }
            }
        }*/
    }

    public int getDepth() {
        return depth;
    }
    
    public void addWater(int water){
        this.depth += water;
        updateBG();
    }
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    public void setSymbol(){
        int i = r.nextInt(50);
        switch (i) {
            case 0:
                symbol = "▓";
                break;
            case 1:
                symbol = "*";
                break;
            default:
                symbol = " ";
                break;
        }
    }
    
    @Override
    public String toString(){
        return "[#FFFFFF]" + symbol;
    }
    
    public void updateBG(){
        if (depth > 100){
            map.getCoordinate(xPos, yPos).backgroundColour = "[#0052FF]";
        } else if (depth > 0){
            colour = Integer.toHexString(220-(depth*220/100));
            String colour2 = Integer.toHexString(220-(depth*155/100));
            map.getCoordinate(xPos, yPos).backgroundColour = "[#" + colour + colour2 + "FF]";
        } else {
            //map.getCoordinate(this).setBackgroundToGround();
        }
    }

    private boolean thisTotalDepthLess(ArrayList<Coordinate> nearTiles) {
        boolean output = false;
        int totalDepth = map.getCoordinate(this).getDepth() - this.depth;
        for (Coordinate coordinate : nearTiles){
            if (totalDepth < coordinate.getTotalDepth()){
                output = true;
            }
        }
        return output;
    }
}
