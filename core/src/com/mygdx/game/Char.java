package com.mygdx.game;

import com.badlogic.gdx.utils.IntArray;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;

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
    private Random r;
    private int xDest;
    private int yDest;
    public IntArray path;
    public int[] nextStep;
    public int earth;
    
    public Char(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        symbol = "@";
        colour = "[#cfbf4c]";
        r = new Random();
    }

    public int getWaterStore() {
        waterStore = 1000; //remove
        return waterStore;
    }
    
    public int dropWaterStore() {
        int out = waterStore;
        waterStore = 0;
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

    public void setDestination(int xDest, int yDest) {
        this.xDest = xDest;
        this.yDest = yDest;
    }

    public void randomWalk() {
        int x = xPos + r.nextInt(3)-1;
        int y = yPos + r.nextInt(3)-1;
        if (map.getCoordinate(x, y).get(5) == null && !map.getCoordinate(x, y).edge){
                map.getCoordinate(xPos, yPos).set(5, null);
                this.xPos = x;
                this.yPos = y;
                map.getCoordinate(x, y).set(5, this);
        }
    }
    
    public boolean atDestination(){
        return xDest == xPos && yDest == yPos;
    }

    public int[] getNextWalk() {
        return nextStep;
    }
    
    public void removeLastWalk(){
        if (!path.isEmpty()){
            nextStep[1] = path.pop();
            nextStep[0] = path.pop();
        }
    }

    public void startPathing(int xDest, int yDest) {
        this.xDest = xDest;
        this.yDest = yDest;
        
        if (!(xDest == xPos && yDest == yPos)){
            Astar astar = new Astar(map, map.getWidth(), map.getHeight());
            path = astar.getPath(xPos, yPos, xDest, yDest);
            nextStep = new int[2];
            removeLastWalk();
        }
    }
    
    public void endPath(){
        if (path != null){
            path.clear();
        }
        nextStep = null;
    }

    public int getEarth() {
        return earth;
    }

    public void setEarth(int earth) {
        this.earth = earth;
    }
}
