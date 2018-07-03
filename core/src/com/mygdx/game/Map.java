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
    
    private ArrayList<String> logText;
    
    public Map(){
        map = new ArrayList<ArrayList<Coordinate>>();
        coordinates = new ArrayList<Coordinate>();
        
        logText = new ArrayList();
        logText.add("[#139218]>bootseq\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
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
    
    public void newMap(int xPos, int yPos){
        map = new ArrayList<ArrayList<Coordinate>>();
        coordinates = new ArrayList<Coordinate>();
        
        emptyMap();
        
        int newX = xPos;
        int newY = yPos;
        
        if (xPos == 0){
            newX = width-2;
        } else if (xPos == width-1){
            newX = 1;
        }
        
        if (yPos == 0){
            newY = height-2;
        } else if (yPos == height-1){
            newY = 1;
        }
        
        character.setxPos(newX);
        character.setyPos(newY);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
        
        setEdge();
    }
    
    public void setEdge(){
        for (int y = 0; y < height; y++){
            getCoordinate(0, y).setEdge();
            getCoordinate(width-1, y).setEdge();
        }
        
        for (int x = 0; x < width; x++){
            getCoordinate(x, 0).setEdge();
            getCoordinate(x, height-1).setEdge();
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
    
    public void placeChar(int x, int y){
        character = new Char(this, x, y);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
    }
    
    public String GetBorderMap(){
        String output = "[WHITE]";
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                if (x == 0 && y == 0){
                    output += "╔";
                } else if (x == width-1 && y==0 ){
                    output += "╗";
                } else if (x == 0 && y==height-1 ){
                    output += "╚";
                } else if (x == width-1 && y==height-1 ){
                    output += "╝";
                } else if (y == 0 || y == height-1){
                    output += "═";
                } else if (x == 0 || x == width-1){
                    output += "║";
                } else {
                    output += " ";
                }
            }
            output = output + "\n";
        }
        return output;
    }
    
    public String getBackGroundMap(){
        String output = "[WHITE]";
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Coordinate coordinate = getCoordinate(x, y);
                String str = "";
                str = coordinate.getBackGround();
                
                
                output = output + str;
            }
            output = output + "\n";
        }
        return output;
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
            logText("deposit earth");
        } else if (movement[3] == 1){
            getCoordinate(xPos, yPos).digChannel();
            logText("dig channel");
        } else if (movement[4] == 1){
            getCoordinate(xPos, yPos).setSource();
            logText("place water source");
        } else if (movement[5] == 1){
            getCoordinate(xPos, yPos).groundVoid();
            logText("void ground");
        } else if (movement[6] == 1){
            getCoordinate(xPos, yPos).setGrass();
            logText("plant grass seed");
        } else if (movement[7] == 1){
            if (character.getWaterStore() > 0){
                logText("dropped " + character.getWaterStore() + " litres of water");
                getCoordinate(xPos, yPos).dropWater(character.dropWaterStore());
            } else {
                if (getCoordinate(xPos, yPos).getWaterLevel() > 0){
                    character.setWaterStore(getCoordinate(xPos, yPos).getWaterLevel());
                    getCoordinate(xPos, yPos).setWaterLevel(0);
                    logText("picked up " + character.getWaterStore() + " litres of water");
                }
            }
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
        
        if (getCoordinate(xPos, yPos).edge){
            newMap(xPos, yPos);
        } else { 
            update();
        }
    }
    
    public void update(){
        for (Coordinate coordinate : coordinates){
            coordinate.update();
        }
        
        //shuffle(coordinates);
        
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
    
    
    public void logText(String newLine){
        logText.add(0, "[#139218]>" + newLine + "\n");
        if (logText.size() > 5){
            logText.remove(5);
        }
    }
    
    public String genLog(){
        return "[#139218]---\n"+ logText.get(4) + logText.get(3) + logText.get(2) + logText.get(1) + logText.get(0) + "[#139218]---\n";
    }
}
