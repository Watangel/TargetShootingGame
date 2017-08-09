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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Random;

public class TSGGameScreen extends ScreenAdapter{
    private TargetShootingGame mGame;

    //画面幅、高さ
    static final float CAMERA_WIDTH = 300;
    static final float CAMERA_HEIGHT = 450;

    //ゲーム状態
    static final int GAME_STATE_READY = 0;
    static final int GAME_STATE_PLAYING = 1;
    static final int GAME_STATE_GAMEOVER = 2;
    static final int GAME_STATE_STOP = 3;

    //残り時間
    float floatMaxRemainingTime = 2000;
    float floatRemainingTime;
    Sprite maxTime;
    Sprite remainingTime;
    Texture maxTimeTexture;
    Texture remainingTimeTexture;


    //ステージ
    Stage mainStage;

    //ゲーム状態の保持
    int mGameState;

    //銃
    Gun mGun;

    //弾
    public static final int BULLET_STATE_NOMAL = 0;
    int bulletState;
    Bullet bullet;
    ArrayList<Bullet> bullets;
    Texture bulletTexture;
    int addbullet;
    int rate = 3;
    public static final int BULLET_STATE_CHARGE = 1;
    int chargeLevel;

    //弾痕
    BulletHole bulletHole;
    ArrayList<BulletHole> bulletHoles;
    Texture bulletHoleTexture;

    //的
    Target target;
    ArrayList<Target> targets;
    Texture targetTexture;
    int createTargetParSec = 2;
    int createTargetInterval;

    //的タイム回復
    TargetTime targetTime;
    Texture timeTargetTexture;
    float createTimeTargetParSec = 0.3f;
    int createTimeTargetInterval;

    //コイン
    Coin coin;
    Texture coinTexture;
    float createCoinParSec = 0.2f;
    int createCoinInterval;

    //背景
    Sprite whiteBg;

    //タッチポイント
    Vector2 touchPoint;

    //スコア
    int score;

    //フォント
    BitmapFont mFont;

    //チャージゲージ
    Sprite chargeGaugeBg;
    Sprite chargeGauge;
    Sprite chargeGaugeLine1;
    Sprite chargeGaugeLine2;
    Texture chargeGaugeTexture;

    //Preferences
    Preferences mPrefs;
    Preferences coinPrefs;

    public TSGGameScreen(TargetShootingGame game){
        mGame = game;
        mPrefs = Gdx.app.getPreferences("TargetShootingGame");
        bulletState = BULLET_STATE_NOMAL;
        coinPrefs = Gdx.app.getPreferences("jp.MiniGameCollection");

        //チャージボタン
        TextureRegion chargeUp = new TextureRegion(new Texture("ChargeButtonUp.png"), 256, 256);
        TextureRegion chargeDown = new TextureRegion(new Texture("ChargeButtonDown.png"), 256, 256);
        Button.ButtonStyle chargeButtonStyle = new Button.ButtonStyle();
        chargeButtonStyle.up = new TextureRegionDrawable(chargeUp);
        chargeButtonStyle.down = new TextureRegionDrawable(chargeDown);
        chargeButtonStyle.down.setBottomHeight(-3);
        final Button chargeButton = new Button(chargeButtonStyle);
        chargeButton.setSize(80, 80);
        chargeButton.setPosition(CAMERA_WIDTH / 2 - chargeButton.getWidth() / 2 , 0);
        chargeButton.addListener(new InputListener(){
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                bulletState = BULLET_STATE_CHARGE;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button){
                Rectangle chargeRect = new Rectangle(0, 0, chargeButton.getWidth(), chargeButton.getHeight());
                if(chargeRect.contains(x, y)){
                    bulletState = BULLET_STATE_NOMAL;
                    if(chargeLevel >= 30 && chargeLevel < 60){
                        bullet = new Bullet(bulletTexture, 0, 0, 128, 128, 0.8f, 1);
                        bullet.setSize(30, 30);
                        bullet.setPosition(mGun.getX() + mGun.getWidth() / 2 - bullet.getWidth() / 2, mGun.getY() + mGun.getHeight());
                        bullets.add(bullet);
                        chargeLevel = 0;
                    }else if(chargeLevel >= 60 && chargeLevel < 90){
                        bullet = new Bullet(bulletTexture, 0, 0, 128, 128, 0.5f, 2);
                        bullet.setSize(60, 60);
                        bullet.setPosition(mGun.getX() + mGun.getWidth() / 2 - bullet.getWidth() / 2, mGun.getY() + mGun.getHeight());
                        bullets.add(bullet);
                        chargeLevel = 0;
                    }else if(chargeLevel >= 90){
                        bullet = new Bullet(bulletTexture, 0, 0, 128, 128, 0.3f, 3);
                        bullet.setSize(90, 90);
                        bullet.setPosition(mGun.getX() + mGun.getWidth() / 2 - bullet.getWidth() / 2, mGun.getY() + mGun.getHeight());
                        bullets.add(bullet);
                        chargeLevel = 0;
                    }
                    chargeLevel = 0;
                }else{
                    bulletState = BULLET_STATE_NOMAL;
                }
            }
        });

