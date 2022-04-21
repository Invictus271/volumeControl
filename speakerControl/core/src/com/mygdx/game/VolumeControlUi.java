package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.io.InputStream;

//Author: Invictus der stinkende Supreme

public class VolumeControlUi extends ApplicationAdapter {
	SpriteBatch batch;
	Texture speakerTexture;
	Texture soundBarTexture;
	Texture ballTexture;
	Texture volumeTexture;
	Texture[] numbers;
	Texture[] volumeChargeTextures;
	Sprite speaker;
	Sprite soundBar;
	Sprite ball;
	Sprite volumeSprite;
	Sprite[] numberSprites;
	Sprite[] volumeChargeSprites;
	float speakerRotation;
	double ballX=0;
	double ballY=0;
	int flyTime=0;
	final float v0=15f;
	double g;
	double[] v0_vec=new double[2];
	boolean startFly=false;
	boolean flyActive=false;

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	int volume=0;
	
	@Override
	public void create () {
		numbers = new Texture[10];
		numberSprites = new Sprite[10];
		volumeChargeTextures = new Texture[10];
		volumeChargeSprites = new Sprite[10];
		batch = new SpriteBatch();
		speakerTexture = new Texture("VolumeControlAssets/volumeSpeaker.png");
		soundBarTexture = new Texture("VolumeControlAssets/SoundBar2.png");
		ballTexture = new Texture("VolumeControlAssets/Ball.png");
		volumeTexture = new Texture("VolumeControlAssets/Volume.png");
		for(int i=0;i<10;i++){
			numbers[i]=new Texture("VolumeControlAssets/"+i+".png");
			numberSprites[i]=new Sprite(numbers[i]);
			volumeChargeTextures[i]=new Texture("VolumeControlAssets/volumeSpeaker_Charge"+(i+1)+".png");
			volumeChargeSprites[i]=new Sprite(volumeChargeTextures[i]);
		}
		speaker=new Sprite(speakerTexture);
		soundBar=new Sprite(soundBarTexture);
		ball=new Sprite(ballTexture);
		volumeSprite=new Sprite(volumeTexture);
		speakerRotation=0;
		//sprite = new Sprite(speakerTexture,100,100);
		//sprite = new Sprite(soundBarTexture,100,100);

	}
	public void resize(int width, int height) {
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.setColor(1,1,1,1);
		int speakerWidth = Gdx.graphics.getWidth()/10;
		int speakerHeight = Gdx.graphics.getHeight()/10;
		int soundBarWidth = Gdx.graphics.getWidth()/2;
		int soundBarHeight = Gdx.graphics.getHeight()/50;
		int ballWidth = Gdx.graphics.getWidth()/35;
		int ballHeigth = ballWidth;
		int volumeWidth = Gdx.graphics.getWidth()/4;
		int volumeHeight = Gdx.graphics.getHeight()/20;
		float speakerX=(Gdx.graphics.getWidth()/2)-Gdx.graphics.getWidth()*0.3f;
		float speakerY=Gdx.graphics.getHeight()/2;
		boolean speakerDrawn=false;
		if(speakerRotation<=0){
			TextureRegion speakerRegion=new TextureRegion(speaker);
			batch.draw(speakerRegion,speakerX,speakerY,speakerWidth/2,speakerHeight/2,speakerWidth,speakerHeight,1,1,speakerRotation);
			speakerDrawn=true;
		}
		for(int i=0;i<10;i++){
			if(speakerRotation<45/(10-i)&&!speakerDrawn){
				TextureRegion speakerRegion=new TextureRegion(volumeChargeSprites[i]);
				batch.draw(speakerRegion,speakerX,speakerY,speakerWidth/2,speakerHeight/2,speakerWidth,speakerHeight,1,1,speakerRotation);
				speakerDrawn=true;
			}
		}
		if(speakerRotation>45&&!speakerDrawn){
				TextureRegion speakerRegion=new TextureRegion(volumeChargeSprites[9]);
				batch.draw(speakerRegion,speakerX,speakerY,speakerWidth/2,speakerHeight/2,speakerWidth,speakerHeight,1,1,speakerRotation);
				speakerDrawn=true;
		}
		batch.draw(soundBar,Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()*0.29f+speakerWidth,Gdx.graphics.getHeight()/2+speakerHeight/2-soundBarHeight/2,soundBarWidth,soundBarHeight);
		batch.draw(volumeSprite,Gdx.graphics.getHeight()/40,Gdx.graphics.getHeight()/40,volumeWidth,volumeHeight);
		float[] speakerXRange = {speakerX,speakerX+speakerWidth};
		float[] speakerYRange = {speakerY-speakerHeight,speakerY};

		boolean MouseInX=Gdx.input.getX()>speakerXRange[0] && Gdx.input.getX()<speakerXRange[1];
		boolean MouseInY=Gdx.input.getY()>speakerYRange[0] && Gdx.input.getY()<speakerYRange[1];

		g=Math.pow(v0,2)/(soundBarWidth);
		if(MouseInX && MouseInY && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if(speakerRotation<180){
				speakerRotation=speakerRotation+0.5f;
			}
			startFly=true;
		}else{
			if(startFly){
				flyTime=1;
				ballX=0;
				ballY=0;
				if(speakerRotation==90.0f){
					v0_vec[0]=0;
					v0_vec[1]=v0;
				}else {
					v0_vec[0] = Math.cos(Math.toRadians(speakerRotation)) * v0;
					v0_vec[1] = Math.sin(Math.toRadians(speakerRotation)) * v0;
				}

				flyActive=true;
				startFly=false;
				//System.out.println(v0_vec[0]);
				//System.out.println(v0_vec[1]);

			}
			if(flyActive){
				v0_vec[1]=v0_vec[1]-g;
				ballY=ballY+v0_vec[1];
				if(ballY<=0){
					flyActive=false;
					//ballX=ballX-Gdx.graphics.getWidth()*0.4f;
				}else {
					ballX=ballX+v0_vec[0];
					flyTime++;
					batch.draw(ball, Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()*0.29f+speakerWidth + (float)(ballX), Gdx.graphics.getHeight() / 2 + speakerHeight / 2 - ballHeigth / 2 + (float)(ballY), ballWidth, ballHeigth);
				}
			}
			if (speakerRotation > 0) {
				speakerRotation = speakerRotation - 2;
			}
			if(!flyActive) {
				batch.draw(ball,  Gdx.graphics.getWidth()/2-Gdx.graphics.getWidth()*0.29f+speakerWidth + (float)(ballX), Gdx.graphics.getHeight() / 2 + speakerHeight / 2 - ballHeigth / 2, ballWidth, ballHeigth);
			}
			double floatVolume=(ballX/(soundBarWidth-ballWidth))*100;
			volume=(int) floatVolume;
			if(volume<0){
				volume=0;
			}
			if(volume>100){
				volume=100;
			}
			if(volume==100){
				batch.draw(numberSprites[1],Gdx.graphics.getHeight()/40+volumeWidth,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
				batch.draw(numberSprites[0],Gdx.graphics.getHeight()/40+volumeWidth*1.125f,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
				batch.draw(numberSprites[0],Gdx.graphics.getHeight()/40+volumeWidth*1.25f,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
			}else if(volume>=10){
				int firstNumber = volume/10;
				batch.draw(numberSprites[firstNumber],Gdx.graphics.getHeight()/40+volumeWidth,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
				int secondNumber = volume%10;
				batch.draw(numberSprites[secondNumber],Gdx.graphics.getHeight()/40+volumeWidth*1.125f,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
			}else{
				batch.draw(numberSprites[volume],Gdx.graphics.getHeight()/40+volumeWidth,Gdx.graphics.getHeight()/38,volumeWidth/5,volumeHeight);
			}
		}
		String homeDirectory = System.getProperty("user.home");
		try {
			Process p = Runtime.getRuntime().exec("pactl set-sink-volume 0 "+volume+"%");
			p.waitFor();

		}catch(Exception e){
			System.err.println(e);
		}
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		speakerTexture.dispose();
		soundBarTexture.dispose();
		ballTexture.dispose();
		volumeTexture.dispose();
		for(int i=0;i<10;i++){
			numbers[i].dispose();
			volumeChargeTextures[i].dispose();
		}
	}
}
