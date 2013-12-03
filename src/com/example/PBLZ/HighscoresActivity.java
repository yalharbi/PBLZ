package com.example.PBLZ;



import android.app.Activity;
import android.content.Context;
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

public class HighscoresActivity extends Activity{
	class HighscoresView extends View{
		private Bitmap highscoresBitmap;
		private Bitmap tryAgainUp, tryAgainDown;
		private boolean tryAgainPressed = false;
		float screenWidth, screenHeight;
		Context myContext;
		Paint paint;
		MediaPlayer buttonSound;
		String[] highscores;
		public HighscoresView(Context context){
			super(context);
			AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
			buttonSound = MediaPlayer.create(context, R.raw.press);

			highscoresBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.highscoresbackground);
			tryAgainUp = BitmapFactory.decodeResource(getResources(), R.drawable.tryagain);
			tryAgainDown = BitmapFactory.decodeResource(getResources(), R.drawable.tryagaindown);
			paint = new Paint();
			myContext = context;
			highscores = new String[4];
			for(int i=0;i<4;i++)
				highscores[i] = getHighscore(i);
		}
		
		protected void onDraw(Canvas canvas){
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			canvas.drawBitmap(highscoresBitmap, 0, 0, null);
			//canvas.drawColor(Color.BLACK);
			paint.setTextSize(32);
			paint.setStyle(Paint.Style.STROKE);
			//paint.setColor(Color.rgb(46,46,46));
			paint.setColor(Color.WHITE);
			canvas.drawText(highscores[0], 170, 209,  paint);
			paint.setTextSize(28);
			canvas.drawText(highscores[1], 170, 264,  paint);
			paint.setTextSize(24);
			canvas.drawText(highscores[2], 170, 264+55,  paint);
			paint.setTextSize(20);
			canvas.drawText(highscores[3], 170, 264+55+55,  paint);
			if(!tryAgainPressed)
				canvas.drawBitmap(tryAgainUp, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.7, null);
			else
				canvas.drawBitmap(tryAgainDown, (canvas.getWidth()-150)/2, canvas.getHeight()*(float)0.7, null);
			invalidate();
		}
		public boolean onTouchEvent(MotionEvent event){
			float x = event.getX();
			float y = event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
				&& y> (screenHeight*0.7) && y< ((screenHeight*0.7)+75))
					{tryAgainPressed = true;buttonSound.start();}
				else if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
						&& y> (screenHeight*0.8) && y< ((screenHeight*0.8)+75))
							tryAgainPressed = true;
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
		    	tryAgainPressed = false;
				 return true;
			}
			return false;
		}
		public String getHighscore(int rank){
			DatabaseHelper helper = new DatabaseHelper(myContext);
			SQLiteDatabase database = helper.getReadableDatabase();
			Cursor c = database.rawQuery("SELECT * FROM Highscores ORDER BY score DESC", null);
			String highscore = "";
			int i=0;
			if(c.moveToFirst()){
				do{
					if(i==rank) {highscore = c.getString(0) +  " " + c.getInt(1) + "\n"; break;}
					else i++;
				}while(c.moveToNext());
			}
			database.close();
			return highscore;
		}
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		HighscoresView hv = new HighscoresView(this);
		setContentView(hv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.render_view_test, menu);
		return true;
	}
}
