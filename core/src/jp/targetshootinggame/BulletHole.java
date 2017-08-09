package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by iuchi on 2017/07/26.
 */

public class BulletHole extends Sprite {
    float removeCount;
    float removeTime = 1;
    float removeParFrame;
    public BulletHole(Texture texture, int x, int y, int w, int h){
        super(texture, x, y, w, h);
      //  setSize(40, 40);
        removeParFrame = 1 / (60 * removeTime);
    }

    public void update(){
        removeCount ++;
        if(removeCount < 60 * removeTime){
            setAlpha(1 - removeParFrame * removeCount);
        }
        if(removeCount > 60 * removeTime - 2){
            setAlpha(0);
        }
    }

    public boolean candraw(){
        if(removeCount > 60 * removeTime){
            return false;
        }else {
            return true;
        }
    }
}
