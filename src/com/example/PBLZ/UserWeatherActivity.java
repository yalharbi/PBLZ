package com.example.PBLZ;

//import com.example.practise.GameActivity.GameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import com.example.PBLZ.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class UserWeatherActivity extends Activity implements OnTouchListener{
	String name;
	int zipcode, weather=-1;
	EditText nameT, zipcodeT;
	ImageView iv, iv2;
	Bitmap proceed, skip;
	class Weather extends AsyncTask<Integer, Void, Integer>{
		@Override
		protected Integer doInBackground(Integer... zipcode){
			URL url;
			InputStream is = null;
			BufferedReader br;
			String line;
			int temperature = -1;
			String link = new String("http://www.wunderground.com/weather-forecast/");
			link  = link+ zipcode[0];
			try{
				url = new URL(link);
				is = url.openStream();
				br = new BufferedReader(new InputStreamReader(is));
				while((line = br.readLine()) != null){
					if(line.contains("tempActual")){
						//System.out.println(line);
						line = line.substring(line.indexOf("value")+7,line.indexOf("value")+11);
						temperature = (int)Float.parseFloat(line);
						//System.out.println(temperature);
					}
				}
			} catch(MalformedURLException e){}
			catch(IOException e){}
			finally{
				try {
					if(is!=null) is.close();
				}catch (IOException e){}
			}
			return temperature;
		}
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		/*StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);*/
		LinearLayout ll = new LinearLayout(this);
		ll.setBackgroundColor(Color.BLACK);
		ll.setOrientation(LinearLayout.VERTICAL);
		nameT = new EditText(this);
		nameT.setText("Enter Your Name Here");
		nameT.setTextColor(Color.WHITE);
		ll.addView(nameT);
		zipcodeT = new EditText(this);
		zipcodeT.setText("Enter Your Zipcode Here");
		zipcodeT.setTextColor(Color.WHITE);
		ll.addView(zipcodeT);
		ll.addView(new TextView(this));
		ll.addView(new TextView(this));
		ll.addView(new TextView(this));
		LinearLayout l2=  new LinearLayout(this);
		l2.setOrientation(LinearLayout.HORIZONTAL);
		proceed = BitmapFactory.decodeResource(getResources(), R.drawable.proceed);
		iv = new ImageView(this);
		iv.setImageBitmap(proceed);
		iv.setOnTouchListener(this);
		l2.addView(iv);
		skip = BitmapFactory.decodeResource(getResources(), R.drawable.skip);
		iv2 = new ImageView(this);
		iv2.setImageBitmap(skip);
		iv2.setOnTouchListener(this);
		l2.addView(iv2);
		ll.addView(l2);
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(ll);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.render_view_test, menu);
		return true;
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event){
		if(event.getAction() == MotionEvent.ACTION_UP){
			if(v.equals(iv)){
				name = nameT.getText().toString();
				zipcode = Integer.parseInt(zipcodeT.getText().toString());
				if(weather==-1){weather=1; try {
					weather = getWeather(zipcode);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
			}
			else if(v.equals(iv2)){
				name = " ";
				weather = -1;
			}
			//button.setText(weather + " ");
    		Intent gameIntent = new Intent(this, GameActivity.class);
    		gameIntent.putExtra("name", name);
    		gameIntent.putExtra("weather", weather);
    		this.startActivity(gameIntent);
    		finish();
		}
		return true;
	}
	
	public int getWeather(int zipcode) throws InterruptedException, ExecutionException{
		return new Weather().execute(zipcode).get();
	}
}
