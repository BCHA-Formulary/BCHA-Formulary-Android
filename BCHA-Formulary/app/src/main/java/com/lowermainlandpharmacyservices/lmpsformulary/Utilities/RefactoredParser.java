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
import java.util.ArrayList;
import java.util.Arrays;
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
                        String[] altNameList = getSeparateAltNames(altName);
                        for (String drugClassName : drugClasses) {
                            if(altNameList != null){
                                formularyList.put(name, new FormularyDrug(name, NameType.GENERIC,
                                        new ArrayList<String>(Arrays.asList(altNameList)), strength, Status.FORMULARY, drugClassName));
                            }
                            else {
                                formularyList.put(name, new FormularyDrug(name, NameType.GENERIC,
                                        altName, strength, Status.FORMULARY, drugClassName)); //TODO altName should be a list
                            }
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

    /**
     * Parse excluded will only be used for the generic, brand, class and first line of reason
     * the rest will be manually added in for simplicity
     * @param csvFile
     */
    public void parseExcluded(InputStream csvFile){
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
                String altName = nextLine[1].toUpperCase().trim();
                String criteria = nextLine[2].toUpperCase().trim();
                String drugClass = nextLine[3].toUpperCase().trim();

                //generic list--------------
                if (!(name.equals(""))) { // handles all the empty lines
                    if (excludedList.containsKey(name)) { //check if drug already exists
                        ExcludedDrug drug = excludedList.get(name);

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
                        String[] altNameList = getSeparateAltNames(altName);
                        for (String drugClassName : drugClasses) {
                            if(altNameList != null) {
                                excludedList.put(name, new ExcludedDrug(name, NameType.GENERIC,
                                        new ArrayList<String>(Arrays.asList(altNameList)), criteria,
                                        Status.EXCLUDED, drugClassName));
                            }
                            else {
                                excludedList.put(name, new ExcludedDrug(name, NameType.GENERIC,
                                        altName, criteria,
                                        Status.EXCLUDED, drugClassName));
                            }
                            Log.d("Excluded parse", altName);
                        }
                    }
                }
                //Brand list--------------
                if (!altName.equals("")) {
                    String[] altNameList = getSeparateAltNames(altName);
                    for (String altNameEntry : altNameList) {
                        if (excludedList.containsKey(altNameEntry)) { //check if drug already exists
                            ExcludedDrug drug = excludedList.get(altNameEntry);

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
                                excludedList.put(altName, new ExcludedDrug(altNameEntry, NameType.BRAND,
                                        name, criteria, Status.EXCLUDED, drugClassName));
                                Log.d("Excluded parse", altName);
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

    /**
     * Parse excluded will only be used for the generic, brand, class and first line of reason
     * the rest will be manually added in for simplicity
     * @param csvFile
     */
    public void parseRestricted(InputStream csvFile){
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
                String altName = nextLine[1].toUpperCase().trim();
                String criteria = nextLine[2].toUpperCase().trim();
                String drugClass = nextLine[3].toUpperCase().trim();

                //generic list--------------
                if (!(name.equals(""))) { // handles all the empty lines
                    if (restrictedList.containsKey(name)) { //check if drug already exists
                        RestrictedDrug drug = restrictedList.get(name);

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
                        String[] altNameList = getSeparateAltNames(altName);
                        for (String drugClassName : drugClasses) {
                            if(altNameList != null) {
                                restrictedList.put(name, new RestrictedDrug(name, NameType.GENERIC,
                                        new ArrayList<String>(Arrays.asList(altNameList)), criteria,
                                        Status.RESTRICTED, drugClassName));
                            }
                            else {
                                restrictedList.put(name, new RestrictedDrug(name, NameType.GENERIC,
                                        altName, criteria,
                                        Status.RESTRICTED, drugClassName));
                            }
                            Log.d("Restricted parse", altName);
                        }
                    }
                }
                //Brand list--------------
                if (!altName.equals("")) {
                    String[] altNameList = getSeparateAltNames(altName);
                    for (String altNameEntry : altNameList) {
                        if (restrictedList.containsKey(altNameEntry)) { //check if drug already exists
                            RestrictedDrug drug = restrictedList.get(altNameEntry);

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
                                restrictedList.put(altName, new RestrictedDrug(altNameEntry, NameType.BRAND,
                                        name, criteria, Status.RESTRICTED, drugClassName));
                                Log.d("Restricted parse", altName);
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