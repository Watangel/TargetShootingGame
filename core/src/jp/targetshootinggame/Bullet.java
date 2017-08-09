package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by iuchi on 2017/07/24.
 */

public class Bullet extends Sprite {
    public float bulletSpeed;
    public float speedParSec = 200;
    public float accuracy;
    public int damage;

    public Bullet(Texture texture, int x, int y, int w, int h, float decideAccuracy, int bulletdamage){
        super(texture, x, y, w, h);
        damage = bulletdamage;
        bulletSpeed  = speedParSec / 60;
        accuracy = 100 * (((float)(new Random().nextInt(20)) / 10 - 1)) * decideAccuracy / 60;
    }

    public void update(){
        setPosition(getX() + accuracy, getY() + bulletSpeed);
    }
}
