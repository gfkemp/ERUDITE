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
    
    public Ground(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        
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
        symbol = "u";
        channel = true;
    }
    
    public boolean isChannel(){
        return channel;
    }
}
