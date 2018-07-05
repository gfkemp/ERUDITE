package com.mygdx.game;

import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gregclemp
 */
public class Char extends Thing{
    
    private int waterStore = 0;
    private int spores = 0;
    
    public Char(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        symbol = "@";
        colour = "[#cfbf4c]";
    }

    public int getWaterStore() {
        System.out.println("char water level: " + waterStore);
        return waterStore;
    }
    
    public int dropWaterStore() {
        System.out.println("char water level: " + waterStore);
        int out = waterStore;
        waterStore = 0;
        System.out.println("char water level: " + waterStore);
        return out;
    }

    public void setWaterStore(int waterStore) {
        this.waterStore = waterStore;
        System.out.println("char water level: " + waterStore);
    }

    public void addSpores(int i) {
        this.spores += i;
        if(this.spores > 7){
            this.spores = 7;
        }
    }

    public int getSpores() {
        return spores;
    }
}
