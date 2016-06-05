package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class RestrictedDrug extends DrugBase {
    public String primaryName;
    public NameType nameType;
    public List<String> alternateName;
    public String criteria;

    //Constructor - altName list
    public RestrictedDrug(String primaryName, NameType nameType,
                        List<String> alternateName, String criteria, String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = alternateName;
        this.criteria = criteria;
    }

    //Constructor - only one alt name provided
    public RestrictedDrug(String primaryName, NameType nameType,
                        String alternateName, String criteria, String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
        this.primaryName = primaryName;
        this.nameType = nameType;
        this.alternateName = new ArrayList<String>();
        this.alternateName.add(alternateName);
        this.criteria = criteria;
    }

    //Constructor - no alt name and no strength provided
    public RestrictedDrug(String primaryName, NameType nameType, String criteria,
                        String status, DrugType drugType, DrugClass drugClass){
        super(status, drugClass, drugType);
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
}
