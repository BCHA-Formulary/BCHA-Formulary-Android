package com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored;

import java.util.List;

/**
 * Created by Kelvin on 6/5/2016.
 */
public class FormularyDrug extends DrugBase{
    public List<String> strengths;

    public FormularyDrug() {}

    public FormularyDrug(List<String> strengths, DrugBase drugBase) {
        super(drugBase);
        this.strengths = strengths;
    }

    public FormularyDrug(String primaryName, NameType nameType, List<String> alternateNames, List<String> drugClass, Status status, List<String> strengths) {
        super(primaryName, nameType, alternateNames, drugClass, status);
        this.strengths = strengths;
    }
}
