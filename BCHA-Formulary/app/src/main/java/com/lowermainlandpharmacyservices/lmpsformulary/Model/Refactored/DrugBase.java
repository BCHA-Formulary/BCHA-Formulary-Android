package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class DrugBase {
    public List<String> drugClass;
    public DrugType drugType;

    public DrugBase(String drugClass, DrugType drugType){
        this.drugClass = new ArrayList<String>();
        this.drugClass.add(drugClass);
        this.drugType = drugType;
    }

    public DrugBase(List<String> drugClass, DrugType drugType){
        this.drugClass = drugClass;
        this.drugType = drugType;
    }

    //getters and setters
    public List<String> getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(List<String> drugClass) {
        this.drugClass = drugClass;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }
}
