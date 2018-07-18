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
public class Wall extends Thing{
    public Wall(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        this.colour = "[WHITE]";
        this.symbol = "#";
    }
}
