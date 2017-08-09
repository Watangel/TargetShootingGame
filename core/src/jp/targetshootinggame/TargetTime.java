package jp.targetshootinggame;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by iuchi on 2017/07/25.
 */

public class TargetTime extends Target {
    public TargetTime(Texture texture, int x, int y, int w, int h){
        super(texture, x, y, w, h);
        super.defaultSpeedParSec = 150;
    }
}
