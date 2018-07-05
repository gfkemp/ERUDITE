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
public class Map extends ArrayList<ArrayList<Coordinate>>{
    private int xPos;
    private int yPos;
    private int height;
    private int width;
    private MapController mapController;
    private Char character = null;
    
    public Map(MapController mapController, int height, int width){
        this.height = height;
        this.width = width;
        this.mapController = mapController;
    }

    public ArrayList<Coordinate> getCoordinates() {
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                coords.add(getCoordinate(x, y));
            }
        }
        return coords;
    }
    
    public ArrayList getNextMapCache(int xPos, int yPos){
        return mapController.getNextMapCache(xPos, yPos);
    }
    
    public Coordinate getCoordinate(int x, int y){
        return get(y).get(x);
    }
    
    public Coordinate getCoordinate(Thing thing){
        return getCoordinate(thing.getxPos(), thing.getyPos());
    }
    
    public void setChar(Char character){
        this.character = character;
    }
    
    public Char getChar(){
        return character;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }
}
