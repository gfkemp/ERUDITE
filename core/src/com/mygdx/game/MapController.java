/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygdx.game;

import com.badlogic.gdx.utils.IntArray;
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
    private int movementCost = 0;
    private Char character;
    private WaterSource source;
    private Random r;
    
    private Map map;
    private ArrayList<Coordinate> coordinates;
    
    private ArrayList<String> logText;
    
    private String borderMap;
    private ArrayList<ArrayList<String>> overlay;
    
    private NoiseMap perlin;
    
    public MapController(World world){
        this.world = world;
        
        this.perlin = new NoiseMap(height, width, world.height, world.width);
        this.perlin.generateMap();
        
        for (int y = 0; y < world.getHeight(); y++){
            for (int x = 0; x < world.getWidth(); x++){
                world.addToWorld(x, y, emptyMap(x, y));
            }
        }
        map = (Map) world.getMap(worldX, worldY);
        coordinates = map.getCoordinates();
        r = new Random();
        
        logText = new ArrayList();
        logText.add("[#139218]>bootseq\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        logText.add("[#139218]\n");
        
        setBorderMap();
        
    }
    
    public Map emptyMap(int mapX, int mapY){
        Map newMap = new Map(this, height, width, mapX, mapY);
        for (int y = 0; y < height; y++){
            newMap.add(new ArrayList<Coordinate>());
            for (int x = 0; x < width; x++){
                Coordinate coordinate = new Coordinate(newMap, x, y, perlin.getDepth(x, y, mapX, mapY));
                newMap.get(y).add(coordinate);
                //coordinate.dropWater(70);
            }
        }
        
        setEdge(newMap);
        return newMap;
    }
    
    //obsolete
    public void newMap(int xPos, int yPos, int mapX, int mapY){
        map = new Map(this, height, width, mapX, mapY);
        coordinates = new ArrayList<Coordinate>();
        
        //emptyMap();
        
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
    
    public void placeChar(int x, int y){
        character = new Char(map, x, y);
        map.setChar(character);
        map.get(character.getyPos()).get(character.getxPos()).set(5, character);
    }
    
    public void setBorderMap(){
        overlay = new ArrayList<ArrayList<String>>();
        String output = "[WHITE]";
        for (int y = 0; y < height; y++){
            overlay.add(new ArrayList<String>());
            for (int x = 0; x < width; x++){
                if (x == 0 && y == 0){
                    overlay.get(y).add("╔");
                    output += "╔";
                } else if (x == width-1 && y==0 ){
                    overlay.get(y).add("╗");
                    output += "╗";
                } else if (x == 0 && y==height-1 ){
                    overlay.get(y).add("╚");
                    output += "╚";
                } else if (x == width-1 && y==height-1 ){
                    overlay.get(y).add("╝");
                    output += "╝";
                } else if (y == 0 || y == height-1){
                    overlay.get(y).add("═");
                    output += "═";
                } else if (x == 0 || x == width-1){
                    overlay.get(y).add("║");
                    output += "║";
                } else {
                    overlay.get(y).add(" ");
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
    
    public String getOverlayMap(int startX, int startY, int targetX, int targetY){
        String output1 = "[WHITE]";
        ArrayList<ArrayList<String>> tempOutput = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < overlay.size(); i++){
            tempOutput.add((ArrayList<String>) overlay.get(i).clone());
        }
        
        Astar astar = new Astar(map, width, height);
        //IntArray path = astar.getPath(startX, startY, targetX, targetY);
        
        if (startX >= 0 && startY >= 0 && startX < width && startY < height) {
            IntArray path = astar.getPath(startX, startY, targetX, targetY);
            for (int i = 0, n = path.size; i < n; i += 2) {
                int x = path.get(i);
                int y = path.get(i + 1);
                tempOutput.get(y).set(x, "[#cfbf4c]·[]");
            }
            tempOutput.get(targetY).set(targetX, "[#cfbf4c]X[]");
	}
        
        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++){
                output1 += tempOutput.get(y).get(x);
            }
            output1 = output1 + "\n";
        }
        return output1;
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
                String str = "[#000000" + Integer.toHexString(light*255/20) + "]▌";
                
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
                String str = "";
                if (coordinate.edge){
                    str = "[#000000]▌";
                } else if (light >= 0){
                    light = light > 15 ? 15 : light;
                    str = "[#000000" + Integer.toHexString(light*255/20) + "]▌";
                } else {
                    light = light < -15 ? -15 : light;
                    int lightValue = Math.abs(light)*255/20;
                    
                    if (lightValue < 16){ //to account for single digit hex values 
                        str = "[#FFFFFF0" + Integer.toHexString(lightValue) + "]▌";
                    } else {
                        str = "[#FFFFFF" + Integer.toHexString(lightValue) + "]▌";
                    }
                }
                
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
            map.getCoordinate(xPos, yPos).getGround().turnToStone();
            logText("deposit earth");
        } else if (movement[3] == 1){
            if (character.getEarth() == 0){
                int digAmount = 0;
                ArrayList<Coordinate> near = map.getSurroundingCoordinates(xPos, yPos);
                for (Coordinate coordinate : near){
                    if (coordinate.depth >= 100){
                        //near.remove(coordinate);
                    } else {
                        int dig = (200 - (coordinate.depth + 100))/4;//coordinate.depth >= 0 ? 400/(Math.max(coordinate.depth, 10)) : coordinate.depth/-1;
                        digAmount += dig;
                        coordinate.digChannel(dig);
                    }
                }

                Coordinate coordinate = map.getCoordinate(character.getxPos(), character.getyPos());
                if (coordinate.depth < 100){
                    int dig = (200 - (coordinate.depth + 100))/4;
                    digAmount += dig;
                    coordinate.digChannel(dig);
                }
                character.setEarth(digAmount);
                logText("removed " + digAmount + " earth");
            } else if (character.getEarth() > 0){
                
                int subtotal = character.getEarth();
                
                while (subtotal > 0){
                    
                    int thisTotal = subtotal > 4 ? 4 : subtotal; //remove 5 (or less)
                    subtotal = subtotal - thisTotal;             //from subtotal
                    
                    int x = character.getxPos() + r.nextInt(3)-1;
                    int y = character.getyPos() + r.nextInt(3)-1;
                    Coordinate coord = map.getSafeCoordinate(x, y);
                    
                    while (thisTotal > 0){
                        if (coord != null){
                            coord.digChannel(-1);
                            thisTotal--;
                            coord = map.lowestDepthSurrounding(coord);
                        } else {
                            coord = map.getSafeCoordinate(x, y);
                        }
                    }
                }
                character.setEarth(subtotal);
                logText("dumped earth");
            }
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
            map.getCoordinate(xPos, yPos).setSpore("[WHITE]");
            logText("plant spore");
        } else if (movement[9] == 1){
            map.addSheep();
            logText("plant sheep");
        }
        
        if (movementCost == 0){
            
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

            moveCharacter(xPos, yPos, oldX, oldY);
            
        }
        
        //if edge -- change maps -> recalc coord
        
        //trample plants
        if (map.getCoordinate(character.getxPos(), character.getyPos()).get(2) != null){
            map.getCoordinate(character.getxPos(), character.getyPos()).get(2).trample();
        } else {
            if (character.getSpores() > 0){
                map.getCoordinate(character.getxPos(), character.getyPos()).setSpore("[WHITE]");
                character.addSpores(-1);
            }
        }
        
        //set spores
    }
    
    public void update(){
        ArrayList<Map> maps = world.getLocalMaps(worldX, worldY);
        
        for (Map mp : maps){
            if (mp != null){
                mp.update();
            }
        }
        
        //shuffle(coordinates); // this might be good?
        
        if (movementCost > 0){
            movementCost --;
        }
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

    private void changeMap(int xPos, int yPos) {
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
            
            if (newMap.getCoordinate(newX, newY).isWalkable(map.getCoordinate(character.getxPos(), character.getyPos()))){
                map.getCoordinate(character.getxPos(), character.getyPos()).set(5, null); //removes the character from the old coordinate
                map.setChar(null); //removes the character from the old map
                
                movementCost += Math.pow(((newMap.getCoordinate(newX, newY).depth-map.getCoordinate(character).depth)/10), 2);
                
                newMap.setChar(character);
                this.map = newMap;
                character.setMap(newMap);
                
                worldX += x;
                worldY += y;
                
                character.setxPos(newX);
                character.setyPos(newY);
                map.getCoordinate(character.getxPos(), character.getyPos()).set(5, character);
            }
        }
    }
    
    public ArrayList getNextMapCache(int xPos, int yPos, int mapX, int mapY) {
        ArrayList cache = new ArrayList();
        int x = 0;
        int y = 0;
        
        if (xPos <= 0){
            x = -1;
        } else if (xPos >= width-1){
            x = 1;
        }

        if (yPos <= 0){
            y = -1;
        } else if (yPos >= height-1){
            y = 1;
        }
        
        int newX = xPos;
        int newY = yPos;

        if (xPos <= 0){
            newX = xPos + width-2;
        } else if (xPos >= width-1){
            newX = xPos - (width-2);
        }

        if (yPos <= 0){
            newY = yPos + height-2;
        } else if (yPos >= height-1){
            newY = yPos - (height-2);
        }
        
        cache.add(world.getMap(mapX + x, mapY + y));
        cache.add(newX);
        cache.add(newY);
        
        return cache;
    }
    
    public Coordinate getCharCoordinate(){
        return map.getCoordinate(character);
    }
    
    public Coordinate getCoordinate(int xPos, int yPos){
        return map.getCoordinate(xPos, yPos);
    }

    public void charWalk() {
        boolean move;
        int[] nextMove = character.getNextWalk();
        
        if (nextMove != null){
            int newX = nextMove[0];
            int newY = nextMove[1];

            move = moveCharacter(newX, newY, character.getxPos(), character.getyPos());

            if (move){
                character.removeLastWalk();
            }
        }
    }

    private boolean moveCharacter(int xPos, int yPos, int oldX, int oldY) {
        if (map.getCoordinate(xPos, yPos).edge){
            changeMap(xPos, yPos);
            character.endPath();
            return true;
        } else { 
            if (map.getCoordinate(xPos, yPos).isWalkable(map.getCoordinate(character.getxPos(), character.getyPos()))){
                character.setxPos(xPos);
                character.setyPos(yPos);
                map.getCoordinate(oldX, oldY).set(5, null);
                map.getCoordinate(character.getxPos(), character.getyPos()).set(5, character);
                movementCost += Math.pow(((map.getCoordinate(oldX, oldY).depth-map.getCoordinate(character).depth)/10), 2);
                
                if (map.getCoordinate(character.getxPos(), character.getyPos()).get(2) != null){
                    Plant plant = (Plant) map.getCoordinate(xPos, yPos).get(2);
                    plant.trample(); 
                }
                
                return true;
            }
        }
        return false;
    }

    public int getWorldX() {
        return worldX;
    }

    public int getWorldY() {
        return worldY;    
    }
}
