package com.example.PBLZ;

import java.util.Random;

import com.example.PBLZ.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity  extends Activity {
	int mycolor;
	int score = 0;
	class GameView extends View {
		Random rand = new Random();
		int screenWidth, screenHeight;
		Bitmap sad;
		Rect rect = new Rect();
		Typeface font;
		Paint paint, paintB, textPaint;
		float posX=-77, posY=-1, startX, startY, endX, endY, deltaX=0, deltaY=0;
		int prevAction=99;
		boolean onAir = false;
		boolean gameOver = false;
		private Context myContext;
		int bColor;
		int numPebbles = 4;
		Pebble pebbles[];
		boolean caught = false;
		int counter=0;
		Bitmap playerPebble, divide;
		Bundle extras;
		MediaPlayer music;
		public GameView(Context context){
			super(context);
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
			
			music = MediaPlayer.create(context, R.raw.music);
			music.setLooping(true);
			extras = getIntent().getExtras();
			playerPebble = BitmapFactory.decodeResource(getResources(), R.drawable.playpebble);
			divide = BitmapFactory.decodeResource(getResources(), R.drawable.divide);
			paint = new Paint();
			paintB = new Paint();
			textPaint = new Paint();
			mycolor = Color.BLACK;
			bColor = Color.YELLOW;
			myContext = context;
			int weather = extras.getInt("weather");
			if(weather>80)
				this.setBackgroundResource(R.drawable.hot);
			else if(weather>60)
				this.setBackgroundResource(R.drawable.warm);
			else
				this.setBackgroundResource(R.drawable.cold);


			pebbles = new Pebble[numPebbles];
			for(int i = 0; i<numPebbles ; i++){
				pebbles[i] = new Pebble(0,0, 25);
				pebbles[i].setSpacedPos(750, 480, numPebbles, i);
			}
			int[] colors = pebbles[0].generateColors(numPebbles, 1);
			for(int i=0; i<numPebbles; i++){
				pebbles[i].setColor(colors[i]);
			}
			music.start();
		}
		
		protected void onDraw(Canvas canvas){
			if(gameOver){
				music.stop();
				updateHighscores();
				Intent in = new Intent(myContext, GameOverActivity.class);
				myContext.startActivity(in);
				finish();
				return;
			}
			if(counter>0){
				paint.setTypeface(font);
				textPaint.setTextSize(32);
				textPaint.setStyle(Paint.Style.STROKE);
				canvas.drawText("Well done! " + extras.getString("name"), screenWidth/2-100, screenHeight/2, textPaint);
				caught = false;
				counter++;
				if(counter>=100) counter = 0;
			}
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			if(posX==-77){ posX = screenWidth/2;
			posY = screenHeight-100;}
			paint.setColor(Color.BLACK);
			paint.setTypeface(font);
			paint.setTextSize(28);
			paint.setTextAlign(Paint.Align.LEFT);
			canvas.drawText("Score: " + score,0, 100, paint);
			paint.setStrokeWidth(10);
			//canvas.drawLine(0, screenHeight-200, screenWidth, screenHeight-200, paint);
			canvas.drawBitmap(divide, 0, screenHeight-200, null);
			paint.setColor(mycolor);
			//canvas.drawCircle(posX,posY, 25, paint);
			canvas.drawBitmap(playerPebble, posX-25, posY-25, null);
			for(int i = 0; i<numPebbles ; i++){
				//paintB.setColor(pebbles[i].color);
				canvas.drawBitmap(pebbles[i].getBitmap(getResources()), pebbles[i].posX-25,pebbles[i].posY-25, null);
			}
			onUpdate();
			//canvas.drawLine(startX, startY, endX, endY, paint);
			invalidate();
		}
		protected void onUpdate(){

			if(endX!=-1 && endY!=-1 && startX != endX && startY != endY && prevAction==MotionEvent.ACTION_UP){
				deltaX = (endX-startX);
				deltaY = (endY-startY);
				prevAction=99;
				endX=-1;
				endY=-1;
				startY=-1;
				endY=-1;
			}
			if(prevAction==MotionEvent.ACTION_MOVE && !onAir) return;
			posX += deltaX/5;
			posY += deltaY/5;
			deltaX-=0.01*deltaX;
			if(posY<screenHeight-100) deltaY+=2;
			if(posX>=screenWidth || posX<=0) deltaX*=-1;
			if(posY>=screenHeight) posY=screenHeight;
			if(posY<=0) posY = 0;
			if(posY<screenHeight-200) onAir = true;
			else if(onAir==true && posY>= screenHeight-200){
				gameOver = true;
				for(int i = 0; i<numPebbles ; i++){

					if (pebbles[i].collides(posX,posY) && pebbles[i].color==77){ gameOver = false;caught=true;}
					else if (pebbles[i].collides(posX+25,posY) && pebbles[i].color==77){ gameOver = false;caught=true;}
					else if (pebbles[i].collides(posX,posY+25) && pebbles[i].color==77){ gameOver = false;caught =true;}
					else if (pebbles[i].collides(posX+25,posY+25) && pebbles[i].color==77){ gameOver = false;caught=true;}
				}
			}
			else onAir = false;
			
			if(caught ){
				counter = 1;
				onAir = false;
				deltaX = 0; deltaY = 0;
				posX = screenWidth/2;
				posY = screenHeight-100;
				endX=-1;
				endY=-1;
				startY=-1;
				endY=-1;
				numPebbles++;
				score+=10;
				pebbles = new Pebble[numPebbles];
				for(int i = 0; i<numPebbles ; i++){
					pebbles[i] = new Pebble(0,0, 25);
					pebbles[i].setSpacedPos(750, 480, numPebbles, i);
					
					//pebbles[i].setColor(Color.YELLOW);
				}
				int[] colors = pebbles[0].generateColors(numPebbles, 1);
				for(int i=0; i<numPebbles; i++){
					pebbles[i].setColor(colors[i]);
				}
			}
			
		}
		@Override
		public boolean onTouchEvent(MotionEvent event){
			float x = event.getX();
			float y = event.getY();
			
			if(onAir){ 
				startX=screenWidth/2;endX=screenWidth/2;startY=screenHeight-50;;endY=screenHeight-50;; prevAction = event.getAction();
				for(int i = 0; i<numPebbles ; i++){
					if(pebbles[i].collides(x,y)) {
						//pebbles[i].setColor(Color.RED);
						if(y<screenHeight-200) y=screenHeight-200;
						pebbles[i].setPos(x,y);
					}
					//else pebbles[i].setColor(Color.YELLOW);
				}
				return true;
			}
			if(y<screenHeight-200) y = screenHeight-200;
			if(x>posX-50 && y>posY-50 && x<posX+100 && y<posY+100){
				mycolor = Color.CYAN;
			}else {mycolor = Color.BLACK;startX=screenWidth/2;endX=screenWidth/2;startY=screenHeight-50;endY=screenHeight-50; prevAction = event.getAction();return true;}
			posX = x;
			posY = y;
			if(event.getAction() == MotionEvent.ACTION_MOVE && prevAction == MotionEvent.ACTION_DOWN ){ startX = x; startY =y;}
			else if( prevAction == MotionEvent.ACTION_MOVE && event.getAction() == MotionEvent.ACTION_UP){ endX = x; endY =y;}
			prevAction = event.getAction();
			return true;
		}
		public void updateHighscores(){
			DatabaseHelper helper = new DatabaseHelper(myContext);
			SQLiteDatabase database = helper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("name", extras.getString("name"));
			values.put("score", score);
			database.insert("Highscores", null, values);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(new GameView(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.render_view_test, menu);
		return true;
	}
}
