package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

/**
 * Created by iuchi on 2017/08/05.
 */

public class Coin extends Target {
    public Coin(Texture texture, int x, int y, int w, int h){
        super(texture, x, y, w, h);
        super.defaultSpeedParSec = 200;
        setPosition(new Random().nextInt(300 - (int)TARGET_WIDTH), 450);
    }

    @Override
    public void update(){
        setPosition(getX(), getY() - defaultSpeedParSec / 60 * speed);
    }
}
