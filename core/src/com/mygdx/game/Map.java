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
public class Map {
    private int height = 25;
    private int width = 36;
    private long totalTime;
    private int count;
    private Char character;
    private WaterSource source;
    
    private ArrayList<ArrayList<Coordinate>> map;
    private ArrayList<Coordinate> coordinates;
    
    public Map(){
        map = new ArrayList<ArrayList<Coordinate>>();
        coordinates = new ArrayList<Coordinate>();
    }
    
    public void emptyMap(){
        
        for (int y = 0; y < height; y++){
            map.add(new ArrayList<Coordinate>());
            for (int x = 0; x < width; x++){
                Coordinate coordinate = new Coordinate(this, x, y);
                map.get(y).add(coordinate);
                coordinates.add(coordinate);
            }
        }
    }
    
    public void setVoid(){
        for (int y = 0; y < height; y++){
            getCoordinate(0, y).groundVoid();
            getCoordinate(width-1, y).groundVoid();
        }
        
        for (int x = 0; x < width; x++){
            getCoordinate(x, 0).groundVoid();
            getCoordinate(x, height-1).groundVoid();
        }
    }
    
    /*
    public void animateMap(){
        for (int x = 0; x < height; x++){
            for (int y = 0; y < width; y++){
                if (map.get(x).get(y).get(0).toString().equals("@")){
                    
                } else {
                map.get(x).get(y).clear();
                map.get(x).get(y).add(new Thing());
                }
            }
        }
    }*/
    
    public void placeChar(){
        character = new Char(this, 5, 5);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
    }
    
    public String getMap(){
        String output = "[WHITE]";
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Coordinate coordinate = getCoordinate(x, y);
                String str = "";
                
                if (coordinate.isEmpty()){
                    str = " ";
                } else {
                    for (Object obj : coordinate){
                        if (obj != null){
                            str = obj.toString();
                        }
                    }
                }
                
                output = output + str;
            }
            output = output + "\n";
        }
        return output;
    }
    
    public void moveChar(int[] movement){
        int yPos = character.getyPos();
        int xPos = character.getxPos();
        map.get(yPos).get(xPos).set(5, null);
        
        if (movement[2] == 1){
            getCoordinate(xPos, yPos).setGround();
        } else if (movement[3] == 1){
            getCoordinate(xPos, yPos).digChannel();
        } else if (movement[4] == 1){
            getCoordinate(xPos, yPos).setSource();
        } else if (movement[5] == 1){
            getCoordinate(xPos, yPos).groundVoid();
        } else if (movement[6] == 1){
            getCoordinate(xPos, yPos).setGrass();
        } else if (movement[7] == 1){
            getCoordinate(xPos, yPos).dropWater(70);
        }
        
        yPos += movement[0];
        if (yPos < 0){
            yPos = 0;
        } else if (yPos > height-1){
            yPos = height-1;
        }
        
        xPos += movement[1];
        if (xPos < 0){
            xPos = 0;
        } else if (xPos > width-1){
            xPos = width-1;
        }
        
        character.setxPos(xPos);
        character.setyPos(yPos);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
        
        update();
    }
    
    public void update(){
        long tStart = System.currentTimeMillis();
        
        for (Coordinate coordinate : coordinates){
            coordinate.update();
        }
        
        shuffle(coordinates);
        
        long tEnd = System.currentTimeMillis();
        totalTime = totalTime + (tEnd - tStart);
        count++;
        if (count == 10){
            System.out.println(totalTime);
            totalTime = 0;
            count = 0;
        }
    }
    
    public Coordinate getCoordinate(int xPos, int yPos){
        return map.get(yPos).get(xPos);
    }
    
    public Coordinate getCoordinate(Thing thing){
        return getCoordinate(thing.getxPos(), thing.getyPos());
    }
    
    public void shuffle(ArrayList a){
        int index; 
        Object temp;
        Random random = new Random();
        for (int i = a.size() - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = a.get(index);
            a.set(index, a.get(i));
            a.set(i, temp);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
    
    public Char getChar(){
        return character;
    }
}
