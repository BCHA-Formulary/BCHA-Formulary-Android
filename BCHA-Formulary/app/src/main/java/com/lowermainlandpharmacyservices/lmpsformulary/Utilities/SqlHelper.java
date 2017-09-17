package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugBase;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * Created by kelvinchan on 2016-06-28.
 */
public class SqlHelper extends SQLiteOpenHelper{

    //DB version
    private static final int DATABASE_VERSION = 1;
    //DB name
    private static final String DATABASE_NAME = "drugsManager";
    //Table names
    private static final String TABLE_DRUG_ENTRY = "drugEntryTable";
    private static final String TABLE_FORMULARY_GENERIC = "formularyGenericTable";
    private static final String TABLE_FORMULARY_BRAND = "formularyBrandTable";
    private static final String TABLE_EXCLUDED_GENERIC = "excludedGenericTable";
    private static final String TABLE_EXCLUDED_BRAND = "excludedBrandTable";
    private static final String TABLE_RESTRICTED_GENERIC = "restrictedGenericTable";
    private static final String TABLE_RESTRICTED_BRAND = "restrictedBrandTable";

    //Drug table columns
    private static final String KEY_ENTRY_PRIMARY_NAME = "primary_id";
    private static final String KEY_ENTRY_DRUG_NAME = "drugName";
    private static final String KEY_ENTRY_NAME_TYPE = "name_type";
    private static final String KEY_ENTRY_STATUS = "status";
    private static final String KEY_ENTRY_CLASS = "class";

    //Formulary Generic Table columns
    private static final String KEY_FORM_GEN_NAME = "form_generic_name";
    private static final String KEY_FORM_GEN_ALT_NAME = "form_generic_alt_name";
    private static final String KEY_FORM_GEN_STRENGTH = "form_generic_strength";

    //Formulary Brand Table columns
    private static final String KEY_FORM_BRAND_NAME = "form_brand_name";
    private static final String KEY_FORM_BRAND_ALT_NAME = "form_brand_alt_name";
    private static final String KEY_FORM_BRAND_STRENGTH = "form_brand_strength";

    //Excluded Generic Table Columns
    private static final String KEY_EXCL_GENERIC_NAME = "excluded_generic_name";
    private static final String KEY_EXCL_GENERIC_ALT_NAME = "excluded_generic_alt_name";
    private static final String KEY_EXCL_GENERIC_CRITERIA = "excluded_generic_criteria";

    //Excluded Brand Table Columns
    private static final String KEY_EXCL_BRAND_NAME = "excluded_brand_name";
    private static final String KEY_EXCL_BRAND_ALT_NAME = "excluded_brand_alt_name";
    private static final String KEY_EXCL_BRAND_CRITERIA = "excluded_brand_criteria";

    //Excluded Generic Table Columns
    private static final String KEY_REST_GENERIC_NAME = "restricted_generic_name";
    private static final String KEY_REST_GENERIC_ALT_NAME = "restricted_generic_alt_name";
    private static final String KEY_REST_GENERIC_CRITERIA = "restricted_generic_criteria";

    //Excluded Brand Table Columns
    private static final String KEY_REST_BRAND_NAME = "restricted_brand_name";
    private static final String KEY_REST_BRAND_ALT_NAME = "restricted_brand_alt_name";
    private static final String KEY_REST_BRAND_CRITERIA = "restricted_brand_criteria";

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
        String CREATE_DRUGS_TABLE = "CREATE TABLE " + TABLE_DRUG_ENTRY + " ("
                + KEY_ENTRY_PRIMARY_NAME + " INTEGER  PRIMARY KEY AUTOINCREMENT,"
                + KEY_ENTRY_DRUG_NAME + " TEXT,"
                + KEY_ENTRY_NAME_TYPE + " TEXT,"
                + KEY_ENTRY_STATUS + " TEXT,"
                + KEY_ENTRY_CLASS + " TEXT" + ");";
        db.execSQL(CREATE_DRUGS_TABLE);

        String CREATE_FORUMARLY_GEN_TABLE = "CREATE TABLE " + TABLE_FORMULARY_GENERIC + " ("
                + KEY_FORM_GEN_NAME + " TEXT PRIMARY KEY,"
                + KEY_FORM_GEN_ALT_NAME + " TEXT,"
                + KEY_FORM_GEN_STRENGTH + " TEXT" + ");";
        db.execSQL(CREATE_FORUMARLY_GEN_TABLE);

