package com.example.uvtstudentapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

    public static final String DB_NAME = "LocalDatabase.db";

    public static final String Table_Note = "Note";
    /*          */
    public static final String Nota = "Nota";
    public static final String Materie = "Materie";
    public static final String Data = "Data";
    public static final String Semestru = "Semestru";

    public static final String Table_Informatii = "Informatii";
    /*          */
    public static final String DataNastere = "DataNastere";
    public static final String Cetatenie = "Cetatenie";
    public static final String CNP = "CNP";
    public static final String BackupEmail = "BackupEmail";
    public static final String Specializare = "Specializare";

    public DataBase(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CreateTableInformatii = "CREATE TABLE IF NOT EXISTS " + Table_Informatii + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DataNastere + " TEXT, " + Cetatenie + " TEXT, " + CNP + " TEXT, " + BackupEmail + " TEXT, " + Specializare + " TEXT)";

        String CreateTableNote = "CREATE TABLE IF NOT EXISTS " + Table_Note + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Nota + " TEXT, " + Materie + " TEXT, " + Data + " TEXT, " + Semestru + " TEXT)";

        db.execSQL(CreateTableInformatii);
        db.execSQL(CreateTableNote);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void UpdataData()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String UpdateInformatii = "DELETE FROM " + Table_Informatii;
        String UpdateNote = "DELETE FROM " + Table_Note;

        database.execSQL(UpdateInformatii);
        database.execSQL(UpdateNote);
    }

    public boolean InserareNote(String Details) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String[] TabelNote = Details.split("~");

        for (String parse : TabelNote) {
            String[] Note = parse.split(":");
            String Nota = Note[0];
            String Materie = Note[1];
            String Data = Note[2];
            String Semestru = Note[3];

            contentValues.put(DataBase.Nota, Nota);
            contentValues.put(DataBase.Materie, Materie);
            contentValues.put(DataBase.Data, Data);
            contentValues.put(DataBase.Semestru, Semestru);

            Log.d("DataBase Informative", "datele urmatoare: " + parse + " sunt adaugate in " + Table_Note);

            long result = database.insert(Table_Note, null, contentValues);

            if (result == -1) {
                return false;
            }
        }
        return true;
    }

    public boolean InserareDate(String Details) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String[] TabelData = Details.split(":");

        contentValues.put(DataNastere, TabelData[0]);
        contentValues.put(Cetatenie, TabelData[1]);
        contentValues.put(CNP, TabelData[2]);
        contentValues.put(BackupEmail, TabelData[3]);
        contentValues.put(Specializare, TabelData[4]);

        Log.d("Database Informative: ", "datele urmatoare: " + Details + " sunt adaugate in " + Table_Informatii);

        long result = database.insert(Table_Informatii, null, contentValues);

        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor returnData() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + Table_Informatii, null);
        return data;
    }

    public Cursor returnNote()
    {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor data = database.rawQuery("SELECT * FROM " + Table_Note + " ORDER BY Semestru", null);
        return data;
    }
    public boolean CheckIfEmpty()
    {
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + Table_Informatii, null);

        if ( cursor.moveToFirst() )
        {
            return true;
        }
        return false;
    }
}
