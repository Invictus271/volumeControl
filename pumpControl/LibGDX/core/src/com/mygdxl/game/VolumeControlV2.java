package com.mygdxl.game;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import sun.font.TrueTypeFont;

public class VolumeControlV2 extends ApplicationAdapter{
	SpriteBatch batch;
	private Pixmap pixmap;
	private Texture background;
	private Texture topPumpTexture;
	private Texture bottomPumpTexture;
	private Texture volumeBarTexture;
	private Sprite topPumpSprite;
	private Sprite bottomPumpSprite;
	private Sprite backgroundSprite;
	private Sprite volumeBarSprite;
	private boolean drag;
	private int oldY=0;
	private float pumpElevation;
	private float volumeBarHeight=0;
	private float oldElevation;
	private float pumpYstart = (float) 835/1500;
	private float pumpXstart = (float) 935/1920;
	private float bottomPumpXDimensions = (float) 50/1920;
	private float bottomPumpYDimensions = (float) 400/1500;
	private float topPumpXstart = (float) 560/1920;
	private float topPumpYstart = pumpYstart+bottomPumpYDimensions;
	private float topPumpXDimensions = (float) 800/1920;
	private float topPumpYDimensions = (float) 40/1500;
	private float volumeBarXstart = (float) 890/1920;
	private float volumeBarYstart = (float) 155/1500;
	private float volumeBarXdimension = (float) 140/1920;
	private float maxVolumeBarHeight = (float) 568/1500;
	private float textStartX= (float) 860/1920;
	private float textStartY =(float) 750/1500;
	private float textDimensionX = (float) 65/1920;
	private float textDimensionY = (float) 50/1500;

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	private int volume;
	private BitmapFont font;
	private Texture[] numbers;
	private Sprite[] numberSprites;

	private int height;
	private int width;
	@Override
	public void create () {
		batch = new SpriteBatch();
		numbers=new Texture[10];
		numberSprites=new Sprite[10];
		background = new Texture("PumpenAssets/Background.png");
		topPumpTexture = new Texture("PumpenAssets/TopPump.png");
		bottomPumpTexture = new Texture("PumpenAssets/bottomPump.png");
		volumeBarTexture = new Texture("PumpenAssets/VolumeBar.png");
		backgroundSprite = new Sprite(background);
		topPumpSprite= new Sprite(topPumpTexture);
		bottomPumpSprite = new Sprite(bottomPumpTexture);
		volumeBarSprite = new Sprite(volumeBarTexture);
		width=Gdx.graphics.getWidth();
		height=Gdx.graphics.getHeight();
		drag=false;
		pumpElevation=topPumpYstart;
		oldElevation=pumpElevation;
		volume=0;
		for(int i=0;i<10;i++){
			numbers[i]=new Texture("PumpenAssets/"+i+".png");
			numberSprites[i]=new Sprite(numbers[i]);
		}

	}
	@Override
	public void resize(int width, int height){
		batch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
	@Override
	public void render(){
		width=Gdx.graphics.getWidth();
		height=Gdx.graphics.getHeight();
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 0.8f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		backgroundSprite.setBounds(0, 0,width,height);
		backgroundSprite.draw(batch);

		bottomPumpSprite.setBounds(pumpXstart*width,pumpYstart*height,bottomPumpXDimensions*width,pumpElevation*height-pumpYstart*height);
		bottomPumpSprite.draw(batch);

		topPumpSprite.setBounds(topPumpXstart*width,pumpElevation*height,topPumpXDimensions*width,topPumpYDimensions*width);
		topPumpSprite.draw(batch);

		volumeBarSprite.setBounds(volumeBarXstart*width,volumeBarYstart*height,volumeBarXdimension*width,volumeBarHeight*height);
		volumeBarSprite.draw(batch);


		//font.draw(batch,""+volume,textStartX*width,textStartY*height);
		if(volume==100){
				volume=99;
				//batch.draw(numberSprites[1],textStartX*width,textStartY*height,textDimensionX*width,textDimensionY*height);
				//batch.draw(numberSprites[0],textStartX*width+textDimensionX*width,textStartY*height,textDimensionX*width,textDimensionY*height);
				//batch.draw(numberSprites[0],textStartX*width+textDimensionX*width*2,textStartY*height,textDimensionX*width,textDimensionY*height);
			}if(volume>=10){
				int firstNumber = volume/10;
				batch.draw(numberSprites[firstNumber],textStartX*width+textDimensionX*0.7f*width,textStartY*height,textDimensionX*width,textDimensionY*height);
				int secondNumber = volume%10;
				batch.draw(numberSprites[secondNumber],textStartX*width+textDimensionX*width*1.3f,textStartY*height,textDimensionX*width,textDimensionY*height);
			}else{
			batch.draw(numberSprites[volume],textStartX*width+textDimensionX*width,textStartY*height,textDimensionX*width,textDimensionY*height);
			}

		float[] xMouseRange = {topPumpXstart*width,topPumpXstart*width+topPumpXDimensions*width};
		float[] yMouseRange = {height-(pumpElevation*height+topPumpYDimensions*height),height-pumpElevation*height};

		boolean mouseInX = Gdx.input.getX()> xMouseRange[0] && Gdx.input.getX() < xMouseRange[1];
		boolean mouseInY = Gdx.input.getY()> yMouseRange[0] && Gdx.input.getY() < yMouseRange[1];
		if(mouseInX && mouseInY && Gdx.input.isButtonPressed(Input.Buttons.LEFT)&& !drag){
			oldY=Gdx.input.getY();
			drag=true;
		}
		int deltaY;
		if(drag && Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			deltaY=Gdx.input.getY()-oldY;
			float previousElevation = pumpElevation;
			if(oldElevation-(float)deltaY/height <=topPumpYstart && oldElevation-(float)deltaY/height>=pumpYstart){
				pumpElevation=oldElevation-(float)deltaY/height;
			}else if(oldElevation-(float)deltaY/height >topPumpYstart){
				pumpElevation=topPumpYstart;
			}else if(oldElevation-(float)deltaY/height<pumpYstart){
				pumpElevation=pumpYstart;
			}
			float deltaPumpElevation = previousElevation-pumpElevation;
			if(deltaPumpElevation>0){
				volumeBarHeight=volumeBarHeight+deltaPumpElevation*0.05f;
			}
			//System.out.println(volumeBarHeight);

		}else{
			drag=false;
			oldElevation=pumpElevation;
		}
		volumeBarHeight=volumeBarHeight-0.00015f;
		if(volumeBarHeight<=0){
			volumeBarHeight=0;
		}
		if(volumeBarHeight>=maxVolumeBarHeight){
			volumeBarHeight=maxVolumeBarHeight;
		}
		float volumeFloat=(volumeBarHeight/maxVolumeBarHeight)*100;
		volume=(int) volumeFloat;
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
		background.dispose();
		topPumpTexture.dispose();
		bottomPumpTexture.dispose();
		volumeBarTexture.dispose();
		for(int i=0;i<10;i++){
			numbers[i].dispose();
		}
	}
}

