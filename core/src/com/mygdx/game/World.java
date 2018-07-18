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
public class World extends ArrayList<ArrayList<Map>>{
    int height = 36;
    int width = 25;
    public World(){
        for (int y = 0; y < height; y++){
            add(new ArrayList<Map>());
            for (int i = 0; i < width; i++){
                get(y).add(null);
            }
        }
    }
    
    public void addToWorld(int x, int y, Map map){
        get(y).set(x, map);
        //set world coords
    }
    
    public Map getMap(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height){
            return null;
        }
        return get(y).get(x);
    }
    
    public Map moveMap(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height){
            return null;
        }
        return get(y).get(x);
    }
    
    public ArrayList<Map> getLocalMaps(int xPos, int yPos){
        ArrayList<Map> output = new ArrayList<Map>();
        for (int y = yPos-1; y <= yPos+1; y++){
            for (int x = xPos-1; x <= xPos+1; x++){
                output.add(getMap(x, y));
            }
        }
        return output;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
