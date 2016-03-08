package com.lowermainlandpharmacyservices.lmpsformulary;

import java.util.ArrayList;

public class BrandDrug extends Drug {

	public String brandName;
	public ArrayList<String> genericNames;

	public BrandDrug(String genericName, String brandName, String status) {
		super(status);
		this.genericNames = new ArrayList<String>();
		this.genericNames.add(genericName);
		this.brandName = brandName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public ArrayList<String> getGenericNames() {
		return genericNames;
	}

	public void addGenericName(String genericName) {
		if (genericName.equals("")) {
		} else
			this.genericNames.add(genericName);
	}

	public boolean containsGenericName(String genericName) {
		for (String name : this.genericNames) {
			if (name.equals(genericName))
				return true;
		}
		return false;
	}

}
