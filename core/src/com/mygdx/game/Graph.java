/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.HashMap;

/**
 *
 * @author gregclemp
 */
public class Graph {
    private World world;
    private Map map;
    private int width = 36;
    private int height = 25;
    public Graph(Map map){
        //this.world = world;
        this.map = map;
    }
    
    public HashMap<Coordinate, Integer> neighbors(Coordinate coord){
        HashMap<Coordinate, Integer> results = new HashMap<Coordinate, Integer>();
        
        //results = {(xPos+1, yPos), (xPos, yPos-1), (xPos-1, yPos), (xPos, yPos+1)};
        int xPos = coord.xPos;
        int yPos = coord.yPos;
        
        for (int y = -1; y <= 1; y++){
            for (int x = -1; x <= 1; x++){
                if (x != 0 && y != 0){
                    if (x+xPos > 0 && x+xPos < width-1 && y+yPos > 0 && y+yPos < height-1){
                        if (map.getCoordinate(x + xPos, y+ yPos) != null){
                            int z = (int) Math.pow((map.getCoordinate(xPos, yPos).depth - map.getCoordinate(x + xPos, y+ yPos).depth)/10, 2);
                            results.put(map.getCoordinate(x + xPos, y+ yPos), z);
                        } else {
                            results.put(map.getCoordinate(x + xPos, y+ yPos), 2147483647); 
                        }
                    }
                }
            }
        }
        /*
        if (x + y) % 2 == 0: results.reverse() # aesthetics
        results = filter(self.in_bounds, results)
        results = filter(self.passable, results)
                */
        return results;
    }
}
