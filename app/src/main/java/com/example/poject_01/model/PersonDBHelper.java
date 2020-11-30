package com.example.poject_01.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by developers on 9/16/2020.
 */

public class PersonDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "fav_poetry.db";
    private static final int DATABASE_VERSION = 5 ;
    public static final String TABLE_NAME = "fav_poetry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_POETRY = "poetry";
    public static final String COLUMN_INSPECTION = "inspection";


    public PersonDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_NAME + " TEXT , " +
                COLUMN_POETRY + " TEXT );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }
    /**create record**/
    public  void insertProductOrder(String user_name, String poetry) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user_name);
        values.put(COLUMN_POETRY, poetry);

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public Cursor getSpecificData(String poetry_match){
        SQLiteDatabase db =this.getWritableDatabase();
        Cursor res=db.rawQuery("select * from "+TABLE_NAME+" where poetry=? " ,new String[]{poetry_match});
        return res;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
    /**delete record**/
    public void deleteOrder(String poetry_delete, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_NAME+" WHERE poetry='"+poetry_delete+"'");
        //Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }
}
