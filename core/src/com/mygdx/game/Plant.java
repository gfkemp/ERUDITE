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
public class Plant extends Thing{
    
    protected String[] growthStages;
    protected int stage;
    
    public Plant(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        this.stage = 0;
        this.colour = "[GREEN]";
    }
    
    public void grow(){
        
    }
    
    public void spread(){
        
    }
    
    public void die(){
        map.getCoordinate(xPos, yPos).set(2, null);
    }
}
