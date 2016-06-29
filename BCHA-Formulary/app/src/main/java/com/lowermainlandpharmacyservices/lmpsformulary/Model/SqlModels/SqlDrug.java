package com.lowermainlandpharmacyservices.lmpsformulary.Model.SqlModels;

/**
 * Created by kelvinchan on 2016-06-28.
 */
public class SqlDrug {
    String primaryName; //primary key
    String nameType;
    String status;
    String drugClass;

    public SqlDrug(){}

    public SqlDrug(String primaryName, String nameType, String status, String drugClass){
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.status = status;
        this.drugClass = drugClass;
    }

    //getters and setters
    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(String drugClass) {
        this.drugClass = drugClass;
    }

}
