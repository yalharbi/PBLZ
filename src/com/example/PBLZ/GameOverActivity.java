package com.example.PBLZ;

import com.example.PBLZ.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class GameOverActivity extends Activity{
	class GameOverView extends View{
		private Bitmap gameOverBitmap;
		private Bitmap tryAgainUp, tryAgainDown;
		private Bitmap highscoresUp, highscoresDown;
		private Bitmap exitUp, exitDown;
		private boolean tryAgainPressed = false;
		private boolean highscoresPressed = false;
		private boolean exitPressed = false;
		float screenWidth, screenHeight;
		Context myContext;
		Paint paint;
		MediaPlayer buttonSound;
		public GameOverView(Context context){
			super(context);
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
			buttonSound = MediaPlayer.create(context, R.raw.press);

			gameOverBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
			tryAgainUp = BitmapFactory.decodeResource(getResources(), R.drawable.tryagain);
			tryAgainDown = BitmapFactory.decodeResource(getResources(), R.drawable.tryagaindown);
			highscoresUp = BitmapFactory.decodeResource(getResources(), R.drawable.highscores);
			highscoresDown = BitmapFactory.decodeResource(getResources(), R.drawable.highscoresdown);
			exitUp = BitmapFactory.decodeResource(getResources(), R.drawable.exitbuttonup);
			exitDown = BitmapFactory.decodeResource(getResources(), R.drawable.exitbuttondown);
			paint = new Paint();
			myContext = context;
		}
		
		protected void onDraw(Canvas canvas){
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			canvas.drawBitmap(gameOverBitmap, 0, 0, null);
			//canvas.drawColor(Color.BLACK);
			paint.setTextSize(32);
			paint.setStyle(Paint.Style.STROKE);
			//paint.setColor(Color.rgb(46,46,46));
			paint.setColor(Color.WHITE);
			if(!tryAgainPressed)
				canvas.drawBitmap(tryAgainUp, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.6, null);
			else
				canvas.drawBitmap(tryAgainDown, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.6, null);
			if(!highscoresPressed)
				canvas.drawBitmap(highscoresUp, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.7, null);
			else
				canvas.drawBitmap(highscoresDown, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.7, null);
			if(!exitPressed)
				canvas.drawBitmap(exitUp, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.8, null);
			else
				canvas.drawBitmap(exitDown, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.8, null);
			invalidate();
		}
		public boolean onTouchEvent(MotionEvent event){
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
				&& y> (screenHeight*0.6) && y< ((screenHeight*0.6)+75))
					{tryAgainPressed = true;buttonSound.start();}
				else if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
						&& y> (screenHeight*0.7) && y< ((screenHeight*0.7)+75))
					{highscoresPressed = true;buttonSound.start();}
				else if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
						&& y> (screenHeight*0.8) && y< ((screenHeight*0.8)+75))
					{exitPressed = true;buttonSound.start();}
			    return true;
		    case MotionEvent.ACTION_MOVE:
		    	return true;
			
		    case MotionEvent.ACTION_UP:
				      // nothing to do
		    	if(tryAgainPressed){
		    		//Intent gameIntent = new Intent(myContext, RenderViewTest.class);
		    		//myContext.startActivity(gameIntent);
		    		finish();
		    	}
		    	if(highscoresPressed){
		    		Intent gameIntent = new Intent(myContext, HighscoresActivity.class);
		    		myContext.startActivity(gameIntent);
		    		finish();
		    	}
		    	if(exitPressed){
		    		finish();
		    	}
		    	tryAgainPressed = false;
				 return true;
			}
			return false;
		}
		public String getEntry(){
			DatabaseHelper helper = new DatabaseHelper(myContext);
			SQLiteDatabase database = helper.getReadableDatabase();
			Cursor c = database.rawQuery("SELECT * FROM Highscores ORDER BY score DESC", null);
			String highscores = "";
			if(c.moveToFirst()){
				do{
					highscores = highscores +c.getString(0) +  " " + c.getInt(1) + "\n";
				}while(c.moveToNext());
			}
			
			return highscores;
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(new GameOverView(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.render_view_test, menu);
		return true;
	}
	

		

}
