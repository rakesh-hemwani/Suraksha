package com.example.womensafety;

import android.app.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private  static final String dbname="PersonDb.db";



    public DbHelper( Context context) {
        super(context, dbname, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String qry="create table Person (id INTEGER  primary key, name varchar2, cont1 varchar2, cont2 varchar2)";
        sqLiteDatabase.execSQL(qry);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Person");
        onCreate(sqLiteDatabase);
    }

    public String addRecord(String s1,String s2,String s3) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", s1);
        cv.put("cont1",s2);
        cv.put("cont2",s3);


        long res = db.insert("Person", null, cv);

        if (res == -1)
            return "failed";
        else

            return "success";


    }

    public boolean upgrade(String name,String Phno1,String Phno2)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("cont1",Phno1);
        cv.put("cont2",Phno2);
       long res= db.update("Person", cv, "name=?", new String[]{name});

        if (res == -1)
            return false;
        else

            return true;

    }

    public Cursor readalldata()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String qry="select * from Person";
        Cursor cursor =db.rawQuery(qry,null);
        return cursor;

    }
}
