package jp.targetshootinggame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;

/**
 * Created by iuchi on 2017/08/08.
 */

public class ButtonInputListener extends InputListener {
    Button mButton;
    float buttonX, buttonY, buttonWidth, buttonHeight;
    float downScale;

    public ButtonInputListener(Button button, float down_scale){
        mButton = button;
        buttonX = mButton.getX();
        buttonY = mButton.getY();
        buttonWidth = mButton.getWidth();
        buttonHeight = mButton.getHeight();
        downScale = down_scale;
    }

    public void touchDragged (InputEvent event, float x, float y, int pointer){
        Rectangle startRect = new Rectangle(0, 0, mButton.getWidth(), mButton.getHeight());
        if(startRect.contains(x, y)){
            mButton.setSize(buttonWidth * downScale, buttonHeight * downScale);
            mButton.setPosition(buttonX + buttonWidth * ((1 - downScale) / 2), buttonY + buttonHeight * ((1 - downScale) / 2));
        }else {
            mButton.setSize(buttonWidth, buttonHeight);
            mButton.setPosition(buttonX, buttonY);
        }
    }
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        mButton.setSize(buttonWidth * downScale, buttonHeight * downScale);
        mButton.setPosition(buttonX + buttonWidth * ((1 - downScale) / 2), buttonY + buttonHeight * ((1 - downScale) / 2));
        return true;
    }
    public void touchUp(InputEvent event, float x, float y, int pointer, int button){
        mButton.setSize(buttonWidth, buttonHeight);
        mButton.setPosition(buttonX, buttonY);
    }
}
