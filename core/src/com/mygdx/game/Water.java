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
    
    public Water(Map map, int xPos, int yPos, int depth){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        this.depth = depth;
        symbol = Integer.toString(depth/10);
    }
    
    public void add(int amount){
        this.depth += amount;
        
        if (depth > 70){
            depth = 70;
        }
        symbol = Integer.toString(depth/10);
    }
    
    public void flow(){
        ArrayList<Coordinate> nearTiles = new ArrayList();
        for (int y = -1; y <= 1; y++){
            for (int x = -1; x <= 1; x++){
                nearTiles.add(map.getCoordinate(x + xPos, y + yPos));
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
            }
        }
        
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
    
    public void setDepth(int depth) {
        this.depth = depth;
    }
    
    @Override
    public String toString(){
        colour = Integer.toHexString(200-(depth*2));
        String colour2 = Integer.toHexString(200-depth);
        return "[#" + colour + colour2 + "FF]" + symbol;
    }
}
