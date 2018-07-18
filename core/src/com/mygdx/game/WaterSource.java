/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.ArrayList;

/**
 *
 * @author gregclemp
 */
public class WaterSource extends Thing{
    
    private Water origin;
    
    public WaterSource(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        
        this.map.getCoordinate(xPos, yPos).backgroundColour = "[#3c8cba]";
        origin = new Water(map, xPos, yPos, 0);
        symbol = "O";
    }
    
    public void flow(){
        Coordinate coordinate = map.getCoordinate(xPos, yPos);
        if (coordinate.contains(origin)){
            if (coordinate.getDepth() > origin.getDepth())
            origin.add(5);
        } else {
            coordinate.setWater(origin);
        }
    }
}
