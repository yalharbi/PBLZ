package com.example.PBLZ;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	 private static final String DATABASE_NAME = "PBLZ";

     private static final int DATABASE_VERSION = 2;

     // Database creation sql statement
     private static final String DATABASE_CREATE = "create table Highscores ( name text primary key,score int not null);";

     public DatabaseHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
     }

     // Method is called during creation of the database
     @Override
     public void onCreate(SQLiteDatabase database) {
         database.execSQL(DATABASE_CREATE);
     }

     // Method is called during an upgrade of the database,
     @Override
     public void onUpgrade(SQLiteDatabase database, int oldVersion,
             int newVersion) {
         Log.w(DatabaseHelper.class.getName(),
                 "Upgrading database from version " + oldVersion + " to "
                         + newVersion + ", which will destroy all old data");
         database.execSQL("DROP TABLE IF EXISTS Highscores");
         onCreate(database);
     }

}
