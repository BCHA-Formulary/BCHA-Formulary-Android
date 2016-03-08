package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;
import java.util.HashMap;

public class BrandDrugList {

	private HashMap<String, Drug> brandDrugList;

	public BrandDrugList() {
		brandDrugList = new HashMap<String, Drug>();
	}

	public void addBrandDrug(BrandDrug brandDrug) {
		brandDrugList.put(brandDrug.getBrandName(), brandDrug);
	}

	public void removeBrandDrug(String brandName) {
		if (brandDrugList.containsKey(brandName))
			brandDrugList.remove(brandName);
	}

	public int getDrugListSize() {
		return brandDrugList.size();
	}

	public Drug getBrandDrug(String drugName) {

		return brandDrugList.get(drugName);
	}

	public boolean isEmpty() {
		return brandDrugList.isEmpty();
	}

	public boolean containsBrandDrugObject(BrandDrug drug) {

		return brandDrugList.containsValue(drug);
	}

	public boolean containsBrandName(String drug) {

		return brandDrugList.containsKey(drug);
	}

	public ArrayList<String> getBrandNameList() {
		ArrayList<String> nameList = new ArrayList<String>();
		for (String genericName : brandDrugList.keySet()) {
			nameList.add(genericName);
		}
		return nameList;
	}

}
