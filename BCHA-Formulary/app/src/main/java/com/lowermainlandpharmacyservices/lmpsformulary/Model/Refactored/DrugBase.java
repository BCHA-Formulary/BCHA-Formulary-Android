package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class DrugBase {
    public List<String> drugClass;
    public Status status;

    public DrugBase(){}
    public DrugBase(String drugClass, Status status){
        this.drugClass = new ArrayList<String>();
        this.drugClass.add(drugClass);
        this.status = status;
    }

    public DrugBase(List<String> drugClass, Status status){
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
