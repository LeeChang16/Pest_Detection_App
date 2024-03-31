package com.example.pestdetectionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Blob;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DataBase_Name = "Offline_Database";
    private static final int Db_Version = 1;


    // Table for Personal Information of Clients
    private static final String Personal_Info_Table = "Personal_Information";
    private static final String Client_Id = "Client_Id";
    private static final String FullName = "Fullname";
    private static final String Province = "Province";
    private static final String Town = "Town";
    private static final String Occupation = "Occupation";
    private static final String Profile_Picture = "Picture";



    //Table for Accounts
    private static final String Account_Table = "Account";
    private static final String Account_Id = "Account_Id";
    private static final String Account_Username = "Username";
    private static final String Account_Password = "Password";
    private static final String Account_Active = "Active";


    // Table for Pest Information
    private static final String Pest_Info_Table = "Pest_Information";
    private static final String Pest_Id = "Pest_Id";
    private static final String Picture = "Sample_Picture";
    private static final String Pest_Name = "Pest_Name";
    private static final String Scientific_Name = "Scientific_Name";
    private static final String Order = "Pest_Order";
    private static final String Family = "Pest_Family";
    private static final String Description = "Description";
    private static final String Intervention = "Intervention";


    // Table for Detected Pest
    private static final String Detected_Pest_Table = "Detected_Pest";
    private static final String Detected_Pest_Id = "Pest_Id";
    private static final String Detected_Pest_Picture = "Sample_Picture";
    private static final String Detected_Pest_Name = "Pest_Name";
    private static final String Detected_Pest_Confidence = "Confidence_Level";
    private static final String Detected_Pest_Date = "Date";
    private static final String Detected_Pest_Time = "Time";
    private static final String Detected_Pest_User = "User_Id";


    public DatabaseHandler(@Nullable Context context) {
        super(context, DataBase_Name, null, Db_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creation of Client Table
        String query1 = "CREATE TABLE " + Personal_Info_Table + " ("
                + Client_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + FullName + " TEXT,"
                + Province + " TEXT,"
                + Town + " TEXT,"
                + Occupation + " TEXT,"
                + Profile_Picture + " BLOB)";

        // Creation of Pest Information Table
        String query2 = "CREATE TABLE " + Pest_Info_Table + " ("
                + Pest_Id + " INTEGER PRIMARY KEY, "
                + Pest_Name + " TEXT,"
                + Scientific_Name + " TEXT,"
                + Order + " TEXT,"
                + Family + " TEXT,"
                + Description + " TEXT,"
                + Intervention + " TEXT,"
                + Picture + " BLOB)";

        String query3 = "CREATE TABLE " + Detected_Pest_Table + " ("
                + Detected_Pest_Id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Detected_Pest_Name + " TEXT,"
                + Detected_Pest_Confidence + " TEXT,"
                + Detected_Pest_Picture + " BLOB,"
                + Detected_Pest_Time + " TEXT,"
                + Detected_Pest_Date + " TEXT,"
                + Detected_Pest_User + " TEXT)";

        String query4 = "CREATE TABLE " + Account_Table + " ("
                + Account_Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Account_Username + " TEXT,"
                + Account_Password + " TEXT,"
                + Account_Active + " INTEGER)";



        db.execSQL(query1);
        db.execSQL(query2);
        db.execSQL(query3);
        db.execSQL(query4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Check if Table Already Existed, if true table will be drop
        db.execSQL("DROP TABLE IF EXISTS " + Personal_Info_Table);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + Pest_Info_Table);
        onCreate(db);

    }

    public void insertPest( String pestname, String confidencelevel, byte[] image, String time, String date, String user_id){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        insertValues.put(Detected_Pest_Name, pestname);
        insertValues.put(Detected_Pest_Confidence, confidencelevel);
        insertValues.put(Detected_Pest_Picture, image);
        insertValues.put(Detected_Pest_Time, time);
        insertValues.put(Detected_Pest_Date, date);
        insertValues.put(Detected_Pest_User, user_id);
        db.insert(Detected_Pest_Table, null, insertValues);

    }

    public void insertAccount(String Username, String Password, int Active){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        insertValues.put(Account_Username, Username);
        insertValues.put(Account_Password, Password);
        insertValues.put(Account_Active, Active);
        db.insert(Account_Table, null, insertValues);

    }

    public void insertClient(String fullname, String province, String town, String occupation, byte[] profile){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        insertValues.put(FullName, fullname);
        insertValues.put(Province, province);
        insertValues.put(Town, town);
        insertValues.put(Occupation, occupation);
        insertValues.put(Profile_Picture, profile);
        db.insert(Personal_Info_Table, null, insertValues);

    }

    public int getId(String pass){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT Account_Id FROM Account WHERE Password = ?",new String[]{pass});
        int Id = 0;
        if (cursor.moveToFirst()) {
            Id = cursor.getInt(0);
        }
        cursor.close();
        return Id;
    }

    public void turn_on_active_status(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+Account_Table+" SET "+Account_Active+" = '1' WHERE "+Account_Id+" = "+id);
    }

    public void turn_off_active_status(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE "+Account_Table+" SET "+Account_Active+" = '0' WHERE "+Account_Id+" = "+id);
    }

    public boolean active_user(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+Account_Table+" WHERE "+Account_Active+" = '1'",null);
        boolean active = cursor.getCount()>0;

        return active;
    }

    public int get_active_user_Id(){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT "+Account_Id+" FROM "+Account_Table+" WHERE "+Account_Active+"= '1'",null);
        int Id =0;

        if(cursor.moveToFirst()) {
            Id = cursor.getInt(0);
            cursor.close();

        }
        return Id;
    }


    public String get_hash(String pass){

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " +Account_Table+ " WHERE " +Account_Password+ "= ?", new String[]{pass});
        String hash = null;
        if (cursor.moveToFirst()) {
            hash = cursor.getString(2);
        }
        cursor.close();


        return hash;
    }

    public String get_user(String user){

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " +Account_Table+ " WHERE " +Account_Username+ "= ?", new String[]{user});
        user = null;
        if (cursor.moveToFirst()) {
            user = cursor.getString(1);
        }
        cursor.close();


        return user;
    }


    public String getPestDetails (int id){
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM "+Pest_Info_Table+" WHERE "+Pest_Id+" = ?",new String[]{String.valueOf(id)});
        return cursor.toString();

    }

    public void insertPestDetails(String id, String name, String sci, String order, String family,String desc, String inter, byte[] image){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues insertValues = new ContentValues();
        insertValues.put(Pest_Id, id);
        insertValues.put(Pest_Name, name);
        insertValues.put(Scientific_Name, sci);
        insertValues.put(Order, order);
        insertValues.put(Family, family);
        insertValues.put(Description, desc);
        insertValues.put(Intervention, inter);
        insertValues.put(Picture, image);
        db.insert(Pest_Info_Table, null, insertValues);

    }





}
