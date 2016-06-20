package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.util.Log;

import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.Status;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by Kelvin on 2016-06-12.
 */
public class RefactoredParser {
    HashMap<String, FormularyDrug> formularyList;
    HashMap<String, ExcludedDrug> excludedList;
    HashMap<String, RestrictedDrug> restrictedList;

    public RefactoredParser() {
        formularyList = new HashMap<String, FormularyDrug>();
        excludedList = new HashMap<String, ExcludedDrug>();
        restrictedList = new HashMap<String, RestrictedDrug>();
    }

    public void parseFormulary(InputStream csvFile) {
        // public void parseFormulary(BufferedReader dataFile){
        BufferedReader dataFile = new BufferedReader(new InputStreamReader(
                csvFile));
        CSVReader reader = null;
        try {
            reader = new CSVReader(dataFile);
            String[] nextLine;
            reader.readNext(); // title line
            while ((nextLine = reader.readNext()) != null) {
                String name = nextLine[0].toUpperCase().trim();
                String strength = nextLine[1].toUpperCase().trim();
                String altName = nextLine[2].toUpperCase().trim();
                String drugClass = nextLine[3].toUpperCase().trim();

                // genericName-------------------------------------------------------------------------------
                if (!(name.equals(""))) { // handles all the empty lines
                    if (formularyList.containsKey(name)) { //check if drug already exists
                        FormularyDrug drug = formularyList.get(name);

                        //add strength
                        drug.getStrengths().add(strength);

                        //checks alt names. if new, add to the list
                        String[] altNameList = getSeparateAltNames(altName);
                        if(altNameList != null) {
                            for (String altNameEntry : altNameList) {
                                if (!altName.equals("") && !drug.getAlternateName().contains(altNameEntry)) {
                                    drug.getAlternateName().add(altNameEntry);
                                }
                            }
                        }

                        //parse drug class (if multiple) and check if classes are in list, add if not
                        String[] drugClasses = getSeparateDrugClasses(drugClass);  //TODO declare local var
                        if(drugClasses != null) {
                            for (String drugClassName : drugClasses) {
                                if (!drug.getDrugClass().contains(drugClassName)) {
                                    drug.getDrugClass().add(drugClassName);
                                }
                            }
                        }
                    } else { //generic drug is new
                        String[] drugClasses = getSeparateDrugClasses(drugClass); //TODO null check
                        for (String drugClassName : drugClasses) {
                            formularyList.put(name, new FormularyDrug(name, NameType.GENERIC,
                                    altName, strength, Status.FORMULARY, drugClassName)); //TODO altName should be a list
                            Log.d("Formulary parse", altName);
                        }
                    }
                }

                // brandList------------------------------------------------------------------------------
                if (!altName.equals("")) {
                    String[] altNameList = getSeparateAltNames(altName);
                    for (String altNameEntry : altNameList) {
                        if (formularyList.containsKey(altNameEntry)) { //check if drug already exists
                            FormularyDrug drug = formularyList.get(altNameEntry);

                            //add strength
                            drug.getStrengths().add(strength);

                            //brand name drugs only have 1 generic name so no check is needed

                            //parse drug names (if multiple) and check if names are in list, add if not
                            String[] drugClasses = getSeparateDrugClasses(drugClass); //TODO declare local var
                            if(drugClasses != null) {
                                for (String drugClassName : drugClasses) {
                                    if (!drug.getDrugClass().contains(drugClassName)) {
                                        drug.getDrugClass().add(drugClassName);
                                    }
                                }
                            }
                        } else { //brand drug is new
                            String[] drugClasses = getSeparateDrugClasses(drugClass); //TODO null check
                            for (String drugClassName : drugClasses) {
                                formularyList.put(altName, new FormularyDrug(altNameEntry, NameType.BRAND,
                                        name, strength, Status.FORMULARY, drugClassName));
                                Log.d("Formulary parse", altName);
                            }
                        }
                    }
                }
            }
            dataFile.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            dataFile = null;
        }
    }

    private String[] getSeparateAltNames(String altNames) {
        String[] altNameList = new String[1];
        if (altNames != null) {
            altNameList = altNames.split(",");
            for (String name : altNameList) {
                name.toUpperCase().trim();
            }
        }
        return altNameList;
    }

    /**
     * Splits drug class input by "," and returns the array with formatting
     * Some edge cases such as "BENZODIAZEPINES (ANXIOLYTICS, SEDATIVES)" were reasoned to be
     * treated as a single class so were excluded from the split
     * @param drugClasses - the raw input from the csv line
     * @return - String[] of the classes
     */
    private String[] getSeparateDrugClasses(String drugClasses) {
        String[] drugClassesList = new String[1];
        if(drugClasses != null){
            if (!drugClasses.contains("(")){
                drugClassesList = drugClasses.split(",");
                for (String name: drugClassesList){
                    name.toUpperCase().trim();
                }
            }
            else
                drugClassesList[0] = drugClasses.toUpperCase().trim();
        }
        return drugClassesList;
    }

    public HashMap<String, FormularyDrug> getFormularyList() {
        return formularyList;
    }

    public HashMap<String, ExcludedDrug> getExcludedList() {
        return excludedList;
    }

    public HashMap<String, RestrictedDrug> getRestrictedList() {
        return restrictedList;
    }
}