package com.example.PBLZ;

import java.util.Random;

import com.example.PBLZ.R;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;


public class Pebble {
	float posX, posY, radius;
	int color;
	Bitmap bitmap;
	public Pebble(float posX, float posY, float radius){
		this.posX = posX;
		this.posY = posY;
		this.radius = radius;
		color = Color.YELLOW;
	}
	
	public void setPos(float posX, float posY){
		this.posX = posX;
		this.posY = posY;
	}
	
	public boolean collides(float x, float y){
		if(x>posX-25 && x<(posX+radius+25) && y>posY-25 && y<(posY+radius+25)){
			return true;
		}
		return false;
	}
	
	public void setColor(int c){
		this.color = c;
	}
	
	public void setSpacedPos(float level, float width, int number, int place){
		int x = ((int)width/(number-1))*place;
		if(x==0) x+=radius;
		if(x==width) x-=radius;
		setPos(x,level);
	}
	
	public int[] generateColors(int pebbles, int required){
		int colors[] = new int[pebbles];
		Random rand = new Random();
		
		for(int i=0;i<pebbles;i++){
			int color = rand.nextInt(5);
			if(color == 0) colors[i] = Color.RED;
			else if(color == 1) colors[i] = Color.GREEN;
			else if(color == 2) colors[i] = Color.BLUE;
			else if(color == 3) colors[i] = Color.YELLOW;
			else if(color == 4) colors[i] = Color.WHITE;
		}
		for(int i=0;i<required; i++){
			int pos = rand.nextInt(pebbles);
			while(colors[pos]==77) rand.nextInt(pebbles);
			colors[pos] = 77;
		}
		return colors;
	}
	
	public Bitmap getBitmap(Resources res){
		if(this.color == 77) return BitmapFactory.decodeResource(res, R.drawable.playpebble);
		if(this.color == Color.RED) return BitmapFactory.decodeResource(res, R.drawable.redpebble);
		if(this.color == Color.BLUE) return BitmapFactory.decodeResource(res, R.drawable.bluepebble);
		if(this.color == Color.GREEN) return BitmapFactory.decodeResource(res, R.drawable.greenpebble);
		if(this.color == Color.YELLOW) return BitmapFactory.decodeResource(res, R.drawable.yellowpebble);
		return BitmapFactory.decodeResource(res, R.drawable.whitepebble);
	}

}
