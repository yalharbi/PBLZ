package com.example.PBLZ;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.example.PBLZ.R;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class RenderViewTest extends Activity{

	class TitleView extends View {
		
		private Bitmap titleBitmap;
		private Bitmap startButtonUp, startButtonDown;
		private Bitmap exitButtonUp, exitButtonDown;
		private boolean startButtonPressed = false;
		private boolean exitButtonPressed = false;
		private Context myContext;
		int screenWidth, screenHeight;
		
		public TitleView(Context context){
			super(context);
			
			titleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.titlebackground);
			startButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.startbuttonup);
			startButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.startbuttondown);
			exitButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.exitbuttonup);
			exitButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.exitbuttondown);
			myContext = context;
		}
		
		@Override
		protected void onDraw(Canvas canvas){
			screenWidth = canvas.getWidth();
			screenHeight = canvas.getHeight();
			canvas.drawBitmap(titleBitmap, 0, 0, null);
			if (!startButtonPressed)
				canvas.drawBitmap(startButtonUp, (float)(canvas.getWidth()-150)/2,canvas.getHeight()*(float)0.7, null);
			else
				canvas.drawBitmap(startButtonDown, (float)(canvas.getWidth()-150)/2,canvas.getHeight()*(float)0.7, null);
			
			if(!exitButtonPressed)
				canvas.drawBitmap(exitButtonUp, (float)(canvas.getWidth()-150)/2,canvas.getHeight()*(float)0.8, null);
			else
				canvas.drawBitmap(exitButtonDown, (float)(canvas.getWidth()-150)/2,canvas.getHeight()*(float)0.7, null);
				
			invalidate();
			
			if(exitButtonPressed) System.exit(0);
		}
		

		@Override
		public boolean onTouchEvent(MotionEvent event){
			float x = event.getX();
			float y = event.getY();

				
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
				&& y> (screenHeight*0.7) && y< ((screenHeight*0.7)+75))
					startButtonPressed = true;
				else if(x>(screenWidth-150)/2 && x< ((screenWidth-150)/2+150)
						&& y> (screenHeight*0.8) && y< ((screenHeight*0.8)+75))
					exitButtonPressed = true;
			    return true;
		    case MotionEvent.ACTION_MOVE:
		    	return true;
			
		    case MotionEvent.ACTION_UP:
				      // nothing to do
		    	if(startButtonPressed){
		    		Intent gameIntent = new Intent(myContext, UserWeatherActivity.class);
		    		myContext.startActivity(gameIntent);
		    	//	finish();
		    	}
		    	startButtonPressed = false;
				 return true;
			}
			return false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		TitleView tv = new TitleView(this);
		setContentView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.render_view_test, menu);
		return true;
	}

}
