package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class WestGame extends ApplicationAdapter {
    BitmapFont font;
    SpriteBatch batch;
    World world;
    MapController game;
    FPSLogger logger;
    
    private GlyphLayout glyph;
    int count = 0;
    int wait;
    String framerate = "0";
    long rendercalls = 0;
    long tStart;
    
    String controls;
    boolean walk = false;

    @Override
    public void create () {
            Gdx.graphics.setWindowedMode(900, 600);
            batch = new SpriteBatch();
            world = new World();
            game = new MapController(world);
            
            font = new BitmapFont(Gdx.files.internal("data/topaz8.fnt"), false);
            font.getData().markupEnabled = true;
            glyph = new GlyphLayout();
            
            game.placeChar(5, 5);
            
            controls = "w - wait 100 ticks\na - open tank\ns - plant seed\nd - plant spore\nf - plant sheep\nz - place ground\nx - dig channel\nc - place spring\nv - void ground\n";
    }

    @Override
    public void render () {
            if (tStart == 0){
                tStart = System.currentTimeMillis();
            }
        
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            game.update();
            
            if (walk){
                game.charWalk();
                walk = !game.getChar().atDestination();
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && count <= 0 && wait == 0){
                walk = false;
                keyPress();
            } else if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
                walk = false;
                mouseDown();
            } else if (wait > 0){
                game.update();
                wait--;
            } else if (count > 0){
                //game.update();
                count--;
            }
            
            batch.begin();
            count--;
            //game.placeChar();
            
            glyph.setText(font, game.genLog());
            font.draw(batch, glyph, 25, 130);
            
            glyph.setText(font, game.getBackGroundMap());
            font.draw(batch, glyph, 15, 585);
            
            glyph.setText(font, game.getMap());
            font.draw(batch, glyph, 15, 585);
            glyph.setText(font, game.getLightMap());
            font.draw(batch, glyph, 15, 585);
            if (Gdx.input.getX() >= 15 
                    && Gdx.input.getX() <= 590 
                    && Gdx.input.getY() >= 15 
                    && Gdx.input.getY() <= 460){
                
                glyph.setText(font, game.getOverlayMap(game.getChar().getxPos(), 
                                                        game.getChar().getyPos(), 
                                                        (Gdx.input.getX()-15)/16, 
                                                        (Gdx.input.getY()-15)/18));
                font.draw(batch, glyph, 20, 585);
                
                glyph.setText(font, controls + "\n" + game.getCoordinate((Gdx.input.getX()-15)/16, (Gdx.input.getY()-15)/18).getStats());
                font.draw(batch, glyph, 600, 585);
                
            } else {
                glyph.setText(font, game.getBorderMap());
                font.draw(batch, glyph, 20, 585);
                glyph.setText(font, controls + "\n" + game.getCharCoordinate().getStats());
                font.draw(batch, glyph, 600, 585);
            }
            
            rendercalls++;
            
            if (rendercalls >= 10){
                long tEnd = System.currentTimeMillis();
                framerate = Long.toString(rendercalls*1000/(tEnd-tStart));
                rendercalls = 0;
                tStart = 0;
            }

            glyph.setText(font, framerate);
            font.draw(batch, glyph, 100, 20);
            batch.end();
    }
    
    public void renderNoKeys () {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            //game.placeChar();
            glyph.setText(font, game.getMap());
            font.draw(batch, glyph, 15, 470);
            //System.out.print(game.getMap());
            batch.end();
    }

    @Override
    public void dispose () {
            batch.dispose();
    }
    
    public void keyPress(){
            int[] movement = new int[10];
            movement[0] = 0;
            movement[1] = 0;
            movement[2] = 0;
            
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                movement[1] = -1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                movement[1] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.UP)){
                movement[0] = -1;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                movement[0] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.Z)){
                movement[2] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.X)){
                movement[3] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.C)){
                movement[4] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.V)){
                movement[5] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.S)){
                movement[6] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.A)){
                movement[7] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.D)){
                movement[8] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.F)){
                movement[9] = 1;
            }
            
            if (Gdx.input.isKeyPressed(Input.Keys.W)){
                wait = 90;
                game.logText("wait");
            }
            
            game.moveChar(movement);
            count = 7;
    }

    private void mouseDown() {
        if (Gdx.input.getX() >= 15 
                    && Gdx.input.getX() <= 590 
                    && Gdx.input.getY() >= 15 
                    && Gdx.input.getY() <= 460){
            walk = true;
            game.getChar().startPathing((Gdx.input.getX()-15)/16, (Gdx.input.getY()-15)/18);
        }
    }
}
