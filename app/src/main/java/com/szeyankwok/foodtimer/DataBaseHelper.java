package com.szeyankwok.foodtimer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    //Set up the SQLite database
    //Database Name
    public static final String DATABASE_NAME = "foodtimer";
    //Database Version i
    private static final int DATABASE_VERSION = 2;
    //Table Names
    public static final String FOOD_TABLE = "Food";
    public static final String CAT_TABLE = "Category";


    //Column name
    public static final String T1_COL_1 = "food_id";
    public static final String T1_COL_2 = "food_name";
    public static final String T1_COL_3 = "food_category";
    public static final String T1_COL_4 = "food_time";
    public static final String T1_COL_5 = "favourite";
    public static final String T2_COL_1 = "category";



    //The SQL to create FOOD_TABLE
    public static final String CREATE_FOOD_TABLE = "CREATE TABLE " + FOOD_TABLE + "(" + T1_COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + T1_COL_2 + " TEXT, " + T1_COL_3  + " TEXT, " + T1_COL_4+ " TEXT, " + T1_COL_5 + " TEXT) ";

    //The SQL to drop Food_TABLE
    public static final String DROP_FOOD_TABLE = "DROP TABLE IF EXISTS "+ FOOD_TABLE ;

    //The SQL to create FOOD_TABLE
    public static final String CREATE_CAT_TABLE = "CREATE TABLE " + CAT_TABLE + "(" + T2_COL_1 + " TEXT PRIMARY KEY ) " ;

    //The SQL to drop Food_TABLE
    public static final String DROP_CAT_TABLE = "DROP TABLE IF EXISTS "+ CAT_TABLE ;

    public DataBaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FOOD_TABLE);
        db.execSQL(CREATE_CAT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //on upgrade drop older tables
        db.execSQL(DROP_FOOD_TABLE);
        db.execSQL(DROP_CAT_TABLE);

        onCreate(db);
    }

    public boolean addFood(String name, String category, String time, Boolean fav ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(T1_COL_2, name);
        values.put(T1_COL_3, category);
        values.put(T1_COL_4, time);
        values.put(T1_COL_5, fav);

        db.insert(FOOD_TABLE, null, values);
        db.close();
        return true;
    }

    public Cursor getCategory() {
        SQLiteDatabase db = this.getReadableDatabase();

        //select category from food order by category
        Cursor mCursor = db.query(CAT_TABLE, new String[] {T2_COL_1}, null,null,null,null,T2_COL_1,null);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor getFoodList() {
        SQLiteDatabase db = this.getReadableDatabase();

        //select name from food order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2, T1_COL_5}, null,null,null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor getFoodbyCat(String category) {
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which food = name , category = category and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2, T1_COL_5},  T1_COL_3+" = ?",new String[] {category},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor getFoodbyFav() {
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which fav = true and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2, T1_COL_5}, T1_COL_5 + "=?" ,new String[] {"1"},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor getFood(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which fav = true and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2,T1_COL_3,T1_COL_4,T1_COL_5}, T1_COL_1 + "=?" ,new String[] {id},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor searchFood(String key){
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which name like key and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2,T1_COL_3,T1_COL_4,T1_COL_5}, T1_COL_2 + "likes ?" ,new String[] {key + "%"},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor searchFavFood(String key){
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which name like key and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2,T1_COL_3,T1_COL_4,T1_COL_5}, T1_COL_2 + " like ?  and " + T1_COL_5 + " =?" ,new String[] {"%" + key + "%", "1"},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Cursor searchFoodByCat(String catergory, String key){
        SQLiteDatabase db = this.getReadableDatabase();

        //select name, category, time and fav from food which name like key and order by name
        Cursor mCursor = db.query(FOOD_TABLE, new String[] {T1_COL_1,T1_COL_2,T1_COL_3,T1_COL_4,T1_COL_5}, T1_COL_2 + " like ?  and " + T1_COL_3 + " =?" ,new String[] {"%" + key + "%", catergory},null,null,T1_COL_2);

        if (mCursor != null){
            mCursor.moveToLast();
        }

        return mCursor;
    }

    public Boolean checkRepeatName(String name, String category) throws SQLException{
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor mCursor = db.query(FOOD_TABLE,new String[] {T1_COL_1}, T1_COL_2 + "=? and "  + T1_COL_3 + " =? ", new String[] {name, category},
                null,null,null,null);
        if (mCursor.getCount() <= 0){
            mCursor.close();
            return false;
        }

        mCursor.close();
        return true;
    }



    public Boolean addToFav (String name, Boolean fav){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1_COL_5, fav);

        db.update(FOOD_TABLE, values, T1_COL_2+" =?",new String[] {name});
        db.close();
        return true;
    }

    public Boolean editInfo (String id, String name, String category, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T1_COL_2, name);
        values.put(T1_COL_3, category);
        values.put(T1_COL_4, time);

        db.update(FOOD_TABLE, values, T1_COL_1 +" =? ",new String[] {id});
        db.close();
        return true;
    }

    public Boolean addCat (String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(T2_COL_1, category);

        db.insert(CAT_TABLE, null,values);
        db.close();
        return true;
    }

}
