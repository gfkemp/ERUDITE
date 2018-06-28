package com.mygdx.game;

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
    
    public Char(Map map, int xPos, int yPos){
        this.map = map;
        this.xPos = xPos;
        this.yPos = yPos;
        symbol = "@";
    }
}
