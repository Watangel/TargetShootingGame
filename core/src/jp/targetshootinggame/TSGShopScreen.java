package jp.targetshootinggame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by iuchi on 2017/08/06.
 */

public class TSGShopScreen extends ScreenAdapter{
    static final float CAMERA_WIDTH = 300;
    static final float CAMERA_HEIGHT = 450;

    TargetShootingGame mGame;

    Sprite mBg;

    //ボタン
    Button backButton;

    //ステージ
    Stage stage;

    //Preference
    Preferences coinPrefs;
    Preferences mPrefs;

    //フォント
    BitmapFont mFont;

    //shop素材
    Sprite nomalGun;
    Sprite bipod;
    Sprite scopeEight;
    Sprite gunFrame1;
    Sprite gunFrame2;
    Sprite magazine;
    Sprite gunState;
    Sprite gunStateGageSpr;

    public TSGShopScreen(TargetShootingGame game){
        mGame = game;

        //Preference
        coinPrefs = Gdx.app.getPreferences("jp.MiniGameCollection");
        mPrefs = Gdx.app.getPreferences("TargetShootingGame");

        //背景
        Texture bgTexture = new Texture("ShopBg.png");
        mBg = new Sprite(bgTexture, 0, 0, 300, 450);

        //フォント
        mFont = new BitmapFont(Gdx.files.internal("font3.fnt"), Gdx.files.internal("font3_0.png"), false);
        mFont.setColor(Color.GOLD);

        //BackButton
        TextureRegion backRegion = new TextureRegion(new Texture("backbutton.png"), 420, 420);
        Button.ButtonStyle backButtonStyle = new Button.ButtonStyle();
        backButtonStyle.up = new TextureRegionDrawable(backRegion);
        backButtonStyle.down = new TextureRegionDrawable(backRegion);

        backButton = new Button(backButtonStyle);
        backButton.setSize(50, 50);
        backButton.setPosition(5, CAMERA_HEIGHT - 55);
        backButton.addListener(new InputListener(){
            public void touchDragged (InputEvent event, float x, float y, int pointer){
                Rectangle back = new Rectangle(0, 0, backButton.getWidth(), backButton.getHeight());
                if(back.contains(x, y)){
                    backButton.setSize(50 * 0.96f, 50 * 0.96f);
                    backButton.setPosition(5 + 50 * 0.02f, CAMERA_HEIGHT - 55 + 50 * 0.02f);
                }else {
                    backButton.setSize(50, 50);
                    backButton.setPosition(5, CAMERA_HEIGHT - 55);
                }
            }
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                backButton.setSize(50 * 0.96f, 50 * 0.96f);
                backButton.setPosition(5 + 50 * 0.02f, CAMERA_HEIGHT - 55 + 50 * 0.02f);
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                backButton.setSize(50, 50);
                backButton.setPosition(5, CAMERA_HEIGHT - 55);
                Rectangle back = new Rectangle(0, 0, backButton.getWidth(), backButton.getHeight());
                if(back.contains(x, y)) {
                    stage.dispose();
                    mGame.setScreen(new TSGGameScreen(mGame));
                }
            }
        });

        //shop素材
        nomalGun = new Sprite(new Texture("Gun.png"), 44, 256);
        nomalGun.setSize(44 * 1.3f, 256 * 1.3f);
        nomalGun.setPosition(CAMERA_WIDTH / 4 * 3 - nomalGun.getWidth() / 2, 0);
        bipod = new Sprite(new Texture("bi_pod.png"), 106, 22);
        bipod.setSize(106 * 1.3f, 22 * 1.3f);
        bipod.setPosition(CAMERA_WIDTH / 4 * 3 - bipod.getWidth() / 2, nomalGun.getHeight() / 3 * 2);
        scopeEight = new Sprite(new Texture("8xScope.png"), 22, 94);
        scopeEight.setSize(22 * 1.3f, 94 * 1.3f);
        scopeEight.setPosition(CAMERA_WIDTH / 4 * 3 - scopeEight.getWidth() / 2, nomalGun.getHeight() / 5 * 1);
        gunFrame1 = new Sprite(new Texture("gun_frame1.png"), 44, 256);
        gunFrame1.setSize(44 * 1.3f, 256 * 1.3f);
        gunFrame1.setPosition(CAMERA_WIDTH / 4 * 3 - nomalGun.getWidth() / 2, 0);
        gunFrame2 = new Sprite(new Texture("gun_frame2.png"), 44, 256);
        gunFrame2.setSize(44 * 1.3f, 256 * 1.3f);
        gunFrame2.setPosition(CAMERA_WIDTH / 4 * 3 - nomalGun.getWidth() / 2, 0);
        magazine = new Sprite(new Texture("magazine.png"), 64, 43);
        magazine.setSize(64 * 1.3f, 43 * 1.3f);
        magazine.setPosition(CAMERA_WIDTH / 4 * 3 - magazine.getWidth(), nomalGun.getHeight() / 5 * 2);

