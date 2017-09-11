//package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import com.lowermainlandpharmacyservices.lmpsformulary.Model.Drug;
//import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
//import com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels.SqlDrug;
//import com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels.SqlFormulary;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by kelvinchan on 2016-06-28.
// */
//public class SqlHelper extends SQLiteOpenHelper{
//
//    //DB version
//    private static final int DATABASE_VERSION = 1;
//    //DB name
//    private static final String DATABASE_NAME = "drugsManager";
//    //Table names
//    private static final String TABLE_DRUG_ENTRY = "drugEntryTable";
//    private static final String TABLE_FORMULARY_GENERIC = "formularyGenericTable";
//    private static final String TABLE_FORMULARY_BRAND = "formularyBrandTable";
//    private static final String TABLE_EXCLUDED_GENERIC = "excludedGenericTable";
//    private static final String TABLE_EXCLUDED_BRAND = "excludedBrandTable";
//    private static final String TABLE_RESTRICTED_GENERIC = "restrictedGenericTable";
//    private static final String TABLE_RESTRICTED_BRAND = "restrictedBrandTable";
//
//    //Drug table columns
//    private static final String KEY_ENTRY_PRIMARY_NAME = "primary_id";
//    private static final String KEY_ENTRY_NAME_TYPE = "name_type";
//    private static final String KEY_ENTRY_STATUS = "status";
//    private static final String KEY_ENTRY_CLASS = "class";
//
//    //Formulary Generic Table columns
//    private static final String KEY_FORM_GEN_NAME = "form_generic_name";
//    private static final String KEY_FORM_GEN_ALT_NAME = "form_generic_alt_name";
//    private static final String KEY_FORM_GEN_STRENGTH = "form_generic_strength";
//
//    //Formulary Brand Table columns
//    private static final String KEY_FORM_BRAND_NAME = "form_brand_name";
//    private static final String KEY_FORM_BRAND_ALT_NAME = "form_brand_alt_name";
//    private static final String KEY_FORM_BRAND_STRENGTH = "form_brand_strength";
//
//    //Excluded Generic Table Columns
//    private static final String KEY_EXCL_GENERIC_NAME = "excluded_generic_name";
//    private static final String KEY_EXCL_GENERIC_ALT_NAME = "excluded_generic_alt_name";
//    private static final String KEY_EXCL_GENERIC_CRITERIA = "excluded_generic_criteria";
//
//    //Excluded Brand Table Columns
//    private static final String KEY_EXCL_BRAND_NAME = "excluded_brand_name";
//    private static final String KEY_EXCL_BRAND_ALT_NAME = "excluded_brand_alt_name";
//    private static final String KEY_EXCL_BRAND_CRITERIA = "excluded_brand_criteria";
//
//    //Excluded Generic Table Columns
//    private static final String KEY_REST_GENERIC_NAME = "restricted_generic_name";
//    private static final String KEY_REST_GENERIC_ALT_NAME = "restricted_generic_alt_name";
//    private static final String KEY_REST_GENERIC_CRITERIA = "restricted_generic_criteria";
//
//    //Excluded Brand Table Columns
//    private static final String KEY_REST_BRAND_NAME = "restricted_brand_name";
//    private static final String KEY_REST_BRAND_ALT_NAME = "restricted_brand_alt_name";
//    private static final String KEY_REST_BRAND_CRITERIA = "restricted_brand_criteria";
//
//    public SqlHelper (Context context){
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        /**
//         * CREATE TABLE drugTable (primary_id TEXT PRIMARY KEY,
//         *                         name_type TEXT,
//         *                         status TEXT,
//         *                         class TEXT)
//         *
//         */
//        String CREATE_DRUGS_TABLE = "CREATE TABLE " + TABLE_DRUG_ENTRY + " ("
//                + KEY_ENTRY_PRIMARY_NAME + " TEXT PRIMARY KEY,"
//                + KEY_ENTRY_NAME_TYPE + " TEXT,"
//                + KEY_ENTRY_STATUS + " TEXT,"
//                + KEY_ENTRY_CLASS + " TEXT" + ")";
//        db.execSQL(CREATE_DRUGS_TABLE);
//
//        String CREATE_FORUMARLY_GEN_TABLE = "CREATE TABLE " + TABLE_FORMULARY_GENERIC + " ("
//                + KEY_FORM_GEN_NAME + " TEXT PRIMARY KEY,"
//                + KEY_FORM_GEN_ALT_NAME + " TEXT,"
//                + KEY_FORM_GEN_STRENGTH + "TEXT" + ")";
//        db.execSQL(CREATE_FORUMARLY_GEN_TABLE);
//
//        String CREATE_FORUMARLY_BRAND_TABLE = "CREATE TABLE " + TABLE_FORMULARY_BRAND + " ("
//                + KEY_FORM_BRAND_NAME + " TEXT PRIMARY KEY,"
//                + KEY_FORM_BRAND_ALT_NAME + " TEXT,"
//                + KEY_FORM_BRAND_STRENGTH + "TEXT" + ")";
//        db.execSQL(CREATE_FORUMARLY_BRAND_TABLE);
//
//        String CREATE_EXCLUDED_GEN_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_GENERIC + " ("
//                + KEY_EXCL_GENERIC_NAME + " TEXT PRIMARY KEY,"
//                + KEY_EXCL_GENERIC_ALT_NAME + " TEXT,"
//                + KEY_EXCL_GENERIC_CRITERIA + "TEXT" + ")";
//        db.execSQL(CREATE_EXCLUDED_GEN_TABLE);
//
//        String CREATE_EXCLUDED_BRAND_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_BRAND + " ("
//                + KEY_EXCL_BRAND_NAME + " TEXT PRIMARY KEY,"
//                + KEY_EXCL_BRAND_ALT_NAME + " TEXT,"
//                + KEY_EXCL_BRAND_CRITERIA + "TEXT" + ")";
//        db.execSQL(CREATE_EXCLUDED_BRAND_TABLE);
//
//        String CREATE_RESTRICTED_GEN_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_GENERIC + " ("
//                + KEY_REST_GENERIC_NAME + " TEXT PRIMARY KEY,"
//                + KEY_REST_GENERIC_ALT_NAME + " TEXT,"
//                + KEY_REST_GENERIC_CRITERIA + "TEXT" + ")";
//        db.execSQL(CREATE_RESTRICTED_GEN_TABLE);
//
//        String CREATE_RESTRICTED_BRAND_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_BRAND + " ("
//                + KEY_REST_BRAND_NAME + " TEXT PRIMARY KEY,"
//                + KEY_REST_BRAND_ALT_NAME + " TEXT,"
//                + KEY_REST_BRAND_CRITERIA + "TEXT" + ")";
//        db.execSQL(CREATE_RESTRICTED_BRAND_TABLE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        //drop old table if exist
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_ENTRY);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULARY_GENERIC);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULARY_BRAND);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCLUDED_GENERIC);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCLUDED_BRAND);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTRICTED_GENERIC);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTRICTED_BRAND);
//
//        //create new table
//        this.onCreate(db);
//    }
//
//    //add single drug row
//    public void addDrug(SqlDrug drug){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        //put row values in
//        ContentValues values = new ContentValues();
//        values.put(KEY_PRIMARY_NAME, drug.getPrimaryName());
//        values.put(KEY_NAME_TYPE, drug.getNameType());
//        values.put(KEY_STATUS, drug.getStatus());
//        values.put(KEY_CLASS, drug.getDrugClass());
//
//        //write to table
//        db.insert(TABLE_DRUG, null, values);
//        db.close();
//    }
//
//    public void addDrug(DrugBase drug) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put();
//    }
//
//    /**
//     * Inserts a single SqlFormulary drug into the table
//     * This method does NOT add the drug into the drug table and will
//     * need to be manually added
//     *
//     * Primary Key "uid" is auto-generated and does not need to be included into values
//     * @param formulary
//     */
//    public void addFormulary(SqlFormulary formulary){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        //put row values in
//        ContentValues values = new ContentValues();
//        values.put(KEY_GENERIC_NAME, formulary.getGenericName());
//        values.put(KEY_BRAND_NAME, formulary.getBrandName());
//        values.put(KEY_STRENGTH, formulary.getStrength());
//
//        //write to table
//        db.insert(TABLE_FORMULARY, null, values);
//        db.close();
//    }
//
//    //read single drug row
//    public SqlDrug getDrug(String name){
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_DRUG, new String[]{KEY_PRIMARY_NAME, KEY_NAME_TYPE, KEY_STATUS, KEY_CLASS},
//                KEY_PRIMARY_NAME + "=?", new String[] { KEY_PRIMARY_NAME}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        SqlDrug drug = new SqlDrug(cursor.getString(0), cursor.getString(1),
//                                    cursor.getString(2), cursor.getString(3));
//        return drug;
//    }
//
//    /**
//     * From the formulary table, find all drugs with a certain generic or brand name
//     * @param name - Name of the drug to find
//     * @param isGenericName - true if the name is generic, false if it is a brand
//     * @return
//     */
//    public List<SqlFormulary> getFormularyDrugs(String name, boolean isGenericName){
//        SQLiteDatabase db = this.getReadableDatabase();
//        List<SqlFormulary> forumaryList = new ArrayList<SqlFormulary>();
//
//        //Build query based on name
//        String key_column = isGenericName ? KEY_GENERIC_NAME : KEY_BRAND_NAME;
//        String query = "SELECT * FROM " + TABLE_FORMULARY + " WHERE " + key_column +" = " + name.trim();
//        Cursor c = db.rawQuery(query, null);
//
//        //Add all query results to list
//        if(c.moveToFirst()){
//            do{
//                SqlFormulary formularyDrug = new SqlFormulary(c.getString(1), c.getString(2), c.getString(3));
//                forumaryList.add(formularyDrug);
//            } while (c.moveToNext());
//        }
//
//        return forumaryList;
//    }
//
//    //get all drugs
//    public List<SqlDrug> getAllDrugs(){
//        List<SqlDrug> drugList = new ArrayList<SqlDrug>();
//
//        String selectAllQuery = "SELECT * FROM " + TABLE_DRUG;
//
//        SQLiteDatabase db = getReadableDatabase(); //TODO example says make writable...we'll see
//        Cursor cursor = db.rawQuery(selectAllQuery, null);
//
//        if(cursor.moveToFirst()){
//            do{
//                SqlDrug drug = new SqlDrug(cursor.getString(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3));
//                drugList.add(drug);
//            } while (cursor.moveToNext());
//        }
//
//        return drugList;
//    }
//
//    //get drug count
//    public int getDrugCount(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selectAllQuery = "SELECT * FROM " + TABLE_DRUG;
//        Cursor cursor = db.rawQuery(selectAllQuery, null);
//        cursor.close();
//
//        return cursor.getCount();
//    }
//
//    public int updateDrug(SqlDrug drug){
//        SQLiteDatabase db = getWritableDatabase();
//
//        //put row values in
//        ContentValues values = new ContentValues();
//        values.put(KEY_PRIMARY_NAME, drug.getPrimaryName());
//        values.put(KEY_NAME_TYPE, drug.getNameType());
//        values.put(KEY_STATUS, drug.getStatus());
//        values.put(KEY_CLASS, drug.getDrugClass());
//
//        return db.update(TABLE_DRUG, values, KEY_PRIMARY_NAME + " =?",
//                new String[]{drug.getPrimaryName()});
//    }
//
////    public int updateFormulary(SqlFormulary formulary){
////        SQLiteDatabase db = getWritableDatabase();
////
////        //put row values in
////        ContentValues values = new ContentValues();
////        values.put(KEY_GENERIC_NAME, formulary.getGenericName());
////        values.put(KEY_BRAND_NAME, formulary.getBrandName());
////        values.put(KEY_STRENGTH, formulary.getStrength());
////
////        return db.update(TABLE_FORMULARY, values, )
////    }
//
//    public void deleteDrug(SqlDrug drug){
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_DRUG, KEY_PRIMARY_NAME + " = ?", new String[]{KEY_PRIMARY_NAME});
//        db.close();
//    }
//}