        String CREATE_FORUMARLY_BRAND_TABLE = "CREATE TABLE " + TABLE_FORMULARY_BRAND + " ("
                + KEY_FORM_BRAND_NAME + " TEXT PRIMARY KEY,"
                + KEY_FORM_BRAND_ALT_NAME + " TEXT,"
                + KEY_FORM_BRAND_STRENGTH + " TEXT" + ");";
        db.execSQL(CREATE_FORUMARLY_BRAND_TABLE);

        String CREATE_EXCLUDED_GEN_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_GENERIC + " ("
                + KEY_EXCL_GENERIC_NAME + " TEXT PRIMARY KEY,"
                + KEY_EXCL_GENERIC_ALT_NAME + " TEXT,"
                + KEY_EXCL_GENERIC_CRITERIA + " TEXT" + ");";
        db.execSQL(CREATE_EXCLUDED_GEN_TABLE);

        String CREATE_EXCLUDED_BRAND_TABLE = "CREATE TABLE " + TABLE_EXCLUDED_BRAND + " ("
                + KEY_EXCL_BRAND_NAME + " TEXT PRIMARY KEY,"
                + KEY_EXCL_BRAND_ALT_NAME + " TEXT,"
                + KEY_EXCL_BRAND_CRITERIA + " TEXT" + ");";
        db.execSQL(CREATE_EXCLUDED_BRAND_TABLE);

        String CREATE_RESTRICTED_GEN_TABLE = "CREATE TABLE " + TABLE_RESTRICTED_GENERIC + " ("
                + KEY_REST_GENERIC_NAME + " TEXT PRIMARY KEY,"
                + KEY_REST_GENERIC_ALT_NAME + " TEXT,"
                + KEY_REST_GENERIC_CRITERIA + " TEXT" + ");";
        db.execSQL(CREATE_RESTRICTED_GEN_TABLE);