        //ボタン
            //8xScope
        TextureRegion scope8xRegion = new TextureRegion(new Texture("8x_scope_button.png"), 600, 100);
        Button.ButtonStyle scope8xButtonStyle = new Button.ButtonStyle();
        scope8xButtonStyle.up = new TextureRegionDrawable(scope8xRegion);
        scope8xButtonStyle.down = new TextureRegionDrawable(scope8xRegion);
        Button scope8xButton = new Button(scope8xButtonStyle);
        scope8xButton.setSize(600 / 4, 100 / 4);
        scope8xButton.setPosition(CAMERA_WIDTH / 15, CAMERA_HEIGHT / 10 * 5);
        scope8xButton.addListener(new ButtonInputListener(scope8xButton, 0.96f){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                super.touchUp(event, x, y, pointer, button);
                Rectangle startRect = new Rectangle(0, 0, mButton.getWidth(), mButton.getHeight());
                if(startRect.contains(x, y)){
                    if(mPrefs.getInteger("8xSCOPE", 0) == 0){
                        mPrefs.putInteger("8xSCOPE", 1);
                    }
                    if(mPrefs.getInteger("8xSCOPE", 0) == 1){
                        mPrefs.putInteger("8xSCOPE", 2);
                    }
                    if(mPrefs.getInteger("8xSCOPE", 0) == 2){
                        mPrefs.putInteger("8xSCOPE", 1);
                    }
                    mPrefs.flush();
                }
            }
        });
            //バイポッド
        TextureRegion bipodRegion = new TextureRegion(new Texture("bipod_button.png"), 600, 100);
        Button.ButtonStyle bipodButtonStyle = new Button.ButtonStyle();
        bipodButtonStyle.up = new TextureRegionDrawable(bipodRegion);
        bipodButtonStyle.down = new TextureRegionDrawable(bipodRegion);
        Button bipodButton = new Button(bipodButtonStyle);
        bipodButton.setSize(600 / 4, 100 / 4);
        bipodButton.setPosition(CAMERA_WIDTH / 15, CAMERA_HEIGHT / 10 * 4);
        bipodButton.addListener(new ButtonInputListener(bipodButton, 0.96f){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                super.touchUp(event, x, y, pointer, button);
                Rectangle startRect = new Rectangle(0, 0, mButton.getWidth(), mButton.getHeight());
                if(startRect.contains(x, y)){
                    if(mPrefs.getInteger("BIPOD", 0) == 0){
                        mPrefs.putInteger("BIPOD", 1);
                    }
                    if(mPrefs.getInteger("BIPOD", 0) == 1){
                        mPrefs.putInteger("BIPOD", 2);
                    }
                    if(mPrefs.getInteger("BIPOD", 0) == 2){
                        mPrefs.putInteger("BIPOD", 1);
                    }
                    mPrefs.flush();
                }
            }
        });
            //マガジン
        TextureRegion magazineRegion = new TextureRegion(new Texture("magazine_button.png"), 600, 100);
        Button.ButtonStyle magazineButtonStyle = new Button.ButtonStyle();
        magazineButtonStyle.up = new TextureRegionDrawable(magazineRegion);
        magazineButtonStyle.down = new TextureRegionDrawable(magazineRegion);
        Button magazineButton = new Button(magazineButtonStyle);
        magazineButton.setSize(600 / 4, 100 / 4);
        magazineButton.setPosition(CAMERA_WIDTH / 15, CAMERA_HEIGHT / 10 * 3);
        magazineButton.addListener(new ButtonInputListener(magazineButton, 0.96f){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                super.touchUp(event, x, y, pointer, button);
                Rectangle startRect = new Rectangle(0, 0, mButton.getWidth(), mButton.getHeight());
                if(startRect.contains(x, y)){
                    if(mPrefs.getInteger("MAGAZINE", 0) == 0){
                        mPrefs.putInteger("MAGAZINE", 1);
                    }
                    if(mPrefs.getInteger("MAGAZINE", 0) == 1){
                        mPrefs.putInteger("MAGAZINE", 2);
                    }
                    if(mPrefs.getInteger("MAGAZINE", 0) == 2){
                        mPrefs.putInteger("MAGAZINE", 1);
                    }
                    mPrefs.flush();
                }
            }
        });
            //フレーム
        TextureRegion frameRegion = new TextureRegion(new Texture("frame_button.png"), 600, 100);
        Button.ButtonStyle frameButtonStyle = new Button.ButtonStyle();
        frameButtonStyle.up = new TextureRegionDrawable(frameRegion);
        frameButtonStyle.down = new TextureRegionDrawable(frameRegion);
        Button frameButton = new Button(frameButtonStyle);
        frameButton.setSize(600 / 4, 100 / 4);
        frameButton.setPosition(CAMERA_WIDTH / 15, CAMERA_HEIGHT / 10 * 2);
        frameButton.addListener(new ButtonInputListener(frameButton, 0.96f){
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                super.touchUp(event, x, y, pointer, button);
                Rectangle startRect = new Rectangle(0, 0, mButton.getWidth(), mButton.getHeight());
                if(startRect.contains(x, y)){
                    if(mPrefs.getInteger("METAL FRAME", 0) == 0){
                        mPrefs.putInteger("METAL FRAME", 1);
                    }
                    if(mPrefs.getInteger("METAL FRAME", 0) == 1){
                        mPrefs.putInteger("METAL FRAME", 2);
                    }
                    if(mPrefs.getInteger("METAL FRAME", 0) == 2){
                        mPrefs.putInteger("METAL FRAME", 1);
                    }
                    mPrefs.flush();
                }
            }
        });

        //ステータス
        gunState = new Sprite(new Texture("gun_state.png"), 150, 90);
        gunState.setPosition(CAMERA_WIDTH / 10 * 1, CAMERA_HEIGHT / 5 * 3);
        gunStateGageSpr = new Sprite(new Texture("gun_state_gage.png"), 30, 30);

        //ステージ
        stage = new Stage(new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT));
        Gdx.input.setInputProcessor(stage);
        stage.addActor(backButton);
        stage.addActor(scope8xButton);
        stage.addActor(bipodButton);
        stage.addActor(magazineButton);
        stage.addActor(frameButton);
        Matrix4 cameraMatrix = stage.getViewport().getCamera().combined;
        mGame.batch.setProjectionMatrix(cameraMatrix);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();

        mGame.batch.begin();
        mBg.draw(mGame.batch);
        mFont.draw(mGame.batch, "COIN:" + coinPrefs.getInteger("COIN", 0), 60, 430);
        //shop
        if(mPrefs.getInteger("BIPOD", 0) == 2){
            bipod.draw(mGame.batch);
        }
        nomalGun.draw(mGame.batch);
        if(mPrefs.getInteger("METAL FRAME", 0) == 2){
            gunFrame1.draw(mGame.batch);
            gunFrame2.draw(mGame.batch);
        }
        if(mPrefs.getInteger("8xSCOPE", 0) == 2){
            scopeEight.draw(mGame.batch);
        }
        if(mPrefs.getInteger("MAGAZINE", 0) == 2){
            magazine.draw(mGame.batch);
        }
        //state
        gunStateDraw();
        gunState.draw(mGame.batch);
        mGame.batch.end();

        stage.draw();
    }

    public void gunStateDraw(){
        //muzzle velocity
        gunStateGageSpr.setSize(30 * mPrefs.getInteger("MUZZLE VELOCITY", 2), 30);
        gunStateGageSpr.setPosition(gunState.getX(), gunState.getY() + gunState.getHeight() / 3 * 2);
        gunStateGageSpr.draw(mGame.batch);
        //accuracy
        gunStateGageSpr.setSize(30 * mPrefs.getInteger("ACCURACY", 1), 30);
        gunStateGageSpr.setPosition(gunState.getX(), gunState.getY() + gunState.getHeight() / 3);
        gunStateGageSpr.draw(mGame.batch);
        //rate of fire
        gunStateGageSpr.setSize(30 * mPrefs.getInteger("RATE OF FIRE", 3), 30);
        gunStateGageSpr.setPosition(gunState.getX(), gunState.getY());
        gunStateGageSpr.draw(mGame.batch);
    }

    @Override
    public void resize(int width, int height){
        stage.getViewport().update(width, height);
        //       stageDrawable.getViewport().update(width, height);
    }
}
