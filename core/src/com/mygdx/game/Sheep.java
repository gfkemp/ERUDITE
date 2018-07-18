/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.Random;

/**
 *
 * @author gregclemp
 */
public class Sheep extends Thing{
    private Random r;
    public Sheep(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        this.colour = "[WHITE]";
        this.symbol = "Q";
        r = new Random();
    }
    
    @Override
    public void move(){
        if (r.nextInt(10) == 1){
            int dx = r.nextInt(3) - 1;
            int dy = r.nextInt(3) - 1;

            int newX = dx + xPos;
            int newY = dy + yPos;

            Coordinate destination = map.getSafeCoordinate(newX, newY);
            if (destination != null && destination.get(5) == null){
                destination.set(5, this);
                map.getCoordinate(xPos, yPos).set(5, null);
                this.map = destination.getMap();
                this.xPos = destination.getxPos();
                this.yPos = destination.getyPos();
            }
            
            if (map.getCoordinate(xPos, yPos).get(2) != null){
                Plant plant = (Plant) map.getCoordinate(xPos, yPos).get(2);
                if ("Grass".equals(plant.getType()) && plant.stage == 2){
                    faec ++;
                }
                plant.trample(); 
            }
        }
    }
}
