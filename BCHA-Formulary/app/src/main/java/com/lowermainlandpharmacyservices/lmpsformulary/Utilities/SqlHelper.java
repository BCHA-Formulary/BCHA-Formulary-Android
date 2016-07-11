package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels.SqlDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels.SqlFormulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelvinchan on 2016-06-28.
 */
public class SqlHelper extends SQLiteOpenHelper{

    //DB version
    private static final int DATABASE_VERSION = 1;
    //DB name
    private static final String DATABASE_NAME = "drugsManager";
    //Table names
    private static final String TABLE_DRUG = "drugTable";
    private static final String TABLE_FORMULARY = "formularyTable";
    private static final String TABLE_EXCLUDED = "excludedTable";
    private static final String TABLE_RESTRICTED = "restrictedTable";

    //Drug table columns
    private static final String KEY_PRIMARY_NAME = "primary_id";
    private static final String KEY_NAME_TYPE = "name_type";
    private static final String KEY_STATUS = "status";
    private static final String KEY_CLASS = "class";

    //Formulary Table columns
    private static final String KEY_PRIMARY_UID = "uid";
    private static final String KEY_GENERIC_NAME = "generic_name";
    private static final String KEY_BRAND_NAME = "brand_name";
    private static final String KEY_STRENGTH = "strength";


    public SqlHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * CREATE TABLE drugTable (primary_id TEXT PRIMARY KEY,
         *                         name_type TEXT,
         *                         status TEXT,
         *                         class TEXT)
         *
         */
        String CREATE_DRUGS_TABLE = "CREATE TABLE " + TABLE_DRUG + " ("
                + KEY_PRIMARY_NAME + " TEXT PRIMARY KEY,"
                + KEY_NAME_TYPE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_CLASS + " TEXT" + ")";
        db.execSQL(CREATE_DRUGS_TABLE);

        String CREATE_FORUMARLY_TABLE = "CREATE TABLE " + TABLE_FORMULARY + " ("
                + KEY_PRIMARY_UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_GENERIC_NAME + " TEXT,"
                + KEY_BRAND_NAME + "TEXT,"
                + KEY_STRENGTH + "TEXT" + ")";
        db.execSQL(CREATE_FORUMARLY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULARY);

        //create new table
        this.onCreate(db);
    }

    //add single drug row
    public void addDrug(SqlDrug drug){
        SQLiteDatabase db = this.getWritableDatabase();

        //put row values in
        ContentValues values = new ContentValues();
        values.put(KEY_PRIMARY_NAME, drug.getPrimaryName());
        values.put(KEY_NAME_TYPE, drug.getNameType());
        values.put(KEY_STATUS, drug.getStatus());
        values.put(KEY_CLASS, drug.getDrugClass());

        //write to table
        db.insert(TABLE_DRUG, null, values);
        db.close();
    }

    /**
     * Inserts a single SqlFormulary drug into the table
     * This method does NOT add the drug into the drug table and will
     * need to be manually added
     *
     * Primary Key "uid" is auto-generated and does not need to be included into values
     * @param formulary
     */
    public void addFormulary(SqlFormulary formulary){
        SQLiteDatabase db = this.getWritableDatabase();

        //put row values in
        ContentValues values = new ContentValues();
        values.put(KEY_GENERIC_NAME, formulary.getGenericName());
        values.put(KEY_BRAND_NAME, formulary.getBrandName());
        values.put(KEY_STRENGTH, formulary.getStrength());

        //write to table
        db.insert(TABLE_FORMULARY, null, values);
        db.close();
    }

    //read single drug row
    public SqlDrug getDrug(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_DRUG, new String[]{KEY_PRIMARY_NAME, KEY_NAME_TYPE, KEY_STATUS, KEY_CLASS},
                KEY_PRIMARY_NAME + "=?", new String[] { KEY_PRIMARY_NAME}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SqlDrug drug = new SqlDrug(cursor.getString(0), cursor.getString(1),
                                    cursor.getString(2), cursor.getString(3));
        return drug;
    }

    /**
     * From the formulary table, find all drugs with a certain generic or brand name
     * @param name - Name of the drug to find
     * @param isGenericName - true if the name is generic, false if it is a brand
     * @return
     */
    public List<SqlFormulary> getFormularyDrugs(String name, boolean isGenericName){
        SQLiteDatabase db = this.getReadableDatabase();
        List<SqlFormulary> forumaryList = new ArrayList<SqlFormulary>();

        //Build query based on name
        String key_column = isGenericName ? KEY_GENERIC_NAME : KEY_BRAND_NAME;
        String query = "SELECT * FROM " + TABLE_FORMULARY + " WHERE " + key_column +" = " + name.trim();
        Cursor c = db.rawQuery(query, null);

        //Add all query results to list
        if(c.moveToFirst()){
            do{
                SqlFormulary formularyDrug = new SqlFormulary(c.getString(1), c.getString(2), c.getString(3));
                forumaryList.add(formularyDrug);
            } while (c.moveToNext());
        }

        return forumaryList;
    }

    //get all drugs
    public List<SqlDrug> getAllDrugs(){
        List<SqlDrug> drugList = new ArrayList<SqlDrug>();

        String selectAllQuery = "SELECT * FROM " + TABLE_DRUG;

        SQLiteDatabase db = getReadableDatabase(); //TODO example says make writable...we'll see
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if(cursor.moveToFirst()){
            do{
                SqlDrug drug = new SqlDrug(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3));
                drugList.add(drug);
            } while (cursor.moveToNext());
        }

        return drugList;
    }

    //get drug count
    public int getDrugCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectAllQuery = "SELECT * FROM " + TABLE_DRUG;
        Cursor cursor = db.rawQuery(selectAllQuery, null);
        cursor.close();

        return cursor.getCount();
    }

    public int updateDrug(SqlDrug drug){
        SQLiteDatabase db = getWritableDatabase();

        //put row values in
        ContentValues values = new ContentValues();
        values.put(KEY_PRIMARY_NAME, drug.getPrimaryName());
        values.put(KEY_NAME_TYPE, drug.getNameType());
        values.put(KEY_STATUS, drug.getStatus());
        values.put(KEY_CLASS, drug.getDrugClass());

        return db.update(TABLE_DRUG, values, KEY_PRIMARY_NAME + " =?",
                new String[]{drug.getPrimaryName()});
    }

//    public int updateFormulary(SqlFormulary formulary){
//        SQLiteDatabase db = getWritableDatabase();
//
//        //put row values in
//        ContentValues values = new ContentValues();
//        values.put(KEY_GENERIC_NAME, formulary.getGenericName());
//        values.put(KEY_BRAND_NAME, formulary.getBrandName());
//        values.put(KEY_STRENGTH, formulary.getStrength());
//
//        return db.update(TABLE_FORMULARY, values, )
//    }

    public void deleteDrug(SqlDrug drug){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_DRUG, KEY_PRIMARY_NAME + " = ?", new String[]{KEY_PRIMARY_NAME});
        db.close();
    }
}