        String CREATE_RESTRICTED_BRAND_TABLE = "CREATE TABLE " + TABLE_RESTRICTED_BRAND + " ("
                + KEY_REST_BRAND_NAME + " TEXT PRIMARY KEY,"
                + KEY_REST_BRAND_ALT_NAME + " TEXT,"
                + KEY_REST_BRAND_CRITERIA + " TEXT" + ");";
        db.execSQL(CREATE_RESTRICTED_BRAND_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRUG_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULARY_GENERIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORMULARY_BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCLUDED_GENERIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXCLUDED_BRAND);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTRICTED_GENERIC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESTRICTED_BRAND);

        //create new table
        this.onCreate(db);
    }

    public void clearAllTables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRUG_ENTRY, null, null);
        db.delete(TABLE_FORMULARY_GENERIC, null, null);
        db.delete(TABLE_FORMULARY_BRAND, null, null);
        db.delete(TABLE_EXCLUDED_GENERIC, null, null);
        db.delete(TABLE_EXCLUDED_BRAND, null, null);
        db.delete(TABLE_RESTRICTED_GENERIC, null, null);
        db.delete(TABLE_RESTRICTED_BRAND, null, null);
    }

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

    public void addAllDrug(List<DrugBase> allDrugs) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(DrugBase drug: allDrugs) {
            for (String drugClass: drug.drugClass) {
                ContentValues values = new ContentValues();
                values.put(KEY_ENTRY_DRUG_NAME, drug.primaryName);
                values.put(KEY_ENTRY_NAME_TYPE, drug.nameType.name());
                values.put(KEY_ENTRY_STATUS, drug.status.name());
                values.put(KEY_ENTRY_CLASS, drugClass);

                db.insert(TABLE_DRUG_ENTRY, null, values);
            }

            if (drug instanceof FormularyDrug) {
                addFormularyDrug((FormularyDrug) drug, db);
            } else if (drug instanceof ExcludedDrug) {
                addExcludedDrug((ExcludedDrug) drug, db);
            } else {
                addRestrictedDrug((RestrictedDrug) drug, db);
            }
        }
        db.close();
    }

    private void addFormularyDrug(FormularyDrug formularyDrug, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        if (formularyDrug.nameType == NameType.GENERIC) {
            values.put(KEY_FORM_GEN_NAME, formularyDrug.primaryName);
            values.put(KEY_FORM_GEN_ALT_NAME, jsonifyList(formularyDrug.alternateNames));
            values.put(KEY_FORM_GEN_STRENGTH, jsonifyList(formularyDrug.strengths));
            db.insert(TABLE_FORMULARY_GENERIC, null, values);
        } else {
            values.put(KEY_FORM_BRAND_NAME, formularyDrug.primaryName);
            values.put(KEY_FORM_BRAND_ALT_NAME, jsonifyList(formularyDrug.alternateNames));
            values.put(KEY_FORM_BRAND_STRENGTH, jsonifyList(formularyDrug.strengths));
            long resp = db.insert(TABLE_FORMULARY_BRAND, null, values);
            System.out.println(resp);
        }
    }

    private void addExcludedDrug (ExcludedDrug excludedDrug, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        if (excludedDrug.nameType == NameType.GENERIC) {
            values.put(KEY_EXCL_GENERIC_NAME, excludedDrug.primaryName);
            values.put(KEY_EXCL_GENERIC_ALT_NAME, jsonifyList(excludedDrug.alternateNames));
            values.put(KEY_EXCL_GENERIC_CRITERIA, excludedDrug.criteria);
            db.insert(TABLE_EXCLUDED_GENERIC, null, values);
        } else {
            values.put(KEY_EXCL_BRAND_NAME, excludedDrug.primaryName);
            values.put(KEY_EXCL_BRAND_ALT_NAME, jsonifyList(excludedDrug.alternateNames));
            values.put(KEY_EXCL_BRAND_CRITERIA, excludedDrug.criteria);
            db.insert(TABLE_EXCLUDED_BRAND, null, values);
        }
    }

    private void addRestrictedDrug (RestrictedDrug restrictedDrug, SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        if (restrictedDrug.nameType == NameType.GENERIC) {
            values.put(KEY_REST_GENERIC_NAME, restrictedDrug.primaryName);
            values.put(KEY_REST_GENERIC_ALT_NAME, jsonifyList(restrictedDrug.alternateNames));
            values.put(KEY_REST_GENERIC_CRITERIA, restrictedDrug.criteria);
            db.insert(TABLE_RESTRICTED_GENERIC, null, values);
        } else {
            values.put(KEY_REST_BRAND_NAME, restrictedDrug.primaryName);
            values.put(KEY_REST_BRAND_ALT_NAME, jsonifyList(restrictedDrug.alternateNames));
            values.put(KEY_REST_BRAND_CRITERIA, restrictedDrug.criteria);
            db.insert(TABLE_RESTRICTED_BRAND, null, values);
        }
    }

    private String jsonifyList(List<String> list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }
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
    public List<String> getAllDrugNames() {
        Set<String> drugNamesSet = new HashSet<>();

        String selectAllQuery = "SELECT * FROM " + TABLE_DRUG_ENTRY;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectAllQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String primaryName = cursor.getString(1);
                drugNamesSet.add(primaryName);
            } while (cursor.moveToNext());
        }
        db.close();
        return new ArrayList<>(drugNamesSet);
    }

    public DrugBase queryDrug(String name){
//        String drugQuery = "SELECT * FROM " + TABLE_DRUG_ENTRY + " WHERE " + KEY_ENTRY_PRIMARY_NAME + " = " + name.trim().toUpperCase();
        String drugQuery = "SELECT * FROM " + TABLE_DRUG_ENTRY + " WHERE " + KEY_ENTRY_DRUG_NAME + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(drugQuery, new String[]{name.trim().toUpperCase()});

            if (cursor.moveToFirst()) {
                Gson gson = new Gson();
                String primaryName = cursor.getString(1);
                NameType nameType = NameType.valueOf(cursor.getString(2));
                List<String> altNames = new ArrayList<>();
                String a = cursor.getString(3);
                String b = cursor.getString(4);
                Status status = Status.valueOf(cursor.getString(3));

                List<String> drugClasses = new ArrayList<>();
                do {
                    String drugClass = cursor.getString(4);
                    drugClasses.add(drugClass);
                } while (cursor.moveToNext());
//                Type type = new TypeToken<ArrayList<String>>() {
//                }.getType();
//                List<String> drugClasses = gson.fromJson(cursor.getString(4), type);

                DrugBase drugBase = new DrugBase(primaryName, nameType, altNames, drugClasses, status);

                if (drugBase.status == Status.FORMULARY) {
                    return getFormularyDrug(drugBase);
                } else if (drugBase.status == Status.EXCLUDED) {
                    return getExcludedDrug(drugBase);
                } else {
                    return getRestrictedDrug(drugBase);
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return null;
    }

    public List<String> getDrugNamesFromClass(String drugClass) {
        String drugQuery = "SELECT " + KEY_ENTRY_DRUG_NAME + " FROM " + TABLE_DRUG_ENTRY + " WHERE " + KEY_ENTRY_CLASS + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        List<String> drugNameList = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(drugQuery, new String[]{drugClass.trim().toUpperCase()});
            if (cursor.moveToFirst()) {
//                Gson gson = new Gson();
//                Type type = new TypeToken<String>(){}.getType();
                do {
                    String drugName = cursor.getString(0);
                    drugNameList.add(drugName);
                } while (cursor.moveToNext());
//                drugBase.alternateNames = gson.fromJson(cursor.getString(1), type);
//                List<String> strengths = gson.fromJson(cursor.getString(2), type);
//                return new FormularyDrug(strengths, drugBase);
            }
        }  catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return drugNameList;
    }

    private FormularyDrug getFormularyDrug(DrugBase drugBase) {
        String drugTable = drugBase.nameType == NameType.GENERIC ? TABLE_FORMULARY_GENERIC : TABLE_FORMULARY_BRAND;
        String queryName = drugBase.nameType == NameType.GENERIC ? KEY_FORM_GEN_NAME : KEY_FORM_BRAND_NAME;
        String drugQuery = "SELECT * FROM " + drugTable + " WHERE " + queryName + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(drugQuery, new String[]{drugBase.primaryName.trim().toUpperCase()});
            if (cursor.moveToFirst()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>(){}.getType();
                drugBase.alternateNames = gson.fromJson(cursor.getString(1), type);
                List<String> strengths = gson.fromJson(cursor.getString(2), type);
                return new FormularyDrug(strengths, drugBase);
            }
        } catch (Exception e) {
                Log.e(TAG, "Could not make formulary drug: " + drugBase.primaryName + ". Reason: " + e.getMessage());
        }
        return null;
    }

    private ExcludedDrug getExcludedDrug(DrugBase drugBase) {
        String drugTable = drugBase.nameType == NameType.GENERIC ? TABLE_EXCLUDED_GENERIC : TABLE_EXCLUDED_BRAND;
        String queryName = drugBase.nameType == NameType.GENERIC ? KEY_EXCL_GENERIC_NAME : KEY_EXCL_BRAND_NAME;
        String drugQuery = "SELECT * FROM " + drugTable + " WHERE " + queryName + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(drugQuery, new String[]{drugBase.primaryName.trim().toUpperCase()});
            if (cursor.moveToFirst()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {}.getType();
                drugBase.alternateNames = gson.fromJson(cursor.getString(1), type);
                String criteria = cursor.getString(2);
                return new ExcludedDrug(criteria, drugBase);
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not make excluded drug: " + drugBase.primaryName + ". Reason: " + e.getMessage());
        }
        return null;
    }

    private RestrictedDrug getRestrictedDrug(DrugBase drugBase) {
        String drugTable = drugBase.nameType == NameType.GENERIC ? TABLE_RESTRICTED_GENERIC : TABLE_RESTRICTED_BRAND;
        String queryName = drugBase.nameType == NameType.GENERIC ? KEY_REST_GENERIC_NAME : KEY_REST_BRAND_NAME;
        String drugQuery = "SELECT * FROM " + drugTable + " WHERE " + queryName + " = ?";
        SQLiteDatabase db = getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery(drugQuery, new String[]{drugBase.primaryName.trim().toUpperCase()});
            if (cursor.moveToFirst()) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>() {}.getType();
                drugBase.alternateNames = gson.fromJson(cursor.getString(1), type);
                String criteria = cursor.getString(2);
                return new RestrictedDrug(criteria, drugBase);
            }
        } catch (Exception e) {
            Log.e(TAG, "Could not make excluded drug: " + drugBase.primaryName + ". Reason: " + e.getMessage());
        }
        return null;
    }



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
//    public void deleteDrug(SqlDrug drug){
//        SQLiteDatabase db = getWritableDatabase();
//        db.delete(TABLE_DRUG, KEY_PRIMARY_NAME + " = ?", new String[]{KEY_PRIMARY_NAME});
//        db.close();
//    }
}
