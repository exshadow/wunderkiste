package com.example.dominikglueck.whatshouldaido;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by D3v1lishGrin on 18.08.2016.
 */

public class MySQLiteDatabaseCreator  {
    public MySQLiteDatabaseCreator(){
    }

    public static abstract class FeedEntry implements BaseColumns{

        /**Namen der einzelnen Tabellen die erstellt werden **/
        public static final String TABLE_NAME_FRAGEN = "fragen";
        public static final String TABLE_NAME_OPTIONEN = "optionen";
        public static final String TABLE_NAME_ANTWORTEN = "antworten";

        /**Spalten der Fragetabelle**/
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_FRAGE = "frage";

        /**Spalten der Optionentabelle**/
        public static final String COLUMN_NAME_VARIANTE = "variante";


        /**Spalten der Antworttabelle**/
        public static final String COLUMN_NAME_ANTWORTEN = "antworten";


        /** Bei der Erstellung einer MySQLite Datenbank muss man eine Spalte haben
         die einen NULL Wert bekommen könnte daher diese Spalte**/
        public static final String COLUMN_NAME_NULL_EXPECTER = "Null_Expecter";

        private static final String INT_TYPE = " INT";
        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA_SEP = ",";

        /**SQL Statements**/
        private static final String SQL_CREATE_FRAGEN =
                "CREATE TABLE " + TABLE_NAME_FRAGEN + " (" +
                        FeedEntry.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_FRAGE + TEXT_TYPE +
                        FeedEntry.COLUMN_NAME_VARIANTE + TEXT_TYPE +
                        FeedEntry.COLUMN_NAME_NULL_EXPECTER + TEXT_TYPE +
                        " )";

        private static final String SQL_CREATE_OPTIONEN =
                "CREATE TABLE " + TABLE_NAME_OPTIONEN + " (" +
                        FeedEntry.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_FRAGE + TEXT_TYPE +
                        FeedEntry.COLUMN_NAME_VARIANTE + TEXT_TYPE +
                        FeedEntry.COLUMN_NAME_NULL_EXPECTER + TEXT_TYPE +
                        " )";
        private static final String SQL_CREATE_ANTWORTEN =
                "CREATE TABLE " + TABLE_NAME_ANTWORTEN + " (" +
                        FeedEntry.COLUMN_NAME_ID + INT_TYPE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_ANTWORTEN + TEXT_TYPE +
                        FeedEntry.COLUMN_NAME_NULL_EXPECTER + TEXT_TYPE +
                        " )";

        private static final String SQL_DELETE_FRAGEN =
                "DELETE * FROM fragen WHERE id = ";

        private static final String SQL_DELETE_ANTWORTEN =
                "DELETE * FROM antworten WHERE id = ";

        //private static final String SQL_SHOW_FRAGEN =
        //        "SELECT * " + "FROM " + FeedEntry.TABLE_NAME_FRAGEN +";";


        public static void onCreate(SQLiteDatabase db){
            db.execSQL(SQL_CREATE_ANTWORTEN);
            db.execSQL(SQL_CREATE_FRAGEN);
            db.execSQL(SQL_CREATE_OPTIONEN);
            db.close();
        }

        public static void deleteQuestions(SQLiteDatabase db, int id){
            db.execSQL(SQL_DELETE_FRAGEN + Integer.toString(id));
        }


        public static void deleteAnswers(SQLiteDatabase db, int id){
            db.execSQL(SQL_DELETE_ANTWORTEN + Integer.toString(id));
        }

        public static void newQuestion(SQLiteDatabase db,  String value){
            String SQL = "INSERT INTO fragen (frage) VALUES ("+value+")";
            db.execSQL(SQL);
        }

        public static void newAnswer(SQLiteDatabase db, String value){
            String SQL = "INSERT INTO antworten (antwort) VALUES ("+value+")";
            db.execSQL(SQL);
        }

    }
}
