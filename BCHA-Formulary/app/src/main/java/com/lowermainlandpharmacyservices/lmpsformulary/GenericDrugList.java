package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;
import java.util.HashMap;

public class GenericDrugList {

	private HashMap<String, Drug> genericDrugList;

	public GenericDrugList() {
		genericDrugList = new HashMap<String, Drug>();
	}

	public int getDrugListSize() {
		return genericDrugList.size();
	}

	public void addGenericDrug(GenericDrug newDrug) {
		genericDrugList.put(newDrug.getGenericName(), newDrug);
	}

	public void removeGenericDrug(GenericDrug drug) {

		if (genericDrugList.containsValue(drug) != false) {
			genericDrugList.remove(drug.getGenericName());
		}

	}

	public Drug getGenericDrug(String drug) {

		return genericDrugList.get(drug);
	}

	public boolean isEmpty() {
		return genericDrugList.isEmpty();
	}

	public boolean containsGenericDrugObject(Drug drug) {

		return genericDrugList.containsValue(drug);
	}

	public boolean containsGenericName(String drug) {

		return genericDrugList.containsKey(drug);
	}

	public ArrayList<String> getGenericNameList() {
		ArrayList<String> nameList = new ArrayList<String>();
		for (String genericName : genericDrugList.keySet()) {
			nameList.add(genericName);
		}
		return nameList;
	}

}
