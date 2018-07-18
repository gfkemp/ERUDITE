/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gregclemp
 */
public class Map extends ArrayList<ArrayList<Coordinate>> {
    private int xPos;
    private int yPos;
    private int worldX;
    private int worldY;
    private int height;
    private int width;
    private MapController mapController;
    private Char character = null;
    private Random r;
    
    public Map(MapController mapController, int height, int width, int worldX, int worldY){
        this.height = height;
        this.width = width;
        this.worldX = worldX;
        this.worldY = worldY;
        
        this.mapController = mapController;
        r = new Random();
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
        return mapController.getNextMapCache(xPos, yPos, worldX, worldY);
    }
    
    public Coordinate getCoordinate(int x, int y){
        return get(y).get(x);
    }
    
    public Coordinate getSafeCoordinate(int x, int y){
        if (x < 1 || x > width - 2 || y < 1 || y > height - 2){
            ArrayList cache = mapController.getNextMapCache(x, y, worldX, worldY);
            Map nextMap = (Map) cache.get(0);
            if (nextMap != null){
                return nextMap.getCoordinate((Integer) cache.get(1), (Integer) cache.get(2));
            }
        } else {
            return get(y).get(x);
        }
        return null;
    }
    
    public Coordinate getCoordinate(Thing thing){
        return getCoordinate(thing.getxPos(), thing.getyPos());
    }
    
    public ArrayList<Coordinate> getSurroundingCoordinates(int xPos, int yPos){
        ArrayList<Coordinate> output = new ArrayList<Coordinate>();
        
        for (int y = -1; y <= 1; y++){
            for (int x = -1; x <= 1; x++){
                if (x != 0 || y != 0){
                    Coordinate coord = getCoordinate(x + xPos, y + yPos);
                    if (!coord.edge){
                        output.add(coord);
                    } else {
                        /*
                        ArrayList cache = getNextMapCache(x + xPos, y + yPos);
                        Map nextMap = (Map) cache.get(0);
                        if (nextMap != null){
                            output.add(nextMap.getCoordinate((Integer) cache.get(1), (Integer) cache.get(2)));
                        }*/
                        coord = getSafeCoordinate(x + xPos, y + yPos);
                        if (coord != null){
                            output.add(getSafeCoordinate(x + xPos, y + yPos));
                        }
                    }
                }
            }
        }
        shuffle(output);
        return output;
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
    
    public void update(){
        for (Coordinate coordinate : getCoordinates()){
            coordinate.update();
        }
    }
    
    public void shuffle(ArrayList a){
        int index; 
        Object temp;
        for (int i = a.size() - 1; i > 0; i--)
        {
            index = r.nextInt(i + 1);
            temp = a.get(index);
            a.set(index, a.get(i));
            a.set(i, temp);
        }
    }
    
    public void addSheep(){
        int rX = r.nextInt(width-1) + 1;
        int rY = r.nextInt(height-1) + 1;
        Coordinate destination = getCoordinate(rX, rY);
        if (destination.get(5) == null){
            destination.set(5, new Sheep(this, rX, rY));
        }
    }

    public MapController getMapController() {
        return mapController;
    }

    public Coordinate lowestDepthSurrounding(Coordinate coord) {
        Coordinate output = coord;
        ArrayList<Coordinate> nearTiles = getSurroundingCoordinates(coord.xPos, coord.yPos);
        
        for (Coordinate that : nearTiles){
            output = coord.depth >= that.depth ? coord : that;
        }
        return output;
    }
}
