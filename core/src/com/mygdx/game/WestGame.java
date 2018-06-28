package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WestGame extends ApplicationAdapter {
    BitmapFont font;
    SpriteBatch batch;
    Map game;
    private GlyphLayout glyph;
    int count = 0;

    @Override
    public void create () {
            batch = new SpriteBatch();
            game = new Map();
            font = new BitmapFont(Gdx.files.internal("data/ProggySquare.fnt"), false);
            font.getData().markupEnabled = true;
            glyph = new GlyphLayout();
            game.emptyMap();
            game.placeChar();
            game.setVoid();
    }

    @Override
    public void render () {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.begin();
            if (Gdx.input.isKeyPressed(Input.Keys.ANY_KEY) && count <= 0){
                keyPress();
            }
            count--;
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
            int[] movement = new int[6];
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
            
            game.moveChar(movement);
            count = 5;
    }
}
