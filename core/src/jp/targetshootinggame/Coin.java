package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by iuchi on 2017/08/05.
 */

public class Coin extends Target {
    public Coin(Texture texture, int x, int y, int w, int h){
        super(texture, x, y, w, h);
        super.defaultSpeedParSec = 200;
    }
}
