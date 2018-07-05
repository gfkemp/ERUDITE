/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author gregclemp
 */
public class MapController {
    private int height = 25;
    private int width = 36;
    private World world;
    private int worldX = 1;
    private int worldY = 1;
    private long totalTime;
    private int count;
    private Char character;
    private WaterSource source;
    
    private Map map;
    private ArrayList<Coordinate> coordinates;
    
    private ArrayList<String> logText;
    
    private String borderMap;
    
    public MapController(World world){
        this.world = world;
        for (int y = 0; y < world.getHeight(); y++){
            for (int x = 0; x < world.getWidth(); x++){
                world.addToWorld(x, y, emptyMap());
            }
        }
        map = (Map) world.getMap(worldX, worldY);
        coordinates = map.getCoordinates();
        
        logText = new ArrayList();
        logText.add("[#139218]>bootseq\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        
        setBorderMap();
    }
    
    public Map emptyMap(){
        Map newMap = new Map(this, height, width);
        for (int y = 0; y < height; y++){
            newMap.add(new ArrayList<Coordinate>());
            for (int x = 0; x < width; x++){
                Coordinate coordinate = new Coordinate(newMap, x, y);
                newMap.get(y).add(coordinate);
                //coordinate.dropWater(70);
            }
        }
        
        setEdge(newMap);
        return newMap;
    }
    
    //obsolete
    public void newMap(int xPos, int yPos){
        map = new Map(this, height, width);
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
        
        setEdge(map);
    }
    
    public ArrayList<Coordinate> getCoordinates(Map map){
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                coords.add(map.getCoordinate(x, y));
            }
        }
        return coords;
    }
    
    public void setEdge(Map newMap){
        for (int y = 0; y < height; y++){
            newMap.getCoordinate(0, y).setEdge();
            newMap.getCoordinate(width-1, y).setEdge();
        }
        
        for (int x = 0; x < width; x++){
            newMap.getCoordinate(x, 0).setEdge();
            newMap.getCoordinate(x, height-1).setEdge();
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
        character = new Char(map, x, y);
        map.setChar(character);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
    }
    
    public void setBorderMap(){
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
        borderMap = output;
    }
    
    public String getBorderMap(){
        return borderMap;
    }
    
    public HashMap<String, String> getMaps(){
        HashMap<String, String> output = new HashMap<String, String>();
        
        String lightMap = "[BLACK]";
        String backgroundMap = "[WHITE]";
        String objectMap = "[WHITE]";
        
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Coordinate coordinate = map.getCoordinate(x, y);
                
                //generate light map
                int light = coordinate.getLightLevel();
                String str = "[#000000" + Integer.toHexString(light*255/10) + "]▌";
                
                lightMap = lightMap + str;
                
                //generate bg map
                backgroundMap = backgroundMap + coordinate.getBackGround();
                
                //generate obj map
                if (coordinate.isEmpty()){
                    str = " ";
                } else {
                    for (Object obj : coordinate){
                        if (obj != null){
                            str = obj.toString();
                        }
                    }
                }
                objectMap = objectMap + str;
            }
            lightMap = lightMap + "\n";
            backgroundMap = backgroundMap + "\n";
            objectMap = objectMap + "\n";
        }
        
        output.put("lightMap", lightMap);
        output.put("backgroundMap", backgroundMap);
        output.put("objectMap", objectMap);
        output.put("borderMap", getBorderMap());
        
        return output;
    }
    
    public String getLightMap(){
        String output = "[BLACK]";
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Coordinate coordinate = map.getCoordinate(x, y);
                int light = coordinate.getLightLevel();
                String str = "[#000000" + Integer.toHexString(light*255/10) + "]▌";
                
                output = output + str;
            }
            output = output + "\n";
        }
        return output;
    }
    
    public String getBackGroundMap(){
        String output = "[WHITE]";
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                Coordinate coordinate = map.getCoordinate(x, y);
                String str;
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
                Coordinate coordinate = map.getCoordinate(x, y);
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
        
        if (movement[2] == 1){
            map.getCoordinate(xPos, yPos).setGround();
            logText("deposit earth");
        } else if (movement[3] == 1){
            map.getCoordinate(xPos, yPos).digChannel();
            logText("dig channel");
        } else if (movement[4] == 1){
            map.getCoordinate(xPos, yPos).setSource();
            logText("place water source");
        } else if (movement[5] == 1){
            map.getCoordinate(xPos, yPos).groundVoid();
            logText("void ground");
        } else if (movement[6] == 1){
            map.getCoordinate(xPos, yPos).setGrass();
            logText("plant grass seed");
        } else if (movement[7] == 1){
            if (character.getWaterStore() > 0){
                logText("dropped " + character.getWaterStore() + " litres of water");
                map.getCoordinate(xPos, yPos).dropWater(character.dropWaterStore());
            } else {
                if (map.getCoordinate(xPos, yPos).getWaterLevel() > 0){
                    character.setWaterStore(map.getCoordinate(xPos, yPos).getWaterLevel());
                    map.getCoordinate(xPos, yPos).setWaterLevel(0);
                    logText("picked up " + character.getWaterStore() + " litres of water");
                }
            }
        } else if (movement[8] == 1){
            map.getCoordinate(xPos, yPos).setSpore();
            logText("plant spore");
        }
        
        int oldY = yPos;
        yPos += movement[0];
        if (yPos < 0){
            yPos = 0;
        } else if (yPos > height-1){
            yPos = height-1;
        }
        
        int oldX = xPos;
        xPos += movement[1];
        if (xPos < 0){
            xPos = 0;
        } else if (xPos > width-1){
            xPos = width-1;
        }
        
        
        
        if (map.getCoordinate(xPos, yPos).edge){
            Boolean change = changeMap(xPos, yPos);
            if (change){
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
                map.getCoordinate(character.getxPos(), character.getyPos()).set(5, character);
            }
        } else { 
            character.setxPos(xPos);
            character.setyPos(yPos);
            map.getCoordinate(oldX, oldY).set(5, null);
            map.getCoordinate(character.getxPos(), character.getyPos()).set(5, character);
            update();
        }
        
        //trample plants
        if (map.getCoordinate(character.getxPos(), character.getyPos()).get(2) != null){
            map.getCoordinate(character.getxPos(), character.getyPos()).get(2).trample();
        } else {
            if (character.getSpores() > 0){
                map.getCoordinate(character.getxPos(), character.getyPos()).setSpore();
                character.addSpores(-1);
            }
        }
        
        //set spores
    }
    
    public void update(){
        for (Coordinate coordinate : map.getCoordinates()){
            coordinate.update();
        }
        
        //shuffle(coordinates);
        
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

    private Boolean changeMap(int xPos, int yPos) {
        Boolean mapChange = false;
        int x = 0;
        int y = 0;
        if (xPos == 0){
            x = -1;
        } else if (xPos == width-1){
            x = 1;
        }

        if (yPos == 0){
            y = -1;
        } else if (yPos == height-1){
            y = 1;
        }
        
        Map newMap = world.getMap(worldX + x, worldY + y);
        if (newMap != null){
            map.getCoordinate(character.getxPos(), character.getyPos()).set(5, null); //removes the character from the old coordinate
            map.setChar(null); //removes the character from the old map
            newMap.setChar(character);
            this.map = newMap;
            mapChange = true;
            worldX += x;
            worldY += y;
            
        }
        
        return mapChange;
    }
    
    public ArrayList getNextMapCache(int xPos, int yPos) {
        ArrayList cache = new ArrayList();
        int x = 0;
        int y = 0;
        if (xPos == 0){
            x = -1;
        } else if (xPos == width-1){
            x = 1;
        }

        if (yPos == 0){
            y = -1;
        } else if (yPos == height-1){
            y = 1;
        }
        
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
        
        cache.add(world.getMap(worldX + x, worldY + y));
        cache.add(newX);
        cache.add(newY);
        
        return cache;
    }
}
