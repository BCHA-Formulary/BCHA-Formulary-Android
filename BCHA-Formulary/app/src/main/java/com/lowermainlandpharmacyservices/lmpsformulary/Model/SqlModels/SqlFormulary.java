package com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels;

/**
 * Created by kelvinchan on 2016-07-10.
 *
 * Column 1(Primary Key): "uid" - int, autoincrement
 * Column 2: "generic_name" - Text
 * Column 3: "brand_name" - Text
 * Column 4: "strength" - Text
 */
public class SqlFormulary {
    //Primary Key: KEY_UID - INTEGER PRIMARY KEY AUTOINCREMENT
    String genericName;
    String brandName;
    String strength;

    public SqlFormulary(){}

    public SqlFormulary(String genericName, String brandName, String strength){
        this.genericName = genericName;
        this.brandName = brandName;
        this.strength = strength;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }
}
