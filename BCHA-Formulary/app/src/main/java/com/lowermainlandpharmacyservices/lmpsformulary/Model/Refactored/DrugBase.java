package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import com.google.firebase.database.Exclude;

import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class DrugBase {
    public String primaryName;
    public NameType nameType;
    public List<String> alternateNames;
    public List<String> drugClass;
    public Status status;

    public DrugBase(){}

    public DrugBase(DrugBase drug) {
        this.primaryName = drug.primaryName;
        this.nameType = drug.nameType;
        this.alternateNames = drug.alternateNames;
        this.drugClass = drug.drugClass;
        this.status = drug.status;
    }

    public DrugBase(String primaryName, NameType nameType, List<String> alternateNames, List<String> drugClass, Status status) {
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateNames = alternateNames;
        this.drugClass = drugClass;
        this.status = status;
    }

    //getters and setters
    public List<String> getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(List<String> drugClass) {
        this.drugClass = drugClass;
    }

    @Exclude //exclude type for firebase
    public Status getStatusVal() {
        return status;
    }

    // Firebase has issues using enum types, convert to string for now
    // http://stackoverflow.com/questions/37335712/android-firebase-9-0-0-setvalue-to-serialize-enums/37357484#37357484
    public String getStatus() { return status.name();}

    public void setStatus(Status status) {
        this.status = status;
    }
}
