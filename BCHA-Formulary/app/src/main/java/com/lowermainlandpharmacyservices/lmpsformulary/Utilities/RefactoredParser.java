package com.lowermainlandpharmacyservices.lmpsformulary.Utilities;

import android.util.Log;

import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.DrugType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.ExcludedDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.FormularyDrug;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.NameType;
import com.lowermainlandpharmacyservices.lmpsformulary.Model.Refactored.RestrictedDrug;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
                        for (String drugClassName : drugClasses) {
                            formularyList.put(name, new FormularyDrug(name, NameType.GENERIC,
                                    altName, strength, DrugType.FORMULARY, drugClassName)); //TODO altName should be a list
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
                                        name, strength, DrugType.FORMULARY, drugClassName));
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

    public void parseExcluded(InputStream csvFile) {
        BufferedReader dataFile;
        try {
            dataFile = new BufferedReader(new InputStreamReader(csvFile,
                    "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            dataFile = new BufferedReader(new InputStreamReader(csvFile));
            e1.printStackTrace();
            System.out.println("Can not read");
        }
        CSVReader reader = null;
        try {
            reader = new CSVReader(dataFile);
            String[] nextLine;
            String lastGenericDrug = null;
            String lastBrandDrug = null;
            String[] lastDrugClasses = new String[1];
            ArrayList<String> excludedBrandNameList = new ArrayList<String>();

            reader.readNext(); // title line
            while ((nextLine = reader.readNext()) != null) {

                String name = nextLine[0].toUpperCase();
                String altName = nextLine[1].toUpperCase();
                String reason = nextLine[2].trim();
                String drugClass = nextLine[3].toUpperCase().trim();
//
//                // extraline for restricted criteria
                if ((name.equals("")) && !(altName.equals(""))) {
                    //generic drug---------------------------------------
                    if(excludedList.containsKey(name)){ //generic name exists already
                        ExcludedDrug excludedDrug = excludedList.get(name);

                        //check alternate name list
                        String[] altNameList = getSeparateAltNames(altName);
                        for(String altNameEntry : altNameList){
                            if(!altNameEntry.equals("") &&
                                    !excludedDrug.getAlternateName().contains(altNameEntry)){
                                excludedDrug.getAlternateName().add(altNameEntry);
                            }
                        }

                        //check drug class in list
                        String[] drugClassList = getSeparateDrugClasses(drugClass);
                        for(String drugClassName: drugClassList){
                            if(!drugClassName.equals("") && !excludedDrug.getDrugClass().contains(drugClassName)){
                                excludedDrug.getDrugClass().add(drugClassName);
                            }
                        }
                    }
                    else{ //generic drug is new
                        String[] drugClassList = getSeparateDrugClasses(drugClass);
                        String[] altNameList = getSeparateAltNames(altName);

                        for(String drugClassName: drugClassList){
                            excludedList.put(name, new ExcludedDrug(name, NameType.GENERIC,
                                    Arrays.asList(altNameList), reason, DrugType.EXCLUDED, drugClassName));
                        }
                    }
                }
//                    ((GenericExcludedDrug) genericList
//                            .getGenericDrug(lastGenericDrug))
//                            .additionalCriteria(brandname);
//                    ((BrandExcludedDrug) brandList.getBrandDrug(lastBrandDrug))
//                            .additionalCriteria(brandname);
//                } else if (!(name.equals(""))) {// handles blank lines
//                    if (nextLine[1].equals("")) {// no brandname
//                        genericList.addGenericDrug(new GenericExcludedDrug(
//                                name, "", brandname, drugClass));
//                        lastGenericDrug = name; // sets the last drug if next
//                        // line is extra criteria
//                    } else {
//                        if (nextLine[1].contains(",")) {
//                            String[] brandNameList;
//                            brandNameList = nextLine[1].split(",");
//                            brandList.addBrandDrug(new BrandExcludedDrug(name,
//                                    brandNameList[0], brandname, drugClass));
//                            excludedBrandNameList.add(brandNameList[0]);
//                            for (String additionalBrand : brandNameList) {
//                                // if brand name already exists, add just the
//                                // generic name to the list
//                                if (excludedBrandNameList
//                                        .contains(additionalBrand.trim())) {
//                                    ((BrandExcludedDrug) brandList
//                                            .getBrandDrug(additionalBrand
//                                                    .trim()))
//                                            .addGenericName(name);
//                                } else {
//                                    brandList
//                                            .addBrandDrug(new BrandExcludedDrug(
//                                                    name, additionalBrand
//                                                    .trim(), brandname, drugClass));
//                                    excludedBrandNameList.add(additionalBrand
//                                            .trim());
//                                    genericList
//                                            .addGenericDrug(new GenericExcludedDrug(
//                                                    name, additionalBrand
//                                                    .trim(), brandname, drugClass));
//                                    lastGenericDrug = name; // sets the last
//                                    // drug if next line
//                                    // is extra criteria
//                                }
//                            }
//                            lastBrandDrug = brandNameList[0];
//                        } else {
//                            if (brandList.containsBrandName(nextLine[1])
//                                    && brandList.getBrandDrug(nextLine[1])
//                                    .getStatus().equals("Excluded")) {
//                                ((BrandExcludedDrug) brandList
//                                        .getBrandDrug(nextLine[1]))
//                                        .addGenericName(name);
//                            } else {
//                                brandList.addBrandDrug(new BrandExcludedDrug(
//                                        name, nextLine[1], brandname, drugClass));
//                                excludedBrandNameList.add(nextLine[1]);
//                                lastBrandDrug = nextLine[1];
//                                genericList
//                                        .addGenericDrug(new GenericExcludedDrug(
//                                                name, nextLine[1], brandname,drugClass));
//                                lastGenericDrug = name; // sets the last drug if
//                                // next line is extra
//                                // criteria
//                            }
//                        }
//                    }
//                }
            }
            dataFile.close();
        } catch (IOException e) {
            System.out.println("I/O error " + e.getMessage());
            e.printStackTrace();
        } finally {
            dataFile = null;
        }
    }

    public void parseRestricted(InputStream csvFile) {
//        BufferedReader dataFile;
//        try {
//            dataFile = new BufferedReader(new InputStreamReader(csvFile,
//                    "UTF-8"));
//        } catch (UnsupportedEncodingException e1) {
//            dataFile = new BufferedReader(new InputStreamReader(csvFile));
//            e1.printStackTrace();
//        }
//        CSVReader reader = null;
//        try {
//            reader = new CSVReader(dataFile);
//            String[] nextLine;
//            String lastGenericDrug = null;
//            String lastBrandDrug = null;
//            ArrayList<String> restrictedBrandNameList = new ArrayList<String>();
//
//            reader.readNext(); // title line
//            while ((nextLine = reader.readNext()) != null) {
//                String name = nextLine[0].trim().toUpperCase();
//                String restrictedCriteria = nextLine[2].trim().toUpperCase();
//                String drugClass = nextLine[3].trim().toUpperCase();
//
//                if(!genericList.containsGenericName(name)){
//
//                    // extra restricted criteria line
//                    if ((name.equals("")) && !(restrictedCriteria.equals(""))) {
//
//                        ((GenericRestrictedDrug) genericList.getGenericDrug(lastGenericDrug)).additionalCriteria(restrictedCriteria);
//                        ((BrandRestrictedDrug) brandList.getBrandDrug(lastBrandDrug)).additionalCriteria(restrictedCriteria);
//
//                    } else if (!(name.equals(""))) {// handles blank lines
//
//                        if (nextLine[1].equals("")) {// no brandname
//                            genericList.addGenericDrug(new GenericRestrictedDrug(
//                                    name, "", restrictedCriteria, drugClass));
//                            lastGenericDrug = name; // sets the last drug if next
//                            // line is extra criteria
//                        } if (nextLine[1].contains(",")) { // multiple brand names
//                            String[] brandNameList;
//                            brandNameList = nextLine[1].split(",");
//
//                            String firstBrandName = brandNameList[0].trim().toUpperCase();
//                            brandList.addBrandDrug(new BrandRestrictedDrug(name, firstBrandName,restrictedCriteria, drugClass));
//                            restrictedBrandNameList.add(firstBrandName);
//
//                            for (String additionalBrand : brandNameList) {
//                                String currBrandName = additionalBrand.trim().toUpperCase();
//
//                                if (!currBrandName.equals(brandNameList[0])){
//
//
//
//                                    // if brand name already exists, add just the
//                                    // generic name to the list
////									System.out.println(currBrandName);
//                                    if (restrictedBrandNameList.contains(currBrandName)) {
//                                        ((BrandRestrictedDrug) brandList
//                                                .getBrandDrug(currBrandName))
//                                                .addGenericName(name);
//
//                                    } else {
//                                        brandList
//                                                .addBrandDrug(new BrandRestrictedDrug(
//                                                        name, currBrandName,
//                                                        restrictedCriteria, drugClass));
//                                        genericList
//                                                .addGenericDrug(new GenericRestrictedDrug(
//                                                        name, currBrandName,
//                                                        restrictedCriteria, drugClass));
//                                        restrictedBrandNameList.add(currBrandName);
//                                    }
//                                }
//                            }
//                            lastGenericDrug = name; // sets the last drug if
//                            // next line is extra
//                            // criteria
//                            lastBrandDrug = firstBrandName;
//                        } else { // single brand name
//                            String onlyBrandName = nextLine[1].trim().toUpperCase();
//                            if (brandList.containsBrandName(onlyBrandName)
//                                    && brandList.getBrandDrug(onlyBrandName)
//                                    .getStatus().equals("Restricted")) {
//                                ((BrandRestrictedDrug) brandList
//                                        .getBrandDrug(onlyBrandName))
//                                        .addGenericName(name);
//                            } else {
////								System.out.println(name + " " + onlyBrandName);
//                                brandList.addBrandDrug(new BrandRestrictedDrug(name, onlyBrandName, restrictedCriteria, drugClass));
//                                restrictedBrandNameList.add(onlyBrandName);
//                                lastBrandDrug = onlyBrandName;
//                                genericList.addGenericDrug(new GenericRestrictedDrug(name, onlyBrandName, restrictedCriteria, drugClass));
//                                lastGenericDrug = name; // sets the last drug if
//                                // next line is extra
//                                // criteria
//                            }
//                        }
//                    }
//                }
//
//            }
//            dataFile.close();
//        } catch (IOException e) {
//            System.out.println("I/O error " + e.getMessage());
//            e.printStackTrace();
//        } finally {
//            dataFile = null;
//        }
    }
//
//    private void addBrandNameFormulary(String genericName, String brandName,
//                                       String strength, String drugClass) {
//        if (brandList.containsBrandName(brandName)) {
//            // add strength
//            ((BrandFormularyDrug) brandList.getBrandDrug(brandName))
//                    .addStrength(strength);
//            // add generic name
//            if (!((BrandFormularyDrug) brandList.getBrandDrug(brandName))
//                    .containsGenericName(genericName))
//                ((BrandFormularyDrug) brandList.getBrandDrug(brandName))
//                        .addGenericName(genericName);
//        } else {
//            brandList.addBrandDrug(new BrandFormularyDrug(genericName,
//                    brandName, strength, drugClass));
//        }
//    }
//
//    private void addGenericFormularyDrugWithBrandName(String genericName,
//                                                      String strength, String brandName, String drugClass) {
//        if (brandName.contains(",")) {
//            String[] brandNameList;
//            brandNameList = brandName.split(",");
//            genericList.addGenericDrug(new GenericFormularyDrug(genericName,
//                    brandNameList[0].trim().toUpperCase(), strength, drugClass));
//            for (int i = 1; i < brandNameList.length; i++) {
//                brandName = brandNameList[i].trim();
//                addBrandNameToExistingFormularyDrug(genericName,
//                        brandNameList[i].trim());
//            }
//        } else {
//            genericList.addGenericDrug(new GenericFormularyDrug(genericName,
//                    brandName, strength, drugClass));
//        }
//
//    }
//
//    private void addBrandNameToExistingFormularyDrug(String genericName,
//                                                     String brandName) {
//        if (genericList.containsGenericName(genericName)) {
//            if (!((GenericFormularyDrug) genericList
//                    .getGenericDrug(genericName)).containsBrandName(brandName)) {
//                ((GenericFormularyDrug) genericList.getGenericDrug(genericName))
//                        .addBrandName(brandName);
//            }
//        }
//    }
//
//    public GenericDrugList getListByGeneric() {
//        return genericList;
//    }
//
//    public BrandDrugList getListByBrand() {
//        return brandList;
//    }

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
}