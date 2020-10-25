package com.fawzy.mapsapplication.DB;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.fawzy.mapsapplication.AddActivity;

import java.util.ArrayList;
import java.util.List;

public class DB extends SQLiteOpenHelper {


    public DB(@Nullable Context context) {
        super(context, constants.DB_Name,null, constants.DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String Crt_db = " CREATE TABLE " + constants.DB_TABLE + " (" + constants.KEY_ID + " INTEGER PRIMARY KEY, " +
                constants.LATITUDE + " TEXT, " +
                constants.LONGITUTDE + " TEXT, " +
                constants.TITLE + " TEXT )" ;
        db.execSQL(Crt_db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL(" DROP TABLE IF EXISTS " + constants.DB_Name) ;
         onCreate(db);
    }

    public void addplace(places place){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(constants.LATITUDE,place.getLatitude());
        contentValues.put(constants.LONGITUTDE,place.getLongtitude());
        contentValues.put(constants.TITLE,place.getTittle());
        sqLiteDatabase.insert(constants.DB_TABLE,null,contentValues);
    }


    public List<places> getAllPlaces(){
         SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
         List<places> placesList = new ArrayList<>();
         String GetAll = "SELECT * FROM " + constants.DB_TABLE ;
        Cursor cursor = sqLiteDatabase.rawQuery(GetAll,null);
        if (cursor.moveToFirst()){
            do {
                places place = new places();
                place.setId(cursor.getInt(cursor.getColumnIndex(constants.KEY_ID)));
                place.setLatitude(cursor.getString(cursor.getColumnIndex(constants.LATITUDE)));
                place.setLongtitude(cursor.getString(cursor.getColumnIndex(constants.LONGITUTDE)));
                place.setTittle(cursor.getString(cursor.getColumnIndex(constants.TITLE)));
                placesList.add(place);
            }while (cursor.moveToNext());
                cursor.close();
            }
        return  placesList ;
        }

        public  int getplaceNum (){
            SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
            String GetAll =  "SELECT * FROM " + constants.DB_TABLE ;
            Cursor cursor = sqLiteDatabase.rawQuery(GetAll,null);
            return  cursor.getCount();
        }

        public void removeplace(places place){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            sqLiteDatabase.delete(constants.DB_TABLE,constants.KEY_ID+"=?",new String[]{String.valueOf(place.getId())});
            sqLiteDatabase.close();
        }

        public int editplace(places place){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(constants.LATITUDE,place.getLatitude());
            contentValues.put(constants.LONGITUTDE,place.getLongtitude());
            contentValues.put(constants.TITLE,place.getTittle());
        int result =  sqLiteDatabase.update(constants.DB_TABLE,contentValues,constants.KEY_ID+"=?" , new String[]{String.valueOf(place.getId())});
        sqLiteDatabase.close();
        return  result ;
        }










    }








