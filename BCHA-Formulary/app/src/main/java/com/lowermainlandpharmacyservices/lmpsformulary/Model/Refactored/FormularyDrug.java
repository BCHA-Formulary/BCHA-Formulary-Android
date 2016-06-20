package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class FormularyDrug extends DrugBase{
    public String primaryName;
    public NameType nameType;
    public List<String> alternateName;
    public List<String> strengths;

    public FormularyDrug() {}

    //Constructor - altName list and strength list are provided
    public FormularyDrug(String primaryName, NameType nameType,
                         List<String> alternateName, List<String> strengths, Status status, String drugClass){
        super(drugClass, status);

        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = alternateName;
        this.strengths = strengths;
    }

    //Constructor - altName list and one strength are provided
    public FormularyDrug(String primaryName, NameType nameType,
                         List<String> alternateName, String strengths, Status status, String drugClass){
        super(drugClass, status);

        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = alternateName;
        this.strengths = new ArrayList<String>();
        this.strengths.add(strengths);
    }

    //Constructor - only one alt name and one strength are provided
    public FormularyDrug(String primaryName, NameType nameType,
                         String alternateName, String strengths, Status status, String drugClass){
        super(drugClass, status);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.alternateName.add(alternateName);
        this.strengths = new ArrayList<String>();
        this.strengths.add(strengths);
    }

    //Constructor - no alt name and no strength provided
    public FormularyDrug(String primaryName, NameType nameType,
                         Status status, String drugClass){
        super(drugClass, status);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.strengths = new ArrayList<String>();
    }

    //Constructor - Firebase friendly
    public FormularyDrug(String primaryName, String nameType,
                         String status, String drugClass){
        super(drugClass, Status.valueOf(status));
        this.primaryName = primaryName;
        this.nameType = NameType.valueOf(nameType);
        this.alternateName = new ArrayList<String>();
        this.strengths = new ArrayList<String>();
    }

    /**getters and setters
     * Firebase has issues using enum types (no longer supports jackson), convert to string for now
     * http://stackoverflow.com/questions/37335712/android-firebase-9-0-0-setvalue-to-serialize-enums/37357484#37357484
     */
    public String getPrimaryName() {
        return primaryName;
    }

    public void setPrimaryName(String primaryName) {
        this.primaryName = primaryName;
    }

    @Exclude //exclude from firebase
    public NameType getNameTypeVal() {
        return nameType;
    }

    public String getNameType(){ return nameType.name();}

    @Exclude
    public void setNameTypeVal(NameType nameType) {
        this.nameType = nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = NameType.valueOf(nameType);
    }

    public List<String> getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(List<String> alternateName) {
        this.alternateName = alternateName;
    }

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
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