        //チャージゲージ
        chargeGaugeBg = new Sprite(new Texture("ChargeGaugeBg.png"), 0, 0, 256, 48);
        chargeGaugeBg.setSize(75, 30);
        chargeGaugeBg.setPosition(CAMERA_WIDTH / 2 - chargeGaugeBg.getWidth() / 2, 25);
        chargeGaugeTexture = new Texture("ChargeGauge.png");
        chargeGauge = new Sprite(chargeGaugeTexture, 0, 0, 256, 48);
        chargeGauge.setSize(75, 30);
        chargeGauge.setPosition(CAMERA_WIDTH / 2 - chargeGaugeBg.getWidth() / 2, 25);
        chargeGaugeLine1 = new Sprite(new Texture("ChargeGaugeLine.png"), 0, 0, 1, 48);
        chargeGaugeLine1.setSize(1, 30);
        chargeGaugeLine1.setPosition(CAMERA_WIDTH / 2 - chargeGaugeBg.getWidth() / 2 + chargeGaugeBg.getWidth() / 3, 25);
        chargeGaugeLine2 = new Sprite(new Texture("ChargeGaugeLine.png"), 0, 0, 1, 48);
        chargeGaugeLine2.setSize(1, 30);
        chargeGaugeLine2.setPosition(CAMERA_WIDTH / 2 - chargeGaugeBg.getWidth() / 2 + chargeGaugeBg.getWidth() / 3 * 2, 25);

        //フォント
        mFont = new BitmapFont(Gdx.files.internal("font3.fnt"), Gdx.files.internal("font3_0.png" ), false);
        mFont.getData().setScale(0.7f);
        mFont.setColor(Color.ORANGE);

        //スコア


        //タッチポイント
        touchPoint = new Vector2();

        //残り時間
        floatRemainingTime =floatMaxRemainingTime;
        remainingTimeTexture = new Texture("RemainingTime.png");
        maxTimeTexture = new Texture("MaxRemainingTime.png");
        maxTime = new Sprite(maxTimeTexture, 0, 0, 476, 37);
        maxTime.setSize(280, 15);
        maxTime.setPosition(10, 420);
        remainingTime = new Sprite(remainingTimeTexture, 0, 0, 476, 37);
        remainingTime.setSize(280, 15);
        remainingTime.setPosition(10, 420);

        //ゲーム初期状態
        mGameState = GAME_STATE_READY;

        //的生成インターバル
        createTargetInterval = 60 / createTargetParSec;
        createTimeTargetInterval = (int)(60 / createTimeTargetParSec);
        createCoinInterval = (int)(60 / createCoinParSec);


        //メインステージ
        mainStage = new Stage(new FitViewport(CAMERA_WIDTH, CAMERA_HEIGHT));

