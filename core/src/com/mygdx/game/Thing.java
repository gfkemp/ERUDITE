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
public class Thing {
    
    protected String symbol;
    protected String colour = "[WHITE]";
    protected Map map;
    protected int xPos;
    protected int yPos;
    protected int faec = 0;
    
    public Thing(){
    }
    
    /*
    public Thing(Map map, int xPos, int yPos){
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
            symbol = " ";
        } else if (r < 96){
            symbol = "=";
        } else if (r < 97){
            symbol = "-";
        } else if (r < 98){
            symbol = "#";
        } else {
            symbol = "~";
        }
    }*/
    
    public Thing(String name){
        symbol = "@";
    }
    
    public String getSymbol(){
        return symbol;
    }
    
    public void trample(){
        
    }
    
    public void move(){
        
    }
    
    @Override
    public String toString(){
        return colour + symbol;
    }
    
    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public int getFaec() {
        return faec;
    }

    public void setFaec(int faec) {
        this.faec = faec;
    }
}
