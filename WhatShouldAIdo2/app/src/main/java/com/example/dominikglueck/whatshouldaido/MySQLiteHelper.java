package com.example.dominikglueck.whatshouldaido;

/**
 * Created by dominik.glueck on 08.08.2016.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dominik.glueck on 25.07.2016.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static String DB_PATH = "/data/data/com.example.dominikglueck.whatshouldaido/databases/";


    // Database Name
    private SQLiteDatabase myDataBase;
    private static final int DATABASE_VERSION = 1;
    private final Context myContext;

    private static final String DATABASE_NAME = "lottozahlen.db";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " geladen.");
        this.myContext = context;
    }
    public void CreateDataBase() throws IOException {

        if(!checkDataBase()){
            this.getReadableDatabase();

            try{
                copyDataBase();
            }catch (IOException e) {
                throw new Error("Error copy db");
            }
        }
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){
            //existiert nicht
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null ? true : false;

    }
    private void copyDataBase() throws IOException{

        InputStream myInput = myContext.getAssets().open(DATABASE_NAME);

        String outFileName = DB_PATH + DATABASE_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }
    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        MySQLiteDatabaseCreator.FeedEntry.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public String frage(int option){
        Cursor cursor = null;
        String result;
        cursor = myDataBase.rawQuery("SELECT * AS Frage FROM Fragen\n" +
                "WHERE id ="+option+";", null);
        cursor.moveToFirst();
        result = cursor.getString(0);
        cursor.close();
        return result;
    }
    public int option(String variance){
        Cursor cursor  = null;
        int result;
        cursor = myDataBase.rawQuery("SELECT ID FROM optionen WHERE variante ="+variance+";", null);
        cursor.moveToFirst();
        result= cursor.getInt(0);
        cursor.close();
        return result;
    }
    public String antwort(String variance){
        Cursor cursor = null;
        String result;
        cursor = myDataBase.rawQuery("SELECT * FROM antworten WHERE variante ="+variance+");",null);
        cursor.moveToFirst();
        result= cursor.getString(0);
        cursor.close();
        return result;
    }

    public List<Result> getEntries(String tablename) {
        String selectQuery = "SELECT * FROM " + tablename;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        List<Result> resultArrayList = new ArrayList<Result>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Result result = new Result();
                result.setId(cursor.getString(1));
                result.setContent(cursor.getString(2));
                // Adding contact to list
                resultArrayList.add(result);
            } while (cursor.moveToNext());
        }
        // return contact list
        return resultArrayList;
    }
}