        //銃
        Texture BambooGunTexture = new Texture("Gun.png");
        mGun = new Gun(BambooGunTexture, 0, 0, 44, 256);
        mGun.setPosition(CAMERA_WIDTH / 2 - mGun.getWidth()/ 2, 0);

        //弾
        bullets = new ArrayList<Bullet>();
        bulletTexture = new Texture("Bullet.png");

        //弾痕
        bulletHoles = new ArrayList<BulletHole>();
        bulletHoleTexture = new Texture("BrokenBullet.png");

        //的
        targets = new ArrayList<Target>();
        targetTexture = new Texture("Target.png");

        //的タイム回復
        timeTargetTexture = new Texture("TimeTarget.png");

        //コイン
        coinTexture = new Texture("coin_target.png");

        //背景
        whiteBg = new Sprite(new Texture("white.png"), 0, 0, 300, 450);
        whiteBg.setSize(300, 450);
        whiteBg.setPosition(0, 0);

        mainStage.addActor(chargeButton);
    }

    @Override
    public void render(float delta){
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        //ステージ
        Gdx.input.setInputProcessor(mainStage);
        Matrix4 cameraMatrix = mainStage.getViewport().getCamera().combined;
        mGame.batch.setProjectionMatrix(cameraMatrix);
        mainStage.act();
        mainStage.getViewport().getCamera().update();

        mGame.batch.begin();
        whiteBg.draw(mGame.batch);
        mGun.draw(mGame.batch);
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).draw(mGame.batch);
        }
        for(int i = 0; i < targets.size(); i++){
            targets.get(i).draw(mGame.batch);
        }
        for(int i = 0; i < bulletHoles.size(); i++){
            if(bulletHoles.get(i).candraw()){
                bulletHoles.get(i).draw(mGame.batch);
            }
        }
        mFont.draw(mGame.batch, "SCORE: " + score, 10, 415);
        mFont.draw(mGame.batch, "COIN: " + coinPrefs.getInteger("COIN", 0), 10, 390);
        drawRemainingTime();
        mGame.batch.end();

        mainStage.draw();

        mGame.batch.begin();
        if(bulletState == BULLET_STATE_CHARGE){
            chargeGaugeBg.draw(mGame.batch);
            drawChargeGauge();
            chargeGaugeLine1.draw(mGame.batch);
            chargeGaugeLine2.draw(mGame.batch);
        }
        mGame.batch.end();
    }

    public void update(){
        switch (mGameState){
            case GAME_STATE_READY:
                updateReady();
                break;
            case GAME_STATE_PLAYING:
                floatRemainingTime --;
                updatePlaying();
                break;
        }
    }

    private void updateReady(){
        if(Gdx.input.justTouched()){
            mGameState = GAME_STATE_PLAYING;
        }
    }

    private void updatePlaying(){
        //弾
        if(bulletState == BULLET_STATE_NOMAL){
            addbullet ++;
            if(addbullet == 60 / rate){
                bullet = new Bullet(bulletTexture, 0, 0, 128, 128, 1, 1);
                bullet.setSize(mGun.getWidth(), mGun.getWidth());
                bullet.setPosition(mGun.getX(), mGun.getY() + mGun.getHeight());
                bullets.add(bullet);
                addbullet = 0;
            }
        }
        if(bulletState == BULLET_STATE_CHARGE){
            if(chargeLevel < 90){
                chargeLevel ++;
            }
        }
        for(int i = 0; i < bullets.size(); i++){
            bullets.get(i).update();
            if(bullets.get(i).getY() > CAMERA_HEIGHT){
                bullets.remove(i);
                i --;
            }
        }

        //弾痕
        for(int i = 0; i < bulletHoles.size(); i++){
            bulletHoles.get(i).update();
        }

        //的
        if(new Random().nextInt(createTargetInterval) == 1){
            createTarget();
        }
        for(int i = 0; i < targets.size(); i++){
            targets.get(i).update();
            if(targets.get(i).getX() < 0 - targets.get(i).getWidth() ){
                targets.remove(i);
                i--;
            }
        }
        //タイム回復的
        if(new Random().nextInt(createTimeTargetInterval) == 1){
            createTimeTarget();
        }

        //コイン
        if(new Random().nextInt(createCoinInterval) == 1){
            createCoin();
        }

        //当たり判定
        for(int i = 0; i < bullets.size(); i++){
            for(int j = 0; j < targets.size() && i >= 0; j++){
                if(bullets.get(i).getBoundingRectangle().overlaps(targets.get(j).getBoundingRectangle())){
                    bulletHole = new BulletHole(bulletHoleTexture, 0 ,0, 128, 128);
                    bulletHole.setPosition(bullets.get(i).getX(), bullets.get(i).getY());
                    bulletHole.setSize(bullets.get(i).getWidth(), bullets.get(i).getHeight());
                    bulletHoles.add(bulletHole);
                    targets.get(j).durability -= bullets.get(i).damage;
                    bullets.remove(i);
                    i--;
                    //コイン
                    if(targets.get(j).durability <= 0){
                        if(targets.get(j) instanceof Coin){
                            targets.remove(j);
                            score += 400;
                            coinPrefs.putInteger("COIN", coinPrefs.getInteger("COIN", 0) + 1);
                            coinPrefs.flush();
                            j--;
                    //タイム回復
                        }else if(targets.get(j) instanceof TargetTime){
                            targets.remove(j);
                            score += 350;
                            j--;
                            if(floatRemainingTime < floatMaxRemainingTime - 150){
                                floatRemainingTime += 150;
                            }
                    //的
                        }else{
                            targets.remove(j);
                            score += 200;
                            j--;
                        }
                    }
                }
            }
        }

        if(Gdx.input.isTouched()){
            mainStage.getViewport().unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY()));
            Rectangle left = new Rectangle(0, 0, CAMERA_WIDTH / 2 - 40, CAMERA_HEIGHT);
            Rectangle right = new Rectangle(CAMERA_WIDTH / 2 + 40, 0, CAMERA_WIDTH / 2, CAMERA_HEIGHT);
            if(left.contains(touchPoint.x, touchPoint.y)){
                if(mGun.getX() > 0){
                    mGun.moveLeft();
                }else {
                    mGun.setX(0);
                }
            }
            if(right.contains(touchPoint.x, touchPoint.y)){
                if(mGun.getX() < CAMERA_WIDTH - mGun.getWidth()){
                    mGun.moveRight();
                }else {
                    mGun.setX(CAMERA_WIDTH - mGun.getWidth());
                }
            }
        }

    }

    public void createTarget(){
        target = new Target(targetTexture, 0, 0, 128, 128);
        targets.add(target);
    }

    public void createTimeTarget(){
        targetTime = new TargetTime(timeTargetTexture, 0, 0, 128, 128);
        targets.add(targetTime);
    }

    public void createCoin(){
        coin = new Coin(coinTexture, 0, 0, 128, 128);
        targets.add(coin);
    }

    public void drawRemainingTime(){
        maxTime.draw(mGame.batch);
        remainingTime.setRegionWidth((int)(476 / floatMaxRemainingTime * floatRemainingTime));
        remainingTime.setSize(280 / floatMaxRemainingTime * floatRemainingTime, remainingTime.getHeight());
        remainingTime.draw(mGame.batch);
    }

    public void drawChargeGauge(){
        chargeGauge.setRegionWidth((int)((float)(256) / (float)(90) * (float)(chargeLevel)));
        chargeGauge.setSize((int)((float)(75) / (float)(90) * (float)(chargeLevel)), chargeGauge.getHeight());
        chargeGauge.draw(mGame.batch);
    }

    @Override
    public void resize(int width, int height){
        mainStage.getViewport().update(width, height);
    }
}
