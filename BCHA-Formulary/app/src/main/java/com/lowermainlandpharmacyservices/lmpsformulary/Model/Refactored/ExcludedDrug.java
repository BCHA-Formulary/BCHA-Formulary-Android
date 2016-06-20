package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class ExcludedDrug extends DrugBase {
    public String primaryName;
    public NameType nameType;
    public List<String> alternateName;
    public String criteria;

    //Constructor - altName list
    public ExcludedDrug(String primaryName, NameType nameType,
                        List<String> alternateName, String criteria, Status status, String drugClass){
        super(drugClass, status);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = alternateName;
        this.criteria = criteria;
    }

    //Constructor - only one alt name provided
    public ExcludedDrug(String primaryName, NameType nameType,
                        String alternateName, String criteria, Status status, String drugClass){
        super(drugClass, status);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.alternateName.add(alternateName);
        this.criteria = criteria;
    }

    //Constructor - no alt name and no strength provided
    public ExcludedDrug(String primaryName, NameType nameType, String criteria,
                        Status status, String drugClass){
        super(drugClass, status);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.criteria = criteria;
        this.alternateName = new ArrayList<String>();
    }

    //getters and setters
    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    public NameType getNameType() {
        return nameType;
    }

    public void setNameType(NameType nameType) {
        this.nameType = nameType;
    }

    public List<String> getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(List<String> alternateName) {
        this.alternateName = alternateName;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    //getters and setters from base class so firebase can get/set properties
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

    public String getStatus() { return status.name();}

    @Exclude
    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        this.status = Status.valueOf(status);
    }
}
