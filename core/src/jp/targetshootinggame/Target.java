package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by iuchi on 2017/07/25.
 */

public class Target extends Sprite {
    float speed;
    float defaultSpeedParSec = 100;

    int durability;

    static final float TARGET_WIDTH = 40;
    static final float TARGET_HEIGHT = 40;

    public Target(Texture texture, int x, int y, int w, int h){
        super(texture, x, y, w, h);
        speed = ((float)(new Random().nextInt(15) + 5 + 1)) / 10;
        setSize(TARGET_WIDTH, TARGET_HEIGHT);
        durability = new Random().nextInt(3) + 1;
        //4段階で位置を決定
        setPosition(300, 215 + new Random().nextInt(4) * 50);
    }

    public void update(){
        setPosition(getX() - defaultSpeedParSec / 60 * speed, getY());
    }
}
