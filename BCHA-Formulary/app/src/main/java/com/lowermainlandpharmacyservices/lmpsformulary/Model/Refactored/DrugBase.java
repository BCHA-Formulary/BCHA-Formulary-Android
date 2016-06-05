package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class DrugBase {
    public String status;
    public DrugClass drugClass;
    public DrugType drugType;

    public DrugBase(String status, DrugClass drugClass, DrugType drugType){
        this.status = status;
        this.drugClass = drugClass;
        this.drugType = drugType;
    }

    //getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DrugClass getDrugClass() {
        return drugClass;
    }

    public void setDrugClass(DrugClass drugClass) {
        this.drugClass = drugClass;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }
}
