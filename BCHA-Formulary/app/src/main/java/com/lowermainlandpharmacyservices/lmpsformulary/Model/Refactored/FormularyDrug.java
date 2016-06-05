package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

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

    //Constructor - altName list and strength list are provided
    public FormularyDrug(String primaryName, NameType nameType,
                         List<String> alternateName, List<String> strengths, String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = alternateName;
        this.strengths = strengths;
    }

    //Constructor - only one alt name and one strength are provided
    public FormularyDrug(String primaryName, NameType nameType,
                         String alternateName, String strengths, String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.alternateName.add(alternateName);
        this.strengths = new ArrayList<String>();
        this.strengths.add(strengths);
    }

    //Constructor - no alt name and no strength provided
    public FormularyDrug(String primaryName, NameType nameType,
                         String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.strengths = new ArrayList<String>();
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

    public List<String> getStrengths() {
        return strengths;
    }

    public void setStrengths(List<String> strengths) {
        this.strengths = strengths;
    }
}
